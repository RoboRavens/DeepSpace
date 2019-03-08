/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.hatchpanel;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.commands.beak.BeakCaptureHatchPanelCommand;

public class HatchPanelCheckCommand extends Command {
  private Command _command = new Command() {
    protected boolean isFinished() {
      return Robot.BEAK_SUBSYSTEM.hasHatchPanelStrict() == false;
    }
  };

  public HatchPanelCheckCommand(Command command) {
    requires(Robot.BEAK_SUBSYSTEM);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if (Robot.BEAK_SUBSYSTEM.hasHatchPanelStrict()) {
      new BeakCaptureHatchPanelCommand();
      _command.start();
  } else {
    isFinished();
  }
}

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
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
