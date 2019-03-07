package frc.robot.subsystems;

import frc.controls.ButtonCode;
import frc.ravenhardware.BufferedDigitalInput;
import frc.ravenhardware.RavenLighting;

import edu.wpi.first.wpilibj.command.Subsystem;

public class LineAlignmentSubsystem extends Subsystem {
	private BufferedDigitalInput _frontLineSensor;
	private BufferedDigitalInput _rearLineSensor;
  private RavenLighting _binaryLeds;
  
	public ArmSubsystem(BufferedDigitalInput frontLineSensor, BufferedDigitalInput rearLineSensor, RavenLighting binaryLeds) {
		_frontLineSensor = frontLineSensor;
		_rearLineSensor = rearLineSensor;
    _binaryLeds = binaryLeds;
  }

	public void initDefaultCommand() {
		setDefaultCommand(new LineAlignmentCheckCommand());
	}

	public void periodic() {
		_frontLineSensor.maintainState();
		_rearLineSensor.maintainState();
	}

  public boolean getIsAligned() {
    boolean aligned = false;
    
    if (getFrontSensorIsAligned() && getRearSensorIsAligned()) {
      aligned = true;
    }
    
    return aligned;      
  }

	public boolean getFrontSensorIsAligned() {
		return _frontLineSensor.get();
	}

	public boolean getRearSensorIsAligned() {
		return _rearLineSensor.get();
	}
  
  public void turnOn() {
    _binaryLeds.turnOn();
  }
  
  public void turnOff() {
    _binaryLeds.turnOff();
  }
}
