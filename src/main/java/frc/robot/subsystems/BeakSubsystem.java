/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.Robot;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Relay.Value;
import frc.util.NetworkTableDiagnostics;
import frc.robot.RobotMap;
import frc.ravenhardware.BufferedDigitalInput;

/**
 * Add your docs here.
 */
public class BeakSubsystem extends Subsystem {
  private Solenoid _beakCapture;
  private Solenoid _beakRelease;
  private DigitalInput _hatchPanelSensor;
  private Timer _hasHatchPanelDurationTimer = new Timer();

  public BeakSubsystem() {
    _beakCapture = new Solenoid(RobotMap.beakCaptureSolenoid);
    _beakRelease = new Solenoid(RobotMap.beakReleaseSolenoid);
		_hatchPanelSensor = new DigitalInput(RobotMap.hatchPanelSensor);
    _hasHatchPanelDurationTimer.start();
    
    NetworkTableDiagnostics.SubsystemBoolean("HatchPanel", "HasHatchPanel", () -> hasHatchPanel());
  }

  public void initDefaultCommand() {
    //setDefaultCommand(new BeakCaptureHatchPanelCommand());
  }

  public boolean hasHatchPanel() {
		return !_hatchPanelSensor.get();
  }
  
  public void periodic()  {
		if (hasHatchPanel()) {
			Robot.HAS_HATCH_PANEL_LEDS_RELAY.set(Value.kForward);
		} else {
      _hasHatchPanelDurationTimer.reset();
      Robot.HAS_HATCH_PANEL_LEDS_RELAY.set(Value.kOff);
    }
  }
  

  public void release() {
    _beakRelease.set(true);
    _beakCapture.set(false);
  }

  public void capture() {
    _beakCapture.set(true);
    _beakRelease.set(false);
  }
}
