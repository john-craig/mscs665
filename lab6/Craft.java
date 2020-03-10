package lab6;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Craft {

    private String craft_still = "/res/craft-1.png";
    private String craft_moving = "/res/craft-a.png";
    private String craft_firing = "/res/craft-b.png";

    private int dx;
    private int dy;
    private int x;
    private int y;
    private Image image1;
    private Image imageA;
    private Image imageB;
    
    private Boolean isMoving;
    private Boolean isFiring;
    private ArrayList<Missile> missiles;
    
    private final int CRAFT_SIZE = 20;

    public Craft() {
        ImageIcon ii1 = new ImageIcon(this.getClass().getResource(craft_still));
        image1 = ii1.getImage();
        
        ImageIcon iiA = new ImageIcon(this.getClass().getResource(craft_moving));
        imageA = iiA.getImage();
        
        ImageIcon iiB = new ImageIcon(this.getClass().getResource(craft_firing));
        imageB = iiB.getImage();
        
        x = 40;
        y = 60;
        
        missiles = new ArrayList<Missile>();
        
        isMoving = false;
        isFiring = false;
    }


    public void move() {
        x += dx;
        y += dy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        Image image = image1;
        		
        if(isMoving) {
        	image = imageA;
        } else if (isFiring) {
        	image = imageB;
        }
        
        return image;
    }
    
    public void fire() {
        missiles.add(new Missile(x + CRAFT_SIZE, y + CRAFT_SIZE/2));
    }
    
    public ArrayList<Missile> getMissiles() {
    	return missiles;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
        	//Resolve missile firing
        	isFiring = true;
        	
        	fire();
        } else {
        	//Resolve movement
        	isMoving = true;
        	
        	if (key == KeyEvent.VK_LEFT) {
                dx = -1;
            }

            if (key == KeyEvent.VK_RIGHT) {
                dx = 1;
            }

            if (key == KeyEvent.VK_UP) {
                dy = -1;
            }

            if (key == KeyEvent.VK_DOWN) {
                dy = 1;
            }

        }
    }

    public void keyReleased(KeyEvent e) {
    	isMoving = false;
    	isFiring = false;
    	
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_UP) {
            dy = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
    }
}
