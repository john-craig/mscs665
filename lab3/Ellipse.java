package lab3;

public class Ellipse {
    protected int w;
    protected int h;
    
    public Ellipse(int width, int height) {
    	this.w = width;
    	this.h = height;
    }

    public double area() {
        
        return w * h * Math.PI;
    }
    
    public int getWidth() {return w;}
    
    public int getHeight() {return h;}
    
    @Override
    public String toString() {return "Width =  " + w + ", Height = " + h;}
}