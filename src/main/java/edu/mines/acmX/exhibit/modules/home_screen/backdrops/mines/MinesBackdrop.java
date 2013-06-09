package edu.mines.acmX.exhibit.modules.home_screen.backdrops.mines;

import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import processing.core.PApplet;
import processing.core.PImage;
import edu.mines.acmX.exhibit.module_management.modules.ProcessingModule;
import edu.mines.acmX.exhibit.modules.home_screen.HomeScreen;
import edu.mines.acmX.exhibit.modules.home_screen.backdrops.Backdrop;
import edu.mines.acmX.exhibit.stdlib.graphics.Coordinate;

/**
 * A simple demonstration backdrop for use as a template of an image background.
 * There is an image in each corner, and the top right corner image rotates slowly. 
 * 
 * @author Ryan Stauffer
 *
 * @see {@link Backdrop}
 */
public class MinesBackdrop extends Backdrop {

	//file folder holding slideshow pictures
	public static final String SLIDE_SHOW_LOCATION = "SlideShowImages/";
	//time (in millis) to wait before loading the next picture
	public static final int TIME_TO_ADVANCE = 15000;
	//minimum time to allow for a picture to fade in (worst case, fades out as soon as fully in)
	private static final int MAX_FADE_TIME = TIME_TO_ADVANCE / 2;
	//rate of change for fading in/out, in number of ticks per fade
	public static final int NUM_FADE_TICKS = 85;
	//actual value added/subtracted for alpha changes
	public static final int FADE_CHANGE = (int) ((float) 256 / NUM_FADE_TICKS);
	//amount to turn (in radians) per update tick
	public static final float ROTATION_INCREMENT = (float) 0.05;

	private PImage logo;
	private PImage banner;
	private PImage blaster;
	private PImage spinLogo;
	private PImage currentSlideShowImage;

	private List<PImage> slideShowImages;
	private int currentImage;
	private int lastRefreshTime;
	private int pictureFadeTime;
	private int alpha;

	private float rotationTotal;
	private Coordinate spinLocation;

	public MinesBackdrop(HomeScreen par) {
		super(par);
		logo = parent.loadImage("CSMLogo.png");
		banner = parent.loadImage("EECS_logo.png");
		blaster = parent.loadImage("CSMBlaster.png");
		spinLogo = parent.loadImage("CSMLogoSimple.jpg");
		spinLocation = new Coordinate(parent.width - parent.height * 0.1, parent.height * 0.1);
		rotationTotal = 0;
		slideShowImages = new ArrayList<PImage>();
		currentImage = 0;
		loadSlideShowImages();
		currentSlideShowImage = slideShowImages.get(currentImage);
		lastRefreshTime = parent.millis();
		pictureFadeTime = 0;

		//TODO documentation about adding pics/note to submissions tester (check before approving)
	}

	/**
	 * Increments the rotation for the top right image.
	 */
	@Override
	public void update() {
		//increase rotation amount
		rotationTotal += ROTATION_INCREMENT;
		//move to the next picture if the time to advance is met
		if (parent.millis() - lastRefreshTime >= TIME_TO_ADVANCE) {
			lastRefreshTime = parent.millis(); //reset timer
			alpha = 0;
			currentImage++;
			if (currentImage >= slideShowImages.size()) currentImage = 0;
			currentSlideShowImage = slideShowImages.get(currentImage);
		}
		
		//control fade-in/out of the background images
		if (alpha < 255 && parent.millis() - lastRefreshTime < MAX_FADE_TIME) {
			alpha+= FADE_CHANGE;
			//bound max alpha
			if (alpha >= 255) {
				alpha = 255;
				//record how long it took to reach full visibility
				pictureFadeTime = parent.millis() - lastRefreshTime;
			}
		} else if (parent.millis() - lastRefreshTime > TIME_TO_ADVANCE - (1.02 * pictureFadeTime)) {
			alpha -= FADE_CHANGE;
			//bound min alpha
			if (alpha < 0) alpha = 0;
		}
	}

	/**
	 * Draws the images on the page. <br/>
	 * NOTE: All images are drawn according to percentage of total screen 
	 * size for proper scaling. 
	 */
	@Override
	public void draw() {
		//white background
		parent.background(255, 255, 255);
		parent.imageMode(PApplet.CENTER);
		parent.pushMatrix();
		parent.translate((float) spinLocation.getX(), (float) spinLocation.getY());
		parent.rotate(rotationTotal); //rotate logo
		parent.image(spinLogo, 0, 0, (float) (parent.height * 0.15), (float) (parent.height * 0.15));
		parent.popMatrix(); //revert to normal coordinate system
		parent.image(blaster, (float) (parent.height * 0.1), (float) (parent.height * 0.1), 
				(float) (parent.height * 0.15), (float) (parent.height * 0.15));
		parent.imageMode(PApplet.CORNER);
		parent.image(banner, (float) (parent.width * 0.025), (float) (parent.height * 0.8),
				(float) (parent.width * 0.7), (float) (parent.height * 0.15));
		parent.imageMode(PApplet.CORNERS);
		parent.image(logo, (float) (parent.width * 0.9), (float) (parent.height * 0.8), 
				(float) (parent.width * 0.975), (float) (parent.height * 0.95));
		parent.imageMode(PApplet.CORNER);
	}

	/**
	 * An alternate draw function, where anything drawn will only appear once
	 * the home screen has faded out from inactivity.
	 */
	//TODO write function(s) that will bound the image to a certain size, but not change
	//aspect ratio (no distortion)
	@Override
	public void alternateDrawFaded() {
		//will do nothing if no pictures are loaded
		if (!slideShowImages.isEmpty()) {
			
			//if alpha is less than 255, make the image transparent
			if (alpha < 255) parent.tint(255, 255, 255, alpha);
			else parent.noTint();
			
			parent.imageMode(PApplet.CENTER);
			//draw just above center
			parent.image(currentSlideShowImage, parent.width/2, (float) (parent.height * 0.4));
			parent.noTint();
			parent.imageMode(PApplet.CORNER);
		}
	}

	/**
	 * Private function that opens the class jar and loads all pictures
	 * associated with the backdrop slide show. 
	 */
	//TODO move into stdlib (this style of access could be very helpful for devs)
	private void loadSlideShowImages() {
		CodeSource src = MinesBackdrop.class.getProtectionDomain().getCodeSource();
		if (src != null) {
			//get absolute location of own jar
			URL jarLoc = src.getLocation();
			try {
				//open jar as ZipInputStream
				ZipInputStream jarStream = new ZipInputStream(jarLoc.openStream());
				ZipEntry ze = null;
				List<String> fileNames = new ArrayList<String>();
				//while there are still files in the specified path, check them
				while ((ze = jarStream.getNextEntry()) != null) {
					String entryName = ze.getName();
					//if in correct folder, add to potential names list
					if (entryName.startsWith(ProcessingModule.IMAGES_LOCATION + SLIDE_SHOW_LOCATION)) {
						fileNames.add(entryName);
					}
				}
				//run through all potential names
				for (int i = 0; i < fileNames.size(); ++i) {
					//if .jpg or .png, load the image and store it
					if (fileNames.get(i).contains("jpg") || 
							fileNames.get(i).contains("png")) {
						String truePath = fileNames.get(i).substring(ProcessingModule.IMAGES_LOCATION.length());
						PImage tempImage = parent.loadImage(truePath);
						slideShowImages.add(tempImage);
						//TODO split this into two functions for auto-refreshing during runtime
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
