package edu.mines.acmX.exhibit.modules.home_screen;

import SimpleOpenNI.SimpleOpenNI;
import processing.core.PApplet;

public class ProcessingExample extends PApplet {
	SimpleOpenNI context;
	
	public void setup() {
		context = new SimpleOpenNI(this);
		context.enableDepth();
		context.enableRGB();
		size(context.depthWidth() * 2 + 10, context.depthHeight());
		background(200, 0, 0);
		
		
	}
	
	public boolean sketchFullScreen() {
		return true;
	}
	
	public void draw() {
		context.update();
		image(context.depthImage(), 0, 0);
		image(context.rgbImage(), context.depthWidth() + 10, 0);
	}
	
    public static void main( String[] args )
    {
        PApplet.main(new String[] { "--present", "ProcessingExample"});
    }
	
}
