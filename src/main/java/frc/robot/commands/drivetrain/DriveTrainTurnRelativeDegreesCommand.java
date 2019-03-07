package frc.robot.commands.drivetrain;

import frc.ravenhardware.RavenTank;
import frc.robot.Calibrations;
import frc.robot.Robot;
import frc.robot.subsystems.DriveTrainSubsystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class DriveTrainTurnRelativeDegreesCommand extends Command {
    RavenTank ravenTank;
    Timer safetyTimer;

    // Negative degrees means turn left; positive means turn right.
    double degreesToTurn;
    double driveTrainOriginalHeading;
    double temporaryGyroScaleFactor;
    double previousGyroScaleFactor;
    private double _timeoutSeconds;

    public DriveTrainTurnRelativeDegreesCommand(DriveTrainSubsystem driveTrain, double degreesToTurn, double gyroScaleFactor, double timeoutSeconds) {
        requires(driveTrain);
        this.ravenTank = Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank;
        this.degreesToTurn = degreesToTurn;
        this.safetyTimer = new Timer();
        this.previousGyroScaleFactor = Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.getGyroAdjustmentScaleFactor();
        this.temporaryGyroScaleFactor = gyroScaleFactor;
        _timeoutSeconds = timeoutSeconds;
    }

    public DriveTrainTurnRelativeDegreesCommand(DriveTrainSubsystem driveTrain, double degreesToTurn, double gyroScaleFactor) {
        this(driveTrain, degreesToTurn, gyroScaleFactor, Calibrations.DriveTrainTurnRelativeDegreesSafetyTimerSeconds);
    }

    public DriveTrainTurnRelativeDegreesCommand(DriveTrainSubsystem driveTrain, double degreesToTurn) {
        this(driveTrain, degreesToTurn, Calibrations.driveTrainTurnRelativeDegreesGyroAdjustmentScaleFactor);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        driveTrainOriginalHeading = ravenTank.getCurrentHeading();
        Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.setGyroAdjustmentScaleFactor(temporaryGyroScaleFactor);
        ravenTank.turnRelativeDegrees(degreesToTurn);
        safetyTimer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        ravenTank.fpsTank(0, 0);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        double currentHeading = ravenTank.getCurrentHeading();
        double degreesTurned = currentHeading - driveTrainOriginalHeading;

        double degreesAwayFromTarget = Math.abs(degreesToTurn - degreesTurned);
        boolean turnComplete = (degreesAwayFromTarget < Calibrations.gyroAutoTurnAcceptableErrorDegrees);

        if (safetyTimer.get() > _timeoutSeconds) {
            System.out.println("TIMEOUT TIMEOUT TIMEOUT TIMEOUT");
            turnComplete = true;
        }

        return turnComplete;
    }

    // Called once after isFinished returns true
    protected void end() {
        ravenTank.setGyroTargetHeadingToCurrentHeading();
        Robot.DRIVE_TRAIN_SUBSYSTEM.ravenTank.setGyroAdjustmentScaleFactor(previousGyroScaleFactor);
        ravenTank.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
