package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import frc.util.NetworkTableDiagnostics;
import frc.robot.Calibrations;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.gamepiecepossessed.ReadyToCollectCommand;
import frc.robot.commands.intaketransport.IntakeRetractCommand;

public class BeakSubsystem extends Subsystem {
  private Solenoid _beakCapture;
  private Solenoid _beakRelease;
  private DigitalInput _hatchPanelSensorLeft;
  private DigitalInput _hatchPanelSensorRight;
  private boolean _readyToCollect;
  private Timer _hasHatchPanelDurationTimer = new Timer();

  public BeakSubsystem() {
    _beakCapture = new Solenoid(RobotMap.beakCaptureSolenoid);
    _beakRelease = new Solenoid(RobotMap.beakReleaseSolenoid);
    _hatchPanelSensorLeft = new DigitalInput(RobotMap.hatchPanelSensorLeft);
    _hatchPanelSensorRight = new DigitalInput(RobotMap.hatchPanelSensorRight);
    
    NetworkTableDiagnostics.SubsystemBoolean("HatchPanel", "HasHatchPanel", () -> hasHatchPanelStrict());
    NetworkTableDiagnostics.SubsystemBoolean("HatchPanel", "LeftHatchSensor", () -> getLeftHatchPanelSensor());
    NetworkTableDiagnostics.SubsystemBoolean("HatchPanel", "RightHatchSensor", () -> getRightHatchPanelSensor());
    NetworkTableDiagnostics.SubsystemBoolean("HatchPanel", "ReadyToCollect", () -> _readyToCollect);
  }

  public void initDefaultCommand() {
  }

  // True only if BOTH hatch sensors are true.
  public boolean hasHatchPanelStrict() {
	  boolean hasPanel = false;
		
		if (getLeftHatchPanelSensor() && getRightHatchPanelSensor()) {
			hasPanel = true;
		}
		
		return hasPanel;
  }

  // True if EITHER hatch sensor is true.
  public boolean hasHatchPanelLenient() {
		boolean hasPanel = false;
		
		if (getLeftHatchPanelSensor() || getRightHatchPanelSensor()) {
			hasPanel = true;
		}
		
		return hasPanel;
}

  public boolean getLeftHatchPanelSensor() {
    return !_hatchPanelSensorLeft.get();
  }
    
  public boolean getRightHatchPanelSensor() {
    return !_hatchPanelSensorRight.get();
  }
  
  public void periodic()  {
    if (_readyToCollect) {
      new ReadyToCollectCommand();
      _readyToCollect = false;
    } 
  }
  
  public void release() {
    _beakRelease.set(true);
    _beakCapture.set(false);
  }

  public void capture() {
    System.out.println("Capturing");
    _beakCapture.set(true);
    _beakRelease.set(false);
  }

  public void setIsReadyToCollect(boolean readyToCollect) {
    _readyToCollect = readyToCollect;
  }
}
