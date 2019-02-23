package frc.robot;

public class AutonomousCalibrations {
	public static final String DoNothing = "NOTHING";
	public static final String CrossLine = "CROSS";
	public static final String Switch = "SWITCH";
	public static final String Scale = "SCALE";
	public static final String FlexScale = "FLEXSCALE";
	public static final String FlexSwitch = "FLEXSWITCH";

	public static final double AutonomousCrossAutoLineDriveForwardInches = 160;
	public static final double AutonomousCrossAutoLineDriveForwardPowerMagnitude = .5;

	public static final double AutonomousScoreLeftSwitchPosition1DriveForwardInches = 140;
	public static final double AutonomousOppositeSideSwitchFirstForwardSegmentInches = 72;
	public static final double AutonomousOppositeSideSwitchLateralMovementInches = 102;
	public static final double AutonomousOppositeSideSwitchSecondForwardSegmentInches = 72;
	public static final double AutonomousScoreSwitchDriveForwardPowerMagnitude = .3;

	public static final double AutonomousScoreLeftSwitchGrabCubePosition1FirstDriveForwardInches = 118;
	public static final double AutonomousScoreLeftSwitchGrabCubePosition1SecondDriveForwardInches = 132;
	public static final double AutonomousScoreLeftSwitchGrabCubePosition3FirstDriveForwardInches = 118;
	public static final double AutonomousScoreLeftSwitchGrabCubePosition3SecondDriveForwardInches = 132;

	public static final double AutonomousDriveStraightToScaleForwardElevatorDownSegmentInches = 150;
	public static final double AutonomousDriveStraightToScaleForwardElevatorLiftingSegmentInches = 138;
	public static final double AutonomousDriveStraightToScaleApproachScaleInches = 8;
	public static final double AutonomousDriveLeftScalePosition1Inches = 77;
	public static final double AutonomousDriveLeftScalePosition3FirstDriveForwardInches = 95;
	public static final double AutonomousDriveLeftScalePosition3SecondDriveForwardInches = 260;
	public static final double AutonomousDriveOppositeSideScaleElevatorUpSegmentInches = 83;
	public static final double AutonomousDriveScaleDriveForwardPowerMagniude = .4;

	public static final double AutonomousOppositeSideScaleForwardMovementtInches = 205;
	public static final double AutonomousOppositeSideScaleLateralMovementInches = 230;
	public static final double AutonomousOppositeSideScaleMoveIntoNullZoneInches = 50;

	public static final double AutonomousScoreRightSwitchPosition1DriveForwardInches = 140;
	public static final double AutonomousScoreRightSwitchPosition3FirstDriveForwardInches = 50;
	public static final double AutonomousScoreRightSwitchPosition3SecondDriveForwardInches = 226.5;
	public static final double AutonomousScoreRightSwitchPosition3ThirdDriveForwardInches = 118;
	public static final double AutonomousScoreRightSwitchDriveForwardPowerMagnitude = .6;

	public static final double AutonomousScoreRightSwitchGrabCubePosition3FirstDriveForwardInches = 118;
	public static final double AutonomousScoreRightSwitchGrabCubePosition3SecondDriveForwardInches = 132;
	public static final double AutonomousScoreRightSwitchGrabCubePosition1FirstDriveForwardInches = 118;
	public static final double AutonomousScoreRightSwitchGrabCubePosition1SecondDriveForwardInches = 132;

	public static final double AutonomousDriveRightScalePosition3DriveForwardInches = 288;
	public static final double AutonomousDriveRightScalePosition1FirstDriveForwardInches = 95;
	public static final double AutonomousDriveRightScalePosition1SecondDriveForwardInches = 260;
	public static final double AutonomousDriveRightScalePosition1ThirdDriveForwardInches = 193;

	public static final double AutonomousScoreSwitchCargoPushPowerMagnitude = .5;
	public static final double AutonomousGrabCubeCargoPullPowerMagnitude = 1;

	public static final double AutonomousScaleCargoSpitPowerMagnitude = 1;

	public static final double AutonomousArmExtensionPowerMagnitude = .3;
	public static final double AutonomousArmExtensionEncoderDistance = 2048;
	public static final double AutonomousScoreSwitchMiddlePositionLateralDriveForwardInches = 60;
	public static final double AutonomousScoreSwitchMiddlePositionDriveForwardFirstSegmentInches = 22;
	public static final double AutonomousScoreSwitchMiddlePositionDriveForwardSecondSegmentInches = 80;

	public static final double LengthBetweenDriverWallAndSwitch = 140;
	public static final double LengthBetweenDriverWallAndScale = 270.65;
	public static final double LengthOfSwitch = 56;
	public static final double LengthOfRobotBuffer = 40;

	public static final double WidthOfSwitch = 189.5;

	public static final double ExchangeZoneBufferMiddlePositionLeftSwitch = 6;
	public static final double ExchangeZoneBufferMiddlePositionRightSwitch = -30;
	public static final double AutonomousDriveFromScaleToSwitchCubeStraightInches = 34;
	public static final double AutonomousDriveFromScaleToSwitchCubeAngledInches = 94;
	public static final double StraightSwitchDriveForwardFromWallInches = 140;
	public static final double StraightSwitchDriveForwardToSwitchInches = 40;
	public static final double SideSwitchDriveForwardFromWallTimeoutSeconds = 5;

	// was .012
	public static final double SwitchGyroScaleFactor = .008;

}
