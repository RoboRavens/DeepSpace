package frc.robot.commands.elevator;

import frc.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ElevatorHoldPositionCommand extends Command {
	double targetPosition;

	public ElevatorHoldPositionCommand() {
		requires(Robot.ELEVATOR_SUBSYSTEM);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		this.targetPosition = Robot.ELEVATOR_SUBSYSTEM.getEncoderPosition();
		System.out.println("ElevatorHoldPositionCommand init");
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		// The goal of this command is to send a very small amount of power to the
		// elevator motors to fight against gravity - NOT to move the elevator, at all.
		if (Robot.ELEVATOR_SUBSYSTEM.isAtRetractionLimit() == false) {
			Robot.ELEVATOR_SUBSYSTEM.holdPosition();
		} else {
			Robot.ELEVATOR_SUBSYSTEM.stop();
		}
	}

	protected boolean isFinished() {
		// This command is never finished; once it is called, the only way it stops is
		// if another command overrides it.
		return false;
	}

}
