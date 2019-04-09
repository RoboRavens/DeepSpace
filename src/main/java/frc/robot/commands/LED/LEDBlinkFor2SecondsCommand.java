package frc.robot.commands.LED;

import frc.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class LEDBlinkFor2SecondsCommand extends Command {
    private Timer _timer = new Timer();
    private float _red;
    private float _green;
    private float _blue;
    private float _duration;

    public LEDBlinkFor2SecondsCommand(float red, float green, float blue, float duration) {
        requires(Robot.PROGRAMMABLE_LED_SUBSYSTEM);
        _red = red;
        _green = green;
        _blue = blue;
        _duration = duration;
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
        _timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        boolean isFinished = false;
        if (_timer.get() > 2) {
            isFinished = true;
        }

        return isFinished;
    }

    // Called once after isFinished returns true
    protected void end() {
        _timer.stop();
        _timer.reset();
        
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
