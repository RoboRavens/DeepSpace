/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.gamepad.ButtonCode;
import frc.gamepad.Gamepad;
import frc.gamepad.OperationPanel;
import frc.gamepad.OperationPanel2;
import frc.ravenhardware.RavenLighting;
import frc.robot.commands.LED.LEDBlinkFor2SecondsCommand;
import frc.robot.commands.arm.ArmExtendFullyCommand;
import frc.robot.commands.arm.ArmExtendToHighScaleCommand;
import frc.robot.commands.arm.ArmExtendWhileHeldCommand;
import frc.robot.commands.arm.ArmMoveToHeightCommand;
import frc.robot.commands.arm.ArmRetractFullyCommand;
import frc.robot.commands.arm.ArmRetractWhileHeldCommand;
import frc.robot.commands.drivetrain.SetCutPowerFalse;
import frc.robot.commands.drivetrain.SetCutPowerTrue;
import frc.robot.commands.drivetrain.SetGyroTargetHeading;
import frc.robot.commands.elevator.ElevatorExtendFullyCommand;
import frc.robot.commands.elevator.ElevatorExtendQuarterWithPIDCommand;
import frc.robot.commands.elevator.ElevatorExtendWhileHeldCommand;
import frc.robot.commands.elevator.ElevatorMoveToHeightCommand;
import frc.robot.commands.elevator.ElevatorRetractFullyCommand;
import frc.robot.commands.elevator.ElevatorRetractWhileHeldCommand;
import frc.robot.commands.cargointake.CargoWheelPullCommand;
import frc.robot.commands.cargointake.CargoWheelPushHardCommand;
import frc.robot.commands.cargointake.CargoWheelPushSoftCommand;
import frc.robot.commands.misc.SetOverride1Command;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.CargoWheelSubsystem;
import frc.robot.subsystems.ProgrammableLEDSubsystem;
import frc.robot.subsystems.LightSubsystem;
import frc.util.LoggerOverlord;
import frc.util.OverrideSystem;
import frc.util.PCDashboardDiagnostics;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
//import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.cameraserver.CameraServer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {
	Command m_autonomousCommand;
	SendableChooser<Command> m_chooser = new SendableChooser<>();

	public DriverStation driverStation;
	public PowerDistributionPanel PDP = new PowerDistributionPanel();
	
	Diagnostics diagnostics = new Diagnostics();
	public static final LoggerOverlord LOGGER_OVERLORD = new LoggerOverlord(1f);
	
	public static final Gamepad DRIVE_CONTROLLER = new Gamepad(0);
	public static final Gamepad OPERATION_CONTROLLER = new Gamepad(3);
	public static final OperationPanel OPERATION_PANEL = new OperationPanel(1);	
	public static final OperationPanel2 OPERATION_PANEL2 = new OperationPanel2(2);
			
	public static final DriveTrainSubsystem DRIVE_TRAIN_SUBSYSTEM = new DriveTrainSubsystem();
	public static final ElevatorSubsystem ELEVATOR_SUBSYSTEM = new ElevatorSubsystem();
	public static final ArmSubsystem ARM_SUBSYSTEM = new ArmSubsystem();
	public static final CargoWheelSubsystem CARGO_WHEEL_SUBSYSTEM = new CargoWheelSubsystem();
	public static final LightSubsystem LIGHT_SUBSYSTEM = new LightSubsystem();
	public static final ProgrammableLEDSubsystem LED_SUBSYSTEM = new ProgrammableLEDSubsystem();
	
	public static final Relay HAS_CUBE_LEDS_RELAY = new Relay(RobotMap.hasCubeLEDLightRelay);
	public static final Relay UNDERGLOW_RELAY = new Relay(RobotMap.underglowLightRelay);
	public static final RavenLighting HAS_CUBE_LEDS = new RavenLighting(HAS_CUBE_LEDS_RELAY);
	public static final RavenLighting UNDERGLOW = new RavenLighting(UNDERGLOW_RELAY);

	NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
	NetworkTableEntry tx = table.getEntry("tx");
	NetworkTableEntry ty = table.getEntry("ty");
	NetworkTableEntry ta = table.getEntry("ta");

	CameraServer server;

	// public static final ArmJoystickControlCommand ARM_JOYSTICK_CONTROL_COMMAND = new ArmJoystickControlCommand();
	
	public static final OverrideSystem OVERRIDE_SYSTEM_ELEVATOR_EXTEND = new OverrideSystem();
	public static final OverrideSystem OVERRIDE_SYSTEM_ARM_EXTEND = new OverrideSystem();
	public static final OverrideSystem OVERRIDE_SYSTEM_ELEVATOR_RETRACT = new OverrideSystem();
	public static final OverrideSystem OVERRIDE_SYSTEM_ARM_RETRACT = new OverrideSystem();
	public static final OverrideSystem OVERRIDE_SYSTEM_CARGO = new OverrideSystem();
	
	public static final Solenoid ARM_HOLD_BACK = new Solenoid(RobotMap.armHoldBackSolenoid);

	Command autonomousCommand;
	
	public boolean isRedAlliance;	

	String autoFromDashboard;
	String positionFromDashboard;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		
		driverStation = DriverStation.getInstance();
		//NetworkTable table = NetworkTable.getTable("limelight");
		//table.putDouble("ledMode", 1);
		// System.out.println("disable limelight");
		
		// m_chooser.addDefault("Default Auto", new Command());
		// m_chooser.addObject(name, object);
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", m_chooser);
		driverStation.getMatchTime();
		// Zero the elevator encoders; the robot should always start with the elevator down.
		// Note that this may not be true in practice, so we should later integrate the reset with limit switch code.
		Robot.ELEVATOR_SUBSYSTEM.resetEncodersToRetractedLimit();
		Robot.ARM_SUBSYSTEM.resetEncodersToRetractionLimit();
		
		setupDriveController();
		setupOperationPanel();
		

		// this.elevator.getPosition();
		// this.elevator.getIsAtLimits();
		
		ELEVATOR_SUBSYSTEM.elevatorMotor.config_kF(TalonSRXConstants.kPIDLoopIdx, 0.0, TalonSRXConstants.kTimeoutMs);
		ELEVATOR_SUBSYSTEM.elevatorMotor.config_kP(TalonSRXConstants.kPIDLoopIdx, 10.3, TalonSRXConstants.kTimeoutMs);
		ELEVATOR_SUBSYSTEM.elevatorMotor.config_kI(TalonSRXConstants.kPIDLoopIdx, 0.0, TalonSRXConstants.kTimeoutMs);
		ELEVATOR_SUBSYSTEM.elevatorMotor.config_kD(TalonSRXConstants.kPIDLoopIdx, 0.0, TalonSRXConstants.kTimeoutMs);
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		Robot.LED_SUBSYSTEM.setDisabledPattern();

	}

	public Robot() {
		server = CameraServer.getInstance();
		//server.setQuality(Calibrations.cameraQuality);
		server.startAutomaticCapture();
    }

	@Override
	public void disabledPeriodic() {
		Robot.LED_SUBSYSTEM.setDisabledPattern();
		
		Scheduler.getInstance().run();
		
		autoFromDashboard = SmartDashboard.getString("DB/String 0", "myDefaultData");
		outputAutoModeToDashboardStringOne(autoFromDashboard);
		
		
		// autoFromDashboard = SmartDashboard.getString("DB/String 0", "myDefaultData");
		positionFromDashboard = SmartDashboard.getString("DB/String 2", "myDefaultData");
		
		outputPositionToDashboardStringThree(positionFromDashboard);
		// outputAutoModeToDashboardStringOne(autoFromDashboard);
		
		// System.out.println("Deployed");

		// DRIVE_TRAIN_SUBSYSTEM.ravenTank.resetOrientationGyro();
		
		Alliance alliance = driverStation.getAlliance();
		
		String allianceString = "";
		
		if (alliance.compareTo(Alliance.Blue) == 0) {
			allianceString = "Blue alliance";
			this.isRedAlliance = false;
		}
		else if (alliance.compareTo(Alliance.Red) == 0) {
			allianceString = "Red alliance";
			this.isRedAlliance = true;
		}
		else {
			allianceString = "Alliance not identified.";
			this.isRedAlliance = false;
		}
	
		SmartDashboard.putString("DB/String 4", allianceString);
		

		Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.setGyroTargetHeadingToCurrentHeading();
		
		diagnostics.outputDisabledDiagnostics();
		
		SmartDashboard.putString("DB/String 5", "TBD - Awaiting plates");
		
		// this.elevator.getPosition();
		// this.elevator.getIsAtLimits();
		//this.arm.getPosition();
		
		if (DRIVE_CONTROLLER.getButtonValue(ControlsMap.driveShiftToHighGearButton)) {
			Robot.ELEVATOR_SUBSYSTEM.resetEncodersToRetractedLimit();
		}

		//read values periodically
		double x = tx.getDouble(0.0);
		double y = ty.getDouble(0.0);
		double area = ta.getDouble(0.0);

		//post to smart dashboard periodically
		PCDashboardDiagnostics.AdHocNumber("Limelight X",x);
		PCDashboardDiagnostics.AdHocNumber("Limelight Y",y);
		PCDashboardDiagnostics.AdHocNumber("Limelight Area",area);

		double heightDifference = Calibrations.FLOOR_TO_TARGET_CENTER_HEIGHT - Calibrations.FLOOR_TO_LIMELIGHT_LENS_HEIGHT;
		double angleToTargetFromHorizontal = Math.tan(Math.toRadians(Calibrations.CAMERA_ANGLE_OFFSET_FROM_HORIZONTAL + y));
		PCDashboardDiagnostics.AdHocNumber("Vision Tracking Distance (Inches)", (heightDifference/angleToTargetFromHorizontal));
		PCDashboardDiagnostics.AdHocNumber("Height Difference", heightDifference);
		PCDashboardDiagnostics.AdHocNumber("Angle Thing", angleToTargetFromHorizontal);
	}

	public void outputAutoModeToDashboardStringOne(String autoMode) {
		String autonomousModeConfirmation = "Confirmed - auto mode: ";
		String autonomousModeName = "";
	
		switch (autoFromDashboard.toUpperCase()) {
			case AutonomousCalibrations.DoNothing:
				autonomousModeName += "Do nothing.";
			break;
			case AutonomousCalibrations.CrossLine:
				autonomousModeName += "Cross auto line.";
				break;
			case AutonomousCalibrations.Switch:
				autonomousModeName += "Score in switch.";
				break;
			case AutonomousCalibrations.Scale:
				autonomousModeName += "Score on scale.";
				break;
			case AutonomousCalibrations.FlexSwitch:
				autonomousModeName += "Flex - switch priority.";
				break;
			case AutonomousCalibrations.FlexScale:
				autonomousModeName += "Flex - scale priority.";
				break;
				
			default:
				autonomousModeConfirmation = "ERROR!";
				autonomousModeName = "Mode not recognized.";
				break;	
		}
		
		putSmartDashboardAutonomousMode(autonomousModeConfirmation, autonomousModeName);
	}
	
	public void putSmartDashboardAutonomousMode(String autonomousModeConfirmation, String autonomousModeName) {
		SmartDashboard.putString("DB/String 1", autonomousModeConfirmation);
		SmartDashboard.putString("DB/String 6", autonomousModeName);
	}
	
	
	public void outputPositionToDashboardStringThree(String position) {
		String positionConfirmation = "Confirmed - position: ";
		String startingPosition = "";
	
		switch (position.toUpperCase()) {
			case "LEFT":
				startingPosition += "Left.";
				break;
			case "MIDDLE":
				startingPosition += "Middle.";
				break;
			case "RIGHT":
				startingPosition += "Right.";
				break;
			default:
				positionConfirmation = "ERROR!";
				startingPosition = "Position not recognized.";
				break;	
		}
		
		putSmartDashboardStartingPosition(positionConfirmation, startingPosition);
	}
	
	public void putSmartDashboardStartingPosition(String positionConfirmation, String startingPosition) {
		SmartDashboard.putString("DB/String 3", positionConfirmation);
		SmartDashboard.putString("DB/String 8", startingPosition);
	}
	
	
	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
	
	}
	
	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		diagnostics.outputAutonomousDiagnostics();
	}

	@Override
	public void teleopInit() {
		ARM_HOLD_BACK.set(true); // retract support solenoid
		DRIVE_TRAIN_SUBSYSTEM.ravenTank.setGyroTargetHeadingToCurrentHeading();
		DRIVE_TRAIN_SUBSYSTEM.ravenTank.resetGyroAdjustmentScaleFactor();
		// DRIVE_TRAIN_SUBSYSTEM.ravenTank.resetOrientationGyro();
		
		
		
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
		}
		
		Robot.LED_SUBSYSTEM.setEnabledPattern();
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		
	   /* if (DRIVE_TRAIN_SUBSYSTEM.ravenTank.userControlOfCutPower) {
	      if (DRIVE_CONTROLLER.getAxis(ControlsMap.driveCutPowerAxis) > .25) {
	    	  System.out.println("CUT POWER TRUE");
	        DRIVE_TRAIN_SUBSYSTEM.ravenTank.setCutPower(true);
	      }
	      else {
	        DRIVE_TRAIN_SUBSYSTEM.ravenTank.setCutPower(false);
	      }		
	    }*/
		 
		 diagnostics.outputTeleopDiagnostics();

		 if (getMatchIsAtTime(90)) {
			 LEDBlinkFor2SecondsCommand command = new LEDBlinkFor2SecondsCommand(4, false);
			    command.start();
		 } else if (getMatchIsAtTime(60)) {
			 LEDBlinkFor2SecondsCommand command = new LEDBlinkFor2SecondsCommand(3, false);
				command.start();
		 } else if (getMatchIsAtTime(30)) {
			 LEDBlinkFor2SecondsCommand command = new LEDBlinkFor2SecondsCommand(2, true);
				command.start();			
		 } else if (getMatchIsAtTime(10)) {
			 LEDBlinkFor2SecondsCommand command = new LEDBlinkFor2SecondsCommand(1, true);
				command.start();		
		 }	
		 
		/*double x = tx.getDouble(0.0);
		double y = ty.getDouble(0.0);
		double area = ta.getDouble(0.0);*/

		//PCDashboardDiagnostics.AdHocNumber("LimelightX", tx.getDouble(0.0));
		//PCDashboardDiagnostics.AdHocNumber("LimelightY", ty.getDouble(0.0));
		//PCDashboardDiagnostics.AdHocNumber("LimelightArea", ta.getDouble(0.0));
	}
	
	public boolean getMatchIsAtTime(int matchSecond) {
		boolean isFinished = false;
		double matchTime = driverStation.getMatchTime();
		if (matchTime > matchSecond - .5 && matchTime < matchSecond + .5) {
			isFinished = true;
		}
		
		return isFinished;
	}
	
	public void setupDriveController() {
		
		DRIVE_CONTROLLER.getButton(ButtonCode.X).whenPressed(new SetGyroTargetHeading(270));
		DRIVE_CONTROLLER.getButton(ButtonCode.B).whenPressed(new SetGyroTargetHeading(90));
		DRIVE_CONTROLLER.getButton(ButtonCode.Y).whenPressed(new SetGyroTargetHeading(0));
		DRIVE_CONTROLLER.getButton(ButtonCode.A).whenPressed(new SetCutPowerTrue());//was new SetGyroTargetHeading(180)
		DRIVE_CONTROLLER.getButton(ButtonCode.A).whenReleased(new SetCutPowerFalse());
	}
		
	public void setupOperationPanel() {
		System.out.println("Operation PANEL CONFIGURED!!! Operation PANEL CONFIGURED!!!Operation PANEL CONFIGURED!!!Operation PANEL CONFIGURED!!!Operation PANEL CONFIGURED!!!Operation PANEL CONFIGURED!!!");
		OPERATION_PANEL2.getButton(ButtonCode.ARMEXTEND).whenPressed(new ArmExtendFullyCommand());
		OPERATION_PANEL2.getButton(ButtonCode.ARMMIDRANGE).whenPressed(new ArmMoveToHeightCommand(Calibrations.armEncoderValueMidway)); 
		OPERATION_PANEL.getButton(ButtonCode.ARMMANUALOVERRIDEEXTEND).whileHeld(new ArmExtendWhileHeldCommand());
		//OPERATION_PANEL.getButton(ButtonCode.ARMMANUALOVERRIDEEXTEND).whenPressed(new SetOverride1Command(Robot.OVERRIDE_SYSTEM_ARM_EXTEND, true));
		//OPERATION_PANEL.getButton(ButtonCode.ARMMANUALOVERRIDEEXTEND).whenReleased(new SetOverride1Command(Robot.OVERRIDE_SYSTEM_ARM_EXTEND, false));
		
		OPERATION_PANEL.getButton(ButtonCode.ARMMANUALOVERRIDERETRACT).whileHeld(new ArmRetractWhileHeldCommand());
		//OPERATION_PANEL.getButton(ButtonCode.ARMMANUALOVERRIDERETRACT).whenPressed(new SetOverride1Command(Robot.OVERRIDE_SYSTEM_ARM_RETRACT, true));
		//OPERATION_PANEL.getButton(ButtonCode.ARMMANUALOVERRIDERETRACT).whenReleased(new SetOverride1Command(Robot.OVERRIDE_SYSTEM_ARM_RETRACT, false));
		OPERATION_PANEL2.getButton(ButtonCode.ARMRETRACT).whenPressed(new ArmRetractFullyCommand());
		
		OPERATION_PANEL.getButton(ButtonCode.ELEVATORMANUALOVERRIDEDOWN).whileHeld(new ElevatorRetractWhileHeldCommand());
		
		
		OPERATION_PANEL.getButton(ButtonCode.ELEVATORMANUALOVERRIDEDOWN).whenPressed(new SetOverride1Command(Robot.OVERRIDE_SYSTEM_ELEVATOR_RETRACT, true));
		OPERATION_PANEL.getButton(ButtonCode.ELEVATORMANUALOVERRIDEDOWN).whenReleased(new SetOverride1Command(Robot.OVERRIDE_SYSTEM_ELEVATOR_RETRACT, false));
		
		OPERATION_PANEL.getButton(ButtonCode.ELEVATORMANUALOVERRIDEUP).whileHeld(new ElevatorExtendWhileHeldCommand());
		OPERATION_PANEL.getButton(ButtonCode.ELEVATORMANUALOVERRIDEUP).whenPressed(new SetOverride1Command(Robot.OVERRIDE_SYSTEM_ELEVATOR_EXTEND, true));
		OPERATION_PANEL.getButton(ButtonCode.ELEVATORMANUALOVERRIDEUP).whenReleased(new SetOverride1Command(Robot.OVERRIDE_SYSTEM_ELEVATOR_EXTEND, false));
		OPERATION_PANEL.getButton(ButtonCode.ELEVATOREXTEND).whenPressed(new ElevatorExtendQuarterWithPIDCommand(this.ELEVATOR_SUBSYSTEM));
		//OPERATION_PANEL.getButton(ButtonCode.ELEVATOREXTEND).whenPressed(new ArmMoveToHeightCommand(6000));
		OPERATION_PANEL.getButton(ButtonCode.ELEVATORMIDRANGE).whenPressed(new ElevatorMoveToHeightCommand(Calibrations.elevatorMidwayEncoderValue));
		OPERATION_PANEL.getButton(ButtonCode.ELEVATORMIDRANGE).whenPressed(new ArmMoveToHeightCommand(Calibrations.armEncoderValueMidway));
		OPERATION_PANEL.getButton(ButtonCode.ELEVATORSWITCHHEIGHT).whenPressed(new ElevatorMoveToHeightCommand(Calibrations.elevatorSwitchEncoderValue));
		OPERATION_PANEL.getButton(ButtonCode.ELEVATORSWITCHHEIGHT).whenPressed(new ArmExtendFullyCommand());
		OPERATION_PANEL.getButton(ButtonCode.ELEVATORRETRACT).whenPressed(new ElevatorRetractFullyCommand());
		
		
		// CZB Shepherd Friday: stealing this button to make a one-press, elevator to high scale button.
		OPERATION_PANEL2.getButton(ButtonCode.CARGOOVERRIDE).whenPressed(new ArmExtendToHighScaleCommand());
		OPERATION_PANEL2.getButton(ButtonCode.CARGOOVERRIDE).whenPressed(new ElevatorExtendFullyCommand());		
		
		
		OPERATION_PANEL2.getButton(ButtonCode.CARGODROP).whileHeld(new CargoWheelPushSoftCommand());
		//OPERATION_PANEL2.getButton(ButtonCode.CARGOOVERRIDE).whileHeld(new CargoWheelPullCommand());
		//OPERATION_PANEL2.getButton(ButtonCode.CARGOOVERRIDE).whenPressed(new SetOverride1Command(Robot.OVERRIDE_SYSTEM_CARGO, true));
		//OPERATION_PANEL2.getButton(ButtonCode.CARGOOVERRIDE).whenReleased(new SetOverride1Command(Robot.OVERRIDE_SYSTEM_CARGO, false));
		OPERATION_PANEL2.getButton(ButtonCode.CARGOSPIT).whileHeld(new CargoWheelPushHardCommand());
		OPERATION_PANEL2.getButton(ButtonCode.RUNCARGO).whileHeld(new CargoWheelPullCommand());
		
		//PCDashboardDiagnostics.SubsystemBoolean("Elevator", "RUNCARGO", OPERATION_PANEL.getButtonValue(ButtonCode.ELEVATORRETRACT));		
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
