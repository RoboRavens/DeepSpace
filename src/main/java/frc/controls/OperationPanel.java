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
		case ELEVATOROVERRIDEEXTEND:
			buttonNumber = 1;
			break;
		case ELEVATOROVERRIDERETRACT:
			buttonNumber = 2;
			break;
		case ARMOVERRIDEEXTEND:
			buttonNumber = 3;
			break;
		case ARMOVERRIDERETRACT:
			buttonNumber = 4;
			break;
		case CARGOSPITOVERRIDE:
			buttonNumber = 5;
			break;
		case BEAKRELEASEOVERRIDE:
			buttonNumber = 6;
			break;
		case CARGOORHATCHPANEL:
			buttonNumber = 7;
			break;
		case SETLOCATIONCARGOSHIP:
			buttonNumber = 8;
			break;
		case SETLOCATIONROCKET:
			buttonNumber = 9;
			break;
		case ROCKETHEIGHTHIGH:
			buttonNumber = 10;
			break;
		case ROCKETHEIGHTMID:
			buttonNumber = 11;
			break;
		case ROCKETHEIGHTLOW:
			buttonNumber = 12;
			break;
		default:
			throw new IllegalArgumentException("Invalid Button Code");
		}

		return buttonNumber;
	}
}
