/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.hatchpanel;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Calibrations;
import frc.robot.commands.arm.ArmMoveToHeightCommand;
import frc.robot.commands.beak.BeakReleaseHatchPanelCommand;
import frc.robot.commands.drivetrain.DriveTrainDriveInchesCommand;

import frc.robot.commands.elevator.ElevatorMoveToHeightCommand;

public class HatchPanelScoreHighRocketWithCheckCommand extends CommandGroup {
  
  public HatchPanelScoreHighRocketWithCheckCommand() {
    
    addParallel(new ElevatorMoveToHeightCommand(Calibrations.elevatorHighHatchEncoderValue));
    addSequential(new ArmMoveToHeightCommand(Calibrations.armHighHatchEncoderValue));
    addSequential(new BeakReleaseHatchPanelCommand());
    addSequential(new DriveTrainDriveInchesCommand(2, .6, Calibrations.drivingBackward));
    addSequential(new HatchPanelCheckCommand(new HatchPanelScoreHighRocketWithCheckCommand()));
    addParallel(new ElevatorMoveToHeightCommand(Calibrations.elevatorLowHatchEncoderValue));
    addParallel(new ArmMoveToHeightCommand(Calibrations.armLowHatchEncoderValue));
  }
}
