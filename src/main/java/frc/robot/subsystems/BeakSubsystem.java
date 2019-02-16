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
import frc.robot.RobotMap;
import frc.ravenhardware.BufferedDigitalInput;

/**
 * Add your docs here.
 */
public class BeakSubsystem extends Subsystem {
  //Solenoid beak = new Solenoid(RobotMap.beakSolenoid);
  //BufferedDigitalInput hatchPanelSensor;
  private Timer _hasHatchPanelDurationTimer = new Timer();

  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  public BeakSubsystem() {
		//this.beak = new Solenoid(RobotMap.beakSolenoid);
		//this.hatchPanelSensor = new BufferedDigitalInput(RobotMap.hatchPanelSensor);
		_hasHatchPanelDurationTimer.start();
  }

  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
    setDefaultCommand(new BeakCaptureHatchPanelCommand());
  }

  public boolean hasHatchPanel() {
		boolean otherLimit = false;
		//boolean hasHatchPanel = hatchPanelSensor.get() == false;

		return Robot.OVERRIDE_SYSTEM_CARGO.getIsAtLimit(false, otherLimit);
  }
  
  public void periodic()  {
    if (this.hasHatchPanel() == false) {
			_hasHatchPanelDurationTimer.reset();
		}

		if (this.hasHatchPanel()) {
			Robot.HAS_HATCH_PANEL_LEDS_RELAY.set(Value.kForward);
		} else {
			Robot.HAS_HATCH_PANEL_LEDS_RELAY.set(Value.kOff);
		}
  }
  

  public void release() {
    //beak.set(false);
  }

  public void capture() {
    //beak.set(true);
  }
}
