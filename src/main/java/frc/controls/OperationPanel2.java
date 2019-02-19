package frc.controls;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class OperationPanel2 {
	Joystick joystick;

	public OperationPanel2(int port) {
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
		case ROCKETHEIGHTHIGH:
			buttonNumber = 1;
			break;
		case ROCKETHEIGHTMID:
			buttonNumber = 2;
			break;
		case ROCKETHEIGHTLOW:
			buttonNumber = 3;
			break;
		case RUNAUTOMATEDCOMMAND:
			buttonNumber = 4;
			break;
		case BEAKRELEASEOVERRIDE:
			buttonNumber = 5;
			break;
		case CARGOSPITOVERRIDE:
			buttonNumber = 6;
			break;
		case TESTINGBUTTON:
			buttonNumber = 7;
			break;
		default:
			throw new IllegalArgumentException("Invalid Button Code");
		}

		return buttonNumber;
	}
}
