package frc.robot.subsystems;

import frc.robot.Robot;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.phoenix.CANifier;

public class ProgrammableLEDSubsystem extends Subsystem {
	private static CANifier _canifier;
	DriverStation driverStation = DriverStation.getInstance();

	public ProgrammableLEDSubsystem() {
		_canifier = new CANifier(0);
	}

	@Override
	protected void initDefaultCommand() {
		//setDefaultCommand(new LEDDuringMatchCommand());
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

	public void setDisabledPattern() {
		this.SetLEDColor(100, 0, 0);
	}

	public void setEnabledPattern() {
		this.SetLEDColor(0, 75, 0);
	}

	public void setMatchDefaultPattern() {
		if (Robot.isRedAlliance) {
			this.SetLEDColor(100, 0, 0);
		} else {
			this.SetLEDColor(0, 0, 100);				
		}			
	}

	public void setGamePiecePosessedPattern() {
		this.SetLEDColor(75, 0, 25);
	}

	public void off() {
		this.SetLEDColor(0, 0, 0);
	}
	
	private void SetLEDColor(float red, float green, float blue) {
		_canifier.setLEDOutput(green, CANifier.LEDChannel.LEDChannelA); 
		_canifier.setLEDOutput(red, CANifier.LEDChannel.LEDChannelB); 
		_canifier.setLEDOutput(blue, CANifier.LEDChannel.LEDChannelC); 
	}
}