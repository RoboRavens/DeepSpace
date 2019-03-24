package frc.robot.commands.climber;
import frc.robot.Calibrations;
import frc.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ClimberExtendWhileHeldCommandCZB extends Command {

    public ClimberExtendWhileHeldCommandCZB() {
        requires(Robot.CLIMBER_SUBSYSTEM);
        requires(Robot.ARM_SUBSYSTEM);
        requires(Robot.ELEVATOR_SUBSYSTEM);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Robot.CLIMBER_SUBSYSTEM.extend(Calibrations.climberExtendPowerMagnitude);
        Robot.ELEVATOR_SUBSYSTEM.retract(Calibrations.elevatorClimbingRetractionPowerMagnitude);
        Robot.ARM_SUBSYSTEM.extendIfNotAtExtensionLimit(Calibrations.armExtendWhileClimbingPowerMagnitude);
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
        Robot.CLIMBER_SUBSYSTEM.stop();
        Robot.ARM_SUBSYSTEM.stop();
    }
}
