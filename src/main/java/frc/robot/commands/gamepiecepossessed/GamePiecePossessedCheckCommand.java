package frc.robot.commands.gamepiecepossessed;

import frc.robot.Calibrations;
import frc.robot.Robot;
import frc.robot.commands.misc.RumbleDriveControllerCommand;
import edu.wpi.first.wpilibj.command.Command;

public class GamePiecePossessedCheckCommand extends Command {

    public GamePiecePossessedCheckCommand() {
        requires(Robot.GAME_PIECE_POSSESSED_SUBSYSTEM);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        System.out.println("GamePiecePossessedCheckCommand init");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        // If we possess either type of piece, turn on the lights.
        if (Robot.BEAK_SUBSYSTEM.getHatchPanelSensor() || Robot.CARGO_WHEEL_SUBSYSTEM.hasCargoStrict()) {
            Robot.GAME_PIECE_POSSESSED_SUBSYSTEM.turnOn();

            if (Robot.GAME_PIECE_POSSESSED_SUBSYSTEM.getHasGamePiece() == false) {
                Command rumbleCommand = new RumbleDriveControllerCommand(Calibrations.gamePieceCollectedRumbleSeconds);
                rumbleCommand.start();
                rumbleCommand.close();

                Robot.GAME_PIECE_POSSESSED_SUBSYSTEM.setHasGamePiece(true);
            }
        }
        else {
            Robot.GAME_PIECE_POSSESSED_SUBSYSTEM.turnOff();
            Robot.GAME_PIECE_POSSESSED_SUBSYSTEM.setHasGamePiece(false);
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}