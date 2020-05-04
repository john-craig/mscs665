package project;

public class Barrier extends Sprite implements Commons{

	private static final int FRAME_HEIGHT = 12;
	private static final int FRAME_WIDTH = 42;

	private final String barrier = "/res/barrier.png";
    protected final SpriteSheet sheet =
			new SpriteSheet(barrier,FRAME_WIDTH, FRAME_HEIGHT); 
    
    private int currentFrame = 0;
    private int strength;

    public Barrier(int x, int y) {
        strength = 60;
        
        setX(x);
        setY(y);
        
        updateImage();
    }
    
    public void takeHit() {
    	strength -= 1;
    	currentFrame = 3 - (strength / 20);
    	
    	if(this.isDestroyed()) {
    		this.setDying(true);
    	}
    	
    	updateImage();
    }
    
    public Boolean isDestroyed() {return (strength < 0);}

    private void updateImage() {
    	setImage(sheet.getFrame(0, currentFrame));
    }
}