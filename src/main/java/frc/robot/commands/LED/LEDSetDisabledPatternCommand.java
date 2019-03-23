package frc.robot.commands.LED;

import frc.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class LEDSetDisabledPatternCommand extends Command {

	public LEDSetDisabledPatternCommand() {
		requires(Robot.LED_SUBSYSTEM);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		System.out.println("LEDSetDisabledPatternCommand init");
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.LED_SUBSYSTEM.setDisabledPattern();
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}

}
