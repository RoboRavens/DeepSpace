package frc.robot.subsystems;

import frc.ravenhardware.BufferedDigitalInput;
import frc.robot.Calibrations;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.cargowheel.CargoWheelStopCommand;
import frc.util.NetworkTableDiagnostics;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;


public class CargoWheelSubsystem extends Subsystem {
	private TalonSRX _topCargoMotor;
	private TalonSRX _bottomCargoMotor;
	private BufferedDigitalInput _cargoSensor;
	private Timer _hasCargoDurationTimer = new Timer();

	public CargoWheelSubsystem() {
		_topCargoMotor = new TalonSRX(RobotMap.topCargoMotor);
		_bottomCargoMotor = new TalonSRX(RobotMap.bottomCargoMotor);
		_cargoSensor = new BufferedDigitalInput(RobotMap.cargoSensor);
		_hasCargoDurationTimer.start();

		NetworkTableDiagnostics.SubsystemBoolean("CargoWheel", "HasCargo", () -> this.hasCargo());
		NetworkTableDiagnostics.SubsystemBoolean("CargoWheel", "HasCargoSensorRaw", () -> _cargoSensor.get());
		// NetworkTableDiagnostics.SubsystemNumber("CargoWheel", "MotorOutputPercent", () -> _topCargoMotor.getMotorOutputPercent());
	}
	
	public void periodic() {
		_cargoSensor.maintainState();

		if (this.hasCargo() == false) {
			_hasCargoDurationTimer.reset();
		}
	}

	public boolean hasCargo() {
		boolean otherLimit = false;
		boolean hasCargo = _cargoSensor.get() == false;

		return Robot.OVERRIDE_SYSTEM_CARGO.getIsAtLimit(hasCargo, otherLimit);
	}

	public void initDefaultCommand() {
		setDefaultCommand(new CargoWheelStopCommand());
	}

	// TOP MOTOR

	public void topMotorSuck(double magnitude) {
		if (hasCargo() == false) {
			this.setTopMotor(-1 * magnitude);
		} else {
			this.topMotorStop();
		}
	}

	public void topMotorSpit(double magnitude) {
		this.setTopMotor(magnitude);
	}

	public void topMotorHold() {
		this.setTopMotor(-1 * Calibrations.cargoHoldPowerMagnitude);
	}


	public void topMotorIdle() {
		this.setTopMotor(0.1);
	}

	public void topMotorStop() {
		this.setTopMotor(0);
	}

	private void setTopMotor(double magnitude) {
		_topCargoMotor.set(ControlMode.PercentOutput, magnitude);
	}  

	// BOTTOM MOTOR

	public void bottomMotorSuck(double magnitude) {
		if (hasCargo() == false) {
			this.setbottomMotor(-1 * magnitude);
		} else {
			this.bottomMotorStop();
		}
	}

	public void bottomMotorSpit(double magnitude) {
		this.setbottomMotor(magnitude);
	}

	public void bottomMotorHold() {
		this.setbottomMotor(-1 * Calibrations.cargoHoldPowerMagnitude);
	}


	public void bottomMotorIdle() {
		this.setbottomMotor(0.1);
	}

	public void bottomMotorStop() {
		this.setbottomMotor(0);
	}

	private void setbottomMotor(double magnitude) {
		_bottomCargoMotor.set(ControlMode.PercentOutput, magnitude);
	}  
}
