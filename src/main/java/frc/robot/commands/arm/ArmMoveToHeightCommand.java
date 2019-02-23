package frc.robot.commands.arm;

import frc.robot.Calibrations;
import frc.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ArmMoveToHeightCommand extends Command {
    int _targetEncoderPosition;

    public ArmMoveToHeightCommand(int encoderPosition) {
        requires(Robot.ARM_SUBSYSTEM);
        _targetEncoderPosition = encoderPosition;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.ARM_SUBSYSTEM.resetSafetyTimer();
        Robot.ARM_SUBSYSTEM.startSafetyTimer();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Robot.ARM_SUBSYSTEM.setMotorsPID(_targetEncoderPosition);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        boolean isFinished = false;

        if (Robot.ARM_SUBSYSTEM.getSafetyTimer() > Calibrations.ELEVATOR_MOVE_TO_POSITION_TIMEOUT) {
            isFinished = true;
        }

        if (Robot.ARM_SUBSYSTEM.getIsAtTarget(_targetEncoderPosition)) {
            Robot.ARM_SUBSYSTEM.stop();
            isFinished = true;
        }

        return isFinished;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.ARM_SUBSYSTEM.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
