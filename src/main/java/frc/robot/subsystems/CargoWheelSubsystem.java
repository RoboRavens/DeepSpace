package frc.robot.subsystems;

import frc.ravenhardware.BufferedDigitalInput;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.cargowheel.CargoWheelStopCommand;
import frc.util.PCDashboardDiagnostics;

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
	BufferedDigitalInput cargoSensor;
	private Timer _hasCargoDurationTimer = new Timer();

	public CargoWheelSubsystem() {
		this.cargoMotor = new TalonSRX(RobotMap.cargoMotor);
		this.cargoSensor = new BufferedDigitalInput(RobotMap.cargoSensor);
		_hasCargoDurationTimer.start();
	}

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new CargoWheelStopCommand());
	}

	public void suck(double magnitude) {
		// if(this.hasCubePullTimeout() == true) {
		// this.stop();
		// } else {
		this.set(-1 * magnitude);
		// }
	}

	public void spit(double magnitude) {
		this.set(magnitude);
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

	public boolean hasCube() {
		boolean otherLimit = false;
		boolean hasCube = cargoSensor.get() == false;

		return Robot.OVERRIDE_SYSTEM_CARGO.getIsAtLimit(hasCube, otherLimit);
	}

	public void periodic() {
		cargoSensor.maintainState();

		PCDashboardDiagnostics.SubsystemBoolean("CargoWheel", "HasCargo", this.hasCube());
		PCDashboardDiagnostics.SubsystemBoolean("CargoWheel", "HasCargoSensorRaw", cargoSensor.get());
		PCDashboardDiagnostics.SubsystemNumber("CargoWheel", "MotorOutputPercent", cargoMotor.getMotorOutputPercent());

		if (this.hasCube() == false) {
			_hasCargoDurationTimer.reset();
		}

		if (this.hasCube()) {
			Robot.HAS_CUBE_LEDS_RELAY.set(Value.kForward);
		} else {
			Robot.HAS_CUBE_LEDS_RELAY.set(Value.kOff);
		}
	}

	/*private boolean hasCubePullTimeout() {
		return _hasCargoDurationTimer.get() > .5;
	}*/
}
