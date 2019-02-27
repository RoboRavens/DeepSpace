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
import frc.robot.commands.climber.ClimberHoldPositionCommand;
import frc.util.PCDashboardDiagnostics;
import frc.robot.TalonSRXConstants;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Timer;

public class ClimberSubsystem extends Subsystem {
	public TalonSRX climberMotor;
	private Timer _safetyTimer = new Timer();
	private double _expectedPower;

	public ClimberSubsystem() {
		this.climberMotor = new TalonSRX(RobotMap.climberMotor);
		climberMotor.config_kF(TalonSRXConstants.kPIDLoopIdx, Calibrations.climberkF, TalonSRXConstants.kTimeoutMs);
		climberMotor.config_kP(TalonSRXConstants.kPIDLoopIdx, Calibrations.climberkP, TalonSRXConstants.kTimeoutMs);
		climberMotor.config_kI(TalonSRXConstants.kPIDLoopIdx, Calibrations.climberkI, TalonSRXConstants.kTimeoutMs);
		climberMotor.config_kD(TalonSRXConstants.kPIDLoopIdx, Calibrations.climberkD, TalonSRXConstants.kTimeoutMs);

		this.climberMotor.setSensorPhase(false);
		this.climberMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, TalonSRXConstants.kTimeoutMs);
		this.climberMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, TalonSRXConstants.kTimeoutMs);

		/* Don't neutral motor if remote limit source is not available */
		this.climberMotor.configLimitSwitchDisableNeutralOnLOS(true, TalonSRXConstants.kTimeoutMs);
	}

	public void initDefaultCommand() {
		setDefaultCommand(new ClimberHoldPositionCommand());
	}

	public void extend(double magnitude) {
		if (isAtExtensionLimit() /*&& Robot.OPERATION_PANEL.getButtonValue(ButtonCode.CLIMBERDOUBLEOVERRIDEEXTEND) == false*/) {
			stop();
		} else {
			set(magnitude);
		}
	}

	public void retract(double magnitude) {
		if (isAtRetractionLimit() /*&& Robot.OPERATION_PANEL.getButtonValue(ButtonCode.CLIMBERDOUBLEOVERRIDERETRACT) == false*/) {
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

		climberMotor.set(ControlMode.PercentOutput, magnitude);
	}

	public void getPosition() {
		System.out.print("Climber Position: " + getEncoderPosition());
	}

	public double getEncoderPosition() {
		int EncoderPosition = climberMotor.getSelectedSensorPosition();

		return EncoderPosition;
	}

	public void periodic() {
		this.isAtExtensionLimit();
		this.isAtRetractionLimit();

		climberSubsystemDiagnostics();
		checkExpectedSpeedVersusPower();
	}

	public void climberSubsystemDiagnostics() {
		PCDashboardDiagnostics.SubsystemNumber("Climber", "Encoder", getEncoderPosition());
		PCDashboardDiagnostics.SubsystemBoolean("Climber", "LimitEncoderExtended", isEncoderAtExtensionLimit());
		PCDashboardDiagnostics.SubsystemBoolean("Climber", "LimitEncoderRetracted", isEncoderAtRetractionLimit());
		PCDashboardDiagnostics.SubsystemBoolean("Climber", "LimitSwitchExtended", getClimberExtensionLimitSwitchValue());
		PCDashboardDiagnostics.SubsystemBoolean("Climber", "LimitSwitchRetracted", getClimberRetractionLimitSwitchValue());
		PCDashboardDiagnostics.SubsystemBoolean("Climber", "LimitFinalExtension", isAtExtensionLimit());
		PCDashboardDiagnostics.SubsystemBoolean("Climber", "LimitFinalRetraction", isAtRetractionLimit());

		PCDashboardDiagnostics.SubsystemBoolean("Climber", "OverrideExtend", Robot.OVERRIDE_SYSTEM_CLIMBER_EXTEND.getOverride1());
		PCDashboardDiagnostics.SubsystemBoolean("Climber", "OverrideRetract", Robot.OVERRIDE_SYSTEM_CLIMBER_RETRACT.getOverride1());
		// Measure power sent to climber
		PCDashboardDiagnostics.SubsystemNumber("Climber", "EncoderExpectedPower", _expectedPower);

		PCDashboardDiagnostics.SubsystemBoolean("Climber", "LimitSwitchAndEncoderAgreeRetracted", encoderAndLimitsMatchRetracted());
		PCDashboardDiagnostics.SubsystemBoolean("Climber", "LimitSwitchAndEncoderAgreeExtended", encoderAndLimitsMatchExtended());
	}

	public boolean encoderAndLimitsMatchRetracted() {
		boolean match = true;

		if (getEncoderPosition() < Calibrations.climberEncoderMinimumValue
				&& getClimberRetractionLimitSwitchValue() == false) {
			match = false;
		}

		if (getClimberRetractionLimitSwitchValue() == true
				&& getEncoderPosition() > Calibrations.climberEncoderMinimumValue
						+ Calibrations.climberLiftDownwardSafetyMargin) {
			match = false;
		}

		return match;
	}

	public boolean encoderAndLimitsMatchExtended() {
		if (getEncoderPosition() > Calibrations.climberEncoderMaximumValue
				&& getClimberExtensionLimitSwitchValue() == false) {
			return false;
		}

		if (getClimberExtensionLimitSwitchValue() == true
				&& getEncoderPosition() < Calibrations.climberEncoderMaximumValue
						- Calibrations.climberLiftUpwardSafetyMargin) {
			return false;
		}

		return true;
	}

	public void checkExpectedSpeedVersusPower() {
		// Check if climber is being sent power and not moving at the right speed
		if (Math.abs(_expectedPower) > Calibrations.climberHoldPositionPowerMagnitude) {
			// The line below only returns as true if the climber is pushing harder than it needs to not move it
			if (Math.abs(
					climberMotor.getSelectedSensorVelocity()) < Calibrations.climberConsideredMovingEncoderRate) {
				burnoutProtection();
			}
		}
	}

	public void burnoutProtection() {
		ClimberHoldPositionCommand command = new ClimberHoldPositionCommand();
		command.start();
		command.close();
	}

	public void getIsAtLimits() {
		System.out.print(" Extension Limit: " + isAtExtensionLimit() + " Retraction Limit: " + isAtRetractionLimit());
	}

	public void resetEncodersToRetractedLimit() {
		climberMotor.setSelectedSensorPosition(Calibrations.climberEncoderMinimumValue, 0, 0);
	}

	public void resetEncodersToExtendedLimit() {
		climberMotor.setSelectedSensorPosition(Calibrations.climberEncoderMaximumValue, 0, 0);
	}

	public void setMotorsPID(int position) {
		climberMotor.set(ControlMode.Position, position);
	}

	public void stop() {
		climberMotor.set(ControlMode.PercentOutput, 0);
	}

	// Right now this method just looks at the right limit switch; some combination of both should be used.

	public void expectClimberToBeAtRetractionLimit() {
		if (getClimberRetractionLimitSwitchValue()) {
			resetEncodersToExtendedLimit();
		}
	}

	public void expectClimberToBeAtExtensionLimit() {
		if (getClimberExtensionLimitSwitchValue()) {
			resetEncodersToExtendedLimit();
		}
	}

	public boolean isEncoderAtExtensionLimit() {
    	boolean encoderLimit = false;
    	
    	if (this.getEncoderPosition() >= Calibrations.climberEncoderMaximumValue - Calibrations.climberLiftUpwardSafetyMargin) {
    		encoderLimit = true;
    	}
    	
    	return encoderLimit;
    }
    
    public boolean isEncoderAtRetractionLimit() {
    	boolean encoderLimit = false;
    	
    	if (this.getEncoderPosition() <= Calibrations.climberEncoderMinimumValue + Calibrations.climberLiftDownwardSafetyMargin) {
    		encoderLimit = true;
    	}
    	
    	return encoderLimit;
    }

	// Right now this method just looks at the right limit switch; some combination of both should be used.
	public boolean isAtExtensionLimit() {
    	boolean encoderLimit = false;
    	boolean switchLimit = false;
    	
    	encoderLimit = this.isEncoderAtExtensionLimit();
    
    	if (this.getClimberExtensionLimitSwitchValue() == true) {
    		switchLimit = true;
    		this.resetEncodersToExtendedLimit();
    	}
    	
    	return Robot.OVERRIDE_SYSTEM_CLIMBER_EXTEND.getIsAtLimit(encoderLimit, switchLimit);
    }

	public boolean isAtRetractionLimit() {
    	boolean encoderLimit = false;
    	boolean switchLimit = false;
    	
    	encoderLimit = this.isEncoderAtRetractionLimit();
    	
    	if (this.getClimberRetractionLimitSwitchValue() == true) {
    		switchLimit = true;
    		this.resetEncodersToRetractedLimit();
    	}
    	
    	return Robot.OVERRIDE_SYSTEM_CLIMBER_RETRACT.getIsAtLimit(encoderLimit, switchLimit);
    }

	public void holdPosition() {
		climberMotor.set(ControlMode.PercentOutput, Calibrations.climberHoldPositionPowerMagnitude);
	}

	public double getClimberHeightPercentage() {
		double encoderMax = (double) Calibrations.climberEncoderMaximumValue;
		double encoderMin = (double) Calibrations.climberEncoderMinimumValue;
		double encoderCurrent = getEncoderPosition();

		double heightPercentage = (encoderCurrent - encoderMin) / (encoderMax - encoderMin);
		heightPercentage = Math.min(1, heightPercentage);
		heightPercentage = Math.max(0, heightPercentage);

		return heightPercentage;
	}

	public static double inchesToTicks(double inches) {
		double encoderTicks = inches;
		encoderTicks -= Calibrations.climberInchesToEncoderTicksOffsetValue;
		encoderTicks *= Calibrations.climberInchesToEncoderTicksConversionValue;

		return encoderTicks;
	}

	public static double ticksToInches(double encoderTicks) {
		double inches = encoderTicks;
		inches /= Calibrations.climberInchesToEncoderTicksConversionValue;
		inches += Calibrations.climberInchesToEncoderTicksOffsetValue;

		return inches;
	}

	public boolean getClimberExtensionLimitSwitchValue() {
		return this.climberMotor.getSensorCollection().isFwdLimitSwitchClosed();
	}

	public boolean getClimberRetractionLimitSwitchValue() {
		return this.climberMotor.getSensorCollection().isRevLimitSwitchClosed();
	}

	public boolean getIsExtendedPastEncoderPosition(int encoderPosition) {
		if (getEncoderPosition() > encoderPosition + Calibrations.CLIMBER_AT_POSITION_BUFFER) {
			return true;
		}

		return false;
	}

	public boolean getIsRetractedBeforeEncoderPosition(int encoderPosition) {
		if (getEncoderPosition() < encoderPosition - Calibrations.CLIMBER_AT_POSITION_BUFFER) {
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
