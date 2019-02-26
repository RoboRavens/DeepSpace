package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.ravenhardware.BufferedValue;
import frc.robot.Calibrations;
import frc.robot.Robot;
import frc.robot.commands.drivetrain.DriveTrainDriveInchesCommand;
import frc.robot.commands.drivetrain.DriveTrainStopCommand;
import frc.util.PCDashboardDiagnostics;

/**
 *
 */
public class LimelightSubsystem extends Subsystem {
	static double Limit = 0.00;
	edu.wpi.first.networktables.NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
	NetworkTableEntry tx = table.getEntry("tx");
	NetworkTableEntry ty = table.getEntry("ty");
	NetworkTableEntry ta = table.getEntry("ta");
	NetworkTableEntry tv = table.getEntry("tv");
	NetworkTableEntry ledMode = table.getEntry("ledMode");

	private double _heightDifference = Calibrations.FLOOR_TO_TARGET_CENTER_HEIGHT - Calibrations.FLOOR_TO_LIMELIGHT_LENS_HEIGHT;
	private double _angleToTargetFromHorizontal = Math.tan(Math.toRadians(Calibrations.CAMERA_ANGLE_OFFSET_FROM_HORIZONTAL + ty.getDouble(0.0)));
	private double _inchesToTarget = _heightDifference/_angleToTargetFromHorizontal;
	private double _powerMagnitude = 0.0;
  	private double _distanceDesiredFromTarget = 0.0;
  	private double _distanceToDrive = 0.0;
	private int _direction = 0;
  	double timeoutSeconds = Calibrations.DriveTrainDriveInchesSafetyTimerSeconds;
  	DriveTrainDriveInchesCommand driveTrainDriveInchesCommand = new DriveTrainDriveInchesCommand(_distanceToDrive, _powerMagnitude, _direction);

	private BufferedValue bufferedAngleOffHorizontal = new BufferedValue(9);

	public void initDefaultCommand() {

	}

	public void periodic() {
		table.getEntry("ledMode").setNumber(0);
		table.getEntry("camMode").setNumber(0);
		PCDashboardDiagnostics.SubsystemNumber("Limelight", "TargetArea", this.getTargetArea());
		PCDashboardDiagnostics.SubsystemNumber("Limelight", "angleOffHorizontal", this.angleOffHorizontal());
		PCDashboardDiagnostics.SubsystemNumber("Limelight", "angleOffVertical", this.angleOffVertical());
		PCDashboardDiagnostics.SubsystemBoolean("Limelight", "hasTarget", this.hasTarget());
		PCDashboardDiagnostics.AdHocNumber("Vision Tracking Distance (Inches)", (_heightDifference / _angleToTargetFromHorizontal));
		PCDashboardDiagnostics.AdHocNumber("Height Difference", _heightDifference);
		PCDashboardDiagnostics.AdHocNumber("Angle From Crosshair to Target", _angleToTargetFromHorizontal);
	
		bufferedAngleOffHorizontal.maintainState(this.angleOffHorizontal());
	}

	public double getTargetArea() {
		return ta.getDouble(0);
	}

	public boolean hasTarget() {
		boolean hasTarget;
		if (tv.getDouble(0) == 1) {
			hasTarget = true;
		} else {
			hasTarget = false;
		}
		return hasTarget;
	}

	public double angleOffHorizontal() {
		return tx.getDouble(0);
	}

	public double angleOffVertical() {
		return ty.getDouble(0);
	}

	public static void limeLightDiagnostics() {

	}

	public void turnToTarget() {
		Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.setGyroTargetHeading(Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.getCurrentHeading() + (tx.getDouble(0.0)));
	}

	public void driveToTarget(double distanceDesiredFromTarget) {
		_distanceDesiredFromTarget = distanceDesiredFromTarget;

		if (_inchesToTarget > (_distanceDesiredFromTarget + 18)) {
			_distanceToDrive = _inchesToTarget - _distanceDesiredFromTarget;
			_powerMagnitude = 0.6;
			_direction = Calibrations.drivingForward;
			this.driveTrainDriveInchesCommand.start();
			System.out.println("MOVE FORWARD " + _distanceToDrive + " INCHES");

		} else if (_inchesToTarget < (_distanceDesiredFromTarget - 18)) {
			_distanceToDrive = _distanceDesiredFromTarget - _inchesToTarget;
			_powerMagnitude = 0.6;
			_direction = Calibrations.drivingForward;
			this.driveTrainDriveInchesCommand.start();
			System.out.println("BACKING UP " + _distanceToDrive + " INCHES");

		} else if (this.hasTarget() == false) {
			(new DriveTrainStopCommand()).start();

		} else {
			(new DriveTrainStopCommand()).start();
			System.out.println("DO NOTHING, I'M AT 10 FEET");
		} 
	}

	

		/*Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.setGyroTargetHeading(Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.getCurrentHeading() + x);
        
		if (_inchesToTarget < Calibrations.MINIMUM_DISTANCE_FROM_LIMELIGHT) {
			_inchesToTarget = Calibrations.MINIMUM_DISTANCE_FROM_LIMELIGHT;
		}

		if (_inchesToTarget > Calibrations.MAXIMUM_DISTANCE_FROM_LIMELIGHT) {
			_inchesToTarget = Calibrations.MAXIMUM_DISTANCE_FROM_LIMELIGHT;
		}
		
		if (hasTarget()) {
			if (_inchesToTarget < (distanceDesiredFromTarget + Calibrations.desiredTargetBuffer) && _inchesToTarget > (distanceDesiredFromTarget - Calibrations.desiredTargetBuffer)) {
				(new DriveTrainDriveFPSCommand()).start();
				System.out.println("DO NOTHING, I'M AT 2 FEET");
			} else if (_inchesToTarget > distanceDesiredFromTarget) {
				DriveTrainDriveInchesCommand nick = new DriveTrainDriveInchesCommand(_inchesToTarget - distanceDesiredFromTarget, .2, Calibrations.drivingForward);
				nick.start();
				System.out.println("MOVE FORWARD " + (_inchesToTarget - distanceDesiredFromTarget) + " INCHES");
			} else if (_inchesToTarget < distanceDesiredFromTarget) {
				DriveTrainDriveInchesCommand nick = new DriveTrainDriveInchesCommand(distanceDesiredFromTarget - _inchesToTarget, .2, Calibrations.drivingBackward);
				nick.start();
				System.out.println("BACKING UP " + (distanceDesiredFromTarget - _inchesToTarget) + " INCHES");
			} 
		} else {
			
		*/

		/*driveTrainDriveInchesCommand.start();
    	
    	if (direction == Calibrations.drivingBackward) {
    	netInchesTraveledSoFar = distanceToDriveBlind - Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.getNetInchesTraveled();
    	} else {
    	netInchesTraveledSoFar = Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.getNetInchesTraveled() - distanceToDriveBlind;
		   }*/
		   
		   /*  boolean hasTraveledTargetDistance = (netInchesTraveledSoFar >= distanceToDriveBlind); 
    	double area = ta.getDouble(0.0);
        
   	 	if (timeoutTimer.get() > timeoutSeconds) {
      	hasTraveledTargetDistance = true;

      	System.out.println("TIMEOUTTIMEOUTTIMEOUT");
    	}		

    	if (area > 0.0) {
      	new DriveDistanceToTargetCommand().start();
      	hasTraveledTargetDistance = true;
    	}
    	
    	return hasTraveledTargetDistance; */
}
