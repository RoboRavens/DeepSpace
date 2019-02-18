/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.controls.AxisCode;
import frc.controls.ButtonCode;
import frc.controls.Gamepad;
import frc.controls.OperationPanel;
import frc.controls.OperationPanel2;
import frc.ravenhardware.RavenLighting;
import frc.robot.commands.LED.LEDBlinkFor2SecondsCommand;
import frc.robot.commands.arm.ArmExtendWhileHeldCommand;
import frc.robot.commands.arm.ArmRetractWhileHeldCommand;
import frc.robot.commands.automatedscoring.SetAutomatedCommand;
import frc.robot.commands.automatedscoring.SetCargoOrHatchPanelCommand;
import frc.robot.commands.automatedscoring.SetLocationCargoShipCommand;
import frc.robot.commands.automatedscoring.SetLocationRocketCommand;
import frc.robot.commands.automatedscoring.SetRocketHeightHighCommand;
import frc.robot.commands.automatedscoring.SetRocketHeightLowCommand;
import frc.robot.commands.automatedscoring.SetRocketHeightMidCommand;
import frc.robot.commands.cargowheel.CargoWheelSpitCommand;
import frc.robot.commands.cargowheel.CargoWheelSuckCommand;
import frc.robot.commands.drivetrain.DriveTrainTurnTargetCommand;
import frc.robot.commands.elevator.ElevatorExtendWhileHeldCommand;
import frc.robot.commands.elevator.ElevatorRetractWhileHeldCommand;
import frc.robot.commands.misc.SetOverride1Command;
import frc.robot.commands.beak.BeakCaptureHatchPanelCommand;
import frc.robot.commands.beak.BeakReleaseHatchPanelCommand;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.BeakSubsystem;
import frc.robot.subsystems.CargoWheelSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.CompressorSubsystem;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.LightSubsystem;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.ProgrammableLEDSubsystem;
import frc.robot.subsystems.SetCommandSubsystem;
import frc.util.LoggerOverlord;
import frc.util.NetworkTableDiagnostics;
import frc.util.OverrideSystem;

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
	public static final OperationPanel OPERATION_PANEL = new OperationPanel(1);
	public static final OperationPanel2 OPERATION_PANEL_2 = new OperationPanel2(2);
	public static final Gamepad OPERATION_CONTROLLER = new Gamepad(3);

	public static final ArmSubsystem ARM_SUBSYSTEM = new ArmSubsystem();
	public static final BeakSubsystem BEAK_SUBSYSTEM = new BeakSubsystem();
	public static final CargoWheelSubsystem CARGO_WHEEL_SUBSYSTEM = new CargoWheelSubsystem();
	public static final ClimberSubsystem CLIMBER_SUBSYSTEM = new ClimberSubsystem();
	public static final CompressorSubsystem COMPRESSOR_SUBSYSTEM = new CompressorSubsystem();
	public static final DriveTrainSubsystem DRIVE_TRAIN_SUBSYSTEM = new DriveTrainSubsystem();
	public static final ElevatorSubsystem ELEVATOR_SUBSYSTEM = new ElevatorSubsystem();
	public static final LightSubsystem LIGHT_SUBSYSTEM = new LightSubsystem();
	public static final LimelightSubsystem LIMELIGHT_SUBSYSTEM = new LimelightSubsystem();
	public static final ProgrammableLEDSubsystem LED_SUBSYSTEM = new ProgrammableLEDSubsystem();
	public static final SetCommandSubsystem SET_COMMAND_SUBSYSTEM = new SetCommandSubsystem();

	public static final Relay HAS_HATCH_PANEL_LEDS_RELAY = new Relay(RobotMap.hasHatchPanelLEDLightRelay);
	public static final Relay HAS_CARGO_LEDS_RELAY = new Relay(RobotMap.hasCargoLEDLightRelay);
	public static final Relay UNDERGLOW_RELAY = new Relay(RobotMap.underglowLightRelay);
	public static final RavenLighting HAS_CUBE_LEDS = new RavenLighting(HAS_HATCH_PANEL_LEDS_RELAY);
	public static final RavenLighting UNDERGLOW = new RavenLighting(UNDERGLOW_RELAY);

	CameraServer server;

	public static final OverrideSystem OVERRIDE_SYSTEM_ELEVATOR_EXTEND = new OverrideSystem();
	public static final OverrideSystem OVERRIDE_SYSTEM_ARM_EXTEND = new OverrideSystem();
	public static final OverrideSystem OVERRIDE_SYSTEM_ELEVATOR_RETRACT = new OverrideSystem();
	public static final OverrideSystem OVERRIDE_SYSTEM_ARM_RETRACT = new OverrideSystem();
	public static final OverrideSystem OVERRIDE_SYSTEM_CARGO = new OverrideSystem();

	Command autonomousCommand;

	public boolean isRedAlliance;

	String autoFromDashboard;
	String positionFromDashboard;

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {

		driverStation = DriverStation.getInstance();

		// m_chooser.addDefault("Default Auto", new Command());
		// m_chooser.addObject(name, object);
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", m_chooser);
		driverStation.getMatchTime();
		// Zero the elevator encoders; the robot should always start with the elevator
		// down.
		// Note that this may not be true in practice, so we should later integrate the
		// reset with limit switch code.
		Robot.ELEVATOR_SUBSYSTEM.resetEncodersToRetractedLimit();
		Robot.ARM_SUBSYSTEM.resetEncodersToRetractionLimit();

		this.setupDriveController();
		this.setupOperationPanel();
	}

	@Override
	public void robotPeriodic() {
		NetworkTableDiagnostics.SendData();
	}

	/**
	 * This function is called once each time the robot enters Disabled mode. You
	 * can use it to reset any subsystem information you want to clear when the
	 * robot is disabled.
	 */
	@Override
	public void disabledInit() {
		Robot.LED_SUBSYSTEM.setDisabledPattern();

	}

	public Robot() {
		server = CameraServer.getInstance();
		// server.setQuality(Calibrations.cameraQuality);
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
		} else if (alliance.compareTo(Alliance.Red) == 0) {
			allianceString = "Red alliance";
			this.isRedAlliance = true;
		} else {
			allianceString = "Alliance not identified.";
			this.isRedAlliance = false;
		}

		SmartDashboard.putString("DB/String 4", allianceString);

		Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.setGyroTargetHeadingToCurrentHeading();

		diagnostics.outputDisabledDiagnostics();

		SmartDashboard.putString("DB/String 5", "TBD - Awaiting plates");

		// this.elevator.getPosition();
		// this.elevator.getIsAtLimits();
		// this.arm.getPosition();
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
	 * between different autonomous modes using the dashboard. The sendable chooser
	 * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
	 * remove all of the chooser code and uncomment the getString code to get the
	 * auto name from the text box below the Gyro
	 *
	 * <p>
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons to
	 * the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {}

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

		diagnostics.outputTeleopDiagnostics();

		if (getMatchIsAtTime(90)) {
			LEDBlinkFor2SecondsCommand command = new LEDBlinkFor2SecondsCommand(4, false);
			command.start();
			command.close();
		} else if (getMatchIsAtTime(60)) {
			LEDBlinkFor2SecondsCommand command = new LEDBlinkFor2SecondsCommand(3, false);
			command.start();
			command.close();
		} else if (getMatchIsAtTime(30)) {
			LEDBlinkFor2SecondsCommand command = new LEDBlinkFor2SecondsCommand(2, true);
			command.start();
			command.close();
		} else if (getMatchIsAtTime(10)) {
			LEDBlinkFor2SecondsCommand command = new LEDBlinkFor2SecondsCommand(1, true);
			command.start();
			command.close();
		}
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
		DRIVE_CONTROLLER.getButton(ButtonCode.Y).whenPressed(new BeakCaptureHatchPanelCommand());
		DRIVE_CONTROLLER.getButton(ButtonCode.A).whenPressed(new CargoWheelSuckCommand());
		if (DRIVE_CONTROLLER.getAxisIsPressed(AxisCode.RIGHTTRIGGER)) {
			new DriveTrainTurnTargetCommand();
		}
	}

	public void setupOperationPanel() {
		System.out.println("Operation PANEL CONFIGURED!!! Operation PANEL CONFIGURED!!!");
		OPERATION_PANEL.getButton(ButtonCode.ELEVATOROVERRIDERETRACT).whileHeld(new ElevatorRetractWhileHeldCommand());
		OPERATION_PANEL.getButton(ButtonCode.ELEVATOROVERRIDERETRACT).whenPressed(new SetOverride1Command(Robot.OVERRIDE_SYSTEM_ELEVATOR_RETRACT, true));
		OPERATION_PANEL.getButton(ButtonCode.ELEVATOROVERRIDERETRACT).whenReleased(new SetOverride1Command(Robot.OVERRIDE_SYSTEM_ELEVATOR_RETRACT, false));
		
		OPERATION_PANEL.getButton(ButtonCode.ELEVATOROVERRIDEEXTEND).whileHeld(new ElevatorExtendWhileHeldCommand());
		OPERATION_PANEL.getButton(ButtonCode.ELEVATOROVERRIDEEXTEND).whenPressed(new SetOverride1Command(Robot.OVERRIDE_SYSTEM_ELEVATOR_EXTEND, true));
		OPERATION_PANEL.getButton(ButtonCode.ELEVATOROVERRIDEEXTEND).whenReleased(new SetOverride1Command(Robot.OVERRIDE_SYSTEM_ELEVATOR_EXTEND, false));
		
		OPERATION_PANEL.getButton(ButtonCode.ARMOVERRIDEEXTEND).whileHeld(new ArmExtendWhileHeldCommand());
		OPERATION_PANEL.getButton(ButtonCode.ARMOVERRIDEEXTEND).whenPressed(new SetOverride1Command(Robot.OVERRIDE_SYSTEM_ARM_EXTEND, true));
		OPERATION_PANEL.getButton(ButtonCode.ARMOVERRIDEEXTEND).whenReleased(new SetOverride1Command(Robot.OVERRIDE_SYSTEM_ARM_EXTEND, false));

		OPERATION_PANEL.getButton(ButtonCode.ARMOVERRIDERETRACT).whileHeld(new ArmRetractWhileHeldCommand());
		OPERATION_PANEL.getButton(ButtonCode.ARMOVERRIDERETRACT).whenPressed(new SetOverride1Command(Robot.OVERRIDE_SYSTEM_ARM_EXTEND, true));
		OPERATION_PANEL.getButton(ButtonCode.ARMOVERRIDERETRACT).whenReleased(new SetOverride1Command(Robot.OVERRIDE_SYSTEM_ARM_EXTEND, false));

		OPERATION_PANEL_2.getButton(ButtonCode.CARGOSPITOVERRIDE).whileHeld(new CargoWheelSpitCommand());
		OPERATION_PANEL_2.getButton(ButtonCode.BEAKRELEASEOVERRIDE).whenPressed(new BeakReleaseHatchPanelCommand());

		OPERATION_PANEL.getButton(ButtonCode.SETHATCH).whenPressed(new SetCargoOrHatchPanelCommand("Hatch Panel"));
		OPERATION_PANEL.getButton(ButtonCode.SETCARGO).whenPressed(new SetCargoOrHatchPanelCommand("Cargo"));
		OPERATION_PANEL.getButton(ButtonCode.SETLOCATIONCARGOSHIP).whenPressed(new SetLocationCargoShipCommand());
		OPERATION_PANEL.getButton(ButtonCode.SETLOCATIONROCKET).whenPressed(new SetLocationRocketCommand());
		OPERATION_PANEL_2.getButton(ButtonCode.ROCKETHEIGHTHIGH).whenPressed(new SetRocketHeightHighCommand());
		OPERATION_PANEL_2.getButton(ButtonCode.ROCKETHEIGHTMID).whenPressed(new SetRocketHeightMidCommand());
		OPERATION_PANEL_2.getButton(ButtonCode.ROCKETHEIGHTLOW).whenPressed(new SetRocketHeightLowCommand());
		OPERATION_PANEL_2.getButton(ButtonCode.SETAUTOMATEDCOMMAND).whileHeld(new SetAutomatedCommand());

		//OPERATION_PANEL_2.getButton(ButtonCode.TESTINGBUTTON).whileHeld(); USE WHEN TESTING NEW COMMANDS
	}
	

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}