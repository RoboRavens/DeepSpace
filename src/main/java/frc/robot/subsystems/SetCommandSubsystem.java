/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.commands.cargo.CargoScoreCargoShipCommand;
import frc.robot.commands.cargo.CargoScoreHighRocketCommand;
import frc.robot.commands.cargo.CargoScoreLowRocketCommand;
import frc.robot.commands.cargo.CargoScoreMidRocketCommand;
import frc.robot.commands.hatchpanel.HatchPanelScoreHighRocketCommand;
import frc.robot.commands.hatchpanel.HatchPanelScoreLowCommand;
import frc.robot.commands.hatchpanel.HatchPanelScoreMidRocketCommand;

/**
 * Add your docs here.
 */
public class SetCommandSubsystem extends Subsystem {
  private String _cargoOrHatchPanel;
  private String _location;
  private String _rocketHeight;

  public void setCargoOrHatchPanel(String cargoOrHatchPanel) {
    this._cargoOrHatchPanel = cargoOrHatchPanel;
  }

  public void setLocation(String location) {
    this._location = location;
  }

  public void setRocketHeight(String rocketHeight) {
    this._rocketHeight = rocketHeight;
  }

  public void callAutomatedCommand() {
    if (this._cargoOrHatchPanel == "Cargo") {
      if (this._location == "Cargo Ship") {
        System.out.println("running CargoScoreCargoShipCommand");
        new CargoScoreCargoShipCommand();
      }
      if (this._location == "Rocket") {
        if (this._rocketHeight == "High") {
          System.out.println("running CargoScoreHighRocketCommand");
          new CargoScoreHighRocketCommand();
        }
        if (this._rocketHeight == "Mid") {
          System.out.println("running CargoScoreMidRocketCommand");
          new CargoScoreMidRocketCommand();
        }
        if (this._rocketHeight == "Low") {
          System.out.println("running CargoScoreLowRocketCommand");
          new CargoScoreLowRocketCommand();          
        }
      }
    }
    if (this._cargoOrHatchPanel == "Hatch Panel") {
      if (this._location == "Cargo Ship") {
        System.out.println("running HatchPanelScoreCargoShipCommand");
        new HatchPanelScoreLowCommand();
      }
      if (this._location == "Rocket") {
        if (this._rocketHeight == "High") {
          System.out.println("running HatchPanelScoreHighRocketCommand");
          new HatchPanelScoreHighRocketCommand();
        }
        if (this._rocketHeight == "Mid") {
          System.out.println("running HatchPanelScoreMidRocketCommand");
          new HatchPanelScoreMidRocketCommand();
        }
        if (this._rocketHeight == "Low") {
          System.out.println("running HatchPanelScoreLowRocketCommand");
          new HatchPanelScoreLowCommand();
        }
      }
    } else {

    }
  }
  

  @Override
  public void initDefaultCommand() {}
}
