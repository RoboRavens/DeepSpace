package frc.robot.subsystems;

import team401.LightLink;
import frc.controls.ButtonCode;
import frc.controls.Gamepad;
import frc.robot.Robot;
import frc.robot.commands.LED.LEDCommand;

import edu.wpi.first.wpilibj.command.Subsystem;

public class ProgrammableLEDSubsystem extends Subsystem {

	public static final Gamepad CONTROLLER = Robot.OPERATION_CONTROLLER;
	private LightLink _led;

	public ProgrammableLEDSubsystem() {
		_led = new LightLink();
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