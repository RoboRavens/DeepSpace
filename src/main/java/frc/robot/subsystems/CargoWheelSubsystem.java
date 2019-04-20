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
	private BufferedDigitalInput _cargoSensorLeft;
	private BufferedDigitalInput _cargoSensorRight;
	private Timer _timer = new Timer();

	public CargoWheelSubsystem() {
		_topCargoMotor = new TalonSRX(RobotMap.topCargoMotor);
		_bottomCargoMotor = new TalonSRX(RobotMap.bottomCargoMotor);
		_cargoSensorLeft = new BufferedDigitalInput(RobotMap.cargoSensorLeft);
		_cargoSensorRight = new BufferedDigitalInput(RobotMap.cargoSensorRight);

		NetworkTableDiagnostics.SubsystemBoolean("CargoWheel", "HasCargoStrict", () -> this.hasCargoStrict());
		NetworkTableDiagnostics.SubsystemBoolean("CargoWheel", "HasCargoLeft", () -> this.getLeftCargoSensor());
		NetworkTableDiagnostics.SubsystemBoolean("CargoWheel", "HasCargoRight", () -> this.getRightCargoSensor());
	}
	
	public void periodic() {
		_cargoSensorLeft.maintainState();
		_cargoSensorRight.maintainState();
	}

	 // True only if BOTH cargo sensors are true.
	 public boolean hasCargoStrict() {
		boolean hasCargo = false;
		  
		  if (getLeftCargoSensor() && getRightCargoSensor()) {
			  hasCargo = true;
		  }
		  
		  return hasCargo;
	}
  
	// True if EITHER cargo sensor is true.
	public boolean hasCargoLenient() {
		boolean hasCargo = false;
		
		if (getLeftCargoSensor() || getRightCargoSensor()) {
			hasCargo = true;
		}
		
		return hasCargo;
	}

	public boolean getLeftCargoSensor() {
		return !_cargoSensorLeft.get();
	}

	public boolean getRightCargoSensor() {
		return !_cargoSensorRight.get();
	}

	public void initDefaultCommand() {
		setDefaultCommand(new CargoWheelStopCommand());
	}

	// TOP MOTOR

	public void topMotorSuck(double magnitude) {
		if (hasCargoStrict() == false) {
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
		if (hasCargoStrict() == false) {
			this.setbottomMotor(magnitude);
		} else {
			this.bottomMotorStop();
		}
	}

	public void bottomMotorSpit(double magnitude) {
		this.setbottomMotor(-1 * magnitude);
	}

	public void bottomMotorHold() {
		this.setbottomMotor(Calibrations.cargoHoldPowerMagnitude);
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

	public void resetTimer() {
		_timer.reset();
	}

	public void startTimer() {
		_timer.start();
	}

	public double getTimer() {
		return _timer.get();
	}
}
