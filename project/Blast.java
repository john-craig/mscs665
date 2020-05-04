package project;

public class Blast extends Sprite {
	private static final int FRAME_HEIGHT = 30;
	private static final int FRAME_WIDTH = 30;

	private final String blast = "/res/blast.png";
    protected final SpriteSheet sheet =
			new SpriteSheet(blast,FRAME_WIDTH, FRAME_HEIGHT); 
    
    private int frameDecay = 10;
    private int currentFrame = 0;
    
    public Blast() {
    }

    public Blast(int x, int y) {
        updateImage();
        
        setX(x - (FRAME_HEIGHT / 2));
        setY(y - (FRAME_HEIGHT / 2));
    }
    
    public void act() {
    	frameDecay -= 1;
    	
    	if(frameDecay == 0) {
    		frameDecay = 10;
    		currentFrame += 1;
    	}
    	
    	if(currentFrame > 2) {
    		setVisible(false);
    	} else {
    		updateImage();
    	}
    }
    
    private void updateImage() {
    	setImage(sheet.getFrame(currentFrame, 0));
    }
}
