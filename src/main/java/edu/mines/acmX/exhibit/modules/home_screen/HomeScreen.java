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
import edu.mines.acmX.exhibit.modules.home_screen.view.ListLayout;
import edu.mines.acmX.exhibit.modules.home_screen.view.ModuleElement;
import edu.mines.acmX.exhibit.modules.home_screen.view.ModuleListView;
import edu.mines.acmX.exhibit.modules.home_screen.view.Side;
import edu.mines.acmX.exhibit.modules.home_screen.view.SpaceElement;
import edu.mines.acmX.exhibit.modules.home_screen.view.inputmethod.VirtualRectClick;

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
	
	//private ModuleList moduleList;
	//private ModuleListView moduleListView;
	
	private OpenNIHandTrackerInputDriver kinect;
	public static final boolean DEBUG_KINECT = false;
	private int handX, handY;
	// root layout for module
	private LinearLayout rootLayout;
	// list layout to hold all module elements
	private ListLayout moduleListLayout;
	// arrows, to manipulate the moduleListLayout
	private ArrowClick leftArrow;
	private ArrowClick rightArrow;
	// set to true if using 2 monitors, or false for 1
	public static final boolean DUAL_SCREEN = true;
	// listLayout scroll speed
	public static final int SCROLL_SPEED = 20;
	// how fast an arrow registers a click
	public static final int ARROW_CLICK_SPEED = 50;
	// time to fall asleep, in millis
	public static final int TIME_TO_SLEEP = 10000;
	// all module elements
	private ArrayList<ModuleElement> moduleElements;
	// millis when last interacted with
	private int lastInput;

		
	public void setup() {
		// Allows for sane usage of dual monitors
		if (DUAL_SCREEN) {
			width = width / 2;
		}
		size(width, height);
		
		// load cursor image
		cursor_image = loadImage(CURSOR_FILENAME);
		cursor_image.resize(32, 32);
		
		// TODO Fall back to different configuration?
		// -Would this have to be a config driven UI?
		
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
		moduleElements = new ArrayList<ModuleElement>();
		moduleListLayout = new ListLayout(Orientation.HORIZONTAL, this, 1.0, 88.0, 1.0);
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
				tempImage = loadImage("question.png");
			}
			// storing icons and package names into their respective ModuleElements.
			ModuleElement tempElement = new ModuleElement(this, screenScale, tempImage, packageNames[i], 1.0);
			moduleElements.add(tempElement);
			moduleListLayout.add(tempElement);
		}
		//moduleList = new ModuleList(moduleElements);
		
		rootLayout = new LinearLayout(Orientation.VERTICAL, this, 1.0, 1.0);
		
		//moduleListView = new ModuleListView(this, screenScale, moduleList, 60.0);
		//modules.add(moduleListView);		
		LinearLayout modules = new LinearLayout(Orientation.HORIZONTAL, this, 1.0, 80.0);
		leftArrow = new ArrowClick(this, 1.0, 6.0, new VirtualRectClick(ARROW_CLICK_SPEED, 0, 0, 0, 0), Side.LEFT);
		rightArrow = new ArrowClick(this, 1.0, 6.0, new VirtualRectClick(ARROW_CLICK_SPEED, 0, 0, 0, 0), Side.RIGHT);
		modules.add(leftArrow);
		modules.add(moduleListLayout);
		modules.add(rightArrow);
		rootLayout.add(new SpaceElement(this, 1.0, 10.0));
		rootLayout.add(modules);
		rootLayout.add(new SpaceElement(this, 1.0, 50.0));
		
		LinearLayout statusBarLayout = new LinearLayout(Orientation.HORIZONTAL, this, 1.0, 20.0);

		rootLayout.add(statusBarLayout);
		// set default settings for root layout
		rootLayout.setOriginX(0);
		rootLayout.setOriginY(0);
		rootLayout.setHeight(height);
		rootLayout.setWidth(width);
		lastInput = millis();
	}
	
	public void update() {
		
		backDrop.update();
		//moduleListView.update(0, 0);
		rootLayout.update(0, 0);
		int millis = millis();
		// check arrows for clicks
		if (leftArrow.completed(millis)) {
			moduleListLayout.incrementViewLength(-SCROLL_SPEED);
		}
		if (rightArrow.completed(millis)) {
			moduleListLayout.incrementViewLength(SCROLL_SPEED);
		}

		// check all ModuleElements for clicks
		checkModulesForClicks();
		
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
		if (millis() - lastInput < TIME_TO_SLEEP) {
			rootLayout.draw();
		}
		
		//temporary place holder for twitter, weather and time feeds.
		//line (0, STATUSBAR_Y, width, STATUSBAR_Y);
		
		image(cursor_image, handX, handY);
	}
	
	public void mouseMoved() {
		//moduleListView.mouseMoved();
//		rightArrow.getClick().update(mouseX, mouseY, millis());
//		leftArrow.getClick().update(mouseX, mouseY, millis());
		lastInput = millis();

		
		if (!DEBUG_KINECT) {
			handX = mouseX;
			handY = mouseY;
		}
		
	}
	
	public void receiveInput(InputEvent e) {
		handX = (int) e.x;
		handY = (int) e.y;
	}

	/**
	 * Iterates through all ModuleElements and checks them for clicks
	 */
	public void checkModulesForClicks() {
		int millis = millis();
		for (ModuleElement element: moduleElements) {
			element.checkClicks(millis);
		}
	}
}
