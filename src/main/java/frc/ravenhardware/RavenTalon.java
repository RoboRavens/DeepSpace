package frc.ravenhardware;

import frc.robot.Calibrations;
import frc.robot.Robot;
import frc.util.LoggerOverlordLogID;
import frc.util.PCDashboardDiagnostics;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Talon;

public class RavenTalon {

	public Talon talon;
	public TalonSRX talonSRX;
	public VictorSPX victorSPX1;
	public VictorSPX victorSPX2;
	protected double outputSpeed;
	private String _name;
	private double _maxPower;

	// The default slew rate of 2 means no acceleration cutting will occur,
	// as this enables changing between -1 and 1 in a single tick.
	protected double maxSlewRate = 2;

	protected double deadband = .05;

	public RavenTalon(int channel, String name, double slewRate, int follower1, int follower2) {
		if (Calibrations.UseTalonSRXForDriveController) {
			talonSRX = new TalonSRX(channel);
			victorSPX1 = new VictorSPX(follower1);
			victorSPX2 = new VictorSPX(follower2);
			victorSPX1.follow(talonSRX);
			victorSPX2.follow(talonSRX);
		} else {
			talon = new Talon(channel);
		}

		_name = name;
		setSlewRate(slewRate);
	}

	// For now, the slew rate is defined in "magnitude of change to
	// motor output, on a -1 to 1 scale, per 'control system tick'" (50hz.)
	// Protip: this number should be greater than zero, but likely not by much.
	// If it's zero the motor will never change output.
	public void setSlewRate(double slewRate) {
		this.maxSlewRate = slewRate;
	}

	public void setMaxPower(double newMaxPower) {
		this._maxPower = newMaxPower;
	}

	public void set(double targetOutput) {
		// prevent targetOutput from being greater than maxPower
		if (Math.abs(targetOutput) > this._maxPower) {
			targetOutput = Math.signum(targetOutput) * this._maxPower;
		}

		// apply deadband to compensate for controller joystick not returning to exactly 0
		if (Math.abs(targetOutput) < this.deadband) {
			targetOutput = 0;
		}

		Robot.LOGGER_OVERLORD.log(LoggerOverlordLogID.DriveTargetOutputPower, "target output power " + targetOutput);
		this.setWithSlewRate(targetOutput);
	}

	public void setWithSlewRate(double targetOutput) {
		double newOutputSpeed = outputSpeed;

		// Never change the speed by more than the difference between target and actual,
		// regardless of what the slew rate is.
		double slewRate = Math.min(maxSlewRate, Math.abs(targetOutput - outputSpeed));

		// Increment or decrement the new output speed,
		// but never to a magnitude larger than 1.
		if (targetOutput > outputSpeed) {
			newOutputSpeed = outputSpeed + slewRate;

			newOutputSpeed = Math.min(newOutputSpeed, 1);
		}

		if (targetOutput < outputSpeed) {
			newOutputSpeed = outputSpeed - slewRate;

			newOutputSpeed = Math.max(newOutputSpeed, -1);
		}

		// Update and set the output speed.
		outputSpeed = newOutputSpeed;

		// System.out.println("Target: " + targetOutput + " Actual: " + outputSpeed + " Slew: " + maxSlewRate);

		PCDashboardDiagnostics.SubsystemNumber("DriveTrain", _name + "OutputPercent", outputSpeed);

		if (Calibrations.UseTalonSRXForDriveController) {
			talonSRX.set(ControlMode.PercentOutput, outputSpeed);
		} else {
			talon.set(outputSpeed);
		}

	}
}
