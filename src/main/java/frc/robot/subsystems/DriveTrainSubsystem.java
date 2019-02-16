package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.controls.Gamepad;
import frc.ravenhardware.RavenEncoder;
import frc.ravenhardware.RavenTank;
import frc.robot.Calibrations;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.drivetrain.DriveTrainDriveFPSCommand;
import frc.util.PCDashboardDiagnostics;

/**
 *
 */
public class DriveTrainSubsystem extends Subsystem {
	public Robot robot;
	Gamepad driveController;
	public RavenTank ravenTank;
	NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
	NetworkTableEntry tx = table.getEntry("tx");

	public DriveTrainSubsystem() {
		this.ravenTank = new RavenTank();
	}

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.

		// setDefaultCommand(new MySpecialCommand());
		setDefaultCommand(new DriveTrainDriveFPSCommand());
	}

	public void periodic() {

		double elevatorHeightPercentage = Robot.ELEVATOR_SUBSYSTEM.getElevatorHeightPercentage();
		double powerSubtractor = (1 - Calibrations.DRIVETRAIN_MAXPOWER_AT_MAX_ELEVEATOR_HEIGHT) * elevatorHeightPercentage;
		double maxPower = Math.min(1, 1 - powerSubtractor);
		this.ravenTank.setMaxPower(maxPower);

		this.ravenTank.setGyroTargetHeading(this.ravenTank.getCurrentHeading() + tx.getDouble(0.0));

		double slewRateDifference = Calibrations.slewRateMaximum - Calibrations.slewRateMinimum;
		double slewRateSubtraction = slewRateDifference * elevatorHeightPercentage;
		double slewRate = Calibrations.slewRateMaximum - slewRateSubtraction;
		slewRate = Math.max(Calibrations.slewRateMinimum, slewRate);
		slewRate = Math.min(Calibrations.slewRateMaximum, slewRate);
		this.ravenTank.setSlewRate(slewRate);

		PCDashboardDiagnostics.SubsystemNumber("DriveTrain", "PowerMax", maxPower);
		PCDashboardDiagnostics.SubsystemNumber("DriveTrain", "EncoderLeftInchesTraveled", this.ravenTank.leftRavenEncoder.getNetInchesTraveled());
		PCDashboardDiagnostics.SubsystemNumber("DriveTrain", "EncoderRightInchesTraveled", this.ravenTank.rightRavenEncoder.getNetInchesTraveled());
		PCDashboardDiagnostics.SubsystemNumber("DriveTrain", "EncoderAvgInchesTraveled", Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.getNetInchesTraveled());
		PCDashboardDiagnostics.SubsystemNumber("DriveTrain", "SlewRate", slewRate);
		PCDashboardDiagnostics.SubsystemBoolean("DriveTrain", "CutPower", this.ravenTank.getCutPower());
	}
}
