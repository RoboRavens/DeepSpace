/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.climbthirdlevel;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Calibrations;
import frc.robot.Robot;

public class DriveOntoThirdPlatformCommand extends Command {
  public DriveOntoThirdPlatformCommand() {
    requires(Robot.CARGO_WHEEL_SUBSYSTEM);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    System.out.println("DriveOntoThirdPlatformCommand init");
    Robot.CARGO_WHEEL_SUBSYSTEM.resetTimer();
    Robot.CARGO_WHEEL_SUBSYSTEM.startTimer();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Robot.CARGO_WHEEL_SUBSYSTEM.bottomMotorSpit(Calibrations.cargoSpitPowerMagnitude);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    if (Robot.CARGO_WHEEL_SUBSYSTEM.getTimer() > Calibrations.cargoBottomMotorSpinTimout) {
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
