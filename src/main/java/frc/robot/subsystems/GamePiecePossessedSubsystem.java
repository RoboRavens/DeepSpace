package frc.robot.subsystems;

import frc.ravenhardware.BufferedDigitalInput;
import frc.ravenhardware.RavenLighting;
import frc.robot.RobotMap;
import frc.robot.commands.gamepiecepossessed.GamePiecePossessedCheckCommand;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;

public class GamePiecePossessedSubsystem extends Subsystem {
	private BufferedDigitalInput _frontLineSensor;
	private BufferedDigitalInput _rearLineSensor;
	private Relay _lightingRelay;
  private RavenLighting _binaryLeds;
  
	public GamePiecePossessedSubsystem() {
		_frontLineSensor = new BufferedDigitalInput(RobotMap.frontLineSensor);
		_rearLineSensor = new BufferedDigitalInput(RobotMap.rearLineSensor);
		_lightingRelay= new Relay(RobotMap.lineAlignmentRelay);
		_binaryLeds = new RavenLighting(_lightingRelay);

  }

	public void initDefaultCommand() {
		setDefaultCommand(new GamePiecePossessedCheckCommand());
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
