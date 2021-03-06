/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.cargo;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Calibrations;
import frc.robot.commands.arm.ArmMoveToHeightCommand;

import frc.robot.commands.elevator.ElevatorMoveToHeightCommand;

public class CargoScoreHighRocketCommand extends CommandGroup {
  
  public CargoScoreHighRocketCommand() {
    addParallel(new ArmMoveToHeightCommand(Calibrations.armHighRocketPortEncoderValue));
    addParallel(new ElevatorMoveToHeightCommand(Calibrations.elevatorHighRocketPortEncoderValue));
  }
}
