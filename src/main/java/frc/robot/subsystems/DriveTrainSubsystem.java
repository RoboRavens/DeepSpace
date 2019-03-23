package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.ravenhardware.RavenTank;
import frc.robot.Calibrations;
import frc.robot.Robot;
import frc.robot.commands.drivetrain.DriveTrainDriveFPSCommand;
import frc.util.NetworkTableDiagnostics;

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
		NetworkTableDiagnostics.SubsystemNumber("DriveTrain", "EncoderAvgInchesTraveled", () -> ravenTank.getNetInchesTraveled());
		NetworkTableDiagnostics.SubsystemNumber("DriveTrain", "SlewRate", () -> _slewRateFinal);
		NetworkTableDiagnostics.SubsystemNumber("DriveTrain", "PitchAngle", () -> ravenTank.getPitchAngle());
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
		_slewRateFinal = Math.min(Calibrations.slewRateMaximum, slewRate);
		ravenTank.setSlewRate(_slewRateFinal);
	}
}
