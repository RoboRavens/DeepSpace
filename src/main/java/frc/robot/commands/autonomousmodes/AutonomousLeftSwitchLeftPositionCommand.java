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

public class AutonomousLeftSwitchLeftPositionCommand extends CommandGroup{
	
	public AutonomousLeftSwitchLeftPositionCommand() {
		
		
		// 148 inches forward; turn right, drive 40 inches, spit cube.
		// At any point, move the arm into position.
		// double driveForwardDistance = AutonomousCalibrations.LengthBetweenDriverWallAndSwitch + AutonomousCalibrations.LengthOfSwitch / 2;
				
		addSequential(new DriveTrainDriveInchesCommand(AutonomousCalibrations.StraightSwitchDriveForwardFromWallInches,
				AutonomousCalibrations.AutonomousScoreSwitchDriveForwardPowerMagnitude,
    			Calibrations.drivingForward,
				AutonomousCalibrations.SideSwitchDriveForwardFromWallTimeoutSeconds));
		addSequential(new DriveTrainTurnRelativeDegreesCommand(Robot.DRIVE_TRAIN_SUBSYSTEM, 90));
		addSequential(new DriveTrainDriveInchesCommand(AutonomousCalibrations.StraightSwitchDriveForwardToSwitchInches,
				AutonomousCalibrations.AutonomousScoreSwitchDriveForwardPowerMagnitude,
    			Calibrations.drivingForward));
		addSequential(new DriveTrainStopCommand());
		addSequential(new ElevatorMoveToHeightCommand(Calibrations.elevatorSwitchEncoderValue));
		addSequential(new ArmExtendFullyCommand());
		addSequential(new IntakeWheelsSpitCommand(AutonomousCalibrations.AutonomousScoreSwitchIntakePushPowerMagnitude));
	}

}
