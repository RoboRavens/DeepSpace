/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.controls.ButtonCode;
import frc.ravenhardware.BufferedDigitalInput;
import frc.robot.Calibrations;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.arm.ArmHoldPositionCommand;
import frc.util.NetworkTableDiagnostics;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.TalonSRXConstants;

public class ArmSubsystem extends Subsystem {
	TalonSRX armMotor;
	//BufferedDigitalInput extensionLimitSwitch;
	//BufferedDigitalInput retractionLimitSwitch;
	private Timer _safetyTimer = new Timer();

	public ArmSubsystem() {
		armMotor = new TalonSRX(RobotMap.armMotor);
		armMotor.setInverted(true);
		//this.retractionLimitSwitch = new BufferedDigitalInput(RobotMap.armRetractionLimitSwitch);
		//this.extensionLimitSwitch = new BufferedDigitalInput(RobotMap.armExtensionLimitSwitch);
		armMotor.config_kF(TalonSRXConstants.kPIDLoopIdx, Calibrations.armkF, TalonSRXConstants.kTimeoutMs);
		armMotor.config_kP(TalonSRXConstants.kPIDLoopIdx, Calibrations.armkP, TalonSRXConstants.kTimeoutMs);
		armMotor.config_kI(TalonSRXConstants.kPIDLoopIdx, Calibrations.armkI, TalonSRXConstants.kTimeoutMs);
		armMotor.config_kD(TalonSRXConstants.kPIDLoopIdx, Calibrations.armkD, TalonSRXConstants.kTimeoutMs);

		NetworkTableDiagnostics.SubsystemNumber("Arm", "Encoder", () -> this.getEncoderPosition());
		NetworkTableDiagnostics.SubsystemBoolean("Arm", "LimitEncoderExtension", () -> this.isEncoderAtExtensionLimit());
		NetworkTableDiagnostics.SubsystemBoolean("Arm", "LimitEncoderRetraction", () -> this.isEncoderAtRetractionLimit());
		NetworkTableDiagnostics.SubsystemBoolean("Arm", "LimitSwitchExtension", () -> this.getExtensionLimitSwitchValue());
		NetworkTableDiagnostics.SubsystemBoolean("Arm", "LimitSwitchRetraction", () -> this.getArmRetractionLimitSwitchValue());
		NetworkTableDiagnostics.SubsystemBoolean("Arm", "LimitFinalExtension", () -> this.getIsAtExtensionLimit());
		NetworkTableDiagnostics.SubsystemBoolean("Arm", "LimitFinalRetraction", () -> this.getIsAtRetractionLimit());
		NetworkTableDiagnostics.SubsystemBoolean("Arm", "LimitSwitchAndEncoderAgreeExtended", () -> this.encoderAndLimitsMatchExtended());
		NetworkTableDiagnostics.SubsystemBoolean("Arm", "LimitSwitchAndEncoderAgreeRetracted", () -> this.encoderAndLimitsMatchRetracted());
		NetworkTableDiagnostics.SubsystemBoolean("Arm", "OverrideExtend", () -> Robot.OVERRIDE_SYSTEM_ARM_EXTEND.getOverride1());
		NetworkTableDiagnostics.SubsystemBoolean("Arm", "OverrideRetract",() ->  Robot.OVERRIDE_SYSTEM_ARM_RETRACT.getOverride1());
	}

	public void initDefaultCommand() {
		setDefaultCommand(new ArmHoldPositionCommand());
	}

	public void extend(double magnitude) {
    	if (getIsAtExtensionLimit() && Robot.OPERATION_PANEL.getButtonValue(ButtonCode.ARMDOUBLEOVERRIDEEXTEND) == false) {
    		stop();
    	}
    	else {
        	set(-1 * magnitude);
    	}
    }
    
    public void retract(double magnitude) {
    	if (getIsAtRetractionLimit() && Robot.OPERATION_PANEL.getButtonValue(ButtonCode.ARMDOUBLEOVERRIDERETRACT) == false) {
    		stop();
    	}
    	else {
    		set(magnitude);
    	}
    }
    
    private void set(double magnitude) {
    	magnitude = Math.min(magnitude, 1);
    	magnitude = Math.max(magnitude, -1);
    	magnitude *= 1;
    	
    	armMotor.set(ControlMode.PercentOutput, magnitude);
    }

	public void periodic() {
		//retractionLimitSwitch.maintainState();
		//extensionLimitSwitch.maintainState();
		getIsAtExtensionLimit();
		getIsAtRetractionLimit();
	}

	public boolean encoderAndLimitsMatchExtended() {
		if (getExtensionLimitSwitchValue() == false
				&& getEncoderPosition() >= Calibrations.armEncoderExtendedValue) {
			return false;
		}

		if (getExtensionLimitSwitchValue() == true
				&& getEncoderPosition() <= Calibrations.armEncoderExtendedValue - Calibrations.ARM_ENCODER_BUFFER) {
			return false;
		}

		return true;
	}

	public boolean encoderAndLimitsMatchRetracted() {
		if (getArmRetractionLimitSwitchValue() == false
				&& getEncoderPosition() <= Calibrations.armEncoderRetractedValue) {
			return false;
		}

		if (getArmRetractionLimitSwitchValue() == true
				&& getEncoderPosition() >= Calibrations.armEncoderRetractedValue + Calibrations.ARM_ENCODER_BUFFER) {
			return false;
		}

		return true;
	}

	public boolean getExtensionLimitSwitchValue() {
		//return !extensionLimitSwitch.get();

		return false;
	}

	public boolean getArmRetractionLimitSwitchValue() {
		//return !retractionLimitSwitch.get();

		return false;
	}

