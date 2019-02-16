/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.ravenhardware.BufferedDigitalInput;
import frc.robot.Calibrations;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.elevator.ElevatorHoldPositionCommand;
import frc.util.PCDashboardDiagnostics;
import frc.robot.TalonSRXConstants;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;

public class ElevatorSubsystem extends Subsystem {
	public TalonSRX elevatorMotor;
	BufferedDigitalInput extendedLimitSwitch;
	BufferedDigitalInput retractedLimitSwitch;
	private Timer _safetyTimer = new Timer();
	private double _expectedPower;

	public ElevatorSubsystem() {
		this.elevatorMotor = new TalonSRX(RobotMap.elevatorMotor);
		//this.retractedLimitSwitch = new BufferedDigitalInput(RobotMap.elevatorRetractionLimitSwitch);
		//this.extendedLimitSwitch = new BufferedDigitalInput(RobotMap.elevatorExtensionLimitSwitch);
		this.elevatorMotor.config_kF(TalonSRXConstants.kPIDLoopIdx, Calibrations.elevatorkF, TalonSRXConstants.kTimeoutMs);
		this.elevatorMotor.config_kP(TalonSRXConstants.kPIDLoopIdx, Calibrations.elevatorkP, TalonSRXConstants.kTimeoutMs);
		this.elevatorMotor.config_kI(TalonSRXConstants.kPIDLoopIdx, Calibrations.elevatorkI, TalonSRXConstants.kTimeoutMs);
		this.elevatorMotor.config_kD(TalonSRXConstants.kPIDLoopIdx, Calibrations.elevatorkD, TalonSRXConstants.kTimeoutMs);
	}

	public void initDefaultCommand() {
		setDefaultCommand(new ElevatorHoldPositionCommand());
	}

	public void extend(double magnitude) {
    	if (this.getIsAtExtensionLimit()) {
    		this.stop();
    	}
    	else {
        	this.set(magnitude);	
    	}
	}

	public void retract(double magnitude) {
    	if (this.getIsAtRetractionLimit()) {
    		this.stop();
    	}
    	else {
    		this.set(-1 * magnitude);
    	}
	}

	private void set(double magnitude) {
    	// System.out.println(rightMotor.get);
    	magnitude = Math.min(magnitude, 1);
    	magnitude = Math.max(magnitude, -1);
    	magnitude *= 1;
    	
    	if (getIsAtExtensionLimit() == true && Math.signum(magnitude) == 1) {
    		magnitude = 0;
    	}
    	if (getIsAtRetractionLimit() == true && Math.signum(magnitude) == -1) {
    		magnitude = 0;
    	}
    	
    	_expectedPower = magnitude;
    	
    	this.elevatorMotor.set(ControlMode.PercentOutput, magnitude);
    }

	public void getPosition() {
		System.out.print("Elevator Position: " + this.getEncoderPosition());
	}

	public int getEncoderPosition() {
		int EncoderPosition = this.elevatorMotor.getSelectedSensorPosition(0);

		return EncoderPosition;
	}

	public void periodic() {
		//retractedLimitSwitch.maintainState();
		//extendedLimitSwitch.maintainState();
		this.getIsAtExtensionLimit();
		this.getIsAtRetractionLimit();

		elevatorSubsystemDiagnostics();
		checkExpectedSpeedVersusPower();
	}

