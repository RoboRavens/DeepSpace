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

	public static final int armExtensionLimitSwitch = 4;
	public static final int armRetractionLimitSwitch = 5;

	public static final int elevatorExtensionLimitSwitch = 6;
	public static final int elevatorRetractionLimitSwitch = 7;
	
	public static final int cargoSensor = 8;

	public static final int hatchPanelSensor = 9;


	// RELAYS
	public static final int hasCargoLEDLightRelay = 0;
	public static final int hasHatchPanelLEDLightRelay = 1;
	public static final int underglowLightRelay = 2;

	// CAMERA
	public static final String cameraName = "cam0";
}
