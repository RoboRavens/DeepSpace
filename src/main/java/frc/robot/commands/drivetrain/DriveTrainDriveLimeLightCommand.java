package frc.robot.commands.drivetrain;

import frc.controls.AxisCode;
import frc.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveTrainDriveLimeLightCommand extends Command {

    public DriveTrainDriveLimeLightCommand() {
        requires(Robot.DRIVE_TRAIN_SUBSYSTEM);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double leftYAxisValue = Robot.DRIVE_CONTROLLER.getAxis(AxisCode.LEFTSTICKY);
        double rightXAxisValue = Robot.DRIVE_CONTROLLER.getAxis(AxisCode.RIGHTSTICKX);
        Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.fpsTankChooseLimelightOrManual(leftYAxisValue, rightXAxisValue);
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
    }
}
