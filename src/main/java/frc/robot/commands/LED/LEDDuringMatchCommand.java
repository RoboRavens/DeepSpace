/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.LED;
import edu.wpi.first.wpilibj.DriverStation;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class LEDDuringMatchCommand extends Command {
  DriverStation driverStation = DriverStation.getInstance();

  public LEDDuringMatchCommand() {
    requires(Robot.PROGRAMMABLE_LED_SUBSYSTEM);

  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if (Robot.PROGRAMMABLE_LED_SUBSYSTEM.getMatchIsAtOrAfterOneTimeAndAtOrBeforeAnotherTime(135, 120)) {
      Robot.PROGRAMMABLE_LED_SUBSYSTEM.setSandstormPattern();
    }
    if (Robot.PROGRAMMABLE_LED_SUBSYSTEM.getMatchIsAtOrAfterTime(120)) {
      Robot.PROGRAMMABLE_LED_SUBSYSTEM.setEnabledPattern();
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    if (Robot.PROGRAMMABLE_LED_SUBSYSTEM.getMatchIsAtTime(0)) {
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
