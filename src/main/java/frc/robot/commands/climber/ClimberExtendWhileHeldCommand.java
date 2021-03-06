package frc.robot.commands.climber;
import frc.robot.Calibrations;
import frc.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ClimberExtendWhileHeldCommand extends Command {

    public ClimberExtendWhileHeldCommand() {
        requires(Robot.CLIMBER_SUBSYSTEM);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        System.out.println("ClimberExtendWhileHeldCommand init");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Robot.CLIMBER_SUBSYSTEM.extend(Calibrations.climberExtendPowerMagnitude);
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
        Robot.CLIMBER_SUBSYSTEM.stop();
    }
}
