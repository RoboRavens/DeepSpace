package frc.robot.commands.arm;

import frc.robot.Calibrations;
import frc.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ArmRetractFullyCommand extends Command {

    public ArmRetractFullyCommand() {
        requires(Robot.ARM_SUBSYSTEM);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Robot.ARM_SUBSYSTEM.setMotorsPID(Calibrations.armEncoderRetractedValue);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        boolean isFinished = false;
        if (Robot.ARM_SUBSYSTEM.getIsAtRetractionLimit()) {
            Robot.ARM_SUBSYSTEM.stop();
            isFinished = true;
        }
        return isFinished;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.ARM_SUBSYSTEM.expectArmToBeAtRetractionLimit();
        Robot.ARM_SUBSYSTEM.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
