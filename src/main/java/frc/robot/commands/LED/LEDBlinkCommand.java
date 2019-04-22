package frc.robot.commands.LED;

import frc.robot.Robot;

import java.awt.Color;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class LEDBlinkCommand extends Command {
    private Timer _timer = new Timer();
    private Color _color;
    private float _duration;
    private double _timeOfLastColorChange;
    private boolean _lastColorChangeIsOff;

    public LEDBlinkCommand(Color color, float duration) {
        requires(Robot.PROGRAMMABLE_LED_SUBSYSTEM);
        _color = color;
        _duration = duration;
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
        System.out.println("LEDBlinkFor2SecondsCommand init");
        _timer.start();
        _timeOfLastColorChange = 0;
        _lastColorChangeIsOff = false;
        Robot.PROGRAMMABLE_LED_SUBSYSTEM.SetLEDColor(_color);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (_timeOfLastColorChange - _timer.get() < .25) {
            return;
        }

        _timeOfLastColorChange = _timer.get();
        if (_lastColorChangeIsOff) {
            _lastColorChangeIsOff = false;
            Robot.PROGRAMMABLE_LED_SUBSYSTEM.SetLEDColor(_color);
        } else {
            _lastColorChangeIsOff = true;
            Robot.PROGRAMMABLE_LED_SUBSYSTEM.off();
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        boolean isFinished = false;
        if (_timer.get() > _duration) {
            isFinished = true;
            Robot.PROGRAMMABLE_LED_SUBSYSTEM.setColorToTeleop();
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
