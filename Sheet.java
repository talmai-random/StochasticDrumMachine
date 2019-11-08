
public class Sheet extends Drawable {
    private static final int WIDTH = 96;
    private static final int HEIGHT = 44;
    
    private int mNumInstruments;
    private int mNumRows;
    private Cell mCells[];
    
    public Sheet(int numInstruments, int numRows) {
        mNumInstruments = numInstruments;
        mNumRows = numRows;
        mCells = new Cell[numInstruments * numRows];
        
        for(int idx=0; idx<mCells.length; ++idx) {
            mCells[idx] = new Cell();
        }
    }
    
    public final int numInstruments() {
        return mNumInstruments;
    }
    
    public final int numRows() {
        return mNumRows;
    }
    
    public final Cell cell(int instrumentNum, int rowNum) {
        return mCells[instrumentNum + mNumInstruments * rowNum];
    }
    
    public void draw() {
        var screen = Main.screen;
        screen.fillRect(super.x, super.y, WIDTH, HEIGHT, 11);
        
        int w = WIDTH / mNumRows;
        int h = HEIGHT / mNumInstruments;
        for(int rowNum=0; rowNum<mNumRows; ++rowNum) {
            for(int instNum=0; instNum<mNumInstruments; ++instNum) {
                if(mCells[rowNum * mNumInstruments + instNum].active) {
                    int x = super.x + rowNum * w;
                    int y = super.y + instNum * h;
                    screen.fillRect(x, y, w, h, 7);
                    screen.drawRect(x, y, w - 1, h - 1, 6);
                }
            }
        }
        
        screen.drawRect(super.x, super.y, WIDTH - 1, HEIGHT - 1, 0);
        
        screen.textColor = 5;
        screen.setTextPosition(11, super.y + 2);
        screen.println("KICK");
        screen.setTextPosition(11, super.y + 13);
        screen.println("CLAP");
        screen.setTextPosition(11, super.y + 24);
        screen.println("SNARE");
        screen.setTextPosition(11, super.y + 35);
        screen.println("TOM");
    }
}
