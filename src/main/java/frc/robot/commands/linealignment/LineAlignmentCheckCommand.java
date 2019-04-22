package frc.robot.commands.linealignment;

import frc.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class LineAlignmentCheckCommand extends Command {

  public LineAlignmentCheckCommand() {
    requires(Robot.LINE_ALIGNMENT_SUBSYSTEM);
  }

  // Called just before this Command runs the first time
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  protected void execute() {
    if (Robot.LINE_ALIGNMENT_SUBSYSTEM.getIsAligned()) {
      Robot.LINE_ALIGNMENT_SUBSYSTEM.turnOn();
    } else {
      Robot.LINE_ALIGNMENT_SUBSYSTEM.turnOff();
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  protected boolean isFinished() {
      return true;
  }

  // Called once after isFinished returns true
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  protected void interrupted() {}
}