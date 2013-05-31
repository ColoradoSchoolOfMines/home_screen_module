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
import edu.mines.acmX.exhibit.modules.home_screen.view.ArrowClick;
import edu.mines.acmX.exhibit.modules.home_screen.view.LinearLayout;
import edu.mines.acmX.exhibit.modules.home_screen.view.ModuleElement;
import edu.mines.acmX.exhibit.modules.home_screen.view.ModuleListView;
import edu.mines.acmX.exhibit.modules.home_screen.view.Side;

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
	public static final int MODULE_OFFSETX = 125;
	public static final int MODULE_WIDTH = 500;
	public static final int MODULE_HEIGHT = 500;
	public static final int MODULE_MARGIN = 70;
	
	public static final String CURSOR_FILENAME = "hand_cursor.png";
	private PImage cursor_image;
	
	private double screenScale = 1.0;
	private Backdrop backDrop;
	
	private ModuleList moduleList;
	private ModuleListView moduleListView;
	
	private OpenNIHandTrackerInputDriver kinect;
	public static final boolean DEBUG_KINECT = false;
	private int handX, handY;
	private LinearLayout linearLayout;

		
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
		int x = MODULE_OFFSETX;
		for (int i = 0; i < packageNames.length; ++i) {
			PImage tempImage = loadImage("icon.png", packageNames[i]);
			if (tempImage == null) {
				tempImage = loadImage("question.jpg");
			}
			// storing icons and package names into their respective ModuleElements.
			ModuleElement tempElement = new ModuleElement(this, screenScale, tempImage, packageNames[i], 1.0, 0, 0);
			moduleElements.add(tempElement);
			x += MODULE_WIDTH + MODULE_OFFSETX;
		}

		moduleList = new ModuleList(moduleElements);
		
		linearLayout = new LinearLayout( width, height, Orientation.VERTICAL, this, 1.0, 1.0);
		LinearLayout modules = new LinearLayout( 0, 0, Orientation.HORIZONTAL, this, 1.0, 80.0);
		moduleListView = new ModuleListView(this, 0, MODULE_OFFSETY, screenScale, moduleList, 60.0, 0, 0);
		modules.add(new ArrowClick(this, 0, 0, 1.0, 20.0, 0, 0, null, Side.LEFT));
		modules.add(moduleListView);		
		modules.add(new ArrowClick(this, 0, 0, 1.0, 20.0, 0, 0, null, Side.RIGHT));
		linearLayout.add(modules);
		LinearLayout statusBarLayout = new LinearLayout( 0, 0, Orientation.HORIZONTAL, this, 1.0, 20.0);

		linearLayout.add(statusBarLayout);
	}
	
	public void update() {
		
		backDrop.update();
		//moduleListView.update(0, 0);
		linearLayout.update(0, 0);
		
		if (DEBUG_KINECT) {
			kinect.pumpInput(this);
		}

	}
	
	public void draw() {
		update();
		
		background(255, 255, 255);
		backDrop.draw();
		// draw the leftmost module
		//moduleListView.draw();
		linearLayout.draw();
		
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
