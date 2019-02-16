/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.ravenhardware.BufferedDigitalInput;
import frc.robot.Calibrations;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.arm.ArmHoldPositionCommand;
import frc.util.PCDashboardDiagnostics;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.TalonSRXConstants;

public class ArmSubsystem extends Subsystem {
	TalonSRX armMotor;
	//BufferedDigitalInput extensionLimitSwitch;
	//BufferedDigitalInput elevatorRetractionLimitSwitch;
	private Timer _safetyTimer = new Timer();

	public ArmSubsystem() {
		this.armMotor = new TalonSRX(RobotMap.armMotor);
		//this.elevatorRetractionLimitSwitch = new BufferedDigitalInput(RobotMap.armelevatorRetractionLimitSwitch);
		//this.extensionLimitSwitch = new BufferedDigitalInput(RobotMap.armExtensionLimitSwitch);
		this.armMotor.config_kF(TalonSRXConstants.kPIDLoopIdx, Calibrations.armkF, TalonSRXConstants.kTimeoutMs);
		this.armMotor.config_kP(TalonSRXConstants.kPIDLoopIdx, Calibrations.armkP, TalonSRXConstants.kTimeoutMs);
		this.armMotor.config_kI(TalonSRXConstants.kPIDLoopIdx, Calibrations.armkI, TalonSRXConstants.kTimeoutMs);
		this.armMotor.config_kD(TalonSRXConstants.kPIDLoopIdx, Calibrations.armkD, TalonSRXConstants.kTimeoutMs);
	}

	public void initDefaultCommand() {
		setDefaultCommand(new ArmHoldPositionCommand());
	}

	public void periodic() {
		//elevatorRetractionLimitSwitch.maintainState();
		//extensionLimitSwitch.maintainState();
		this.getIsAtExtensionLimit();
		this.getIsAtRetractionLimit();

		PCDashboardDiagnostics.SubsystemNumber("Arm", "Encoder", this.getEncoderPosition());
		PCDashboardDiagnostics.SubsystemBoolean("Arm", "LimitEncoderExtension", this.isEncoderAtExtensionLimit());
		PCDashboardDiagnostics.SubsystemBoolean("Arm", "LimitEncoderRetraction", this.isEncoderAtRetractionLimit());
		PCDashboardDiagnostics.SubsystemBoolean("Arm", "LimitSwitchExtension", this.getExtensionLimitSwitchValue());
		PCDashboardDiagnostics.SubsystemBoolean("Arm", "LimitSwitchRetraction", this.getelevatorRetractionLimitSwitchValue());
		PCDashboardDiagnostics.SubsystemBoolean("Arm", "LimitFinalExtension", this.getIsAtExtensionLimit());
		PCDashboardDiagnostics.SubsystemBoolean("Arm", "LimitFinalRetraction", this.getIsAtRetractionLimit());
		PCDashboardDiagnostics.SubsystemBoolean("Arm", "LimitSwitchAndEncoderAgreeExtended", this.encoderAndLimitsMatchExtended());
		PCDashboardDiagnostics.SubsystemBoolean("Arm", "LimitSwitchAndEncoderAgreeRetracted", this.encoderAndLimitsMatchRetracted());
		PCDashboardDiagnostics.SubsystemBoolean("Arm", "OverrideExtend", Robot.OVERRIDE_SYSTEM_ARM_EXTEND.getOverride1());
		PCDashboardDiagnostics.SubsystemBoolean("Arm", "OverrideRetract", Robot.OVERRIDE_SYSTEM_ARM_RETRACT.getOverride1());
	}

	public boolean encoderAndLimitsMatchExtended() {
		boolean match = true;

		if (this.getEncoderPosition() > Calibrations.armEncoderMaximumValue
				&& this.getExtensionLimitSwitchValue() == false) {
			match = false;
		}

		if (this.getExtensionLimitSwitchValue() == true
				&& this.getEncoderPosition() < Calibrations.armEncoderMaximumValue - Calibrations.ARM_ENCODER_BUFFER) {
			match = false;
		}

		return match;
	}

	public boolean encoderAndLimitsMatchRetracted() {
		boolean match = true;

		if (this.getEncoderPosition() < Calibrations.armEncoderMinimumValue
				&& this.getelevatorRetractionLimitSwitchValue() == false) {
			match = false;
		}

		if (this.getelevatorRetractionLimitSwitchValue() == true
				&& this.getEncoderPosition() > Calibrations.armEncoderMinimumValue + Calibrations.ARM_ENCODER_BUFFER) {
			match = false;
		}

		return match;
	}

	public boolean getExtensionLimitSwitchValue() {
		boolean extensionLimitSwitchValue = false;

		//extensionLimitSwitchValue = !extensionLimitSwitch.get();

		return extensionLimitSwitchValue;
	}

	public boolean getelevatorRetractionLimitSwitchValue() {
		boolean elevatorRetractionLimitSwitchValue = false;

		//elevatorRetractionLimitSwitchValue = !elevatorRetractionLimitSwitch.get();

		return elevatorRetractionLimitSwitchValue;
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

		encoderLimit = this.isEncoderAtRetractionLimit();

		if (this.getelevatorRetractionLimitSwitchValue() == true) {
			switchLimit = true;
			this.resetEncodersToRetractionLimit();
		}

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
		boolean isAtLimitSwitch = this.getelevatorRetractionLimitSwitchValue();
		boolean isEncoderWithinRange = isEncoderAtRetractionLimit();

		if (isEncoderWithinRange == false && isAtLimitSwitch == true) {
			this.resetEncodersToRetractionLimit();
		}
	}

	public boolean isEncoderAtExtensionLimit() {
		boolean encoderLimit = false;
		if (this.getEncoderPosition() >= Calibrations.armEncoderMaximumValue - Calibrations.ARM_ENCODER_BUFFER) {
			encoderLimit = true;
		}
		return encoderLimit;
	}

	public boolean isEncoderAtRetractionLimit() {
		boolean encoderLimit = false;
		if (this.getEncoderPosition() <= Calibrations.armEncoderMinimumValue + Calibrations.ARM_ENCODER_BUFFER) {
			encoderLimit = true;
		}
		return encoderLimit;
	}

	public boolean getIsAtExtensionLimit() {
		boolean isAtLimit = false;
		boolean encoderLimit = false;
		boolean switchLimit = false;

		encoderLimit = this.isEncoderAtExtensionLimit();

		if (this.getExtensionLimitSwitchValue() == true) {
			switchLimit = true;
			this.resetEncodersToExtendedLimit();
		}

		isAtLimit = Robot.OVERRIDE_SYSTEM_ARM_EXTEND.getIsAtLimit(encoderLimit, switchLimit);

		return isAtLimit;
	}

	public int getEncoderPosition() {
		return (armMotor.getSelectedSensorPosition(0));
	}

	public void resetEncodersToRetractionLimit() {
		this.armMotor.setSelectedSensorPosition(Calibrations.armEncoderMinimumValue, 0, 0);
	}

	public void resetEncodersToExtendedLimit() {
		this.armMotor.setSelectedSensorPosition(Calibrations.armEncoderMaximumValue, 0, 0);
	}

	public void setMotorsPID(int position) {
		this.armMotor.set(ControlMode.Position, position);
	}

	public void stop() {
		this.armMotor.set(ControlMode.PercentOutput, 0);
		// System.out.println("STOPPING ARM.STOPPING ARM.STOPPING ARM.STOPPING
		// ARM.STOPPING ARM.STOPPING ARM.STOPPING ARM.");
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
