package frc.ravenhardware;

import frc.robot.Calibrations;
import frc.robot.Robot;
import frc.robot.RobotMap;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;

public class RavenTank {
	Robot robot;

	public RavenEncoder leftRavenEncoder;
	public RavenEncoder rightRavenEncoder;

	Timer gyroCooldownTimer;

	AHRS orientationGyro = new AHRS(SPI.Port.kMXP);

	AHRS.BoardYawAxis boardYawAxis;
	double lastAccelerationX;
	double lastAccelerationY;
	double highestJerkX;
	double highestJerkY;
	private double _slewRate;

	protected int driveMode;
	protected int gyroMode;
	private boolean _cutPower;
	protected double gyroTargetHeading;

	protected boolean automatedDrivingEnabled = false;
	protected int automatedDrivingDirection = Calibrations.drivingForward;
	protected double automatedDrivingSpeed = 0;

	protected boolean hasHitObstacle = false;
	protected boolean drivingThroughObstacle = false;
	protected boolean turning = false;
	protected boolean waiting = false;

	public double gyroAdjust;
	double _gyroAdjustmentScaleFactor = Calibrations.gyroAdjustmentDefaultScaleFactor;

	public boolean userControlOfCutPower = true;

	RavenTalon driveLeft = new RavenTalon(RobotMap.leftDriveChannel, "MotorLeft", _slewRate, RobotMap.leftFollower1, RobotMap.leftFollower2);
	RavenTalon driveRight = new RavenTalon(RobotMap.rightDriveChannel, "MotorRight", _slewRate, RobotMap.rightFollower1, RobotMap.rightFollower2);

	public RavenTank() {
		initializeRavenTank();
	}

	private void initializeRavenTank() {
		_slewRate = Calibrations.slewRateMaximum;

		Encoder leftWpiEncoder = new Encoder(RobotMap.leftDriveEncoder1, RobotMap.leftDriveEncoder2);
		Encoder rightWpiEncoder = new Encoder(RobotMap.rightDriveEncoder1, RobotMap.rightDriveEncoder2);
		leftRavenEncoder = new RavenEncoder(leftWpiEncoder, Calibrations.encoderCyclesPerRevolution, Calibrations.wheelDiameterInches, false);
		rightRavenEncoder = new RavenEncoder(rightWpiEncoder, Calibrations.encoderCyclesPerRevolution, Calibrations.wheelDiameterInches, true);

		gyroCooldownTimer = new Timer();

		setDriveMode(Calibrations.defaultDriveMode);
		setCutPower(false);

		setGyroMode(Calibrations.defaultGyroMode);
		gyroTargetHeading = setGyroTargetHeadingToCurrentHeading();
	}

	public void setDriveMode(int driveMode) {
		this.driveMode = driveMode;
	}

	public void setCutPower(boolean cutPower) {
		_cutPower = cutPower;
	}

	public boolean getCutPower() {
		return _cutPower;
	}

	public void setGyroMode(int gyroMode) {
		this.gyroMode = gyroMode;
	}

	public double getGyroAdjustmentScaleFactor() {
		return _gyroAdjustmentScaleFactor;
	}

	public void setGyroAdjustmentScaleFactor(double gyroAdjustmentScaleFactor) {
		_gyroAdjustmentScaleFactor = gyroAdjustmentScaleFactor;
	}

	public void resetGyroAdjustmentScaleFactor() {
		setGyroAdjustmentScaleFactor(Calibrations.gyroAdjustmentDefaultScaleFactor);
	}

	public double deadband(double input) {
		double output = input;

		if (Math.abs(output) < Calibrations.deadbandMagnitude) {
			output = 0;
		}

		return output;
	}

	public void setSlewRate(double slewRate) {
		_slewRate = slewRate;
		driveRight.setSlewRate(_slewRate);
		driveLeft.setSlewRate(_slewRate);
	}

	public void setMaxPower(double maxPower) {
		driveRight.setMaxPower(maxPower);
		driveLeft.setMaxPower(maxPower);
	}

	public void resetDriveEncoders() {
		leftRavenEncoder.resetEncoder();
		rightRavenEncoder.resetEncoder();
	}

