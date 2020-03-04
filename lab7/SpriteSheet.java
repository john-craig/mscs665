package lab06;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * This class is a facade for accessing a sprite sheet.
 * @author Ron Coleman
 *
 */
public class SpriteSheet {
	// Set to true to see how the class cuts the sheet
	public final static Boolean DEBUG = false;

	// Directory where frames are stored -- only if debugging
	public final static String DIR = "c:/tmp/";
	
	// Frames of sheet stored here
	protected BufferedImage[][] frames = null;
	
	// Number of columns and rows in the sheet based on frame width & height
	protected int ncols;
	protected int nrows;
	
	/**
	 * Constructor
	 * @param path File path to the sprite sheet.
	 * @param frameWidth Width of frame in sheet
	 * @param frameHeight Height of frame in sheet
	 */
	public SpriteSheet(String path, int frameWidth, int frameHeight) {
		// Open the sheet
		Image img = new ImageIcon(path).getImage();
		
		// Get its width and height
		int imgw = img.getWidth(null);
		int imgh = img.getHeight(null);
		
		// Copy the sheet to a buffered image which facilitates cutting, frame by frame.
		BufferedImage bimg = new BufferedImage(imgw, imgh, BufferedImage.TYPE_INT_ARGB);
		bimg.getGraphics().drawImage(img, 0,0, null);
		
		// Calculate the number columns and rows in the sheet to hold the frames
		ncols = imgw / frameWidth;
		nrows = imgh / frameHeight;
		
		frames = new BufferedImage[ncols][nrows];
		
		// Cut the sheet into frames
		for(int row=0; row < nrows; row++) {
			for(int col=0; col < ncols; col++) {
				int x = col * frameWidth;
				int y = row * frameHeight;
				
				frames[col][row] = bimg.getSubimage(x, y, frameWidth, frameHeight);
				
				if(DEBUG)
					save(col, row);
			}
		}
	}
	
	/**
	 * Saves the frame in a file.
	 * @param col Frame column number
	 * @param row Frame row number
	 */
	private void save(int col, int row) {
		String name = DIR + "bimg_"+col+"_"+row+".png";
		
		BufferedImage frame = frames[col][row];
		
		try {
			File f = new File(name);

			ImageIO.write(frame, "png", f);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets a frame.
	 * @param col Frame column number
	 * @param row Frame row number
	 * @return Image
	 */
	public Image getFrame(int col, int row) {
		if(col < 0 || col >= ncols || row < 0 || row >= nrows)
			return null;
		
		return frames[col][row];
	}
	
	/**
	 * Gets the number of columns
	 * @return Number of columns
	 */
	public int getNumCols() {
		return ncols;
	}
	
	/**
	 * Gets the number of rows
	 * @return Number of rows
	 */
	public int getNumRows() {
		return nrows;
	}
}
