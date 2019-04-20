package frc.robot.commands.misc;

import frc.robot.Robot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class RumbleDriveControllerCommand extends Command {
    private double _seconds;
    private Timer _timer;

    public RumbleDriveControllerCommand(double seconds) {
        _seconds = seconds;
        _timer = new Timer();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        System.out.println("RumbleDriveControllerCommand init");
        Robot.DRIVE_CONTROLLER.setRumbleOn();
        _timer.reset();
        _timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Robot.DRIVE_CONTROLLER.setRumbleOn();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return (_timer.get() > _seconds);
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.DRIVE_CONTROLLER.setRumbleOff();
        _timer.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        Robot.DRIVE_CONTROLLER.setRumbleOff();
        _timer.stop();
    }
}
