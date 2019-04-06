/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.hatchpanel;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.commands.misc.WaitCommand;
import frc.robot.Calibrations;
import frc.robot.Robot;

public class HatchPanelIntakeCommand extends Command {
  WaitCommand wait = new WaitCommand(Calibrations.hasHatchPanelTimer);

  public HatchPanelIntakeCommand() {
    requires(Robot.BEAK_SUBSYSTEM);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    System.out.println("HatchPanelIntakeCommand init");
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
      Robot.BEAK_SUBSYSTEM.capture();
      wait.start();
      if (wait.isCompleted()) {
        Robot.INTAKE_TRANSPORT_SUBSYSTEM.intakeRetract();
      }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    if (wait.isCompleted()) {
      return true;
    }
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
