package frc.robot.commands.cargointake;

import frc.robot.Calibrations;
import frc.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class CargoWheelPushSoftCommand extends Command {

    public CargoWheelPushSoftCommand() {
        requires(Robot.CARGO_WHEEL_SUBSYSTEM);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println("CargoWheelPushSoftCommandCargoWheelPushSoftCommandCargoWheelPushSoftCommandCargoWheelPushSoftCommandCargoWheelPushSoftCommandCargoWheelPushSoftCommandCargoWheelPushSoftCommand");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.CARGO_WHEEL_SUBSYSTEM.push(Calibrations.cargoDropPowerMagnitude);
    	//System.out.println("CargoWheelSubsystem.push();CargoWheelSubsystem.push();CargoWheelSubsystem.push();CargoWheelSubsystem.push();CargoWheelSubsystem.push();CargoWheelSubsystem.push();CargoWheelSubsystem.push();CargoWheelSubsystem.push();CargoWheelSubsystem.push();");
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	// Robot.CARGO_WHEEL_SUBSYSTEM.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.CARGO_WHEEL_SUBSYSTEM.stop();
    }
}
