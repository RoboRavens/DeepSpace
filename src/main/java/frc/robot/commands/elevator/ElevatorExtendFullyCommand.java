package frc.robot.commands.elevator;

import frc.robot.Calibrations;
import frc.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ElevatorExtendFullyCommand extends Command {

	public ElevatorExtendFullyCommand() {
		requires(Robot.ELEVATOR_SUBSYSTEM);

	}

	// Called just before this Command runs the first time
	protected void initialize() {
		System.out.println("ElevatorExtendFullyCommand init");
		Robot.ELEVATOR_SUBSYSTEM.resetSafetyTimer();
		Robot.ELEVATOR_SUBSYSTEM.startSafetyTimer();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.ELEVATOR_SUBSYSTEM.setMotorsPID(Calibrations.elevatorEncoderMaximumValue);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		if (Robot.ELEVATOR_SUBSYSTEM.getSafetyTimer() > Calibrations.ELEVATOR_SAFETY_TIMER_TIMEOUT) {
			System.out.println("TIMEOUT TIMEOUT TIMEOUT TIMEOUT");
			return true;
		}

		if (Robot.ELEVATOR_SUBSYSTEM.isAtExtensionLimit()) {
			return true;
		}

		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.ELEVATOR_SUBSYSTEM.expectElevatorToBeAtExtensionLimit();
		Robot.ELEVATOR_SUBSYSTEM.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
