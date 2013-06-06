package edu.mines.acmX.exhibit.modules.home_screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import processing.core.PImage;
import edu.mines.acmX.exhibit.input_services.events.EventManager;
import edu.mines.acmX.exhibit.input_services.events.EventType;
import edu.mines.acmX.exhibit.input_services.hardware.BadFunctionalityRequestException;
import edu.mines.acmX.exhibit.input_services.hardware.DeviceConnectionException;
import edu.mines.acmX.exhibit.input_services.hardware.HardwareManager;
import edu.mines.acmX.exhibit.input_services.hardware.HardwareManagerManifestException;
import edu.mines.acmX.exhibit.input_services.hardware.devicedata.HandTrackerInterface;
import edu.mines.acmX.exhibit.module_management.ModuleManager;
import edu.mines.acmX.exhibit.module_management.loaders.ManifestLoadException;
import edu.mines.acmX.exhibit.module_management.loaders.ModuleLoadException;
import edu.mines.acmX.exhibit.modules.home_screen.backdrops.Backdrop;
import edu.mines.acmX.exhibit.modules.home_screen.backdrops.bubbles.BubblesBackdrop;
import edu.mines.acmX.exhibit.modules.home_screen.backdrops.gameoflife.GridBackdrop;
import edu.mines.acmX.exhibit.modules.home_screen.view.ArrowClick;
import edu.mines.acmX.exhibit.modules.home_screen.view.LinearLayout;
import edu.mines.acmX.exhibit.modules.home_screen.view.ListLayout;
import edu.mines.acmX.exhibit.modules.home_screen.view.ModuleElement;
import edu.mines.acmX.exhibit.modules.home_screen.view.Side;
import edu.mines.acmX.exhibit.modules.home_screen.view.SpaceElement;
import edu.mines.acmX.exhibit.modules.home_screen.view.TimeDisplay;
import edu.mines.acmX.exhibit.modules.home_screen.view.inputmethod.VirtualRectClick;
import edu.mines.acmX.exhibit.modules.home_screen.view.weather.WeatherDisplay;

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
public class HomeScreen extends edu.mines.acmX.exhibit.module_management.modules.ProcessingModule {
	
	
	
	public static final String CURSOR_FILENAME = "hand_cursor.png";
	private PImage cursor_image;
	
	//variables that hold the backdrops
	private List<Backdrop> backdrops;
	private Backdrop backdrop;
	
	//private ModuleList moduleList;
	//private ModuleListView moduleListView;
	
	private static float handX, handY;
	// root layout for module
	private LinearLayout rootLayout;
	// list layout to hold all module elements
	private ListLayout moduleListLayout;
	// arrows, to manipulate the moduleListLayout
	private ArrowClick leftArrow;
	private ArrowClick rightArrow;
	// set to true if using 2 monitors, or false for 1
	public static final boolean DUAL_SCREEN = false;
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
	// holds sleeping status
	private boolean isSleeping;
	
	private static HardwareManager hardwareManager;
	private static EventManager eventManager;
	private HandTrackerInterface driver;
	private MyHandReceiver receiver;

		
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
		
		backdrops = new ArrayList<Backdrop>();
		backdrops.add(new BubblesBackdrop(this));
		backdrops.add(new GridBackdrop(this));
		backdrop = backdrops.get(0);
		isSleeping = false;
		
		// Ideally the hand tracker will take over displaying the 'cursor'
		noCursor();
		
		
		// get all module names
		ModuleManager manager;
		String[] packageNames = null;
		moduleElements = new ArrayList<ModuleElement>();
		moduleListLayout = new ListLayout(Orientation.HORIZONTAL, this, 88.0, 1.0);
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
			ModuleElement tempElement = new ModuleElement(this, tempImage, packageNames[i], 1.0);
			moduleElements.add(tempElement);
			moduleListLayout.add(tempElement);
		}
		//moduleList = new ModuleList(moduleElements);
		
		rootLayout = new LinearLayout(Orientation.VERTICAL, this, 1.0);
		
		//moduleListView = new ModuleListView(this, screenScale, moduleList, 60.0);
		//modules.add(moduleListView);		
		LinearLayout modules = new LinearLayout(Orientation.HORIZONTAL, this, 80.0);
		leftArrow = new ArrowClick(this, 6.0, new VirtualRectClick(ARROW_CLICK_SPEED, 0, 0, 0, 0), Side.LEFT);
		rightArrow = new ArrowClick(this, 6.0, new VirtualRectClick(ARROW_CLICK_SPEED, 0, 0, 0, 0), Side.RIGHT);
		modules.add(leftArrow);
		modules.add(moduleListLayout);
		modules.add(rightArrow);
		rootLayout.add(new SpaceElement(this, 10.0));
		rootLayout.add(modules);
		rootLayout.add(new SpaceElement(this, 50.0));
		
		LinearLayout twitter = new LinearLayout(Orientation.HORIZONTAL, this, 10.0);
		LinearLayout weatherAndTime = new LinearLayout(Orientation.HORIZONTAL, this, 10.0);
		weatherAndTime.add(new WeatherDisplay(this, 80.0));
		weatherAndTime.add(new TimeDisplay(this, 20.0));

		rootLayout.add(twitter);
		rootLayout.add(weatherAndTime);
		// set default settings for root layout
		rootLayout.setOriginX(0);
		rootLayout.setOriginY(0);
		rootLayout.setHeight(height);
		rootLayout.setWidth(width);
		lastInput = millis();
		// hardware stuff
		try {
			hardwareManager = HardwareManager.getInstance();
		} catch (HardwareManagerManifestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DeviceConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> drivers;
		try {
			drivers = (ArrayList) hardwareManager.getDevices("handtracking");
			driver = (HandTrackerInterface) hardwareManager.inflateDriver(drivers.get(0), "handtracking");
			
		} catch (BadFunctionalityRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		eventManager = EventManager.getInstance();
		receiver = new MyHandReceiver();
		eventManager.registerReceiver(EventType.HAND_CREATED, receiver);
		eventManager.registerReceiver(EventType.HAND_UPDATED, receiver);
		eventManager.registerReceiver(EventType.HAND_DESTROYED, receiver);
		
		
	}
	
	public void update() {
		driver.updateDriver();
		if (receiver.whichHand() != -1) {
			if(isSleeping) {
				isSleeping = false;
				cycleBackdrop();
			}
			handX = receiver.getX() * (2 + (width / 640));
			handY = receiver.getY() * (2 + (height / 480));
			handX -= 300;
			handY -= 300;
			lastInput = millis();
		}
		backdrop.update();
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
		

	}
	
	public void draw() {
		update();
		
		background(255, 255, 255);
		backdrop.draw();
		// draw the leftmost module
		//moduleListView.draw();
		if (millis() - lastInput < TIME_TO_SLEEP) {
			rootLayout.draw();
		}
		else {
			isSleeping = true;
		}
		
		//temporary place holder for twitter, weather and time feeds.
		//line (0, STATUSBAR_Y, width, STATUSBAR_Y);
		
		image(cursor_image, handX, handY);
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
	
	public void cycleBackdrop() {
		Random rand = new Random();
		int randBackdrop = rand.nextInt(backdrops.size());
		backdrop = backdrops.get(randBackdrop);
	}
	
	public static float getHandX() {
		return handX;
	}
	
	public static float getHandY() {
		return handY;
	}
	
	public MyHandReceiver getReceiver() {
		return receiver;
	}
}
