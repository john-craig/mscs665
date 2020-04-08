package lab10;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Board extends JPanel {

    private Image bardejov;
    private int w, h;

    public Board() {
        initBoard();
    }
    
    private void initBoard() {
        loadImage();
        
        w = bardejov.getWidth(this);
        h =  bardejov.getHeight(this);
        
        setPreferredSize(new Dimension(w, h));        
    }
    
    private void loadImage() {
        
        ImageIcon ii = new ImageIcon("./res/bardejov.png");
        bardejov = ii.getImage();
    }

    @Override
    public void paintComponent(Graphics g) {
    	Graphics2D g2d = (Graphics2D)g;
    	
//    	// See https://stackoverflow.com/questions/8639567/java-rotating-images
//        // Make a backup so that we can reset our graphics object after using it.
        AffineTransform backup = g2d.getTransform();
//        
//    	
//        // Get the transform object rotated pi degrees about the center
        AffineTransform a = AffineTransform.getRotateInstance(Math.PI + (Math.PI / 2), w/2, h/2);
        AffineTransform b = AffineTransform.getScaleInstance(0.5, 0.5);
        
        a.concatenate(b);
//        
//        //Set our Graphics2D object to the transform
        g2d.setTransform(a); 	
        
        //Draw our image like normal
        g2d.drawImage(bardejov, 0, 0, null);
        
//        //Reset our graphics object so we can draw with it again.
//        g2d.setTransform(backup);
    }
}
