package frc.robot.subsystems;

import frc.ravenhardware.BufferedDigitalInput;
import frc.robot.Calibrations;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.cargointake.CargoWheelStopCommand;
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
	TalonSRX cargoMotorRight;
	TalonSRX cargoMotorLeft;
	BufferedDigitalInput cargoSensor;
	private Timer _hasCubeDurationTimer = new Timer();
		
	public CargoWheelSubsystem() {
		this.cargoMotorLeft = new TalonSRX(RobotMap.cargoMotorLeft);
		this.cargoMotorRight = new TalonSRX(RobotMap.cargoMotorRight);
		this.cargoSensor = new BufferedDigitalInput(RobotMap.cargoSensor);
		_hasCubeDurationTimer.start();
	}
	

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
       setDefaultCommand(new CargoWheelStopCommand());
    }
    
    public void pull() {
		this.pull(Calibrations.cargoWheelSuckPowerMagnitude); 
	}
    
    public void pull(double magnitude) { 
		//if(this.hasCubePullTimeout() == true) {
		//	this.stop();
		//} else {
			this.set(-1 * magnitude);
		//}
	}
    
    public void push() {
		this.push(Calibrations.CargoDropPowerMagnitude); 
	}
    
    public void push(double magnitude) {
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
    	double leftMotorMagnitude = -1 * magnitude;
    	double rightMotorMagnitude = magnitude;
    	
    	PCDashboardDiagnostics.SubsystemNumber("CargoWheel", "MotorLeftOutputPercent", leftMotorMagnitude);
    	PCDashboardDiagnostics.SubsystemNumber("CargoWheel", "MotorRightOutputPercent", rightMotorMagnitude);
    	cargoMotorLeft.set(ControlMode.PercentOutput, leftMotorMagnitude);
    	cargoMotorRight.set(ControlMode.PercentOutput, rightMotorMagnitude);
    }
    
    public boolean hasCube() {
    	boolean otherLimit = false;
    	boolean hasCube = cargoSensor.get() == false;
    	
    	return Robot.OVERRIDE_SYSTEM_CARGO.getIsAtLimit(hasCube, otherLimit);
    }
    
    public void periodic() {
    	cargoSensor.maintainState();
    	
    	PCDashboardDiagnostics.SubsystemBoolean("CargoWheel", "HasCube", this.hasCube());
    	PCDashboardDiagnostics.SubsystemBoolean("CargoWheel", "HasCubeSensorRaw", cargoSensor.get());
    	
    	if (this.hasCube() == false) {
    		_hasCubeDurationTimer.reset();
    	}
    	
    	if (this.hasCube()) {
    		Robot.HAS_CUBE_LEDS_RELAY.set(Value.kForward);
    	}
    	else {
    		Robot.HAS_CUBE_LEDS_RELAY.set(Value.kOff);
    	}
    }
    
    private boolean hasCubePullTimeout() {
    	return _hasCubeDurationTimer.get() > .5;
    }
}
