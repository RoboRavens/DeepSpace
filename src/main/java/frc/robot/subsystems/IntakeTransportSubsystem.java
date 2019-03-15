/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

public class IntakeTransportSubsystem extends Subsystem {
  private Solenoid _intakeExtendTransport;
  private Solenoid _intakeRetractTransport;
  
  public IntakeTransportSubsystem() {
    _intakeExtendTransport = new Solenoid(RobotMap.intakeExtendTransportSolenoid);
    _intakeRetractTransport = new Solenoid(RobotMap.intakeRetractTransportSolenoid);
  }

  public void initDefaultCommand() {}

  public void intakeExtend() {
    _intakeExtendTransport.set(true);
    _intakeRetractTransport.set(false);
  }

  public void intakeRetract() {
    _intakeRetractTransport.set(true);
    _intakeExtendTransport.set(false);
  }
}