	public void drive(double left, double rightY, double rightX) {
		left = deadband(left);
		rightY = deadband(rightY);
		rightX = deadband(rightX);

		switch (driveMode) {
		case Calibrations.bulldozerTank:
			bulldozerTank(left, rightY);
			break;
		case Calibrations.fpsTank:
			fpsTank(left, rightX);
			break;
		}
	}

	public void bulldozerTank(double left, double right) {
		// Invert the left side.
		right *= -1;
		if (_cutPower) {
			left *= Calibrations.cutPowerModeMovementRatio;
			right *= Calibrations.cutPowerModeTurnRatio;
		}

		this.driveLeftSide(left);
		this.driveRightSide(right);
	}

	public void fpsTankLimelight(double translation) {
		// This method simply sets the gyro target, it doesn't actually turn the robot.
		// As a result, the gyro adjust value below will effectually turn the robot.
		Robot.LIMELIGHT_SUBSYSTEM.turnToTarget();
	
		if (_cutPower) {
			translation *= Calibrations.cutPowerModeMovementRatio;
		}
	
		double gyroAdjust = getStaticGyroAdjustment();
		double leftFinal = translation * -1 - gyroAdjust;
		double rightFinal = translation - gyroAdjust;

		this.driveLeftSide(leftFinal);
		this.driveRightSide(rightFinal);
	}

	public void fpsTank(double translation, double turn) {

		if (_cutPower) {
			translation *= Calibrations.cutPowerModeMovementRatio;
			turn *= Calibrations.cutPowerModeTurnRatio;
		}

		// Apply a small reduction to turning magnitude based on the magnitude of
		// translation.
		turn = getScaledTurnFromTranslation(translation, turn);

		double gyroAdjust = getTurnableGyroAdjustment(turn);

		

		double leftFinal = (translation - turn) * -1 - gyroAdjust;
		double rightFinal = (translation + turn) - gyroAdjust;

		this.driveLeftSide(leftFinal);
		this.driveRightSide(rightFinal);
	}

	public boolean detectCollisions() {
		boolean collisionDetected = false;

		double currentAccelerationX = orientationGyro.getWorldLinearAccelX();
		double currentAccelerationY = orientationGyro.getWorldLinearAccelY();

		double currentJerkX = currentAccelerationX - lastAccelerationX;
		double currentJerkY = currentAccelerationY - lastAccelerationY;

		lastAccelerationX = currentAccelerationX;
		lastAccelerationY = currentAccelerationY;

		if (currentJerkX > highestJerkX) {
			highestJerkX = currentJerkX;
		}

		if (currentJerkY > highestJerkY) {
			highestJerkY = currentJerkY;
		}

		if (Math.abs(currentJerkX) > Calibrations.DriveTrainCollisionJerkThreshold) {
			collisionDetected = true;
		}

		if (Math.abs(currentJerkY) > Calibrations.DriveTrainCollisionJerkThreshold) {
			collisionDetected = true;
		}

		return collisionDetected;
	}

	public void outputJerk() {
		double currentAccelerationX = orientationGyro.getWorldLinearAccelX();
		double currentAccelerationY = orientationGyro.getWorldLinearAccelY();

		double currentJerkX = currentAccelerationX - lastAccelerationX;
		double currentJerkY = currentAccelerationY - lastAccelerationY;

		System.out.println("X Jerk: " + currentJerkX + " Y Jerk: " + currentJerkY);
	}

	public void outputHighestJerk() {
		System.out.println("Highest X Jerk: " + highestJerkX + " Highest Y Jerk: " + highestJerkY);
	}

	public void driveLeftSide(double magnitude) {
		// System.out.println("Driving left side. Magnitude: " + magnitude);
		driveLeft.set(magnitude);
	}

	public void driveRightSide(double magnitude) {
		driveRight.set(magnitude);
	}

	public double getScaledTurnFromTranslation(double translation, double turn) {
		double turnScaleReduction = Calibrations.translationMaxTurnScaling * Math.abs(translation);
		double turnCoefficient = 1 - turnScaleReduction;
		double netTurn = turn * turnCoefficient;

		return netTurn;
	}

	public void driveOutput() {}

	public double getDriveGyro() {
		return orientationGyro.getAngle();
	}

	public double getGyroTargetHeading() {
		return this.gyroTargetHeading;
	}

	public double setGyroTargetHeadingToCurrentHeading() {
		this.gyroTargetHeading = getCurrentHeading();
		return gyroTargetHeading;
	}

