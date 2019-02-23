/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.Robot;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Relay.Value;
import frc.robot.commands.beak.BeakCaptureHatchPanelCommand;
import frc.util.PCDashboardDiagnostics;
import frc.robot.RobotMap;
import frc.ravenhardware.BufferedDigitalInput;

/**
 * Add your docs here.
 */
public class BeakSubsystem extends Subsystem {
  Solenoid beakCapture;
  Solenoid beakRelease;
  BufferedDigitalInput hatchPanelSensor;
  private Timer _hasHatchPanelDurationTimer = new Timer();

  public BeakSubsystem() {
    beakCapture = new Solenoid(RobotMap.beakCaptureSolenoid);
    beakRelease = new Solenoid(RobotMap.beakReleaseSolenoid);
		hatchPanelSensor = new BufferedDigitalInput(RobotMap.hatchPanelSensor);
		_hasHatchPanelDurationTimer.start();
  }

  public void initDefaultCommand() {
    //setDefaultCommand(new BeakCaptureHatchPanelCommand());
  }

  public boolean hasHatchPanel() {
  //boolean hasHatchPanel = !hatchPanelSensor.get();

		return Robot.OVERRIDE_SYSTEM_CARGO.getIsAtLimit(
      !hatchPanelSensor.get(), 
      false); // otherLimit
  }
  
  public void periodic()  {
		if (hasHatchPanel()) {
			Robot.HAS_HATCH_PANEL_LEDS_RELAY.set(Value.kForward);
		} else {
      _hasHatchPanelDurationTimer.reset();
      Robot.HAS_HATCH_PANEL_LEDS_RELAY.set(Value.kOff);    
    }
    
    PCDashboardDiagnostics.SubsystemBoolean("HatchPanel", "HasHatchPanel", this.hasHatchPanel());
  }
  

  public void release() {
    beakRelease.set(true);
    beakCapture.set(false);
  }

  public void capture() {
    beakCapture.set(true);
    beakRelease.set(false);
  }
}
