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
  }

  public void callAutomatedCommand() {
    if (Robot.OPERATION_PANEL.getButton(ButtonCode.ROCKETHIGH).get()) {
      if (_cargoOrHatchPanel == "Cargo") {
        System.out.println("RUNNING CARGO HIGH ROCKET");
        new CargoScoreHighRocketCommand().start();
      }
      if (_cargoOrHatchPanel == "Hatch Panel") {
        System.out.println("RUNNING HATCH HIGH ROCKET");
        new HatchPanelScoreHighRocketCommand().start();
      }
    }
    if (Robot.OPERATION_PANEL.getButton(ButtonCode.ROCKETMID).get()) {
      if (_cargoOrHatchPanel == "Cargo") {
        System.out.println("RUNNING CARGO MID ROCKET");
        new CargoScoreMidRocketCommand().start();
      }
      if (_cargoOrHatchPanel == "Hatch Panel") {
        System.out.println("RUNNING HATCH MID ROCKET");
        new HatchPanelScoreMidRocketCommand().start();
      }
    }
    if (Robot.OPERATION_PANEL.getButton(ButtonCode.ROCKETLOW).get()) {
      if (_cargoOrHatchPanel == "Cargo") {
        System.out.println("RUNNING CARGO LOW ROCKET");
        new CargoScoreLowRocketCommand().start();
      }
      if (_cargoOrHatchPanel == "Hatch Panel") {
        System.out.println("RUNNING HATCH LOW ROCKET");
        new HatchPanelScoreLowCommand().start();
      }          
    }
    if (Robot.OPERATION_PANEL.getButton(ButtonCode.CARGOSHIP).get()) {
      if (_cargoOrHatchPanel == "Cargo") {
        System.out.println("RUNNING CARGO CARGO SHIP");
        new CargoScoreCargoShipCommand().start();
      }
      if (_cargoOrHatchPanel == "Hatch Panel") {
        System.out.println("RUNNING HATCH CARGO SHIP");
        new HatchPanelScoreLowCommand().start();
      }
    }
  }
  
  public void initDefaultCommand() {}
}
