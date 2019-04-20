package frc.robot.commands.climber;

import frc.robot.Calibrations;
import frc.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class ClimberExtendFullyCommand extends Command {

	public ClimberExtendFullyCommand() {
		requires(Robot.CLIMBER_SUBSYSTEM);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		System.out.println("ClimberExtendFullyCommand init");
		Robot.CLIMBER_SUBSYSTEM.resetSafetyTimer();
		Robot.CLIMBER_SUBSYSTEM.startSafetyTimer();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (Robot.CLIMBER_SUBSYSTEM.isAtExtensionLimit() == false) {
    		Robot.CLIMBER_SUBSYSTEM.extend(Calibrations.climberExtendPowerMagnitude);
    	}
    	else {
    		Robot.CLIMBER_SUBSYSTEM.stop();
    	}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		if (Robot.CLIMBER_SUBSYSTEM.getSafetyTimer() > Calibrations.CLIMBER_SAFETY_TIMER_TIMEOUT) {
			System.out.println("TIMEOUT TIMEOUT TIMEOUT TIMEOUT");
			return true;
		}

		if (Robot.CLIMBER_SUBSYSTEM.isAtExtensionLimit()) {
			return true;
		}

		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.CLIMBER_SUBSYSTEM.resetEncodersToExtendedLimit();
		Robot.CLIMBER_SUBSYSTEM.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
