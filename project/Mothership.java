package project;

import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Mothership extends Sprite implements Commons {
	private final String path = "/res/mothership.png";
	private int direction;
	private int gestation;
	private ArrayList aliens;
	
	public Mothership(int x, int y, ArrayList aliens) {
		this.x = x;
        this.y = y;
        
        this.aliens = aliens;
        
        this.direction = 1;
        this.gestation = 200;
        
        ImageIcon ii = new ImageIcon(this.getClass().getResource(path));
        setImage(ii.getImage());
	}
	
    public void act() {
    	if((this.x + (MOTHERSHIP_WIDTH / 2)) == BOARD_WIDTH / 2) {
    		this.gestate();
    	} else {
    		this.x += this.direction;
        
    		if(this.x > (BOARD_WIDTH + 31) || this.x < -31) {
    			this.direction *= -1;
    		}
    	}
    }
    
    private void gestate() {
    	if(gestation == 0) {
    		aliens.add(new Alien(this.x, this.y));
    		
    		this.x += this.direction;
    		gestation = 200;
    	} else {
    		gestation--;
    	}
    }

}