	public double setGyroTargetHeading(double angle) {
		this.gyroTargetHeading = angle;
		return gyroTargetHeading;
	}

	private boolean adjustGyroDueToTimer() {
		double time = this.gyroCooldownTimer.get();

		boolean adjust = false;

		if (time > 0 && time < Calibrations.gyroCooldownTimerTime) {
			adjust = true;
		} else if (time > Calibrations.gyroCooldownTimerTime) {
			gyroCooldownTimer.stop();
		}

		return adjust;
	}

	public double getTurnableGyroAdjustment(double turn) {
		// If the gyro is in disabled mode, just return immediately.
		if (gyroMode == Calibrations.gyroDisabled) {
			return 0;
		}

		if (Math.abs(turn) > 0 || this.adjustGyroDueToTimer()) {
			this.setGyroTargetHeadingToCurrentHeading();

			if (Math.abs(turn) > 0) {
				this.gyroCooldownTimer.reset();
				this.gyroCooldownTimer.start();
			}
		}

		return getStaticGyroAdjustment();
	}

	public double getStaticGyroAdjustment() {
		// If the gyro is in disabled mode, just return immediately.
		if (gyroMode == Calibrations.gyroDisabled) {

			return 0;
		}

		double heading = getCurrentHeading();

		// Mod to eliminate extra rotations.
		double gyroAdjust = (heading - gyroTargetHeading) % 360;

		if (gyroAdjust < 0) {
			gyroAdjust = 360 + gyroAdjust;
		}

		// This snippet ensures that the robot will spin in the fastest direction to
		// zero
		// if it ends up more than 180 degrees off of intention.
		if (gyroAdjust > 180) {
			gyroAdjust = gyroAdjust - 360;
		}

		if (gyroAdjust > 180 || gyroAdjust < -180) {
			gyroAdjust *= -1;
		}

		// Mod again in case the directional snippet was applied.
		gyroAdjust = Math.round(gyroAdjust) % 360;

		gyroAdjust *= _gyroAdjustmentScaleFactor;

		return gyroAdjust;
	}

	public void stopAndWait() {
		enableAutomatedDriving(0);
	}

	public void turnRelativeDegrees(double degrees) {
		this.setGyroTargetHeading(this.gyroTargetHeading + degrees);
	}

	public void enableAutomatedDriving(int direction, double speed) {
		automatedDrivingDirection = direction;
		enableAutomatedDriving(speed);
	}

	public void enableAutomatedDriving(double speed) {
		automatedDrivingEnabled = true;
		automatedDrivingSpeed = speed;
	}

	public void overrideAutomatedDriving() {
		// Just disable all the automated driving variables, and
		// the normal drive function will immediately resume.
		automatedDrivingEnabled = false;
		drivingThroughObstacle = false;
		hasHitObstacle = false;
		turning = false;
		waiting = false;
	}

	public void stop() {
		this.fpsTank(0, 0);
	}

	public void gyroStop() {
		this.setGyroTargetHeadingToCurrentHeading();
		this.resetGyroAdjustmentScaleFactor();
	}

	public boolean automatedActionHasCompleted() {
		// Just return the opposite of automatedDrivingEnabled.
		return automatedDrivingEnabled == false;
	}

	public void maintainStateWaiting() {
		this.stop();
	}

	public void wake() {
		this.waiting = false;
		this.automatedDrivingEnabled = false;
	}

	public void maintainStateTurning() {
		if (Math.abs(gyroTargetHeading - getCurrentHeading()) < 3) {
			automatedDrivingEnabled = false;
			turning = false;
		}
	}

	public double getCurrentHeading() {
		double heading = orientationGyro.getAngle();

		heading = heading % 360;

		if (heading < 0) {
			heading += 360;
		}

		return heading;
	}

	public double getNetInchesTraveled() {
		double leftInches = this.leftRavenEncoder.getNetInchesTraveled();
		double rightInches = this.rightRavenEncoder.getNetInchesTraveled();
		double netInchesTraveled = (leftInches + rightInches)/2;
		
		return netInchesTraveled;
	}

	public double getSlewRate() {
		return _slewRate;
	}
	public void resetOrientationGyro() {
		orientationGyro.reset();
	}

	public double getGyroAngle() {
		return orientationGyro.getAngle();
	}

}
