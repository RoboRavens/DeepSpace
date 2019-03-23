package frc.robot.commands.drivetrain;

import frc.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveTrainStopCommand extends Command {

    public DriveTrainStopCommand() {
        requires(Robot.DRIVE_TRAIN_SUBSYSTEM);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        System.out.println("DriveTrainStopCommand init");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.stop();
        Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.gyroStop();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.stop();
        Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.gyroStop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.stop();
        Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.gyroStop();
    }
}
