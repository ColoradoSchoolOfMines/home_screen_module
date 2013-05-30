package edu.mines.acmX.exhibit.modules.home_screen;

import java.io.IOException;
import java.util.ArrayList;

import org.OpenNI.Context;
import org.OpenNI.OutArg;
import org.OpenNI.ScriptNode;

import processing.core.PImage;
import edu.mines.acmX.exhibit.input_services.InputEvent;
import edu.mines.acmX.exhibit.input_services.InputReceiver;
import edu.mines.acmX.exhibit.input_services.openni.OpenNIHandTrackerInputDriver;
import edu.mines.acmX.exhibit.module_manager.ManifestLoadException;
import edu.mines.acmX.exhibit.module_manager.ModuleLoadException;
import edu.mines.acmX.exhibit.module_manager.ModuleManager;
import edu.mines.acmX.exhibit.module_manager.ProcessingModule;
import edu.mines.acmX.exhibit.modules.home_screen.backdrops.Backdrop;
import edu.mines.acmX.exhibit.modules.home_screen.backdrops.bubbles.BubblesBackdrop;
import edu.mines.acmX.exhibit.modules.home_screen.model.ModuleList;
import edu.mines.acmX.exhibit.modules.home_screen.view.ModuleElement;
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
 * Should scaling for the screen size be abstracted away from the user? Seems infeasible
 */
public class HomeScreen extends ProcessingModule 
	implements InputReceiver {
	
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
	
	private OpenNIHandTrackerInputDriver kinect;
	public static final boolean DEBUG_KINECT = false;
	private int handX, handY;

		
	public void setup() {
		
		size(width, height);
		
		cursor_image = loadImage(CURSOR_FILENAME);
		cursor_image.resize(32, 32);
		
		// TODO Fall back to different configuration?
		// -Would this have to be a config driven UI?
		//destroy();
		
		screenScale = width / (double) EXPECTED_WIDTH;
		
		backDrop = new BubblesBackdrop(this, screenScale);
		
		
		// Ideally the hand tracker will take over displaying the 'cursor'
		noCursor();
		
		if (DEBUG_KINECT) {
			// TODO Verify on the box whether this HandTracker works!
			Context ctx;
			OutArg<ScriptNode> scriptNode = new OutArg<ScriptNode>();
			try {
				System.out.println(new java.io.File(".").getCanonicalPath());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			kinect = new OpenNIHandTrackerInputDriver();
			kinect.installInto(null);
		}
		
		// get all module names
		ModuleManager manager;
		String[] packageNames = null;
		ArrayList<ModuleElement> moduleElements = new ArrayList<ModuleElement>();
		try {
			manager = ModuleManager.getInstance();
			packageNames = manager.getAllAvailableModules();
		} catch (ManifestLoadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ModuleLoadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Iterates through the array of package names and loads each modules icon.
		for (int i = 0; i < packageNames.length; ++i) {
			PImage tempImage = loadImage("icon.png", packageNames[i]);
			if (tempImage == null) {
				tempImage = loadImage("question.jpg");
			}
			// storing icons and package names into their respective ModuleElements.
			ModuleElement tempElement = new ModuleElement(this, 0, 0, screenScale, tempImage, packageNames[i]);
			moduleElements.add(tempElement);
		}

		moduleList = new ModuleList(moduleElements);
		moduleListView = new ModuleListView(this, 0, MODULE_OFFSETY, screenScale, moduleList);
		
	}
	
	public void update() {
		
		backDrop.update();
		moduleListView.update();
		
		if (DEBUG_KINECT) {
			kinect.pumpInput(this);
		}

	}
	
	public void draw() {
		update();
		
		background(255, 255, 255);
		backDrop.draw();
		// draw the leftmost module
		moduleListView.draw();
		
		//temporary place holder for twitter, weather and time feeds.
		line (0, STATUSBAR_Y, width, STATUSBAR_Y);
		
		image(cursor_image, handX, handY);
	}
	
	public void mouseMoved() {
		moduleListView.mouseMoved();
		
		if (!DEBUG_KINECT) {
			handX = mouseX;
			handY = mouseY;
		}
		
	}
	
	public void receiveInput(InputEvent e) {
		handX = (int) e.x;
		handY = (int) e.y;
	}
}
