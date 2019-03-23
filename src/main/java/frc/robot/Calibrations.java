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
	public static final double gyroAdjustmentDefaultScaleFactor = .025;
	public static final double driveTrainTurnRelativeDegreesGyroAdjustmentScaleFactor = .03;
	public static final double gyroCooldownTimerTime = .5;
	public static final double translationMaxTurnScaling = .5;
	public static final double gyroAutoTurnAcceptableErrorDegrees = 1;
	public static final boolean driveTrainStartingIsInHighGear = false;
	

	public static final double turnFeedForwardMagnitude = .18;
	public static final double translationFeedForwardMagnitude = .1;


	// .35 is for minibot
	// public static final double turnFeedForwardMagnitude = .35;

	// .18 is for minibot
	// public static final double translationFeedForwardMagnitude = .18;
	
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
	public static final double deadbandMagnitude = .2;
	
	// Default drive and gyro modes
	public static final int defaultDriveMode = Calibrations.fpsTank;
	public static final int defaultGyroMode = Calibrations.gyroEnabled;
	
	
	// DRIVE ENCODERS
	public static final int encoderCUI103CyclesPerRevolution = 4096; 
	public static final double wheelDiameterInches = 4;
	public static final double wheelCircumferenceInches = Calibrations.wheelDiameterInches * Math.PI;
	
	// We're using CUI 103 encoders on both sides of the drivetrain.
	public static final int encoderCyclesPerRevolution = Calibrations.encoderCUI103CyclesPerRevolution;

	// Encoder usage choice in case of one side breaking
	public static final int useLeftEncoderOnly = 0;
	public static final int useRightEncoderOnly = 1;
	public static final int useBothEncoders = 2;

	public static final int useWhichEncoders = useBothEncoders;
	
	// Direction magic numbers
	public static final int drivingForward = -1;
	public static final int drivingBackward = 1;
	
	// Adjust max power based on elevator height
	public static final double DRIVETRAIN_MAXPOWER_AT_MAX_ELEVEATOR_HEIGHT = .4;
    

	// ELEVATOR
	// TODO: scale this based on elevator height
	public static final double elevatorHoldPositionPowerMagnitude = .15;

	public static final double elevatorExtendPowerMagnitude = 0.5;
	public static final double elevatorRetractPowerMagnitude = 0.3;

	public static final double elevatorkF = Calibrations.elevatorHoldPositionPowerMagnitude;
    public static final double elevatorkP = 12.0;
    public static final double elevatorkI = 0.0;
    public static final double elevatorkD = 230.0;
	
	public static final double elevatorWheelDiameterInches = 1.25;
	
	public static final int elevatorEncoderMinimumValue = 0;
    public static final int elevatorEncoderMaximumValue = 49000;

    public static final int elevatorLowHatchEncoderValue = 0;
    public static final int elevatorMidHatchEncoderValue = 11900;
    public static final int elevatorHighHatchEncoderValue = 22640;

    public static final int elevatorCargoShipPortEncoderValue = 11800;
    public static final int elevatorLowRocketPortEncoderValue = 9600;
    public static final int elevatorMidRocketPortEncoderValue = 27000;
    public static final int elevatorHighRocketPortEncoderValue = 28500;

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
	

	// CLIMBER (ALL VALUES ARE INCORRECT)
	public static final double climberHoldPositionPowerMagnitude = .13;
	public static final double climberExtendPowerMagnitude = .5;
	public static final double climberRetractPowerMagnitude = .4;

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
	public static final int climberLiftDownwardSafetyMargin = 500;
	public static final int CLIMBER_AT_POSITION_BUFFER = 500;
	
	public static final double climberConsideredMovingEncoderRate = 0;
	
    public static final double CLIMBER_MOVE_TO_POSITION_TIMEOUT = 2;
    public static final double CLIMBER_SAFETY_TIMER_TIMEOUT = 5;
	
	public static final int climberInchesToEncoderTicksConversionValue = 411;
	public static final int climberInchesToEncoderTicksOffsetValue = 10;
	
	public static final int maximumTiltAngleWhileClimbing = 4;
	

	// ARM
	public static final double armkF = 0.1;
	public static final double armkP = 12.0;
	public static final double armkI = 0.0;
	public static final double armkD = 170.0;
	public static final double armHoldPositionPowerMagnitude = 0.04;
	public static final double armExtendPowerMagnitude = 0.6;
	public static final double armRetractPowerMagnitude = 0.8;
	
	public static final int armEncoderRetractedValue = 0;
	public static final int armEncoderExtendedValue = 9700;
	
	public static final int armCargoHPSEncoderValue = 2000;

    public static final int armLowHatchEncoderValue = armEncoderExtendedValue;
    public static final int armMidHatchEncoderValue = armEncoderExtendedValue;
    public static final int armHighHatchEncoderValue = 1600;

    public static final int armCargoShipPortEncoderValue = armEncoderRetractedValue;
    public static final int armLowRocketPortEncoderValue = 6000;
    public static final int armMidRocketPortEncoderValue = 6800;
    public static final int armHighRocketPortEncoderValue = 0;

	public static final int ARM_ENCODER_BUFFER = 300;
	// This value represents the buffer that the arm can be *on either side* of midway,
	// so the true buffer range is this value times two.
	
	public static final double ARM_SAFETY_TIMER_TIMEOUT = 2;


	// CARGO WHEEL
	public static final double cargoWheelSuckPowerMagnitude = 1;
	public static final double cargoSpitPowerMagnitude = 1;
	public static final double cargoDropPowerMagnitude = .5;
	public static final double cargoHoldPowerMagnitude = 0.15;
	
	public static final double AXIS_IS_PRESSED_VALUE = .25;

	public static final double cargoSpitTimer = 1;
	public static final double cargoBottomMotorSpinTimout = 15;


	// BEAK
	public static final double hasHatchPanelTimer = 2;

	
	// LIMELIGHT
	public static final double FLOOR_TO_LIMELIGHT_LENS_HEIGHT = 19.5;
	public static final double FLOOR_TO_TARGET_CENTER_HEIGHT = 28.0;
	public static final double CAMERA_ANGLE_OFFSET_FROM_HORIZONTAL = 2; // degrees
	public static final double MINIMUM_DISTANCE_FROM_LIMELIGHT = 46.0;
	public static final double MAXIMUM_DISTANCE_FROM_LIMELIGHT = 240.0;
	public static final double LIMELIGHT_LENS_TO_ROBOT_CENTER_OFFSET_INCHES = 6.25;
	public static final int desiredTargetBuffer = 16;
	public static final int distanceDesiredFromHPS = 72;
	public static final int distanceDesiredFromRocket = 72;
	public static final int distanceDesiredFromCargoShip = 72;

	// LIGHTING
	public static final double lightingFlashTotalDurationMs = 1000;
	public static final double lightingFlashes = 10;

	//n CAMERA QUALITY
	public static final int cameraQuality = 50;

	// CONTROLLER RUMBLE
	public static final double gamePieceCollectedRumbleSeconds = .25;
}

