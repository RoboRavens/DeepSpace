package frc.robot.commands.autonomousmodes;

import frc.robot.AutonomousCalibrations;
import frc.robot.Calibrations;
import frc.robot.commands.drivetrain.DriveTrainDriveInchesCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonomousCrossAutoLineCommand extends CommandGroup {

	public AutonomousCrossAutoLineCommand() {
		//addSequential(new DriveTrainDriveInchesCommand(AutonomousCalibrations.AutonomousCrossAutoLineDriveForwardInches,
				//AutonomousCalibrations.AutonomousCrossAutoLineDriveForwardPowerMagnitude, Calibrations.drivingForward));
	}
}
