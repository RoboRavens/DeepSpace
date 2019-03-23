package frc.robot.commands.drivetrain;

import frc.robot.Calibrations;
import frc.robot.Robot;
import frc.util.PCDashboardDiagnostics;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class DriveTrainDriveInchesCommand extends Command {	
	private double _powerMagnitude;
	private double _totalInchesToTravel;
	private double _driveTrainNetInchesTraveledAtStart;
	private double _netInchesTraveledSoFar = 0;
	private int _direction;
	private Timer _timeoutTimer;
	private double _timeoutSeconds = Calibrations.DriveTrainDriveInchesSafetyTimerSeconds;
	
    public DriveTrainDriveInchesCommand(double inchesToTravel, double powerMagnitude, int direction) {
    	requires(Robot.DRIVE_TRAIN_SUBSYSTEM);
    	this._totalInchesToTravel = inchesToTravel;
    	this._powerMagnitude = powerMagnitude *= direction;
    	this._direction = direction;
    	this._timeoutTimer = new Timer();
    }
    
    public DriveTrainDriveInchesCommand(double inchesToTravel, double powerMagnitude, int direction, double timeoutSeconds) {
    	requires(Robot.DRIVE_TRAIN_SUBSYSTEM);
    	this._totalInchesToTravel = inchesToTravel;
    	this._powerMagnitude = powerMagnitude *= direction;
    	this._direction = direction;
    	this._timeoutTimer = new Timer();
    	this._timeoutSeconds = timeoutSeconds;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
		System.out.println("DriveTrainDriveInchesCommand init");
    	System.out.println("RT NIT:" + Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.getNetInchesTraveled());
    	_driveTrainNetInchesTraveledAtStart = Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.getNetInchesTraveled();
    	_timeoutTimer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.fpsTankManual(_powerMagnitude, 0);
    	
    	if (_direction == Calibrations.drivingBackward) {
    		_netInchesTraveledSoFar = _driveTrainNetInchesTraveledAtStart - Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.getNetInchesTraveled();
    	} else {
    		_netInchesTraveledSoFar = Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.getNetInchesTraveled() - _driveTrainNetInchesTraveledAtStart;
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	
        boolean hasTraveledTargetDistance = (_netInchesTraveledSoFar >= _totalInchesToTravel); 
        PCDashboardDiagnostics.AdHocNumber("netInchesTraveledSoFar", _netInchesTraveledSoFar);
        PCDashboardDiagnostics.AdHocNumber("totalInchesToTravel", _totalInchesToTravel);
        
        if (_timeoutTimer.get() > _timeoutSeconds) {
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
