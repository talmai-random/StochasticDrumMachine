
import sounds.clap;

public class ClapInstrument implements Instrument {
    private ClapSample mSample;
    
    public ClapInstrument(char channel) {
        mSample = new ClapSample(channel);
    }
    
    public void play() {
        mSample.play();
    }
    
    public void setGain(float gain) {
        mSample.gain = gain;
    }
}

private class ClapSample extends clap {
    public float gain = 1.0;
    
    public ClapSample(char channel) {
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
