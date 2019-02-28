/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.controls.ButtonCode;
import frc.robot.Calibrations;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.elevator.ElevatorHoldPositionCommand;
import frc.util.NetworkTableDiagnostics;
import frc.robot.TalonSRXConstants;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Timer;

public class ElevatorSubsystem extends Subsystem {
	public TalonSRX elevatorMotor;
	public TalonSRX elevatorMotorFollower;
	private Timer _safetyTimer = new Timer();
	private double _expectedPower;

	public ElevatorSubsystem() {
		this.elevatorMotor = new TalonSRX(RobotMap.elevatorMotor);
		this.elevatorMotorFollower = new TalonSRX(RobotMap.elevatorMotorFollower);
		this.elevatorMotorFollower.follow(elevatorMotor);
		elevatorMotor.config_kF(TalonSRXConstants.kPIDLoopIdx, Calibrations.elevatorkF, TalonSRXConstants.kTimeoutMs);
		elevatorMotor.config_kP(TalonSRXConstants.kPIDLoopIdx, Calibrations.elevatorkP, TalonSRXConstants.kTimeoutMs);
		elevatorMotor.config_kI(TalonSRXConstants.kPIDLoopIdx, Calibrations.elevatorkI, TalonSRXConstants.kTimeoutMs);
		elevatorMotor.config_kD(TalonSRXConstants.kPIDLoopIdx, Calibrations.elevatorkD, TalonSRXConstants.kTimeoutMs);

		this.elevatorMotor.setSensorPhase(false);
		this.elevatorMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, TalonSRXConstants.kTimeoutMs);
		this.elevatorMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, TalonSRXConstants.kTimeoutMs);

		/* Don't neutral motor if remote limit source is not available */
		this.elevatorMotor.configLimitSwitchDisableNeutralOnLOS(true, TalonSRXConstants.kTimeoutMs);

		this.registerDiagnostics();
	}

	public void initDefaultCommand() {
		setDefaultCommand(new ElevatorHoldPositionCommand());
	}

	public void extend(double magnitude) {
		if (isAtExtensionLimit() && Robot.OPERATION_PANEL.getButtonValue(ButtonCode.ELEVATORDOUBLEOVERRIDEEXTEND) == false) {
			stop();
		} else {
			set(magnitude);
		}
	}

	public void retract(double magnitude) {
		if (isAtRetractionLimit() && Robot.OPERATION_PANEL.getButtonValue(ButtonCode.ELEVATORDOUBLEOVERRIDERETRACT) == false) {
			stop();
		} else {
			set(-1 * magnitude);
		}
	}

	private void set(double magnitude) {
		magnitude = Math.min(magnitude, 1);
		magnitude = Math.max(magnitude, -1);
		magnitude *= 1;

		if (isAtExtensionLimit() && Math.signum(magnitude) == 1) {
			magnitude = 0;
		}
		if (isAtRetractionLimit() && Math.signum(magnitude) == -1) {
			magnitude = 0;
		}

		_expectedPower = magnitude;

		elevatorMotor.set(ControlMode.PercentOutput, magnitude);
	}

	public void getPosition() {
		System.out.print("Elevator Position: " + getEncoderPosition());
	}

	public double getEncoderPosition() {
		int EncoderPosition = elevatorMotor.getSelectedSensorPosition();

		return EncoderPosition;
	}

	public void periodic() {
		this.isAtExtensionLimit();
		this.isAtRetractionLimit();

		checkExpectedSpeedVersusPower();
	}

	public void registerDiagnostics() {
		NetworkTableDiagnostics.SubsystemNumber("Elevator", "Encoder", () -> getEncoderPosition());
		NetworkTableDiagnostics.SubsystemBoolean("Elevator", "LimitEncoderExtended", () -> isEncoderAtExtensionLimit());
		NetworkTableDiagnostics.SubsystemBoolean("Elevator", "LimitEncoderRetracted", () -> isEncoderAtRetractionLimit());
		NetworkTableDiagnostics.SubsystemBoolean("Elevator", "LimitSwitchExtended", () -> getElevatorExtensionLimitSwitchValue());
		NetworkTableDiagnostics.SubsystemBoolean("Elevator", "LimitSwitchRetracted", () -> getElevatorRetractionLimitSwitchValue());
		NetworkTableDiagnostics.SubsystemBoolean("Elevator", "LimitFinalExtension", () -> isAtExtensionLimit());
		NetworkTableDiagnostics.SubsystemBoolean("Elevator", "LimitFinalRetraction", () -> isAtRetractionLimit());

		NetworkTableDiagnostics.SubsystemBoolean("Elevator", "OverrideExtend", () -> Robot.OVERRIDE_SYSTEM_ELEVATOR_EXTEND.getOverride1());
		NetworkTableDiagnostics.SubsystemBoolean("Elevator", "OverrideRetract", () -> Robot.OVERRIDE_SYSTEM_ELEVATOR_RETRACT.getOverride1());
		// Measure power sent to elevator
		NetworkTableDiagnostics.SubsystemNumber("Elevator", "EncoderExpectedPower", () -> _expectedPower);

		NetworkTableDiagnostics.SubsystemBoolean("Elevator", "LimitSwitchAndEncoderAgreeRetracted", () -> encoderAndLimitsMatchRetracted());
		NetworkTableDiagnostics.SubsystemBoolean("Elevator", "LimitSwitchAndEncoderAgreeExtended", () -> encoderAndLimitsMatchExtended());
	}

	public boolean encoderAndLimitsMatchRetracted() {
		boolean match = true;

		if (getEncoderPosition() < Calibrations.elevatorEncoderMinimumValue
				&& getElevatorRetractionLimitSwitchValue() == false) {
			match = false;
		}

		if (getElevatorRetractionLimitSwitchValue() == true
				&& getEncoderPosition() > Calibrations.elevatorEncoderMinimumValue
						+ Calibrations.elevatorLiftDownwardSafetyMargin) {
			match = false;
		}

		return match;
	}

	public boolean encoderAndLimitsMatchExtended() {
		if (getEncoderPosition() > Calibrations.elevatorEncoderMaximumValue
				&& getElevatorExtensionLimitSwitchValue() == false) {
			return false;
		}

		if (getElevatorExtensionLimitSwitchValue() == true
				&& getEncoderPosition() < Calibrations.elevatorEncoderMaximumValue
						- Calibrations.elevatorLiftUpwardSafetyMargin) {
			return false;
		}

		return true;
	}

	public void checkExpectedSpeedVersusPower() {
		// Check if elevator is being sent power and not moving at the right speed
		if (Math.abs(_expectedPower) > Calibrations.elevatorHoldPositionPowerMagnitude) {
			// The line below only returns as true if the elevator is pushing harder than it needs to not move it
			if (Math.abs(
					elevatorMotor.getSelectedSensorVelocity()) < Calibrations.elevatorConsideredMovingEncoderRate) {
				burnoutProtection();
			}
		}
	}

	public void burnoutProtection() {
		ElevatorHoldPositionCommand command = new ElevatorHoldPositionCommand();
		command.start();
		command.close();
	}

	public void getIsAtLimits() {
		System.out.print(" Extension Limit: " + isAtExtensionLimit() + " Retraction Limit: " + isAtRetractionLimit());
	}

	public void resetEncodersToRetractedLimit() {
		elevatorMotor.setSelectedSensorPosition(Calibrations.elevatorEncoderMinimumValue, 0, 0);
	}

	public void resetEncodersToExtendedLimit() {
		elevatorMotor.setSelectedSensorPosition(Calibrations.elevatorEncoderMaximumValue, 0, 0);
	}

	public void setMotorsPID(int position) {
		elevatorMotor.set(ControlMode.Position, position);
	}

	public void stop() {
		elevatorMotor.set(ControlMode.PercentOutput, 0);
	}

	// Right now this method just looks at the right limit switch; some combination of both should be used.

	public void expectElevatorToBeAtRetractionLimit() {
		if (getElevatorRetractionLimitSwitchValue()) {
			resetEncodersToExtendedLimit();
		}
	}

	public void expectElevatorToBeAtExtensionLimit() {
		if (getElevatorExtensionLimitSwitchValue()) {
			resetEncodersToExtendedLimit();
		}
	}

	public boolean isEncoderAtExtensionLimit() {
    	boolean encoderLimit = false;
    	
    	if (this.getEncoderPosition() >= Calibrations.elevatorEncoderMaximumValue - Calibrations.elevatorLiftUpwardSafetyMargin) {
    		encoderLimit = true;
    	}
    	
    	return encoderLimit;
    }
    
    public boolean isEncoderAtRetractionLimit() {
    	boolean encoderLimit = false;
    	
    	if (this.getEncoderPosition() <= Calibrations.elevatorEncoderMinimumValue + Calibrations.elevatorLiftDownwardSafetyMargin) {
    		encoderLimit = true;
    	}
    	
    	return encoderLimit;
    }

	// Right now this method just looks at the right limit switch; some combination of both should be used.
	public boolean isAtExtensionLimit() {
    	boolean encoderLimit = false;
    	boolean switchLimit = false;
    	
    	encoderLimit = this.isEncoderAtExtensionLimit();
    
    	if (this.getElevatorExtensionLimitSwitchValue() == true) {
    		switchLimit = true;
    		this.resetEncodersToExtendedLimit();
    	}
    	
    	return Robot.OVERRIDE_SYSTEM_ELEVATOR_EXTEND.getIsAtLimit(encoderLimit, switchLimit);
    }

	public boolean isAtRetractionLimit() {
    	boolean encoderLimit = false;
    	boolean switchLimit = false;
    	
    	encoderLimit = this.isEncoderAtRetractionLimit();
    	
    	if (this.getElevatorRetractionLimitSwitchValue() == true) {
    		switchLimit = true;
    		this.resetEncodersToRetractedLimit();
    	}
    	
    	return Robot.OVERRIDE_SYSTEM_ELEVATOR_RETRACT.getIsAtLimit(encoderLimit, switchLimit);
    }

	public void holdPosition() {
		elevatorMotor.set(ControlMode.PercentOutput, Calibrations.elevatorHoldPositionPowerMagnitude);
	}

	public double getElevatorHeightPercentage() {
		double encoderMax = (double) Calibrations.elevatorEncoderMaximumValue;
		double encoderMin = (double) Calibrations.elevatorEncoderMinimumValue;
		double encoderCurrent = getEncoderPosition();

		double heightPercentage = (encoderCurrent - encoderMin) / (encoderMax - encoderMin);
		heightPercentage = Math.min(1, heightPercentage);
		heightPercentage = Math.max(0, heightPercentage);

		return heightPercentage;
	}

	public static double inchesToTicks(double inches) {
		double encoderTicks = inches;
		encoderTicks -= Calibrations.elevatorInchesToEncoderTicksOffsetValue;
		encoderTicks *= Calibrations.elevatorInchesToEncoderTicksConversionValue;

		return encoderTicks;
	}

	public static double ticksToInches(double encoderTicks) {
		double inches = encoderTicks;
		inches /= Calibrations.elevatorInchesToEncoderTicksConversionValue;
		inches += Calibrations.elevatorInchesToEncoderTicksOffsetValue;

		return inches;
	}

	public boolean getElevatorExtensionLimitSwitchValue() {
		return this.elevatorMotor.getSensorCollection().isFwdLimitSwitchClosed();
	}

	public boolean getElevatorRetractionLimitSwitchValue() {
		return this.elevatorMotor.getSensorCollection().isRevLimitSwitchClosed();
	}

	public boolean getIsExtendedPastEncoderPosition(int encoderPosition) {
		if (getEncoderPosition() > encoderPosition + Calibrations.ELEVATOR_AT_POSITION_BUFFER) {
			return true;
		}

		return false;
	}

	public boolean getIsRetractedBeforeEncoderPosition(int encoderPosition) {
		if (getEncoderPosition() < encoderPosition - Calibrations.ELEVATOR_AT_POSITION_BUFFER) {
			return true;
		}

		return false;
	}

	public boolean getIsAtPosition(int encoderPosition) {

		boolean notOverExtended = getIsExtendedPastEncoderPosition(encoderPosition);
		boolean notOverRetracted = getIsRetractedBeforeEncoderPosition(encoderPosition);

		if (notOverExtended == false && notOverRetracted == false) {
			return true;
		}

		return false;
	}

	public void resetSafetyTimer() {
		_safetyTimer.reset();
	}

	public void startSafetyTimer() {
		_safetyTimer.start();
	}

	public double getSafetyTimer() {
		return _safetyTimer.get();
	}

}
