package frc.robot;

public class RobotMap {
	// Drive motors
	public static int leftDriveChannel = 0;
	public static int leftFollower1 = 10;
	public static int leftFollower2 = 11;
	public static int rightDriveChannel = 1;
	public static int rightFollower1 = 12;
	public static int rightFollower2 = 13;

	public static final int leftDriveEncoder1 = 0;
    public static final int leftDriveEncoder2 = 1;
    public static final int rightDriveEncoder1 = 2;
    public static final int rightDriveEncoder2 = 3;
	
	// Elevator System
	public static final int elevatorMotor = 2;
	public static final int elevatorMotorFollower = 3;
	
	public static final int elevatorExtensionLimitSwitch = 4;
	public static final int elevatorRetractionLimitSwitch = 5;

	public static final int elevatorEncoderLeft = 6;
	public static final int elevatorEncoderRight = 7;

	// Arm System
	public static final int armMotor = 4;

	public static int armExtensionLimitSwitch = 8;
	public static int armRetractionLimitSwitch = 9;
	
	// Cargo System 
	public static final int cargoMotor = 5;
	public static final int cargoSensor = 10;

	// Hatch Panel System
	public static final int beakSolenoid = 0;
	public static final int hatchPanelSensor = 11;

	//Climber
	public static final int climberMotor = 6;

	//Relays 
	public static final int hasCargoLEDLightRelay = 0;
	public static final int hasHatchPanelLEDLightRelay = 1;
	public static final int underglowLightRelay = 2;

	// Camera
	public static final String cameraName = "cam0";
}
