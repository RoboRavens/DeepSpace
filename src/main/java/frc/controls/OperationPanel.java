package frc.controls;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class OperationPanel {
	Joystick joystick;

	public OperationPanel(int port) {
		joystick = new Joystick(port);
	}

	public boolean getButtonValue(ButtonCode button) {
		return joystick.getRawButton(getButtonNumber(button));
	}

	public JoystickButton getButton(ButtonCode button) {
		return new JoystickButton(joystick, getButtonNumber(button));
	}

	public int getButtonNumber(ButtonCode button) {
		int buttonNumber;

		switch (button) {
		case ELEVATOROVERRIDERETRACT:
			buttonNumber = 5;
			break;
		case ELEVATOROVERRIDEEXTEND:
			buttonNumber = 6;
			break;
		case ARMOVERRIDERETRACT:
			buttonNumber = 7;
			break;
		case ARMOVERRIDEEXTEND:
			buttonNumber = 8;
			break;
		case SETLOCATIONROCKET:
			buttonNumber = 9;
			break;
		case SETHATCH:
			buttonNumber = 10;
			break;
		case SETLOCATIONCARGOSHIP:
			buttonNumber = 11;
			break;
		case SETCARGO:
			buttonNumber = 12;
			break;
		default:
			throw new IllegalArgumentException("Invalid Button Code");
		}

		return buttonNumber;
	}
}
