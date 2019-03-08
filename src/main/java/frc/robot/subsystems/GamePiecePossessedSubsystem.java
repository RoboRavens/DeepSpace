package frc.robot.subsystems;

import frc.ravenhardware.RavenLighting;
import frc.robot.RobotMap;
import frc.robot.commands.gamepiecepossessed.GamePiecePossessedCheckCommand;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;

public class GamePiecePossessedSubsystem extends Subsystem {
	private Relay _lightingRelay;
	private RavenLighting _binaryLeds;
	private boolean _hasGamePiece = true;
  
	public GamePiecePossessedSubsystem() {
		_lightingRelay = new Relay(RobotMap.hasGamePieceRelay);
		_binaryLeds = new RavenLighting(_lightingRelay);
  }

	public void initDefaultCommand() {
		setDefaultCommand(new GamePiecePossessedCheckCommand());
	}

	public void periodic() {
	}
  
  public void turnOn() {
    _binaryLeds.turnOn();
  }
  
  public void turnOff() {
    _binaryLeds.turnOff();
	}
	
	public void setHasGamePiece(boolean hasGamePiece) {
		_hasGamePiece = hasGamePiece;
	}

	public boolean getHasGamePiece() {
		return _hasGamePiece;
	}
}
