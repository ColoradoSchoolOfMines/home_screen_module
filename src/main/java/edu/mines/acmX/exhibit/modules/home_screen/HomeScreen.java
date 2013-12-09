package edu.mines.acmX.exhibit.modules.home_screen;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.mines.acmX.exhibit.input_services.hardware.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import processing.core.PImage;
import edu.mines.acmX.exhibit.input_services.events.EventManager;
import edu.mines.acmX.exhibit.input_services.events.EventType;
import edu.mines.acmX.exhibit.input_services.hardware.devicedata.HandTrackerInterface;
import edu.mines.acmX.exhibit.input_services.hardware.drivers.InvalidConfigurationFileException;
import edu.mines.acmX.exhibit.module_management.metas.ModuleMetaData;
import edu.mines.acmX.exhibit.module_management.modules.ProcessingModule;
import edu.mines.acmX.exhibit.modules.home_screen.backdrops.Backdrop;
import edu.mines.acmX.exhibit.modules.home_screen.backdrops.bubbles.BubblesBackdrop;
import edu.mines.acmX.exhibit.modules.home_screen.backdrops.gameoflife.GridBackdrop;
import edu.mines.acmX.exhibit.modules.home_screen.backdrops.photo.PhotoBackdrop;
import edu.mines.acmX.exhibit.modules.home_screen.view.ArrowClick;
import edu.mines.acmX.exhibit.modules.home_screen.view.LinearLayout;
import edu.mines.acmX.exhibit.modules.home_screen.view.ListLayout;
import edu.mines.acmX.exhibit.modules.home_screen.view.ModuleElement;
import edu.mines.acmX.exhibit.modules.home_screen.view.Side;
import edu.mines.acmX.exhibit.modules.home_screen.view.SpaceElement;
import edu.mines.acmX.exhibit.modules.home_screen.view.TimeDisplay;
import edu.mines.acmX.exhibit.modules.home_screen.view.inputmethod.VirtualRectClick;
import edu.mines.acmX.exhibit.modules.home_screen.view.weather.WeatherDisplay;
import edu.mines.acmX.exhibit.stdlib.input_processing.tracking.HandTrackingUtilities;

/*
 * TODO documentation
 */
@SuppressWarnings("serial")
public class HomeScreen extends ProcessingModule {

	private static Logger log = LogManager.getLogger(HomeScreen.class);
	//picture used for the user's hand
	public static final String CURSOR_FILENAME = "hand_cursor.png";
	public static final String INFO_IMAGE_PATH = "info_mark.png";
	private PImage cursor_image;

	//variables that hold the backdrops
	private List<Backdrop> backdrops;
	//currently displayed backdrop (only one to update)
	private Backdrop backdrop;

	//hand positions
	private static float handX, handY;
	// root layout for module
	private LinearLayout rootLayout;
	// list layout to hold all module elements
	private ListLayout moduleListLayout;
	// arrows, to manipulate the moduleListLayout
	private ArrowClick leftArrow;
	private ArrowClick rightArrow;
	// listLayout scroll speed, in pixels
	public static final int SCROLL_SPEED = 20;
	// how fast an arrow registers a click
	public static final int ARROW_CLICK_SPEED = 50;
	// time to fall asleep, in millis
	public static final int TIME_TO_SLEEP = 10000;
	// all module elements
	private ArrayList<ModuleElement> moduleElements;
	// millis when last interacted with
	private int lastInput;
	// holds sleeping status (used for cycling backdrops)
	private boolean isSleeping = false;
	//allows the loading backdrop to be randomized
	private boolean RANDOM_BACKDROP = true;

	private static final float BOTTOM_BAR_TEXT_RATIO = 0.01875f;

	//interfaces with input services components
	private static EventManager eventManager;
	private HandTrackerInterface driver;
	private MyHandReceiver receiver;

