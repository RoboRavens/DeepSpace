/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Calibrations;
import frc.robot.commands.arm.ArmRetractFullyCommand;
import frc.robot.commands.drivetrain.DriveTrainDriveInchesCommand;
import frc.robot.commands.misc.CompressorTurnOffWhileClimbingCommand;
import frc.robot.commands.climbthirdlevel.*;

public class ClimberThirdLevelCommand extends CommandGroup {
  
  public ClimberThirdLevelCommand() {
    addSequential(new CompressorTurnOffWhileClimbingCommand());
    addSequential(new ArmRetractFullyCommand());
    addSequential(new DriveTrainDriveInchesCommand(5, .3, Calibrations.drivingBackward));
    addParallel(new RaiseRobotToThirdLevelCommand());
    addSequential(new DriveOntoThirdPlatformCommand());
    addSequential(new ArmRetractFullyCommand());
  }
}
