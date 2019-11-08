
import sounds.snr_rusnarious;

public class SnareDrumInstrument implements Instrument {
    private SnareDrumSample mSample;
    
    public SnareDrumInstrument(char channel) {
        mSample = new SnareDrumSample(channel);
    }
    
    public void play() {
        mSample.play();
    }
    
    public void setGain(float gain) {
        mSample.gain = gain;
    }
}

private class SnareDrumSample extends snr_rusnarious {
    public float gain = 1.0;
    
    public SnareDrumSample(char channel) {
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
