package frc.robot.subsystems;

import team401.LightLink;
import frc.controls.ButtonCode;
import frc.controls.Gamepad;
import frc.robot.Robot;
import frc.robot.commands.LED.LEDBlinkFor2SecondsCommand;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;

import edu.wpi.first.wpilibj.Joystick;
import com.ctre.phoenix.CANifier;

public class ProgrammableLEDSubsystem extends Subsystem {
	public static CANifier canifier = new CANifier(0);
	DriverStation driverStation = DriverStation.getInstance();

	LEDBlinkFor2SecondsCommand command90s = new LEDBlinkFor2SecondsCommand(4, false);
	LEDBlinkFor2SecondsCommand command60s = new LEDBlinkFor2SecondsCommand(3, false);
	LEDBlinkFor2SecondsCommand command30s = new LEDBlinkFor2SecondsCommand(2, false);
	LEDBlinkFor2SecondsCommand command10s = new LEDBlinkFor2SecondsCommand(1, false);

	public ProgrammableLEDSubsystem() {

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

	public void setDisabledPattern() {
		this.SetLEDColor(100, 0, 0);
	}

	public void setEnabledPattern() {
		this.SetLEDColor(0, 100, 0);
	}

	public void setSandstormPattern() {
		this.SetLEDColor(red, green, blue);
	}

	public void setErrorPattern() {
		blink(1, 2);
	}



	




	public void off() {
		this.SetLEDColor(0, 0, 0);
	}
	private void SetLEDColor(float red, float green, float blue) {
		canifier.setLEDOutput(green, CANifier.LEDChannel.LEDChannelA); 
		canifier.setLEDOutput(red, CANifier.LEDChannel.LEDChannelB); 
		canifier.setLEDOutput(blue, CANifier.LEDChannel.LEDChannelC); 
	}

	@Override
	protected void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new LEDCommand());
	}
}