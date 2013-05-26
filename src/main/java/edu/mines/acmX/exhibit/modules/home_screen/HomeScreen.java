package edu.mines.acmX.exhibit.modules.home_screen;

import java.io.IOException;

import org.OpenNI.Context;
import org.OpenNI.GeneralException;
import org.OpenNI.OutArg;
import org.OpenNI.ScriptNode;

import processing.core.PApplet;
import processing.core.PImage;
import edu.mines.acmX.exhibit.input_services.openni.HandTracker;
import edu.mines.acmX.exhibit.modules.home_screen.backdrops.*;
import edu.mines.acmX.exhibit.modules.home_screen.backdrops.bubbles.BubblesBackdrop;
import edu.mines.acmX.exhibit.modules.home_screen.model.ModuleList;
import edu.mines.acmX.exhibit.modules.home_screen.view.ModuleListView;

/*
 * TODO (in order)
 * Cleaning out the unnecessary crap DONE
 * Detect screen ratios DONE
 * Start the Home Screen Skeleton (visual skeleton) DONE
 * Implement one functioning backdrop DONE
 * Hand tracking
 * Getting the feeds working
 * Connecting the Home Screen to the Module API
 * 
 * TODO
 * Should scaling for the screen size be abstracted away from the user?
 */
public class HomeScreen extends PApplet {
	
	public static final int EXPECTED_WIDTH = 1920;
	public static final int EXPECTED_HEIGHT = 1080;
	public static final double EXPECTED_ASPECT_RATIO = 16.0/9;
	
	public static final int STATUSBAR_Y = 930;

	public static final int MODULE_OFFSETY = 175;
	
	public static final String CURSOR_FILENAME = "hand_cursor.png";
	private PImage cursor_image;
	
	private double screenScale = 1.0;
	private Backdrop backDrop;
	
	private ModuleList moduleList;
	private ModuleListView moduleListView;
	
	private HandTracker handTracker;
	
	public void setup() {
		
		size(screenWidth, screenHeight);
		
		cursor_image = loadImage(CURSOR_FILENAME);
		
		if (Math.abs((screenWidth / (float) screenHeight) - EXPECTED_ASPECT_RATIO) > .1) {
			// Screen size is not the ideal aspect ratio.
			// TODO Fall back to different configuration?
			// -Would this have to be a config driven UI?
			destroy();
		}
		
		screenScale = screenWidth / (double) EXPECTED_WIDTH;
		System.out.println("Screen scale: " + screenScale);
		
		backDrop = new BubblesBackdrop(this, screenScale);
		
		moduleList = new ModuleList();
		moduleListView = new ModuleListView(this, 0, MODULE_OFFSETY, screenScale, moduleList);
		
		// Ideally the hand tracker will take over displaying the 'cursor'
		// noCursor();
		
		
		// TODO Verify on the box whether this HandTracker works!
		Context ctx;
		OutArg<ScriptNode> scriptNode = new OutArg<ScriptNode>();
		try {
			System.out.println(new java.io.File(".").getCanonicalPath());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ctx = Context.createFromXmlFile("openni_config.xml", scriptNode);
			handTracker = new HandTracker(ctx);
		} catch (GeneralException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		
		backDrop.update();
		moduleListView.update();

	}
	
	public void draw() {
		update();
		
		background(255, 255, 255);
		backDrop.draw();
		// draw the leftmost module
		moduleListView.draw();
		
		line (0, STATUSBAR_Y, screenWidth, STATUSBAR_Y);
	}
	
	public void mouseMoved() {
		moduleListView.mouseMoved();
	}
	
    public static void main( String[] args )
    {
        PApplet.main(new String[] {"HomeScreen"});
    }
   
}
