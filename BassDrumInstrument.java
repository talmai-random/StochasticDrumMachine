
import sounds.kick;

public class BassDrumInstrument implements Instrument {
    private BassDrumSample mSample;
    
    public BassDrumInstrument(char channel) {
        mSample = new BassDrumSample(channel);
    }
    
    public void play() {
        mSample.play();
    }
    
    public void setGain(float gain) {
        mSample.gain = gain;
    }
}

private class BassDrumSample extends kick {
    public float gain = 1.0;
    
    public BassDrumSample(char channel) {
        super(channel);
    }
    
    public void reset() {
        super.reset();
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
