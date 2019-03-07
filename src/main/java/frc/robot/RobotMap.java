package frc.robot;

public class RobotMap {
	// MOTORS

	public static final int leftDriveChannel = 0;
	public static final int leftFollower1 = 10;
	public static final int leftFollower2 = 11;
	public static final int rightDriveChannel = 1;
	public static final int rightFollower1 = 12;
	public static final int rightFollower2 = 13;

	public static final int elevatorMotor = 2;
	public static final int elevatorMotorFollower = 3;

	public static final int armMotor = 4;

	public static final int cargoMotor = 5;

	public static final int climberMotor = 6;

	// PCM

	public static final int beakCaptureSolenoid = 0;
	public static final int beakReleaseSolenoid = 1;

	
	// DIO PORTS

	public static final int leftDriveEncoder1 = 0;
	public static final int leftDriveEncoder2 = 1;
	public static final int rightDriveEncoder1 = 2;
	public static final int rightDriveEncoder2 = 3;

	// Arm extension and retraction limit switches are deprecated.
	public static final int armExtensionLimitSwitch = 4;
	public static final int armRetractionLimitSwitch = 5;

	public static final int frontLineSensor = 4;
	public static final int rearLineSensor = 5;

	// Elevator extension and retraction limit switches are deprecated.
	public static final int elevatorExtensionLimitSwitch = 6;
	public static final int elevatorRetractionLimitSwitch = 7;
	
	public static final int cargoSensor = 6;

	public static final int hatchPanelSensorLeft = 7;
	public static final int hatchPanelSensorRight = 8;

	// RELAYS
	public static final int hasGamePieceRelay = 0;
	public static final int lineAlignmentRelay = 1;

	// CAMERA
	public static final String cameraName = "cam0";
}
