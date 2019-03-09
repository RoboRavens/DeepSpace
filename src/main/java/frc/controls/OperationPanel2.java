package frc.controls;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class OperationPanel2 {
	private Joystick _joystick;

	public OperationPanel2(int port) {
		_joystick = new Joystick(port);
	}

	public boolean getButtonValue(ButtonCode button) {
		return _joystick.getRawButton(getButtonNumber(button));
	}

	public JoystickButton getButton(ButtonCode button) {
		return new JoystickButton(_joystick, getButtonNumber(button));
	}

	public int getButtonNumber(ButtonCode button) {
		int buttonNumber;

		switch (button) {
		case HATCHOVERRIDE:
			buttonNumber = 1;
			break;
		case CARGOOVERRIDE:
			buttonNumber = 2;
			break;
		case READYTOCOLLECT:
			buttonNumber = 3;
			break;
		case RETRACTALL:
			buttonNumber = 4;
			break;
		case CLIMBEROVERRIDERETRACT:
			buttonNumber = 5;
			break;
		case CLIMBEROVERRIDEEXTEND:
			buttonNumber = 6;
			break;
		case CARGOHPS:		
			buttonNumber = 7;
			break;
		default:
			throw new IllegalArgumentException("Invalid Button Code: " + button);
		}

		return buttonNumber;
	}
}
