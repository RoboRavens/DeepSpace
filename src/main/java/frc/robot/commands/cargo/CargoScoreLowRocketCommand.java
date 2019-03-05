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

public class CargoScoreLowRocketCommand extends CommandGroup {
  
  public CargoScoreLowRocketCommand() {
    addParallel(new ElevatorMoveToHeightCommand(Calibrations.elevatorLowRocketPortEncoderValue));
    addSequential(new ArmMoveToHeightCommand(Calibrations.armLowRocketPortEncoderValue));
    // addSequential(new CargoWheelSuckOrSpitCommand(Calibrations.cargoSpitPowerMagnitude, "Spit"));
    //addParallel(new ElevatorMoveToHeightCommand(Calibrations.elevatorEncoderMinimumValue));
    //addParallel(new ArmMoveToHeightCommand(Calibrations.armEncoderRetractedValue));
    //addSequential(new DriveTrainDriveInchesCommand(24, .6, Calibrations.drivingBackward));
  }
}
