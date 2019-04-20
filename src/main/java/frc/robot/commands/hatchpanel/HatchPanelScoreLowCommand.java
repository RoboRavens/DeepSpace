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
import frc.robot.commands.elevator.ElevatorRetractFullyCommand;

public class HatchPanelScoreLowCommand extends CommandGroup {
  
  public HatchPanelScoreLowCommand() {
    System.out.println("HatchPanelScoreLowCommand init");
    addParallel(new ArmMoveToHeightCommand(Calibrations.armLowHatchEncoderValue));
    addParallel(new ElevatorRetractFullyCommand());
  }
}
