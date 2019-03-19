/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.AutonomousCalibrations;
import frc.robot.Calibrations;
import frc.robot.Robot;

public class DriveTrainNearRocketScoreHatchPanelCommand extends CommandGroup {
  public DriveTrainNearRocketScoreHatchPanelCommand() {
    addSequential(new DriveTrainDriveInchesCommand(AutonomousCalibrations.HPSBackwardsScoreOnNearRocketDistanceInches,
        AutonomousCalibrations.HPSBackwardsScoreOnNearRocketPowerMagnitude, Calibrations.drivingBackward, 0.5));
    addSequential(new DriveTrainTurnRelativeDegreesCommand(Robot.DRIVE_TRAIN_SUBSYSTEM, 150));
  }
}
