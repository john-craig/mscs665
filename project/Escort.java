package project;

import javax.swing.ImageIcon;

public class Escort extends Sprite {

    private final String shot = "/res/xeno.png";
    private int relative_x;
    private int relative_y;
    private Mothership escortee;

    public Escort(Mothership escortee, int relative_x, int relative_y) {
    	this.relative_x = relative_x;
    	this.relative_y = relative_y;
    	this.escortee = escortee;
    	
    	this.act();
    	
        ImageIcon ii = new ImageIcon(this.getClass().getResource(shot));
        setImage(ii.getImage());

    }

    public void act() {
        this.x = escortee.getX() + relative_x;
        this.y = escortee.getY() + relative_y;
    }

}