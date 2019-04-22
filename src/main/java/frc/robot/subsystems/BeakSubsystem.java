package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.util.NetworkTableDiagnostics;

public class BeakSubsystem extends Subsystem {
  private Solenoid _beakCapture;
  private Solenoid _beakRelease;
  private DigitalInput _hatchPanelSensor;

  public BeakSubsystem() {
    _beakCapture = new Solenoid(RobotMap.beakCaptureSolenoid);
    _beakRelease = new Solenoid(RobotMap.beakReleaseSolenoid);
    _hatchPanelSensor = new DigitalInput(RobotMap.hatchPanelSensor);
    
    NetworkTableDiagnostics.SubsystemBoolean("HatchPanel", "HasHatchPanel", () -> getHatchPanelSensor());
  }

  public void initDefaultCommand() {
  }

  public boolean getHatchPanelSensor() {
    return !_hatchPanelSensor.get();
  }
  
  public void release() {
    System.out.println("Releasing");
    _beakRelease.set(true);
    _beakCapture.set(false);
  }

  public void capture() {
    System.out.println("Capturing");
    _beakCapture.set(true);
    _beakRelease.set(false);
  }
}
