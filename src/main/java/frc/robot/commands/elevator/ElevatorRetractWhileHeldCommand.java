package frc.robot.commands.elevator;
import frc.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ElevatorRetractWhileHeldCommand extends Command {

    public ElevatorRetractWhileHeldCommand() {
        requires(Robot.ELEVATOR_SUBSYSTEM);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        //System.out.println("ElevatorRetractCommand init");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Robot.ELEVATOR_SUBSYSTEM.retract(0.1);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        Robot.ELEVATOR_SUBSYSTEM.stop();
    }
}
