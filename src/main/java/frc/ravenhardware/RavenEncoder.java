package frc.ravenhardware;

import edu.wpi.first.wpilibj.Encoder;

public class RavenEncoder {
    Encoder encoder;

    int cyclesPerRevolution;
    double wheelDiameterInches;
    double wheelCircumferenceInches;
    int relativeValue;
    int encoderPositionWhenLimitIsHit;

    boolean inverted = false;

    public RavenEncoder(Encoder encoder, int cyclesPerRevolution, double wheelDiameterInches, boolean inverted) {
        this.encoder = encoder;
        this.cyclesPerRevolution = cyclesPerRevolution;
        this.wheelDiameterInches = wheelDiameterInches;
        this.inverted = inverted;

        this.wheelCircumferenceInches = Math.PI * wheelDiameterInches;
    }

    public double getNetRevolutions() {
        double netRevolutions = (double) encoder.get() / cyclesPerRevolution;

        if (inverted) {
            netRevolutions *= -1;
        }

        return netRevolutions;
    }

    public double getNetInchesTraveled() {
        double netRevolutions = getNetRevolutions();
        double netInchesTraveled = netRevolutions * wheelCircumferenceInches;

        // I added the *2 here
        return netInchesTraveled * 2;
    }

    public int getCycles() {
        int cycles = this.encoder.get();

        if (inverted) {
            cycles *= -1;
        }

        return cycles;
    }

    public void resetEncoder() {
        this.encoder.reset();
    }
}