	/**
	 * setup, called once
	 */
	public void setup() {
		size(width, height);

		// load cursor image
		cursor_image = loadImage(CURSOR_FILENAME);
		cursor_image.resize(32, 32);

		//start storing the list of backdrops
		backdrops = new ArrayList<Backdrop>();

		//draws random bubbles that float up
		backdrops.add(new BubblesBackdrop(this));
		//Conway's Game of Life
		backdrops.add(new GridBackdrop(this));
		//a demonstration of how to use a PhotoBackdrop
		Backdrop demoPictures = new PhotoBackdrop(this, 
				"CSMBlaster.png", "CSMLogoSimple.jpg", "EECS_logo.png", 
				"CSMLogo.png", "SlideShowImages/", "The CSM Campus");
		backdrops.add(demoPictures);
		//the credits page
		Backdrop credits = new PhotoBackdrop(this, 
				"", "", "EECS_logo.png", 
				"CSMLogo.png", "Credits/", "Contributors");
		backdrops.add(credits);

		//pick backdrop to launch with
		if (RANDOM_BACKDROP) {
			cycleBackdrop();
		} else {
			backdrop = backdrops.get(0);
		}

		//disable mouse cursor display
		noCursor();

		moduleElements = new ArrayList<ModuleElement>();
		//builds the layout for displaying modules
		moduleListLayout = new ListLayout(Orientation.HORIZONTAL, this, 88.0, 1.0, 5,color(124,145,156, 64));
		String[] packageNames = new String[0];
		try {
			packageNames = getAllAvailableModules();
		} catch ( RemoteException e ) {
			e.printStackTrace();
		}

		// This is the image that will be displayed in the upper right of a
		// module to indicate a hotspot for extra information
		PImage infoImage = loadImage( INFO_IMAGE_PATH );

		// Iterates through the array of package names and loads each module's icon.
		for (int i = 0; i < packageNames.length; ++i) {
			//removes the home screen module from the list of displayed modules
			try {
				if (packageNames[i].equals( getCurrentModulePackageName() ) ) {
					continue;
				}
			} catch ( RemoteException e ) {
				e.printStackTrace();
			}
			//tries to load specified icon from module
			ModuleMetaData mToLoad = null;
			try {
				mToLoad = getModuleMetaData(packageNames[i]);
			} catch ( RemoteException e ) {
				e.printStackTrace();
			}
			PImage tempImage = loadImage( mToLoad.getIconPath(), mToLoad);
			//load default if this fails
			if (tempImage == null) {
				log.warn("Could not find icon for the module " + mToLoad.getPackageName());
				tempImage = loadImage("question.png");
			}
			// storing icons and package names into their respective ModuleElements.
			ModuleElement tempElement = null;
			try {
				tempElement = new ModuleElement(this, tempImage, infoImage,
						packageNames[i], getModuleMetaData(packageNames[i]), 1.0);
			} catch ( RemoteException e ) {
				e.printStackTrace();
			}
			moduleElements.add(tempElement);
			moduleListLayout.add(tempElement);
		}

		//main layout for the whole screen
		rootLayout = new LinearLayout(Orientation.VERTICAL, this, 1.0);

		//holds the module set layout
		LinearLayout modules = new LinearLayout(Orientation.HORIZONTAL, this, 80.0);
		leftArrow = new ArrowClick(this, 6.0, new VirtualRectClick(ARROW_CLICK_SPEED, 0, 0, 0, 0), Side.LEFT);
		rightArrow = new ArrowClick(this, 6.0, new VirtualRectClick(ARROW_CLICK_SPEED, 0, 0, 0, 0), Side.RIGHT);
		modules.add(leftArrow);
		modules.add(moduleListLayout);
		modules.add(rightArrow);
		//top margin spacing
		rootLayout.add(new SpaceElement(this, 10.0));
		//add modules list
		rootLayout.add(modules);
		//add spacing below modules
		rootLayout.add(new SpaceElement(this, 50.0));

		//add Twitter display
		LinearLayout twitter = new LinearLayout(Orientation.HORIZONTAL, this, 10.0);
		//twitter.add(new TwitterDisplay(this, 100.0)); //TODO get this fully working
		//add Weather/Time display
		LinearLayout weatherAndTime = new LinearLayout(Orientation.HORIZONTAL, this, 10.0);
		int textSize = (int) (width * BOTTOM_BAR_TEXT_RATIO);

		// TODO add back in when Weather decides to stop throwing NPE
		//weatherAndTime.add(new WeatherDisplay(this, 75.0, textSize));
		//weatherAndTime.add(new TimeDisplay(this, 25.0, textSize));
		
		weatherAndTime.add(new TimeDisplay(this, 100.0, textSize));

		rootLayout.add(twitter);
		rootLayout.add(weatherAndTime);
		// set default settings for root layout
		rootLayout.setOriginX(0);
		rootLayout.setOriginY(0);
		rootLayout.setHeight(height);
		rootLayout.setWidth(width);
		lastInput = millis();
		try {
			driver = (HandTrackerInterface) getInitialDriver("handtracking");

		} catch (BadFunctionalityRequestException e) {
			log.error("Asked for nonexistent functionality");
			e.printStackTrace();
		} catch (InvalidConfigurationFileException e) {
			log.error("Invalid configuration file loaded");
			e.printStackTrace();
		} catch (UnknownDriverRequest e) {
			log.error("Trying to access unknown driver");
			e.printStackTrace();
		} catch ( RemoteException e ) {
			e.printStackTrace();
		} catch ( BadDeviceFunctionalityRequestException e ) {
			e.printStackTrace();
		}

		//builds the event manager, and sets the receiver to look for hand events
		eventManager = EventManager.getInstance();
		receiver = new MyHandReceiver();
		eventManager.registerReceiver(EventType.HAND_CREATED, receiver);
		eventManager.registerReceiver(EventType.HAND_UPDATED, receiver);
		eventManager.registerReceiver(EventType.HAND_DESTROYED, receiver);
	}

