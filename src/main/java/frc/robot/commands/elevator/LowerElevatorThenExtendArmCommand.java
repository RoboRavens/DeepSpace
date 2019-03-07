package frc.robot.commands.elevator;

import frc.robot.commands.arm.ArmExtendFullyCommand;
import frc.robot.commands.elevator.ElevatorRetractFullyCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LowerElevatorThenExtendArmCommand extends CommandGroup {

    public LowerElevatorThenExtendArmCommand() {
        addSequential(new ElevatorRetractFullyCommand());
        addSequential(new ArmExtendFullyCommand());
    }
}
