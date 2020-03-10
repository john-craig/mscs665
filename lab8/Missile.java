package lab8;

import java.awt.Image;

import javax.swing.ImageIcon;

import java.awt.Image;

import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Missile {
	private enum State {NONE, VISIBLE, EXPLODING, DONE}
	
    private int x, y;
    private Image image;
    private int width, height;

    private final int BOARD_WIDTH = 390;
    private final int MISSILE_SPEED = 2;

    private State state = State.NONE;
    private Explosion explosion;
    private long startTime;
    
    public Missile(int x, int y) {

        ImageIcon ii =
            new ImageIcon(this.getClass().getResource("/res/missile.png"));
        image = ii.getImage();
        width = image.getWidth(null);
        height = image.getHeight(null);
        this.x = x;
        this.y = y;
        
        this.explosion = new Explosion();
        state = State.VISIBLE;
    }


    public Image getImage() {
        return this.image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isVisible() {
        if(this.state == State.VISIBLE || this.state == State.EXPLODING) {
        	return true;
        } else {
        	return false;
        }
    }

    public void setVisible(Boolean visible) {
        if(visible) {
        	this.state = State.VISIBLE;
        } else {
        	this.state = State.NONE;
        }
    }
    
    public void setExploding(Boolean exploding) {
    	if(exploding && state != State.EXPLODING) {
    		explode();
    	}
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void move() {
        if (this.state == State.VISIBLE) {
        	if(x < BOARD_WIDTH) {
        		x += MISSILE_SPEED;
        	} else {
        		this.state = State.DONE;
        	}
        }
        
        if (this.state == State.EXPLODING) {
        	long elapsed = System.currentTimeMillis() - startTime;
        	int frameNumber = (int)(elapsed / 50);
        	
        	if(explosion.has(frameNumber)) {
        		this.image = explosion.getFrame(frameNumber);
        	} else {
        		this.state = State.DONE;
        	}
        }
    }
    
    public void explode() {
    	startTime = System.currentTimeMillis();
    	this.state = State.EXPLODING;
    	
    	x -= explosion.FRAME_WIDTH / 2;
    	y -= explosion.FRAME_HEIGHT / 2;
    	width = explosion.FRAME_WIDTH;
    	height = explosion.FRAME_HEIGHT;
    }
}