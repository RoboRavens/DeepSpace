/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.readytocollect;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.commands.arm.ArmRetractFullyCommand;
import frc.robot.commands.beak.BeakCaptureHatchPanelCommand;
import frc.robot.commands.hatchpanel.HatchPanelScoreLowCommand;

public class ReadyToCollectCommand extends Command {
  HatchPanelScoreLowCommand hatchPanelScoreLowCommand = new HatchPanelScoreLowCommand();
  ArmRetractFullyCommand armRetractFullyCommand = new ArmRetractFullyCommand();
  BeakCaptureHatchPanelCommand hatchPanelIntakeCommand = new BeakCaptureHatchPanelCommand();
  private boolean _isFinished = false;

  public ReadyToCollectCommand() {
    requires(Robot.READY_TO_COLLECT_SUBSYSTEM);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.INTAKE_TRANSPORT_SUBSYSTEM.intakeExtend();
    hatchPanelScoreLowCommand.start();
    if (Robot.gamePieceIsHatch) {
      Robot.BEAK_SUBSYSTEM.release();
    }
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if (_isFinished) {
      return;
    }

    if (Robot.CARGO_WHEEL_SUBSYSTEM.hasCargo()) {
      armRetractFullyCommand.start();
      _isFinished = true;
    } else if (Robot.BEAK_SUBSYSTEM.hasHatchPanelStrict()) {
      hatchPanelIntakeCommand.start();
      _isFinished = true;
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return _isFinished;
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
