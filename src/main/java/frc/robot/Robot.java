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
import frc.robot.commands.arm.ArmExtendWhileHeldCommand;
import frc.robot.commands.arm.ArmRetractWhileHeldCommand;
import frc.robot.commands.automatedscoring.RunAutomatedCommand;
import frc.robot.commands.automatedscoring.SetCargoOrHatchPanelCommand;
import frc.robot.commands.beak.BeakCaptureHatchPanelCommand;
import frc.robot.commands.beak.BeakReleaseHatchPanelCommand;
import frc.robot.commands.cargo.CargoCaptureHPSCommand;
import frc.robot.commands.cargowheel.CargoWheelSpitCommand;
import frc.robot.commands.cargowheel.CargoWheelStopCommand;
import frc.robot.commands.cargowheel.CargoWheelSuckCommand;
import frc.robot.commands.climber.ClimberExtendWhileHeldCommand;
import frc.robot.commands.climber.ClimberRetractWhileHeldCommand;
import frc.robot.commands.drivetrain.DriveTrainAlignFromHPSToRocketCommand;
import frc.robot.commands.elevator.ElevatorExtendWhileHeldCommand;
import frc.robot.commands.elevator.ElevatorRetractWhileHeldCommand;
import frc.robot.commands.hatchpanel.SetReadyToCollectTrue;
import frc.robot.commands.intaketransport.IntakeExtendCommand;
import frc.robot.commands.intaketransport.IntakeRetractCommand;
import frc.robot.commands.misc.LimelightToggleLEDCommand;
import frc.robot.commands.misc.RetractAllCommand;
import frc.robot.commands.misc.SetOverride1Command;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.BeakSubsystem;
import frc.robot.subsystems.CargoWheelSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.CompressorSubsystem;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.GamePiecePossessedSubsystem;
import frc.robot.subsystems.IntakeTransportSubsystem;
import frc.robot.subsystems.LightSubsystem;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.LineAlignmentSubsystem;
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
	public Command m_autonomousCommand;
	public SendableChooser<Command> m_chooser = new SendableChooser<>();

	public DriverStation driverStation;
	public PowerDistributionPanel PDP = new PowerDistributionPanel();

	public boolean gamePieceIsHatch = true;

	public Diagnostics diagnostics = new Diagnostics();
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
	public static final LineAlignmentSubsystem LINE_ALIGNMENT_SUBSYSTEM = new LineAlignmentSubsystem();
	public static final GamePiecePossessedSubsystem GAME_PIECE_POSSESSED_SUBSYSTEM = new GamePiecePossessedSubsystem();
	public static final IntakeTransportSubsystem INTAKE_TRANSPORT_SUBSYSTEM = new IntakeTransportSubsystem();

	public CameraServer server;

	public static final OverrideSystem OVERRIDE_SYSTEM_ELEVATOR_EXTEND = new OverrideSystem();
	public static final OverrideSystem OVERRIDE_SYSTEM_ARM_EXTEND = new OverrideSystem();
	public static final OverrideSystem OVERRIDE_SYSTEM_ELEVATOR_RETRACT = new OverrideSystem();
	public static final OverrideSystem OVERRIDE_SYSTEM_ARM_RETRACT = new OverrideSystem();
	public static final OverrideSystem OVERRIDE_SYSTEM_CARGO = new OverrideSystem();
	public static final OverrideSystem OVERRIDE_SYSTEM_CLIMBER_EXTEND = new OverrideSystem();
	public static final OverrideSystem OVERRIDE_SYSTEM_CLIMBER_RETRACT = new OverrideSystem();

	public Command autonomousCommand;

	public boolean isRedAlliance;

	public String autoFromDashboard;
	public String positionFromDashboard;

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {

		driverStation = DriverStation.getInstance();

		SmartDashboard.putData("Auto mode", m_chooser);
		driverStation.getMatchTime();
		// Zero the elevator encoders; the robot should always g with the elevator
		// down.
		ELEVATOR_SUBSYSTEM.resetEncodersToRetractedLimit();
		ARM_SUBSYSTEM.resetEncodersToRetractionLimit();

		BEAK_SUBSYSTEM.release();

		LIMELIGHT_SUBSYSTEM.turnLEDOff();

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
		LED_SUBSYSTEM.setDisabledPattern();
	}

	/*public Robot() {
		server = CameraServer.getInstance();
		server.startAutomaticCapture();
	}*/

	@Override
	public void disabledPeriodic() {
		LED_SUBSYSTEM.setDisabledPattern();
		LIMELIGHT_SUBSYSTEM.turnLEDOff();

		Scheduler.getInstance().run();

		autoFromDashboard = SmartDashboard.getString("DB/String 0", "myDefaultData");
		positionFromDashboard = SmartDashboard.getString("DB/String 2", "myDefaultData");

		//outputAutoModeToDashboardStringOne(autoFromDashboard);
		//outputPositionToDashboardStringThree(positionFromDashboard);

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

		DRIVE_TRAIN_SUBSYSTEM.ravenTank.setGyroTargetHeadingToCurrentHeading();

		diagnostics.outputDisabledDiagnostics();

		SmartDashboard.putString("DB/String 5", "TBD - Awaiting plates");
	}

	/*public void outputAutoModeToDashboardStringOne(String autoMode) {
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
	}*/

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
	public void autonomousInit() {
		LED_SUBSYSTEM.setAutonomousPattern();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		diagnostics.outputAutonomousDiagnostics();
		this.teleopPeriodic();
	}

	@Override
	public void teleopInit() {
		DRIVE_TRAIN_SUBSYSTEM.ravenTank.setGyroTargetHeadingToCurrentHeading();
		DRIVE_TRAIN_SUBSYSTEM.ravenTank.resetGyroAdjustmentScaleFactor();

		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
		}

		LED_SUBSYSTEM.setEnabledPattern();
	}

	public void cargoIntake() {

	}

	public void cargoScore() {

	}

	public void hatchIntake() {

	}

	public void hatchScore() {

	}


	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		diagnostics.outputTeleopDiagnostics();

		if (DRIVE_TRAIN_SUBSYSTEM.ravenTank.userControlOfCutPower) {
			if (DRIVE_CONTROLLER.getAxis(AxisCode.RIGHTTRIGGER) > .25) {
				System.out.println("CUT POWER TRUE");
			  DRIVE_TRAIN_SUBSYSTEM.ravenTank.setCutPower(true);
			}
			else {
			  DRIVE_TRAIN_SUBSYSTEM.ravenTank.setCutPower(false);
			}
		}

		if (DRIVE_CONTROLLER.getButton(ButtonCode.LEFTBUMPER).get()) {
			if (gamePieceIsHatch) {
				Command beakCaptureCommand = new BeakCaptureHatchPanelCommand();
				beakCaptureCommand.start();
			}
			else {
				Command cargoCaptureCommand = new CargoWheelSuckCommand();
				cargoCaptureCommand.start();
			}
		}
		else if (DRIVE_CONTROLLER.getButton(ButtonCode.RIGHTBUMPER).get()) {
			if (gamePieceIsHatch) {
				Command command = new BeakReleaseHatchPanelCommand();
				command.start();
			}
			else {
				Command command = new CargoWheelSpitCommand();
				command.start();
			}
		}
		else {
			Command command = new CargoWheelStopCommand();
			command.start();
		}

		if (OPERATION_PANEL_2.getButton(ButtonCode.HATCHOVERRIDE).get()) {
			gamePieceIsHatch = true;
		}

		if (OPERATION_PANEL_2.getButton(ButtonCode.CARGOOVERRIDE).get()) {
			Robot.BEAK_SUBSYSTEM.capture();
			Robot.INTAKE_TRANSPORT_SUBSYSTEM.intakeExtend();
			gamePieceIsHatch = false;
		}
	}

	public void setupDriveController() {
		DRIVE_CONTROLLER.getButton(ButtonCode.RIGHTBUMPER).whenPressed(new IntakeExtendCommand());
		//DRIVE_CONTROLLER.getButton(ButtonCode.A).whenPressed(new ClimberThirdLevelCommand());
		DRIVE_CONTROLLER.getButton(ButtonCode.B).whenPressed(new IntakeRetractCommand());
		DRIVE_CONTROLLER.getButton(ButtonCode.BACK).whenPressed(new LimelightToggleLEDCommand());
		DRIVE_CONTROLLER.getButton(ButtonCode.X).whenPressed(new DriveTrainAlignFromHPSToRocketCommand());
	}

	public void setupOperationPanel() {
		System.out.println("Operation PANEL CONFIGURED!!! Operation PANEL CONFIGURED!!!");
		OPERATION_PANEL.getButton(ButtonCode.ELEVATOROVERRIDERETRACT).whileHeld(new ElevatorRetractWhileHeldCommand());
		OPERATION_PANEL.getButton(ButtonCode.ELEVATORDOUBLEOVERRIDERETRACT).whenPressed(new SetOverride1Command(OVERRIDE_SYSTEM_ELEVATOR_RETRACT, true));
		OPERATION_PANEL.getButton(ButtonCode.ELEVATORDOUBLEOVERRIDERETRACT).whenReleased(new SetOverride1Command(OVERRIDE_SYSTEM_ELEVATOR_RETRACT, false));
		
		OPERATION_PANEL.getButton(ButtonCode.ELEVATOROVERRIDEEXTEND).whileHeld(new ElevatorExtendWhileHeldCommand());
		OPERATION_PANEL.getButton(ButtonCode.ELEVATORDOUBLEOVERRIDEEXTEND).whenPressed(new SetOverride1Command(OVERRIDE_SYSTEM_ELEVATOR_EXTEND, true));
		OPERATION_PANEL.getButton(ButtonCode.ELEVATORDOUBLEOVERRIDEEXTEND).whenReleased(new SetOverride1Command(OVERRIDE_SYSTEM_ELEVATOR_EXTEND, false));
		
		OPERATION_PANEL.getButton(ButtonCode.ARMOVERRIDEEXTEND).whileHeld(new ArmExtendWhileHeldCommand());
		OPERATION_PANEL.getButton(ButtonCode.ARMDOUBLEOVERRIDEEXTEND).whenPressed(new SetOverride1Command(OVERRIDE_SYSTEM_ARM_EXTEND, true));
		OPERATION_PANEL.getButton(ButtonCode.ARMDOUBLEOVERRIDEEXTEND).whenReleased(new SetOverride1Command(OVERRIDE_SYSTEM_ARM_EXTEND, false));

		OPERATION_PANEL.getButton(ButtonCode.ARMOVERRIDERETRACT).whileHeld(new ArmRetractWhileHeldCommand());
		OPERATION_PANEL.getButton(ButtonCode.ARMDOUBLEOVERRIDERETRACT).whenPressed(new SetOverride1Command(OVERRIDE_SYSTEM_ARM_EXTEND, true));
		OPERATION_PANEL.getButton(ButtonCode.ARMDOUBLEOVERRIDERETRACT).whenReleased(new SetOverride1Command(OVERRIDE_SYSTEM_ARM_EXTEND, false));

		OPERATION_PANEL_2.getButton(ButtonCode.CLIMBEROVERRIDEEXTEND).whileHeld(new ClimberExtendWhileHeldCommand());
		OPERATION_PANEL_2.getButton(ButtonCode.CLIMBEROVERRIDERETRACT).whileHeld(new ClimberRetractWhileHeldCommand());

		OPERATION_PANEL.getButton(ButtonCode.ROCKETHIGH).whenPressed(new RunAutomatedCommand());
		OPERATION_PANEL.getButton(ButtonCode.ROCKETMID).whenPressed(new RunAutomatedCommand());
		OPERATION_PANEL.getButton(ButtonCode.ROCKETLOW).whenPressed(new RunAutomatedCommand());
		OPERATION_PANEL.getButton(ButtonCode.CARGOSHIP).whenPressed(new RunAutomatedCommand());

		OPERATION_PANEL_2.getButton(ButtonCode.READYTOCOLLECT).whenPressed(new SetReadyToCollectTrue());
		OPERATION_PANEL_2.getButton(ButtonCode.HATCHOVERRIDE).whenPressed(new SetCargoOrHatchPanelCommand("Hatch"));
		OPERATION_PANEL_2.getButton(ButtonCode.CARGOOVERRIDE).whenPressed(new SetCargoOrHatchPanelCommand("Cargo"));
		OPERATION_PANEL_2.getButton(ButtonCode.RETRACTALL).whenPressed(new RetractAllCommand());
		OPERATION_PANEL_2.getButton(ButtonCode.CARGOHPS).whenPressed(new CargoCaptureHPSCommand());
	}
	
	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}