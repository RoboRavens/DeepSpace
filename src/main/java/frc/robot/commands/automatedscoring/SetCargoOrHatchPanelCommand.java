/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.automatedscoring;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class SetCargoOrHatchPanelCommand extends Command {
  private String _hatchOrCargo;
  public SetCargoOrHatchPanelCommand(String hatchOrCargo) {
    this._hatchOrCargo = hatchOrCargo;
    requires(Robot.SET_COMMAND_SUBSYSTEM);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    System.out.println("Running SetCargoOrHatchPanelCommand");
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() { 
    if (this._hatchOrCargo == "Cargo") {
      Robot.SET_COMMAND_SUBSYSTEM.SetCargoOrHatchPanelCommand("Cargo");
    } else {
      Robot.SET_COMMAND_SUBSYSTEM.SetCargoOrHatchPanelCommand("Hatch Panel");
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
