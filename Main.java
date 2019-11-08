
import femto.mode.HiRes16Color;
import femto.sound.Mixer;
import femto.input.Button;
import femto.Game;
import femto.State;

import femto.palette.Cthulhu16;
import femto.font.TIC80;

class Main extends State {
    private static final HiRes16Color screen = new HiRes16Color(Cthulhu16.palette(), fonts.MamboFont.bin());
    
    private Property mProperties[];
    private int mActivePropIndex = 2;
    
    private Slider mLength;
    private Slider mDensity;
    private Slider mDeviations;
    private Slider mTempo;
    
    private Sheet mSheet;
    private Drummer mDrummer;
    private int mTickNum = 0;
    private long mNextTickTime;
    
    private int mPendingButton = Buttons.BUTTON_NONE;
    
    private boolean mRedrawScreen = true;
    
    // start the game using Main as the initial state
    // and TIC80 as the menu's font
    public static void main(String[] args) {
        Mixer.init(); // Initialize audio
        Game.run(TIC80.font(), new Main());
    }
    
    // Avoid allocation in a State's constructor.
    // Allocate on init instead.
    public void init() {
        mProperties = new Property[5];
        
        mLength = new Slider("LENGTH", 4, 16, 4);
        mLength.x = 66;
        mLength.y = 11;
        mLength.setValue(8);
        mProperties[0] = mLength;
        
        mDensity = new Slider("DENSITY", 0, 1024, 32);
        mDensity.x = 66;
        mDensity.y = 22;
        mDensity.setValue(512);
        mProperties[1] = mDensity;
        
        var prop = new UIButton("GENERATE", new ButtonListener(this));
        prop.x = 66;
        prop.y = 44;
        mProperties[2] = prop;
        
        mDeviations = new Slider("DEVIATIONS", 0, 1024, 32);
        mDeviations.x = 66;
        mDeviations.y = 143;
        mProperties[3] = mDeviations;
        
        mTempo = new Slider("TEMPO", 60, 240, 5);
        mTempo.x = 66;
        mTempo.y = 154;
        mTempo.setValue(160);
        mProperties[4] = mTempo;
        
        mProperties[mActivePropIndex].selected = true;
        
        
        
        generateSheet();
        
        mDrummer = new Drummer();
        mDrummer.fill(mSheet, 0, mDeviations.value());
        
        screen.clear(12);
        draw();
        
        mNextTickTime = System.currentTimeMillis() + 1000;
    }
    
    public void generateSheet() {
        if(mSheet == null || mLength.value() != mSheet.numRows()) {
            mSheet = new Sheet(4, mLength.value());
        }
        mSheet.x = 66;
        mSheet.y = 77;
        
        for(int rowNum=0; rowNum<mSheet.numRows(); ++rowNum) {
            for(int instNum=0; instNum<mSheet.numInstruments(); ++instNum) {
                var cell = mSheet.cell(instNum, rowNum);
                cell.gain = calcGain(rowNum, mSheet.numRows());
                cell.active = Math.random(0, 1024) < mDensity.value();
            }
        }
    }
    
    // update is called by femto.Game every frame
    public void update() {
        var btn = Buttons.poll();
        if(btn != Buttons.BUTTON_NONE) {
            mPendingButton = btn;
        }
        
        var now = System.currentTimeMillis();
        if(now - mNextTickTime >= 0) {
            mDrummer.play(mTickNum & 0x3);
            
            ++mTickNum;
            if(mTickNum / 4 >= mSheet.numRows()) {
                mTickNum = 0;
            }
            
            if((mTickNum & 0x3) == 0) {
                mDrummer.fill(mSheet, mTickNum / 4, mDeviations.value());
            }
            
            handleInput();
            draw();
            
            mNextTickTime += 1000 * 60 / (mSheet.numRows() * mTempo.value());
        }
    }
    
    private void handleInput() {
        var activeProp = mProperties[mActivePropIndex];
        mRedrawScreen = true;
        if(!activeProp.update(mPendingButton)) {
            if(mPendingButton == Buttons.BUTTON_UP) {
                --mActivePropIndex;
                if(mActivePropIndex < 0) {
                    mActivePropIndex = mProperties.length - 1;
                }
                activeProp.selected = false;
                mProperties[mActivePropIndex].selected = true;
            }
            else if(mPendingButton == Buttons.BUTTON_DOWN) {
                ++mActivePropIndex;
                if(mActivePropIndex >= mProperties.length) {
                    mActivePropIndex = 0;
                }
                activeProp.selected = false;
                mProperties[mActivePropIndex].selected = true;
            }
            else {
                // Nothing changed so no need to redraw
                mRedrawScreen = false;
            }
        }
        mPendingButton = Buttons.BUTTON_NONE;
    }
    
    private void draw() {
        if(mRedrawScreen) {
            mRedrawScreen = false;
            
            mSheet.draw();
            for(var prop: mProperties) {
                prop.draw();
            }
            // Update the screen with everything that was drawn
            screen.flush();
        }
    }
    
    private ubyte calcGain(int rowNum, int numRows) {
        if(rowNum == 0) {
            return 255;
        }
        else if(rowNum == numRows / 2) {
            return 176;
        }
        return 80;
    }
}

private class ButtonListener implements UIButtonListener {
    private Main mMain;
    
    public ButtonListener(Main main) {
        mMain = main;
    }
    
    public void onButtonPressed(UIButton button) {
        
    }
    
    public void onButtonReleased(UIButton button) {
        mMain.generateSheet();
    }
}