	public void elevatorSubsystemDiagnostics() {
		PCDashboardDiagnostics.SubsystemNumber("Elevator", "Encoder", this.getEncoderPosition());
		PCDashboardDiagnostics.SubsystemBoolean("Elevator", "LimitEncoderExtended", this.isEncoderAtExtensionLimit());
		PCDashboardDiagnostics.SubsystemBoolean("Elevator", "LimitEncoderRetracted", this.isEncoderAtRetractionLimit());
		PCDashboardDiagnostics.SubsystemBoolean("Elevator", "LimitSwitchExtended", this.getelevatorExtensionLimitSwitchValue());
		PCDashboardDiagnostics.SubsystemBoolean("Elevator", "LimitSwitchRetracted", this.getelevatorRetractionLimitSwitchValue());
		PCDashboardDiagnostics.SubsystemBoolean("Elevator", "LimitFinalExtension", this.getIsAtExtensionLimit());
		PCDashboardDiagnostics.SubsystemBoolean("Elevator", "LimitFinalRetraction", this.getIsAtRetractionLimit());

		PCDashboardDiagnostics.SubsystemBoolean("Elevator", "OverrideExtend", Robot.OVERRIDE_SYSTEM_ELEVATOR_EXTEND.getOverride1());
		PCDashboardDiagnostics.SubsystemBoolean("Elevator", "OverrideRetract", Robot.OVERRIDE_SYSTEM_ELEVATOR_RETRACT.getOverride1());
		// Measure power sent to elevator
		PCDashboardDiagnostics.SubsystemNumber("Elevator", "EncoderExpectedPower", _expectedPower);

		PCDashboardDiagnostics.SubsystemBoolean("Elevator", "LimitSwitchAndEncoderAgreeRetracted", this.encoderAndLimitsMatchRetracted());
		PCDashboardDiagnostics.SubsystemBoolean("Elevator", "LimitSwitchAndEncoderAgreeExtended", this.encoderAndLimitsMatchExtended());
	}

	public boolean encoderAndLimitsMatchRetracted() {
		boolean match = true;

		if (this.getEncoderPosition() < Calibrations.elevatorEncoderMinimumValue
				&& this.getelevatorRetractionLimitSwitchValue() == false) {
			match = false;
		}

		if (this.getelevatorRetractionLimitSwitchValue() == true
				&& this.getEncoderPosition() > Calibrations.elevatorEncoderMinimumValue
						+ Calibrations.elevatorLiftDownwardSafetyMargin) {
			match = false;
		}

		return match;
	}

	public boolean encoderAndLimitsMatchExtended() {
		boolean match = true;

		if (this.getEncoderPosition() > Calibrations.elevatorEncoderMaximumValue
				&& this.getelevatorExtensionLimitSwitchValue() == false) {
			match = false;
		}

		if (this.getelevatorExtensionLimitSwitchValue() == true
				&& this.getEncoderPosition() < Calibrations.elevatorEncoderMaximumValue
						- Calibrations.elevatorLiftUpwardSafetyMargin) {
			match = false;
		}

		return match;
	}

	public void checkExpectedSpeedVersusPower() {
		// Check if elevator is being sent power and not moving at the right speed
		if (Math.abs(_expectedPower) > Calibrations.elevatorHoldPositionPowerMagnitude) {
			// The line below only returns as true if the elevator is pushing harder than it
			// needs to not move it
			if (Math.abs(this.elevatorMotor.getSelectedSensorVelocity()) < Calibrations.elevatorConsideredMovingEncoderRate) {
				burnoutProtection();
			}
		}
	}

	public void burnoutProtection() {
		ElevatorHoldPositionCommand command = new ElevatorHoldPositionCommand();
		command.start();
		command.close();
	}

	public int getElevatorPosition() {
		int elevatorPosition;
		elevatorPosition = (this.getEncoderPosition());
		return elevatorPosition;
	}

	public void getIsAtLimits() {
		System.out.print(" Extension Limit: " + this.getIsAtExtensionLimit() + " Retraction Limit: "
				+ this.getIsAtRetractionLimit());
	}

	public void resetEncodersToRetractedLimit() {
		this.elevatorMotor.setSelectedSensorPosition(Calibrations.elevatorEncoderMinimumValue, 0, 0);
	}

	public void resetEncodersToExtendedLimit() {
		this.elevatorMotor.setSelectedSensorPosition(Calibrations.elevatorEncoderMaximumValue, 0, 0);
	}

	public void setMotorsPID(int position) {
		elevatorMotor.set(ControlMode.Position, position);
	}

	public void stop() {
		this.elevatorMotor.set(ControlMode.PercentOutput, 0);
		// System.out.println("STOPPING ARM.STOPPING ARM.STOPPING ARM.STOPPING
		// ARM.STOPPING ARM.STOPPING ARM.STOPPING ARM.");
	}

	// Right now this method just looks at the right limit switch; some combination
	// of both should be used.
	public boolean getIsAtRetractionLimit() {
		boolean encoderLimit = false;
		boolean switchLimit = false;

		encoderLimit = this.isEncoderAtRetractionLimit();

		if (this.getelevatorRetractionLimitSwitchValue() == true) {
			switchLimit = true;
			this.resetEncodersToRetractedLimit();
		}

		return Robot.OVERRIDE_SYSTEM_ELEVATOR_RETRACT.getIsAtLimit(encoderLimit, switchLimit);
	}

