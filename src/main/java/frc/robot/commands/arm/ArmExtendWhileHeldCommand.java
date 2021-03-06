package frc.robot.commands.arm;
import frc.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class ArmExtendWhileHeldCommand extends Command {

    public ArmExtendWhileHeldCommand() {
        requires(Robot.ARM_SUBSYSTEM);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        System.out.println("ArmExtendWhileHeldCommand init");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Robot.ARM_SUBSYSTEM.extend(0.5);
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
        Robot.ARM_SUBSYSTEM.stop();
    }
}
