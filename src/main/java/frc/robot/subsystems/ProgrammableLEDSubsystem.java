package frc.robot.subsystems;

import team401.LightLink;
import frc.controls.ButtonCode;
import frc.controls.Gamepad;
import frc.robot.Robot;
import frc.robot.commands.LED.LEDBlinkFor2SecondsCommand;
import frc.robot.commands.LED.LEDCommand;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ProgrammableLEDSubsystem extends Subsystem {

	public static final Gamepad CONTROLLER = Robot.OPERATION_CONTROLLER;
	private LightLink _led;

	DriverStation driverStation = DriverStation.getInstance();

	LEDBlinkFor2SecondsCommand command90s = new LEDBlinkFor2SecondsCommand(4, false);
	LEDBlinkFor2SecondsCommand command60s = new LEDBlinkFor2SecondsCommand(3, false);
	LEDBlinkFor2SecondsCommand command30s = new LEDBlinkFor2SecondsCommand(2, false);
	LEDBlinkFor2SecondsCommand command10s = new LEDBlinkFor2SecondsCommand(1, false);

	public ProgrammableLEDSubsystem() {
		_led = new LightLink();
	}

	public void periodic() {
		if (getMatchIsAtTime(90)) {
			command90s.start();
		}
		if (getMatchIsAtTime(60)) {
			command60s.start();
		}
		if (getMatchIsAtTime(30)) {
			command30s.start();
		}
		if (getMatchIsAtTime(10)) {
			command10s.start();
		}

		if (command90s.isCompleted()) {
			command90s.close();
		}
		if (command60s.isCompleted()) {
			command60s.close();
		}
		if (command30s.isCompleted()) {
			command30s.close();
		}
		if (command10s.isCompleted()) {
			command10s.close();
		}
	}

	public boolean getMatchIsAtTime(int matchSecond) {
		double matchTime = driverStation.getMatchTime();
		if (matchTime > matchSecond - .5 && matchTime < matchSecond + .5) {
			return true;
		}

		return false;
	}

	public void run() {

		if (Robot.DRIVE_CONTROLLER.getButtonValue(ButtonCode.A)) {
			// off();
		}

		if (Robot.ELEVATOR_SUBSYSTEM.isAtExtensionLimit()) {
			// rainbow(0);
			// System.out.println("Lighting UP Lighting UP Lighting UP Lighting UP Lighting
			// UP Lighting UP Lighting UP");
		}
	}

	public void setDisabledPattern() {
		breathe(1, 0);
	}

	public void setEnabledPattern() {
		breathe(5, 0);
	}

	public void setAutonomousPattern() {
		rainbow(0);
	}

	public void setErrorPattern() {
		blink(1, 2);
	}

	public void breathe(int color, int speed) {
		_led.breathe(color, speed, 0);
		_led.breathe(color, speed, 1);
		_led.breathe(color, speed, 2);
		_led.breathe(color, speed, 3);
	}

	public void race(int color, int speed) {
		_led.race(color, speed, 0);
		_led.race(color, speed, 1);
		_led.race(color, speed, 2);
		_led.race(color, speed, 3);
	}

	public void blink(int color, int speed) {
		_led.blink(color, speed, 0);
		_led.blink(color, speed, 1);
		_led.blink(color, speed, 3);
	}

	public void bounce(int color, int speed) {
		_led.bounce(color, speed, 0);
		_led.bounce(color, speed, 1);
		_led.bounce(color, speed, 2);
		_led.bounce(color, speed, 3);
	}

	public void solid(int color) {
		_led.solid(color, 0);
		_led.solid(color, 1);
		_led.solid(color, 2);
		_led.solid(color, 3);
	}

	public void rainbow(int speed) {
		_led.rainbow(speed, 0);
		_led.rainbow(speed, 1);
		_led.rainbow(speed, 2);
		_led.rainbow(speed, 3);
	}

	public void off() {
		_led.off(0);
		_led.off(1);
		_led.off(2);
		_led.off(3);
	}

	@Override
	protected void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new LEDCommand());
	}
}