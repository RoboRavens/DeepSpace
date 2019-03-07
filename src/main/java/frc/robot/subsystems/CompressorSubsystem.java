/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

/*
see compressor doc at

http://first.wpi.edu/FRC/roborio/beta/docs/java/edu/wpi/first/wpilibj/Compressor.html
*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.command.Subsystem;

public class CompressorSubsystem extends Subsystem {

  Compressor aCompressor = new Compressor();

  @Override
  public void initDefaultCommand() {

  }

  @Override
  public void periodic() {
    if (RobotController.getBatteryVoltage() < 11.0) {
      stop();
    }

    if (RobotController.getBatteryVoltage() > 11.5) {
      start();
    }
  }

  // enable/disable the auto function
  public void start() {
    aCompressor.start();
  }

  public void stop() {
    aCompressor.stop();
  }

  // returns electrical current measured in amps
  public double getCurrentAmps() {
    return aCompressor.getCompressorCurrent();
  }

  public Boolean isOn() {
    return aCompressor.getPressureSwitchValue();
  }

  public Boolean isOff() {
    return !isOn();
  }
}
