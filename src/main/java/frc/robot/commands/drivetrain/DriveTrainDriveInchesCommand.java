package frc.robot.commands.drivetrain;

import frc.robot.Calibrations;
import frc.robot.Robot;
import frc.util.PCDashboardDiagnostics;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class DriveTrainDriveInchesCommand extends Command {	
	double powerMagnitude;
	double totalInchesToTravel;
	double driveTrainNetInchesTraveledAtStart;
	double netInchesTraveledSoFar = 0;
	int direction;
	Timer timeoutTimer;
	double timeoutSeconds = Calibrations.DriveTrainDriveInchesSafetyTimerSeconds;
	
    public DriveTrainDriveInchesCommand(double inchesToTravel, double powerMagnitude, int direction) {
    	requires(Robot.DRIVE_TRAIN_SUBSYSTEM);
    	this.totalInchesToTravel = inchesToTravel;
    	this.powerMagnitude = powerMagnitude *= direction;
    	this.direction = direction;
    	this.timeoutTimer = new Timer();
    }
    
    public DriveTrainDriveInchesCommand(double inchesToTravel, double powerMagnitude, int direction, double timeoutSeconds) {
    	requires(Robot.DRIVE_TRAIN_SUBSYSTEM);
    	this.totalInchesToTravel = inchesToTravel;
    	this.powerMagnitude = powerMagnitude *= direction;
    	this.direction = direction;
    	this.timeoutTimer = new Timer();
    	this.timeoutSeconds = timeoutSeconds;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println("RT NIT:" + Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.getNetInchesTraveled());
    	driveTrainNetInchesTraveledAtStart = Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.getNetInchesTraveled();
    	timeoutTimer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.fpsTank(powerMagnitude, 0);
    	
    	if (direction == Calibrations.drivingBackward) {
    		netInchesTraveledSoFar = driveTrainNetInchesTraveledAtStart - Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.getNetInchesTraveled();
    	} else {
    		netInchesTraveledSoFar = Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.getNetInchesTraveled() - driveTrainNetInchesTraveledAtStart;
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	
        boolean hasTraveledTargetDistance = (netInchesTraveledSoFar >= totalInchesToTravel); 
        PCDashboardDiagnostics.AdHocNumber("netInchesTraveledSoFar", netInchesTraveledSoFar);
        PCDashboardDiagnostics.AdHocNumber("totalInchesToTravel", totalInchesToTravel);
        
        if (timeoutTimer.get() > timeoutSeconds) {
        	hasTraveledTargetDistance = true;

        	System.out.println("TIMEOUT TIMEOUT TIMEOUT TIMEOUT");
        }
    	
    	return hasTraveledTargetDistance;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
