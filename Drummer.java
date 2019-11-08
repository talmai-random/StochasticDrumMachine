
import femto.sound.Procedural;

public class Drummer {
    private static final int NUM_TRACKS = 4;
    private static final int NUM_ROWS = 4;
    
    private static final Instrument mInstruments[] = {new BassDrumInstrument(0), new ClapInstrument(1), new SnareDrumInstrument(2), new TomDrumInstrument(3)};
    
    private int mNotes[];
    private float mMasterGain = 0.3;
    
    public Drummer() {
        mNotes = new int[NUM_TRACKS * NUM_ROWS];
    }
    
    public void fill(Sheet sheet, int beatNum, int deviations) {
        clear();
        
        for(int trackNum=0; trackNum<NUM_TRACKS; ++trackNum) {
            if(trackNum >= sheet.numInstruments()) {
                break;
            }
            var cell = sheet.cell(trackNum, beatNum);
            
            var propability = (cell.active) ? 1024 - deviations / 2 : deviations / 2;
            if(Math.random(0, 1024) < propability) {
                int improv = Math.random(0, 30 * 1024);
                if(improv < 10 * deviations) { // delayed
                    setNote(1, trackNum, cell.gain);
                }
                else if(improv < 13 * deviations) { // triplet
                    setNote(0, trackNum, cell.gain);
                    setNote(1, trackNum, cell.gain);
                    setNote(2, trackNum, cell.gain);
                }
                else { // normal
                    setNote(0, trackNum, cell.gain);
                }
            }
        }
    }
    
    public int numRows() {
        return mNotes.length / NUM_TRACKS;
    }
    
    public void clear() {
        for(int idx=0; idx<mNotes.length; ++idx) {
            mNotes[idx] = 0;
        }
    }
    
    public void setNote(int rowNum, int trackNum, ubyte gain) {
        mNotes[NUM_TRACKS * rowNum + trackNum] = (gain << 8) | trackNum;
    }
    
    public void play(int rowNum) {
        for(int idx=NUM_TRACKS*rowNum; idx<NUM_TRACKS*(rowNum+1); ++idx) {
            var note = mNotes[idx];
            if(note != 0) {
                var inst = mInstruments[note & 0xff];
                int gain = (note >> 8) & 0xff;
                inst.setGain((mMasterGain * gain) / 255);
                inst.play();
            }
        }
    }
}
