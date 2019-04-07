package frc.robot.subsystems;

import frc.robot.Robot;
import frc.robot.commands.LED.LEDBlinkFor2SecondsCommand;
import frc.robot.commands.LED.LEDDuringMatchCommand;
import frc.robot.commands.misc.WaitCommand;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.phoenix.CANifier;

public class ProgrammableLEDSubsystem extends Subsystem {
	public static CANifier canifier = new CANifier(0);
	DriverStation driverStation = DriverStation.getInstance();

	LEDBlinkFor2SecondsCommand command90s = new LEDBlinkFor2SecondsCommand(0, 100, 0);
	LEDBlinkFor2SecondsCommand command60s = new LEDBlinkFor2SecondsCommand(66, 33, 0);
	LEDBlinkFor2SecondsCommand command30s = new LEDBlinkFor2SecondsCommand(100, 100, 0);
	LEDBlinkFor2SecondsCommand command10s = new LEDBlinkFor2SecondsCommand(100, 0, 0);

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

	public boolean getMatchIsAtTime(int desiredMatchSecond) {
		double currentMatchTime = driverStation.getMatchTime();
		if (currentMatchTime > desiredMatchSecond - .5 && currentMatchTime < desiredMatchSecond + .5) {
			return true;
		}
		return false;
	}

	public boolean getMatchIsAtOrAfterTime(int desiredMatchSecond) {
		double currentMatchTime = driverStation.getMatchTime();
		if (currentMatchTime <= desiredMatchSecond) {
			return true;
		}
		return false;
	}

	public boolean getMatchIsAtOrAfterOneTimeAndAtOrBeforeAnotherTime(int desiredMatchSecond1, int desiredMatchSecond2) {
		double currentMatchTime = driverStation.getMatchTime();
		if (currentMatchTime <= desiredMatchSecond1 && currentMatchTime >= desiredMatchSecond2) {
			return true;
		}
		return false;
	}

	public void blink(float red, float green, float blue)  {
		WaitCommand wait = new WaitCommand(0.2);
		boolean _switch = true;
		if (_switch) {
			this.SetLEDColor(red, green, blue);
			wait.start();
			_switch = false;
		}
		if (_switch == false) {
			this.off();
			wait.start();
			_switch = true;
		}
	}

	public void setDisabledPattern() {
		this.SetLEDColor(75, 0, 25);
	}

	public void setEnabledPattern() {
		this.SetLEDColor(0, 100, 0);
	}

	public void setSandstormPattern() {
		this.SetLEDColor(100, 0, 100);
	}

	public void setGamePiecePosessedPattern() {
		if (Robot.isRedAlliance) {
			this.SetLEDColor(100, 0, 0);
		} else {
			this.SetLEDColor(0, 0, 100);				
		}				
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
		setDefaultCommand(new LEDDuringMatchCommand());
	}
}