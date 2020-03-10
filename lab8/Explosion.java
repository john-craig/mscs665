package lab8;

import java.awt.Image;

/**
 * This class defines what an explosion looks like.
 * In this case, it hides details about the sprite sheet it uses.
 * @author Ron Coleman
 *
 */
public class Explosion {
	public final static int FRAME_WIDTH = 100;
	public final static int FRAME_HEIGHT = 100;
	
	// Sprite sheet was found here https://pngimage.net/explosion-sprite-png-3/.
	// Use static storage since we only need to instantiate the sheet once
	protected static SpriteSheet sheet =
			new SpriteSheet("src/res/spritesheet1.png",FRAME_WIDTH, FRAME_HEIGHT); 
	
	/**
	 * Constructor
	 */
	public Explosion() {
		
	}
	
	/**
	 * Returns true if the frame number exists
	 * @param frameno Frame number
	 * @return
	 */
	public Boolean has(int frameno) {
		return frameno >= 0 && frameno < sheet.getNumRows();
	}
	
	/**
	 * Gets the frame, if it exists, by the frame number
	 * @param frameno Frame number
	 * @return Frame
	 */
	public Image getFrame(int frameno) {
		if(frameno < 0 || frameno >= sheet.getNumRows())
			return null;
		
		return sheet.getFrame(0, frameno);
	}
}
