package frc.robot.commands.autonomousmodes;

import frc.robot.AutonomousCalibrations;
import frc.robot.Calibrations;
import frc.robot.Robot;
import frc.robot.commands.arm.ArmExtendFullyCommand;
import frc.robot.commands.drivetrain.DriveTrainDriveInchesCommand;
import frc.robot.commands.drivetrain.DriveTrainStopCommand;
import frc.robot.commands.drivetrain.DriveTrainTurnRelativeDegreesCommand;
import frc.robot.commands.elevator.ElevatorMoveToHeightCommand;
import frc.robot.commands.intake.IntakeWheelsSpitCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousRightSwitchMiddlePositionCommand extends CommandGroup{
	
	public AutonomousRightSwitchMiddlePositionCommand() {
		addSequential(new DriveTrainDriveInchesCommand(AutonomousCalibrations.AutonomousScoreSwitchMiddlePositionDriveForwardFirstSegmentInches,
				AutonomousCalibrations.AutonomousScoreSwitchDriveForwardPowerMagnitude,
    			Calibrations.drivingForward));
		addSequential(new DriveTrainTurnRelativeDegreesCommand(Robot.DRIVE_TRAIN_SUBSYSTEM, 90, AutonomousCalibrations.SwitchGyroScaleFactor, 1.5));
		addSequential(new DriveTrainDriveInchesCommand(AutonomousCalibrations.AutonomousScoreSwitchMiddlePositionLateralDriveForwardInches + AutonomousCalibrations.ExchangeZoneBufferMiddlePositionRightSwitch,
				AutonomousCalibrations.AutonomousScoreSwitchDriveForwardPowerMagnitude,
    			Calibrations.drivingForward));
		addSequential(new DriveTrainTurnRelativeDegreesCommand(Robot.DRIVE_TRAIN_SUBSYSTEM, -90, AutonomousCalibrations.SwitchGyroScaleFactor, 1.5));
		addSequential(new DriveTrainDriveInchesCommand(AutonomousCalibrations.AutonomousScoreSwitchMiddlePositionDriveForwardSecondSegmentInches,
				AutonomousCalibrations.AutonomousScoreSwitchDriveForwardPowerMagnitude,
    			Calibrations.drivingForward,
    			2.5));

		addSequential(new DriveTrainStopCommand());
		// addSequential(new DriveTrainTurnRelativeDegreesCommand(Robot.DRIVE_TRAIN_SUBSYSTEM, -90));
		addSequential(new ElevatorMoveToHeightCommand(Calibrations.elevatorSwitchEncoderValue));
		addSequential(new ArmExtendFullyCommand());
		addSequential(new IntakeWheelsSpitCommand(AutonomousCalibrations.AutonomousScoreSwitchIntakePushPowerMagnitude));
	}

}