	public void expectElevatorToBeAtRetractionLimit() {
		boolean isAtLimitSwitch = this.getelevatorRetractionLimitSwitchValue();
		// boolean isEncoderWithinRange = isEncoderAtExtensionLimit();

		if (isAtLimitSwitch == true) {
			this.resetEncodersToRetractedLimit();
		}
	}

	public void expectElevatorToBeAtExtensionLimit() {
		boolean isAtLimitSwitch = this.getelevatorExtensionLimitSwitchValue();
		// boolean isEncoderWithinRange = isEncoderAtRetractionLimit();

		if (isAtLimitSwitch == true) {
			this.resetEncodersToExtendedLimit();
		}
	}

	public boolean isEncoderAtExtensionLimit() {
		boolean encoderLimit = false;

		if (this.getEncoderPosition() >= Calibrations.elevatorEncoderMaximumValue
				- Calibrations.elevatorLiftUpwardSafetyMargin) {
			encoderLimit = true;
		}

		return encoderLimit;
	}

	public boolean isEncoderAtRetractionLimit() {
		boolean encoderLimit = false;

		if (this.getEncoderPosition() <= Calibrations.elevatorEncoderMinimumValue
				+ Calibrations.elevatorLiftDownwardSafetyMargin) {
			encoderLimit = true;
		}

		return encoderLimit;
	}

	// Right now this method just looks at the right limit switch; some combination
	// of both should be used.
	public boolean getIsAtExtensionLimit() {
		boolean isAtLimit = false;
		boolean encoderLimit = false;
		boolean switchLimit = false;

		encoderLimit = this.isEncoderAtExtensionLimit();

		if (this.getelevatorExtensionLimitSwitchValue() == true) {
			switchLimit = true;
			this.resetEncodersToExtendedLimit();
		}

		isAtLimit = Robot.OVERRIDE_SYSTEM_ELEVATOR_EXTEND.getIsAtLimit(encoderLimit, switchLimit);

		return isAtLimit;
	}

	public void holdPosition() {
		this.elevatorMotor.set(ControlMode.PercentOutput, Calibrations.elevatorHoldPositionPowerMagnitude);
	}

	public double getElevatorHeightPercentage() {
		double encoderMax = (double) Calibrations.elevatorEncoderMaximumValue;
		double encoderMin = (double) Calibrations.elevatorEncoderMinimumValue;
		double encoderCurrent = this.getElevatorPosition();

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

	public boolean getelevatorExtensionLimitSwitchValue() {
		boolean extendedLimitSwitchValue = false;

		//extendedLimitSwitchValue = !extendedLimitSwitch.get();

		return extendedLimitSwitchValue;
	}

	public boolean getelevatorRetractionLimitSwitchValue() {
		boolean retractionLimitLimitSwitchValue = false;

		//retractionLimitLimitSwitchValue = !retractedLimitSwitch.get();

		return retractionLimitLimitSwitchValue;
	}

	public boolean getIsExtendedPastEncoderPosition(int encoderPosition) {
		boolean isPastMidway = false;

		if (this.getEncoderPosition() > encoderPosition + Calibrations.ELEVATOR_AT_POSITION_BUFFER) {
			isPastMidway = true;
		}

		return isPastMidway;
	}

	public boolean getIsRetractedBeforeEncoderPosition(int encoderPosition) {
		boolean isPastMidway = false;

		if (this.getEncoderPosition() < encoderPosition - Calibrations.ELEVATOR_AT_POSITION_BUFFER) {
			isPastMidway = true;
		}

		return isPastMidway;
	}

	public boolean getIsAtPosition(int encoderPosition) {
		boolean isAtMidway = false;

		boolean notOverExtended = this.getIsExtendedPastEncoderPosition(encoderPosition);
		boolean notOverRetracted = this.getIsRetractedBeforeEncoderPosition(encoderPosition);

		if (notOverExtended == false && notOverRetracted == false) {
			isAtMidway = true;
		}

		return isAtMidway;
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
