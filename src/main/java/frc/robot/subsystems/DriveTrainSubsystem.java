package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.ravenhardware.RavenTank;
import frc.robot.Calibrations;
import frc.robot.Robot;
import frc.robot.commands.drivetrain.DriveTrainDriveFPSCommand;
import frc.util.NetworkTableDiagnostics;
<<<<<<< HEAD
import frc.util.PCDashboardDiagnostics;
=======
>>>>>>> master

/**
 *
 */
public class DriveTrainSubsystem extends Subsystem {
	public RavenTank ravenTank;

	private double _maxPower;
	private double _slewRateFinal;

	public DriveTrainSubsystem() {
		ravenTank = new RavenTank();

		NetworkTableDiagnostics.SubsystemNumber("DriveTrain", "PowerMax", () -> _maxPower);
		NetworkTableDiagnostics.SubsystemNumber("DriveTrain", "EncoderLeftInchesTraveled", () -> ravenTank.getLeftNetInchesTraveled());
		NetworkTableDiagnostics.SubsystemNumber("DriveTrain", "EncoderRightInchesTraveled", () -> ravenTank.getRightNetInchesTraveled());
		NetworkTableDiagnostics.SubsystemNumber("DriveTrain", "EncoderAvgInchesTraveled", () -> Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.getNetInchesTraveled());
		NetworkTableDiagnostics.SubsystemNumber("DriveTrain", "SlewRate", () -> _slewRateFinal);
		NetworkTableDiagnostics.SubsystemBoolean("DriveTrain", "CutPower", () -> ravenTank.getCutPower());
	}

	public void initDefaultCommand() {
		setDefaultCommand(new DriveTrainDriveFPSCommand());
	}

	public void periodic() {

		double elevatorHeightPercentage = Robot.ELEVATOR_SUBSYSTEM.getElevatorHeightPercentage();
		double powerSubtractor = (1 - Calibrations.DRIVETRAIN_MAXPOWER_AT_MAX_ELEVEATOR_HEIGHT) * elevatorHeightPercentage;
		_maxPower = Math.min(1, 1 - powerSubtractor);
		ravenTank.setMaxPower(_maxPower);

		double slewRateDifference = Calibrations.slewRateMaximum - Calibrations.slewRateMinimum;
		double slewRateSubtraction = slewRateDifference * elevatorHeightPercentage;
		double slewRate = Calibrations.slewRateMaximum - slewRateSubtraction;
		slewRate = Math.max(Calibrations.slewRateMinimum, slewRate);
<<<<<<< HEAD
		slewRate = Math.min(Calibrations.slewRateMaximum, slewRate);
		this.ravenTank.setSlewRate(slewRate);

		NetworkTableDiagnostics.SubsystemNumber("DriveTrain", "PowerMax", () -> maxPower);
		NetworkTableDiagnostics.SubsystemNumber("DriveTrain", "EncoderLeftInchesTraveled", () -> this.ravenTank.leftRavenEncoder.getNetInchesTraveled());
		NetworkTableDiagnostics.SubsystemNumber("DriveTrain", "EncoderRightInchesTraveled", () -> this.ravenTank.rightRavenEncoder.getNetInchesTraveled());
		NetworkTableDiagnostics.SubsystemNumber("DriveTrain", "EncoderAvgInchesTraveled", () -> Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.getNetInchesTraveled());
		PCDashboardDiagnostics.SubsystemNumber("DriveTrain", "SlewRate", slewRate);
		NetworkTableDiagnostics.SubsystemBoolean("DriveTrain", "CutPower", () -> this.ravenTank.getCutPower());
=======
		_slewRateFinal = Math.min(Calibrations.slewRateMaximum, slewRate);
		ravenTank.setSlewRate(_slewRateFinal);
>>>>>>> master
	}
}
