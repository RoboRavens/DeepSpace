package frc.robot;

public class RobotMap {
	// MOTORS

	public static int leftDriveChannel = 0;
	public static int leftFollower1 = 10;
	public static int leftFollower2 = 11;
	public static int rightDriveChannel = 1;
	public static int rightFollower1 = 12;
	public static int rightFollower2 = 13;

	public static final int elevatorMotor = 2;
	public static final int elevatorMotorFollower = 3;

	public static final int armMotor = 4;

	public static final int cargoMotor = 5;

	public static final int climberMotor = 6;

	public static final int beakSolenoid = 0;

	// DIO PORTS

	public static final int leftDriveEncoder = 0;
    public static final int rightDriveEncoder = 1;
	
	public static final int elevatorExtensionLimitSwitch = 2;
	public static final int elevatorRetractionLimitSwitch = 3;

	public static int armExtensionLimitSwitch = 4;
	public static int armRetractionLimitSwitch = 5;
	
	public static final int cargoSensor = 6;

	public static final int hatchPanelSensor = 7;


	// RELAYS
	public static final int hasCargoLEDLightRelay = 0;
	public static final int hasHatchPanelLEDLightRelay = 1;
	public static final int underglowLightRelay = 2;

	// CAMERA
	public static final String cameraName = "cam0";
}
