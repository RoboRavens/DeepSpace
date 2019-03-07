package frc.ravenhardware;

import edu.wpi.first.wpilibj.Encoder;

public class RavenEncoder {
    private Encoder _encoder;

    private int _cyclesPerRevolution;
    private double _wheelDiameterInches;
    private double _wheelCircumferenceInches;
    private int _encoderPositionWhenLimitIsHit;

    private boolean _inverted = false;

    public RavenEncoder(Encoder encoder, int cyclesPerRevolution, double wheelDiameterInches, boolean inverted) {
        this._encoder = encoder;
        this._cyclesPerRevolution = cyclesPerRevolution;
        this._wheelDiameterInches = wheelDiameterInches;
        this._inverted = inverted;

        this._wheelCircumferenceInches = Math.PI * wheelDiameterInches;
    }

    public double getNetRevolutions() {
        double netRevolutions = (double) _encoder.get() / _cyclesPerRevolution;

        if (_inverted) {
            netRevolutions *= -1;
        }

        return netRevolutions;
    }

    public double getNetInchesTraveled() {
        double netRevolutions = getNetRevolutions();
        double netInchesTraveled = netRevolutions * _wheelCircumferenceInches;

        // I added the *2 here
        return netInchesTraveled * 2;
    }

    public int getCycles() {
        int cycles = this._encoder.get();

        if (_inverted) {
            cycles *= -1;
        }

        return cycles;
    }

    public void resetEncoder() {
        this._encoder.reset();
    }
}