/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

public class Calibrations {

    // DRIVE TRAIN
	// Slew rate of .2 seems to work well for when the lift is lowered, though more testing
	// is necessary - might turn it up or down slightly for increased performance.
	// public static final double slewRate = .2;
	public static final double slewRateMinimum = .3;
	public static final double slewRateMaximum = .35;
	
	// The safe slew rate changes based upon a few variables:
	// 		- What gear we are in
	//		- How high the lift is
	//		- What direction the robot is moving (forward or backward.)
	//			(backwards to forwards seems worse - but that's with no arm or cube, and a broken chassis)
	//		- A number low enough to be safe for all scenarios will negatively impact normal operation.
	
	public static final double cutPowerModeMovementRatio = .3;
	public static final double cutPowerModeTurnRatio = .5;
	public static final double gyroAdjustmentDefaultScaleFactor = .03;
	public static final double driveTrainTurnRelativeDegreesGyroAdjustmentScaleFactor = .009;
	public static final double gyroCooldownTimerTime = .5;
	public static final double translationMaxTurnScaling = .5;
	public static final double gyroAutoTurnAcceptableErrorDegrees = 1;
	public static final boolean driveTrainStartingIsInHighGear = false;
	
	// Drive collision
	public static final double DriveTrainCollisionJerkThreshold = 4;

	// 2019 and newer robots use talonSRX instead talon
	public static final Boolean UseTalonSRXForDriveController = true;

	// Drive and gyro modes
	public static final int bulldozerTank = 0;
	public static final int fpsTank = 1;
	
	public static final int gyroDisabled = 0;
	public static final int gyroEnabled = 1;
	
	// Any turn taking too long to complete (e.g. wheel scrub has halted the turn) will abandon after this number of seconds.
	public static final double DriveTrainTurnRelativeDegreesSafetyTimerSeconds = 1;
	public static final double DriveTrainDriveInchesSafetyTimerSeconds = 3;
	
	// Deadband
	public static final double deadbandMagnitude = .1;
	
	// Default drive and gyro modes
	public static final int defaultDriveMode = Calibrations.fpsTank;
	public static final int defaultGyroMode = Calibrations.gyroEnabled;
	
	//public static final int defaultGyroMode = Calibrations.gyroDisabled;
	
	
	// DRIVE ENCODERS
	public static final int encoderCUI103CyclesPerRevolution = 4096; 
	// public static final int encoderE4TCyclesPerRevolution = 360;
	// public static final int encoderE4PCyclesPerRevolution = 250;
	public static final double wheelDiameterInches = 4;
	public static final double wheelCircumferenceInches = Calibrations.wheelDiameterInches * Math.PI;
	
	// We're using CUI 103 encoders on both sides of the drivetrain.
	public static final int encoderCyclesPerRevolution = Calibrations.encoderCUI103CyclesPerRevolution;
	
	// Direction magic numbers
	public static final int drivingForward = -1;
	public static final int drivingBackward = 1;
	
	// Adjust max power based on elevator height
	public static final double DRIVETRAIN_MAXPOWER_AT_MAX_ELEVEATOR_HEIGHT = .4;
    

	//ELEVATOR
	public static final double elevatorHoldPositionPowerMagnitude = .13;

	public static final double elevatorkF = Calibrations.elevatorHoldPositionPowerMagnitude;
    public static final double elevatorkP = 12.0;
    public static final double elevatorkI = 0.0;
    public static final double elevatorkD = 230.0;
	
	public static final double elevatorWheelDiameterInches = 1.25;
	
	public static final int elevatorEncoderMinimumValue = 0;
    public static final int elevatorEncoderMaximumValue = 53000;

    public static final int elevatorLowHatchEncoderValue = 0;
    public static final int elevatorMidHatchEncoderValue = 21000;
    public static final int elevatorHighHatchEncoderValue = 40000;

    public static final int elevatorCargoShipPortEncoderValue = 35800;
    public static final int elevatorLowRocketPortEncoderValue = 17000;
    public static final int elevatorMidRocketPortEncoderValue = 47500;
    public static final int elevatorHighRocketPortEncoderValue = 50300;

    // The safety margin is how far away from the end of travel the encoders will stop the lift.
	// At low speeds (max of .3), and a lift max value of 30k, 1500 maxes out the elevator.
	// At higher speeds, a higher value is needed because the elevator will overshoot the target until we have PID.
	
