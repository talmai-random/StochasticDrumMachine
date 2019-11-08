
import sounds.tom_cleansweep;

public class TomDrumInstrument implements Instrument {
    private TomDrumSample mSample;
    
    public TomDrumInstrument(char channel) {
        mSample = new TomDrumSample(channel);
    }
    
    public void play() {
        mSample.play();
    }
    
    public void setGain(float gain) {
        mSample.gain = gain;
    }
}

private class TomDrumSample extends tom_cleansweep {
    public float gain = 1.0;
    
    public TomDrumSample(char channel) {
        super(channel);
    }
    
    ubyte update() {
        int val = super.update();
        val = (int)((val - 128) * gain) + 128;
        if(val < 0) {
            return 0;
        }
        else if(val > 255) {
            return 255;
        }
        return (ubyte)val;
    }
}