	/**
	 * update loop, called continuously before draw
	 */
	public void update() {
		size(width, height);
		//refresh driver for hand tracking
		driver.updateDriver();
		//checks if there is a registered hand detected
		if (receiver.whichHand() != -1) {
			if(isSleeping) {
				//stop sleeping if it was before
				isSleeping = false;
				//switch the backdrop to a random one in the list
				if (RANDOM_BACKDROP) cycleBackdrop();
			}

			float marginFraction = (float) 1/6; //user preference for sensitivity/zoom level (smaller is closer)
			//get hand position - uses scaling to let user reach all of screen
			handX = HandTrackingUtilities.getScaledHandX(receiver.getX(), 
					driver.getHandTrackingWidth(), width, marginFraction);
			handY = HandTrackingUtilities.getScaledHandY(receiver.getY(), 
					driver.getHandTrackingHeight(), height, marginFraction);
			lastInput = millis();
		} else {
			//if no hand is being tracked, reset the hand to (0, 0)
			handX = 0;
			handY = 0;
		}
		//call update for the loaded backdrop
		backdrop.update();
		rootLayout.update(0, 0);
		int millis = millis();
		// check arrows for clicks
		if (leftArrow.completed(millis)) {
			moduleListLayout.incrementOffset(-SCROLL_SPEED);
		}
		if (rightArrow.completed(millis)) {
			moduleListLayout.incrementOffset(SCROLL_SPEED);
		}

		// check all ModuleElements for clicks
		checkModulesForClicks();


	}

	/**
	 * draws the screen
	 */
	public void draw() {
		//call update first
		update();

		//reset to default draw settings, in case other classes didn't
		noFill();
		noStroke();
		imageMode(CORNER);
		textAlign(LEFT, TOP);

		//white background
		background(255, 255, 255);
		backdrop.draw();
		// draw the other modules if not sufficiently inactive
		if (millis() - lastInput < TIME_TO_SLEEP) {
			rootLayout.draw();
		} else {
			//set the screen to sleep
			isSleeping = true;
			backdrop.alternateDrawFaded();
		}

		//draw the hand where the user's hand is (if it has one)
		if (receiver.whichHand() != -1) {
			imageMode(CENTER);
			image(cursor_image, handX, handY);
			imageMode(CORNER);
		} else {
			//if no hand detected, print "Wave to begin"
			textAlign(RIGHT, BOTTOM);
			//on 1600x900, want 48 pt font
			int waveTextSize = (int) (48.0/900 * height);
			textSize(waveTextSize);
			fill(84, 84, 84);
			rectMode(CORNERS);
			//number of pixels to leave between the side and the text
			int rightMargin = 10;
			rect(width - rightMargin - textWidth("Wave to begin"), 
					(float) (height * 0.8 - (textAscent() + textDescent())), 
					width - rightMargin, (float) (height * 0.8));
			rectMode(CORNER);
			fill(200, 200, 200);
			text("Wave to Begin", width - rightMargin, (float) (height * 0.8));
			textAlign(LEFT, TOP);
		}
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

	/**
	 * randomly picks an available backdrop to display
	 */
	public void cycleBackdrop() {
		Random rand = new Random(millis());
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
