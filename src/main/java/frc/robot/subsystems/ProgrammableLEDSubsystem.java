package frc.robot.subsystems;

import frc.robot.Robot;
import frc.robot.commands.LED.LEDDuringMatchCommand;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;

import java.awt.Color;

import com.ctre.phoenix.CANifier;

public class ProgrammableLEDSubsystem extends Subsystem {
	private static CANifier _canifier;
	DriverStation driverStation = DriverStation.getInstance();
	private boolean _teleop = false;

	public ProgrammableLEDSubsystem() {
		_canifier = new CANifier(0);
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new LEDDuringMatchCommand());
	}

	public void setTeleopMode() {
		_teleop = true;
		this.setColorToTeleop();
	}

	public void setColorToTeleop(){
		if (Robot.isRedAlliance) {
			this.SetLEDColor(Color.RED);
		} else {
			this.SetLEDColor(Color.BLUE);				
		}	
	}

	public boolean isTeleopMode(){
		return _teleop;
	}

	public long getMatchSecond() {
		return Math.round(driverStation.getMatchTime());
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

	public boolean getMatchIsAtOrBetweenTimes(int desiredMatchSecond1, int desiredMatchSecond2) {
		double currentMatchTime = driverStation.getMatchTime();
		if (currentMatchTime < desiredMatchSecond1 && currentMatchTime > desiredMatchSecond2) {
			return true;
		}
		return false;
	}

	public void setDisabledMode() {
		_teleop = false;
		this.SetLEDColor(Color.RED);
	}

	public void setAutonomousMode() {
		_teleop = false;		
	}

	public void setTestMode(){
		_teleop = false;
	}

	public void setGamePiecePosessedPattern() {
		this.SetLEDColor(new Color(192, 0, 64));
	}

	public void off() {
		this.SetLEDColor(0, 0, 0);
	}

	public void SetLEDColor(Color color) {
		float red = (color.getRed() / 256);
		float green = (color.getGreen() / 256);
		float blue = (color.getBlue() / 256);
		this.SetLEDColor(red, green, blue);
	}
	
	private void SetLEDColor(float red, float green, float blue) {
		_canifier.setLEDOutput(green, CANifier.LEDChannel.LEDChannelA); 
		_canifier.setLEDOutput(red, CANifier.LEDChannel.LEDChannelB); 
		_canifier.setLEDOutput(blue, CANifier.LEDChannel.LEDChannelC); 
	}
}