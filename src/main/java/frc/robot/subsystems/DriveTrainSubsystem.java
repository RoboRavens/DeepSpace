package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.controls.Gamepad;
import frc.ravenhardware.RavenTank;
import frc.robot.Calibrations;
import frc.robot.Robot;
import frc.robot.commands.drivetrain.DriveTrainDriveFPSCommand;
import frc.util.NetworkTableDiagnostics;
import frc.util.PCDashboardDiagnostics;

/**
 *
 */
public class DriveTrainSubsystem extends Subsystem {
	public Robot robot;
	public Gamepad driveController;
	public RavenTank ravenTank;

	public DriveTrainSubsystem() {
		this.ravenTank = new RavenTank();
	}

	public void initDefaultCommand() {
		setDefaultCommand(new DriveTrainDriveFPSCommand());
	}

	public void periodic() {

		double elevatorHeightPercentage = Robot.ELEVATOR_SUBSYSTEM.getElevatorHeightPercentage();
		double powerSubtractor = (1 - Calibrations.DRIVETRAIN_MAXPOWER_AT_MAX_ELEVEATOR_HEIGHT) * elevatorHeightPercentage;
		double maxPower = Math.min(1, 1 - powerSubtractor);
		this.ravenTank.setMaxPower(maxPower);

		double slewRateDifference = Calibrations.slewRateMaximum - Calibrations.slewRateMinimum;
		double slewRateSubtraction = slewRateDifference * elevatorHeightPercentage;
		double slewRate = Calibrations.slewRateMaximum - slewRateSubtraction;
		slewRate = Math.max(Calibrations.slewRateMinimum, slewRate);
		slewRate = Math.min(Calibrations.slewRateMaximum, slewRate);
		this.ravenTank.setSlewRate(slewRate);

		NetworkTableDiagnostics.SubsystemNumber("DriveTrain", "PowerMax", () -> maxPower);
		NetworkTableDiagnostics.SubsystemNumber("DriveTrain", "EncoderLeftInchesTraveled", () -> this.ravenTank.leftRavenEncoder.getNetInchesTraveled());
		NetworkTableDiagnostics.SubsystemNumber("DriveTrain", "EncoderRightInchesTraveled", () -> this.ravenTank.rightRavenEncoder.getNetInchesTraveled());
		NetworkTableDiagnostics.SubsystemNumber("DriveTrain", "EncoderAvgInchesTraveled", () -> Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.getNetInchesTraveled());
		PCDashboardDiagnostics.SubsystemNumber("DriveTrain", "SlewRate", slewRate);
		NetworkTableDiagnostics.SubsystemBoolean("DriveTrain", "CutPower", () -> this.ravenTank.getCutPower());
	}
}
