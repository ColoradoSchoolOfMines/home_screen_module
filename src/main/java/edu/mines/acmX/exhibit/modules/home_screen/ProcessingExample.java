package edu.mines.acmX.exhibit.modules.home_screen;

import processing.core.PApplet;
import processing.core.PImage;
import edu.mines.acmX.exhibit.modules.home_screen.model.ModuleList;
import edu.mines.acmX.exhibit.modules.home_screen.view.ModuleListView;

/*
 * TODO (in order)
 * Cleaning out the unnecessary crap DONE
 * Detect screen ratios DONE
 * Start the Home Screen Skeleton (visual skeleton) DONE
 * Implement one functioning backdrop
 * Hand tracking
 * Getting the feeds working
 * Connecting the Home Screen to the Module API
 */
public class ProcessingExample extends PApplet {
	
	public static final int EXPECTED_WIDTH = 1920;
	public static final int EXPECTED_HEIGHT = 1080;
	public static final double EXPECTED_ASPECT_RATIO = 16.0/9;
	
	public static final int STATUSBAR_Y = 930;

	public static final int MODULE_OFFSETY = 175;
	
	public static final String CURSOR_FILENAME = "hand_cursor.png";
	private PImage cursor_image;
	
	private double screenScale = 1.0;
	
	private ModuleList moduleList;
	private ModuleListView moduleListView;
	
	public void setup() {
		
		size(screenWidth, screenHeight);
		
		cursor_image = loadImage(CURSOR_FILENAME);
		
		if (Math.abs((screenWidth / (float) screenHeight) - EXPECTED_ASPECT_RATIO) > .1) {
			// Screen size is not the ideal aspect ratio.
			destroy();
		}
		
		screenScale = EXPECTED_WIDTH / (float) screenWidth;
		
		moduleList = new ModuleList();
		moduleListView = new ModuleListView(this, 0, MODULE_OFFSETY, screenScale, moduleList);
		
		// Ideally the hand tracker will take over displaying the 'cursor'
		// noCursor();
	}
	
	public void update() {
		moduleListView.update();

	}
	
	public void draw() {
		update();
		
		background(51, 204, 255, 50);
		// draw the leftmost module
		moduleListView.draw();
		
		line (0, STATUSBAR_Y, screenWidth, STATUSBAR_Y);
	}
	
	public void mouseMoved() {
		moduleListView.mouseMoved();
		
	}
	
    public static void main( String[] args )
    {
        PApplet.main(new String[] {"ProcessingExample"});
    }
   
}
