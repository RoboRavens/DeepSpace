package frc.robot.subsystems;

import frc.ravenhardware.BufferedDigitalInput;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.cargowheel.CargoWheelStopCommand;
import frc.util.NetworkTableDiagnostics;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class CargoWheelSubsystem extends Subsystem {
	TalonSRX cargoMotor;
	private BufferedDigitalInput _cargoSensor;
	private Timer _hasCargoDurationTimer = new Timer();

	public CargoWheelSubsystem() {
		this.cargoMotor = new TalonSRX(RobotMap.cargoMotor);
		_cargoSensor = new BufferedDigitalInput(RobotMap.cargoSensor);
		_hasCargoDurationTimer.start();
	}

	public void initDefaultCommand() {
		setDefaultCommand(new CargoWheelStopCommand());
	}

	public void suck(double magnitude) {
		this.set(-1 * magnitude);
	}

	public void spit(double magnitude) {
		this.set(magnitude);
	}

	public void hold() {
		this.set(-0.5);
	}


	public void idle() {
		this.set(0.1);
	}

	public void stop() {
		this.set(0);
	}

	private void set(double magnitude) {
		// System.out.println("Setting cargo motors: " + magnitude);
		cargoMotor.set(ControlMode.PercentOutput, magnitude);
	}

	public boolean hasCargo() {
		boolean otherLimit = false;
		boolean hasCargo = _cargoSensor.get() == false;

		return Robot.OVERRIDE_SYSTEM_CARGO.getIsAtLimit(hasCargo, otherLimit);
	}

	public void periodic() {
		_cargoSensor.maintainState();

		NetworkTableDiagnostics.SubsystemBoolean("CargoWheel", "HasCargo", () -> this.hasCargo());
		NetworkTableDiagnostics.SubsystemBoolean("CargoWheel", "HasCargoSensorRaw", () -> _cargoSensor.get());
		//NetworkTableDiagnostics.SubsystemNumber("CargoWheel", "MotorOutputPercent", cargoMotor.getMotorOutputPercent());

		if (this.hasCargo() == false) {
			_hasCargoDurationTimer.reset();
		}

		if (this.hasCargo()) {
			Robot.HAS_CARGO_LEDS_RELAY.set(Value.kForward);
		} else {
			Robot.HAS_CARGO_LEDS_RELAY.set(Value.kOff);
		}
	}
}
