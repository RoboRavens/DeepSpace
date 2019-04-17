package frc.robot.commands.cargowheel;

import frc.robot.Calibrations;
import frc.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class CargoWheelSuckCommand extends Command {

    public CargoWheelSuckCommand() {
        requires(Robot.CARGO_WHEEL_SUBSYSTEM);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        System.out.println("CargoWheelSuckCommand init");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Robot.CARGO_WHEEL_SUBSYSTEM.topMotorSuck(Calibrations.cargoWheelSuckPowerMagnitude);
        Robot.CARGO_WHEEL_SUBSYSTEM.bottomMotorSuck(Calibrations.cargoWheelSuckPowerMagnitude);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
