package frc.robot.subsystems;

import frc.robot.Robot;
import frc.robot.commands.gamepiecepossessed.GamePiecePossessedCheckCommand;
import edu.wpi.first.wpilibj.command.Subsystem;

public class GamePiecePossessedSubsystem extends Subsystem {
	private boolean _hasGamePiece = true;
  
	public GamePiecePossessedSubsystem() {
  }

	public void initDefaultCommand() {
		setDefaultCommand(new GamePiecePossessedCheckCommand());
	}

	public void periodic() {
	}
  
  public void turnOn() {
    // Robot.PROGRAMMABLE_LED_SUBSYSTEM.setGamePiecePosessedPattern();
  }
  
  public void turnOff() {
    // Robot.PROGRAMMABLE_LED_SUBSYSTEM.setMatchDefaultPattern();
	}
	
	public void setHasGamePiece(boolean hasGamePiece) {
		_hasGamePiece = hasGamePiece;
	}

	public boolean getHasGamePiece() {
		return _hasGamePiece;
	}
}
