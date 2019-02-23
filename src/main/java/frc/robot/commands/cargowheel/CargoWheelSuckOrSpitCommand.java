/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.cargowheel;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class CargoWheelSuckOrSpitCommand extends Command {
  private double _magnitude;
  private String _suckOrSpit;
  private Timer _timer;

  public CargoWheelSuckOrSpitCommand(double magnitude, String suckOrSpit) {
    requires(Robot.CARGO_WHEEL_SUBSYSTEM);
    this._magnitude = magnitude;
    this._suckOrSpit = suckOrSpit;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    _timer.reset();
    _timer.start();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if (this._suckOrSpit == "Suck" || this._suckOrSpit == "suck") {
      Robot.CARGO_WHEEL_SUBSYSTEM.suck(this._magnitude);
    }
    if (this._suckOrSpit == "Spit" || this._suckOrSpit == "spit") {
      Robot.CARGO_WHEEL_SUBSYSTEM.spit(this._magnitude);
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    if (_timer.get() > 1.0) {
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