	public static final int elevatorLiftUpwardSafetyMargin = 400;
	public static final int elevatorLiftDownwardSafetyMargin = 400;
	public static final int ELEVATOR_AT_POSITION_BUFFER = 500;
	
	public static final double elevatorConsideredMovingEncoderRate = 0;
	
    public static final double ELEVATOR_MOVE_TO_POSITION_TIMEOUT = 2;
    public static final double ELEVATOR_SAFETY_TIMER_TIMEOUT = 5;
	
	public static final int elevatorInchesToEncoderTicksConversionValue = 411;
	public static final int elevatorInchesToEncoderTicksOffsetValue = 10;
	

	//CLIMBER (ALL VALUES ARE INCORRECT)
	public static final double climberHoldPositionPowerMagnitude = .13;

	public static final double climberkF = Calibrations.climberHoldPositionPowerMagnitude;
    public static final double climberkP = 12.0;
    public static final double climberkI = 0.0;
    public static final double climberkD = 230.0;
	
	public static final int climberEncoderMinimumValue = 0;
    public static final int climberEncoderMaximumValue = 53000;

    // The safety margin is how far away from the end of travel the encoders will stop the lift.
	// At low speeds (max of .3), and a lift max value of 30k, 1500 maxes out the climber.
	// At higher speeds, a higher value is needed because the climber will overshoot the target until we have PID.
	
	public static final int climberLiftUpwardSafetyMargin = 400;
	public static final int climberLiftDownwardSafetyMargin = 400;
	public static final int CLIMBER_AT_POSITION_BUFFER = 500;
	
	public static final double climberConsideredMovingEncoderRate = 0;
	
    public static final double CLIMBER_MOVE_TO_POSITION_TIMEOUT = 2;
    public static final double CLIMBER_SAFETY_TIMER_TIMEOUT = 5;
	
	public static final int climberInchesToEncoderTicksConversionValue = 411;
    public static final int climberInchesToEncoderTicksOffsetValue = 10;
	

	//ARM
	public static final double armkF = 0.1;
	public static final double armkP = 12.0;
	public static final double armkI = 0.0;
	public static final double armkD = 170.0;
	public static final double armHoldPositionPowerMagnitude = 0.04;
	
	public static final int armEncoderRetractedValue = 0;
    public static final int armEncoderExtendedValue = 10000;

    public static final int armLowHatchEncoderValue = 10000;
    public static final int armMidHatchEncoderValue = 10000;
    public static final int armHighHatchEncoderValue = 1600;

    public static final int armCargoShipPortEncoderValue = 2000;
    public static final int armLowRocketPortEncoderValue = 6000;
    public static final int armMidRocketPortEncoderValue = 8000;
    public static final int armHighRocketPortEncoderValue = 0;

	public static final int ARM_ENCODER_BUFFER = 300;
	// This value represents the buffer that the arm can be *on either side* of midway,
	// so the true buffer range is this value times two.
	
	public static final double ARM_SAFETY_TIMER_TIMEOUT = 2;


	//CARGO WHEEL
	public static final double cargoWheelSuckPowerMagnitude = 1;
	public static final double cargoSpitPowerMagnitude = 1;
	public static final double cargoDropPowerMagnitude = .5;
	
	public static final double AXIS_IS_PRESSED_VALUE = .25;

	public static final double cargoSpitTimer = 1;

	
	//LIMELIGHT
	public static final double FLOOR_TO_LIMELIGHT_LENS_HEIGHT = 7;
	public static final double FLOOR_TO_TARGET_CENTER_HEIGHT = 28.0;
	public static final double CAMERA_ANGLE_OFFSET_FROM_HORIZONTAL = 27.75;
	public static final double MINIMUM_DISTANCE_FROM_LIMELIGHT = 46.0;
	public static final double MAXIMUM_DISTANCE_FROM_LIMELIGHT = 240.0;
	public static final double LIMELIGHT_LENS_TO_ROBOT_CENTER_OFFSET_INCHES = 5;
	public static final int desiredTargetBuffer = 16;
	public static final int distanceDesiredFromHPS = 72;
	public static final int distanceDesiredFromRocket = 72;
	public static final int distanceDesiredFromCargoShip = 72;


	//LIGHTING
	public static final double lightingFlashTotalDurationMs = 1000;
	public static final double lightingFlashes = 10;
	

	//CAMERA QUALITY
	public static final int cameraQuality = 50;
}