	/*
	 * public boolean isAtBottomLimit() { return this.getEncoderPosition() <=
	 * Calibrations.armEncoderValueAtBottom + Calibrations.ARM_ENCODER_BUFFER; }
	 * 
	 * public boolean isAtTopLimit() { return this.getEncoderPosition() >=
	 * Calibrations.armEncoderValueAtTop - Calibrations.ARM_ENCODER_BUFFER; }
	 */

	public boolean getIsAtRetractionLimit() {
		boolean encoderLimit = false;
		boolean switchLimit = false;

		encoderLimit = isEncoderAtRetractionLimit();

		/*if (this.getArmRetractionLimitSwitchValue() == true) {
			switchLimit = true;
			this.resetEncodersToRetractionLimit();
		}*/
		System.out.println("RETRACTION LIMIT RETRACTION LIMIT");
		return Robot.OVERRIDE_SYSTEM_ARM_RETRACT.getIsAtLimit(encoderLimit, switchLimit);
	}

	public void expectArmToBeAtExtensionLimit() {
		boolean isAtLimitSwitch = this.getExtensionLimitSwitchValue();
		boolean isEncoderWithinRange = isEncoderAtExtensionLimit();

		if (isEncoderWithinRange == false && isAtLimitSwitch == true) {
			this.resetEncodersToExtendedLimit();
		}
	}

	public void expectArmToBeAtRetractionLimit() {
		boolean isAtLimitSwitch = this.getArmRetractionLimitSwitchValue();
		boolean isEncoderWithinRange = isEncoderAtRetractionLimit();

		if (isEncoderWithinRange == false && isAtLimitSwitch == true) {
			this.resetEncodersToRetractionLimit();
		}
	}

	public boolean isEncoderAtExtensionLimit() {
		boolean encoderLimit = false;
		if (this.getEncoderPosition() >= Calibrations.armEncoderExtendedValue - Calibrations.ARM_ENCODER_BUFFER) {
			encoderLimit = true;
		}
		return encoderLimit;
	}

	public boolean isEncoderAtRetractionLimit() {
		boolean encoderLimit = false;
		if (this.getEncoderPosition() <= Calibrations.armEncoderRetractedValue + Calibrations.ARM_ENCODER_BUFFER) {
			encoderLimit = true;
		}
		return encoderLimit;
	}

	public boolean getIsAtExtensionLimit() {
		boolean encoderLimit = false;
		boolean switchLimit = false;

		encoderLimit = this.isEncoderAtExtensionLimit();

		/*if (this.getExtensionLimitSwitchValue() == true) {
			switchLimit = true;
			this.resetEncodersToExtendedLimit();
		}*/
		System.out.println("EXTENSION LIMIT EXTENSION LIMIT");
		return Robot.OVERRIDE_SYSTEM_ARM_EXTEND.getIsAtLimit(encoderLimit, switchLimit);
	}

	public int getEncoderPosition() {
		return (armMotor.getSelectedSensorPosition(0));
	}

	public void resetEncodersToRetractionLimit() {
		this.armMotor.setSelectedSensorPosition(Calibrations.armEncoderRetractedValue, 0, 0);
	}

	public void resetEncodersToExtendedLimit() {
		System.out.println("CRINGLEMINGEREUFBUEBFWUIH");
		this.armMotor.setSelectedSensorPosition(Calibrations.armEncoderExtendedValue, 0, 0);
	}

	public void setMotorsPID(int position) {
		this.armMotor.set(ControlMode.Position, position);
	}

	public void stop() {
		this.armMotor.set(ControlMode.PercentOutput, 0);
	}

	public boolean getIsExtendedPastTarget(int targetEncoderValue) {
		boolean isPastTarget = false;

		if (this.getEncoderPosition() > targetEncoderValue + Calibrations.ARM_ENCODER_BUFFER) {
			isPastTarget = true;
		}

		return isPastTarget;
	}

	public boolean getIsRetractedBeforeTarget(int targetEncoderValue) {
		boolean isPastTarget = false;

		if (this.getEncoderPosition() < targetEncoderValue - Calibrations.ARM_ENCODER_BUFFER) {
			isPastTarget = true;
		}

		return isPastTarget;
	}

	public boolean getIsAtTarget(int targetEncoderValue) {
		boolean isAtTarget = false;

		boolean notOverExtended = this.getIsExtendedPastTarget(targetEncoderValue);
		boolean notOverRetracted = this.getIsRetractedBeforeTarget(targetEncoderValue);

		if (notOverExtended == false && notOverRetracted == false) {
			isAtTarget = true;
		}

		return isAtTarget;
	}

	public boolean getIsExtendedPastHighScale() {
		boolean isPastHighScale = false;

		if (this.getEncoderPosition() > Calibrations.armMidHatchEncoderValue + Calibrations.ARM_ENCODER_BUFFER) {
			isPastHighScale = true;
		}

		return isPastHighScale;
	}

	public boolean getIsRetractedBeforeHighScale() {
		boolean isPastHighScale = false;

		if (this.getEncoderPosition() < Calibrations.armMidHatchEncoderValue - Calibrations.ARM_ENCODER_BUFFER) {
			isPastHighScale = true;
		}

		return isPastHighScale;
	}

	public boolean getIsAtHighScale() {
		boolean isAtHighScale = false;

		boolean notOverExtended = this.getIsExtendedPastHighScale();
		boolean notOverRetracted = this.getIsRetractedBeforeHighScale();

		if (notOverExtended == false && notOverRetracted == false) {
			isAtHighScale = true;
		}

		return isAtHighScale;
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

	public void holdPosition() {
		this.armMotor.set(ControlMode.PercentOutput, Calibrations.armHoldPositionPowerMagnitude);
	}
}
