/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.controls.ButtonCode;
import frc.robot.Robot;
import frc.robot.commands.cargo.CargoScoreCargoShipCommand;
import frc.robot.commands.cargo.CargoScoreHighRocketCommand;
import frc.robot.commands.cargo.CargoScoreLowRocketCommand;
import frc.robot.commands.cargo.CargoScoreMidRocketCommand;
import frc.robot.commands.hatchpanel.HatchPanelScoreHighRocketCommand;
import frc.robot.commands.hatchpanel.HatchPanelScoreLowCommand;
import frc.robot.commands.hatchpanel.HatchPanelScoreMidRocketCommand;

public class SetCommandSubsystem extends Subsystem {
  private String _cargoOrHatchPanel;

  public void SetCargoOrHatchPanel(String cargoOrHatchPanel) {
    _cargoOrHatchPanel = cargoOrHatchPanel;
    if (Robot.BEAK_SUBSYSTEM.hasHatchPanelStrict()) {
      _cargoOrHatchPanel = "Hatch Panel";
    }
    if (Robot.CARGO_WHEEL_SUBSYSTEM.hasCargo()) {
      _cargoOrHatchPanel = "Cargo";
    }
  }

  public void callAutomatedCommand() {
    if (Robot.OPERATION_PANEL.getButton(ButtonCode.ROCKETHIGH).get()) {
      if (_cargoOrHatchPanel == "Cargo") {
        new CargoScoreHighRocketCommand();
      }
      if (_cargoOrHatchPanel == "Hatch Panel") {
        new HatchPanelScoreHighRocketCommand();
      }
    }
    if (Robot.OPERATION_PANEL.getButton(ButtonCode.ROCKETMID).get()) {
      if (_cargoOrHatchPanel == "Cargo") {
        new CargoScoreMidRocketCommand();
      }
      if (_cargoOrHatchPanel == "Hatch Panel") {
        new HatchPanelScoreMidRocketCommand();
      }
    }
    if (Robot.OPERATION_PANEL.getButton(ButtonCode.ROCKETLOW).get()) {
      if (_cargoOrHatchPanel == "Cargo") {
        new CargoScoreLowRocketCommand();
      }
      if (_cargoOrHatchPanel == "Hatch Panel") {
        new HatchPanelScoreLowCommand();
      }          
    }
    if (Robot.OPERATION_PANEL.getButton(ButtonCode.CARGOSHIP).get()) {
      if (_cargoOrHatchPanel == "Cargo") {
        new CargoScoreCargoShipCommand();
      }
      if (_cargoOrHatchPanel == "Hatch Panel") {
        new HatchPanelScoreLowCommand();
      }
    }
  }
  
  public void initDefaultCommand() {}
}
