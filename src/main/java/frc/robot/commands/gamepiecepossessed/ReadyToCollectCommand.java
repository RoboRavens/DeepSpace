/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.gamepiecepossessed;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Calibrations;
import frc.robot.Robot;
import frc.robot.commands.beak.BeakCaptureHatchPanelCommand;
import frc.robot.commands.intaketransport.IntakeExtendCommand;
import frc.robot.commands.intaketransport.IntakeRetractCommand;
import frc.robot.commands.misc.WaitCommand;

public class ReadyToCollectCommand extends CommandGroup {

  public ReadyToCollectCommand() {
    addSequential(new IntakeExtendCommand());
      if (Robot.BEAK_SUBSYSTEM.hasHatchPanelStrict()) {
        addSequential(new BeakCaptureHatchPanelCommand());
        addSequential(new WaitCommand(Calibrations.hasHatchPanelTimer));
        addSequential(new IntakeRetractCommand());
      }
  }
}
