package frc.robot.subsystems;

import team401.LightLink;
import frc.controls.ButtonCode;
import frc.controls.Gamepad;
import frc.robot.Robot;
import frc.robot.commands.LED.LEDCommand;

import edu.wpi.first.wpilibj.command.Subsystem;

public class ProgrammableLEDSubsystem extends Subsystem {

	public static final Gamepad CONTROLLER = Robot.OPERATION_CONTROLLER;
	public LightLink led;

	public ProgrammableLEDSubsystem() {
		this.led = new LightLink();
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
		led.breathe(color, speed, 0);
		led.breathe(color, speed, 1);
		led.breathe(color, speed, 2);
		led.breathe(color, speed, 3);
	}

	public void race(int color, int speed) {
		led.race(color, speed, 0);
		led.race(color, speed, 1);
		led.race(color, speed, 2);
		led.race(color, speed, 3);
	}

	public void blink(int color, int speed) {
		led.blink(color, speed, 0);
		led.blink(color, speed, 1);
		led.blink(color, speed, 2);
		led.blink(color, speed, 3);
	}

	public void bounce(int color, int speed) {
		led.bounce(color, speed, 0);
		led.bounce(color, speed, 1);
		led.bounce(color, speed, 2);
		led.bounce(color, speed, 3);
	}

	public void solid(int color) {
		led.solid(color, 0);
		led.solid(color, 1);
		led.solid(color, 2);
		led.solid(color, 3);
	}

	public void rainbow(int speed) {
		led.rainbow(speed, 0);
		led.rainbow(speed, 1);
		led.rainbow(speed, 2);
		led.rainbow(speed, 3);
	}

	public void off() {
		led.off(0);
		led.off(1);
		led.off(2);
		led.off(3);
	}

	@Override
	protected void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new LEDCommand());
	}
}