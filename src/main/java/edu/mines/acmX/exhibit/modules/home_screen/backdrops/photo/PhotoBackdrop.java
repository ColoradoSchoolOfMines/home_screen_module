package edu.mines.acmX.exhibit.modules.home_screen.backdrops.photo;

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

/**
 * A simple demonstration backdrop for use as a template of an image background.
 * There is an image in each corner, and a slide show once the screen becomes inactive. 
 * A String can also be provided to place a header at the top of the screen. 
 * Any parameter can be left blank and omitted by using "" in its parameter field.
 * 
 * @author Ryan Stauffer
 *
 * @see {@link Backdrop}
 */
public class PhotoBackdrop extends Backdrop {

	//time (in millis) to wait before loading the next picture
	public static final int TIME_TO_ADVANCE = 10000;
	//minimum time to allow for a picture to fade in 
	//worst case, fades out as soon as fully in
	//given 1 second buffer to facilitate more even transitions
	private static final int MAX_FADE_TIME = TIME_TO_ADVANCE / 2 - 1000;
	//rate of change for fading in/out, in number of ticks per fade
	public static final int NUM_FADE_TICKS = 60;
	//actual value added/subtracted for alpha changes
	public static final double FADE_CHANGE = (double) 256 / NUM_FADE_TICKS;
	//controls how long the module waits to fade in an image once idling
	public static final int FADE_WAIT_TIME = 100;
	//controls the percentage of screen size for the slide show images to scale to
	public static final double HORIZONTAL_SCALE = 0.6;
	public static final double VERTICAL_SCALE = 0.6;

	//store images to be drawn
	private PImage upperLeft, upperRight, lowerLeft, lowerRight;
	private PImage currentSlideShowImage;
	private String headerText;

	//stores all of the images for the slide show
	private List<PImage> slideShowImages;
	//index of current image
	private int currentImage;
	private int lastRefreshTime;
	//logs how long it took a picture to fade
	private int pictureFadeTime;
	private int timeSinceDrawn;
	private double alpha;

	public PhotoBackdrop(HomeScreen par, String upLeftImg, String upRightImg, 
			String downLeftImg, String downRightImg, String slideShowFolder, String headerText) {
		super(par);
		//load the corner images if they've been specified and perform proper scaling
		if (!upLeftImg.equals("")) {
			upperLeft = parent.loadImage(upLeftImg);
			upperLeft = scaleImage(upperLeft, parent.width * 0.2, parent.height * 0.1);
		}
		if (!upRightImg.equals("")) {
			upperRight = parent.loadImage(upRightImg);
			upperRight = scaleImage(upperRight, parent.width * 0.2, parent.height * 0.1);
		}
		if (!downLeftImg.equals("")) {
			lowerLeft = parent.loadImage(downLeftImg);
			lowerLeft = scaleImage(lowerLeft, parent.width * 0.5, parent.height * 0.12);
		}
		if (!downRightImg.equals("")) {
			lowerRight = parent.loadImage(downRightImg);
			lowerRight = scaleImage(lowerRight, parent.width * 0.5, parent.height * 0.12);
		}
		this.headerText = headerText;
		slideShowImages = new ArrayList<PImage>();
		//load all images for the slide show, if specified
		if (!slideShowFolder.equals("")) loadSlideShowImages(slideShowFolder);
		//load the current image for the slide show
		if (!slideShowImages.isEmpty()) {
			currentSlideShowImage = slideShowImages.get(currentImage);
			currentSlideShowImage = scaleImage(currentSlideShowImage, 
					parent.width * HORIZONTAL_SCALE, parent.height * VERTICAL_SCALE);
		}
		currentImage = slideShowImages.size();
		lastRefreshTime = parent.millis();
		timeSinceDrawn = 0;
		pictureFadeTime = parent.millis() - FADE_WAIT_TIME;
		alpha = 0;

		//TODO documentation about adding pics/note to submissions tester (check before approving)
	}

	/**
	 * Controls slide show fading. Only used if there are images in the slide show folder.
	 */
	@Override
	public void update() {
		//draw the slide show image if the list of images isn't empty
		//checks if the alternate loop is being drawn (inactive state)
		if (!slideShowImages.isEmpty() && (parent.millis() - timeSinceDrawn) < FADE_WAIT_TIME) {
			//move to the next picture if the time to advance is met
			if (parent.millis() - lastRefreshTime >= TIME_TO_ADVANCE) {
				lastRefreshTime = parent.millis(); //reset timer
				alpha = 0;
				currentImage++;
				if (currentImage >= slideShowImages.size()) currentImage = 0;
				//load the new image and scale accordingly
				currentSlideShowImage = slideShowImages.get(currentImage);
				currentSlideShowImage = scaleImage(currentSlideShowImage, 
						parent.width * HORIZONTAL_SCALE, parent.height * VERTICAL_SCALE);
			}

			//control fade-in/out of the background images
			if (alpha < 255 && (parent.millis() - lastRefreshTime) < MAX_FADE_TIME) {
				alpha+= FADE_CHANGE;
				//bound max alpha
				if (alpha >= 255) alpha = 255;
				if (alpha > 240) {
					//record how long it took to reach full visibility
					pictureFadeTime = parent.millis() - lastRefreshTime;
				}
				
			} else if (alpha < 255 && (parent.millis() - lastRefreshTime) >= MAX_FADE_TIME
					&& (parent.millis() - lastRefreshTime) < (TIME_TO_ADVANCE - (1.02 * pictureFadeTime))) {
				//set fully visible if beyond the max fade time
				alpha = 255;
				pictureFadeTime = MAX_FADE_TIME;
				
			} else if ((parent.millis() - lastRefreshTime) >= (TIME_TO_ADVANCE - (1.02 * pictureFadeTime))) {
				alpha -= FADE_CHANGE;
				//bound min alpha
				if (alpha < 0) alpha = 0;
			}
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
		parent.imageMode(PApplet.CORNER);
		//draw the corner images if they are available
		if (upperLeft != null) parent.image(upperLeft, 
				(float) (parent.width * 0.025), (float) (parent.height * 0.025));

		if (upperRight != null) parent.image(upperRight, 
				(float) (parent.width * 0.975 - upperRight.width), 
				(float) (parent.height * 0.025));

		if (lowerLeft != null) parent.image(lowerLeft, 
				(float) (parent.width * 0.025), (float) (parent.height * 0.8));

		if (lowerRight != null) parent.image(lowerRight, 
				(float) (parent.width * 0.975 - lowerRight.width), 
				(float) (parent.height * 0.8));
	}

	/**
	 * An alternate draw function, where anything drawn will only appear once
	 * the home screen has faded out from inactivity.
	 */
	@Override
	public void alternateDrawFaded() {
		parent.textAlign(PApplet.CENTER, PApplet.TOP);
		parent.textSize(48);
		parent.fill(0);
		//draw the header text
		parent.text(headerText, parent.width / 2, (float) (parent.height * 0.05));
		parent.textAlign(PApplet.LEFT, PApplet.TOP);
		//skip drawing the first loop if it hasn't been drawing recently
		if ((parent.millis() - timeSinceDrawn) >= FADE_WAIT_TIME) {
			timeSinceDrawn = parent.millis();
			return;
		}
		timeSinceDrawn = parent.millis();
		//will do nothing if no pictures are loaded
		if (!slideShowImages.isEmpty()) {

			//if alpha is less than 255, make the image partially transparent
			if (alpha < 255) parent.tint(255, 255, 255, (int) alpha);
			else parent.noTint();

			parent.imageMode(PApplet.CENTER);
			//draw just above center
			parent.image(currentSlideShowImage, (float) parent.width/2, (float) (parent.height * 0.45));
			parent.noTint();
			parent.imageMode(PApplet.CORNER);
		}
	}

	/**
	 * Private function that opens the class jar and loads all pictures
	 * associated with the backdrop slide show. 
	 * @param slideShowLocation The string name of the folder that holds the slide show images
	 */
	private void loadSlideShowImages(String slideShowLocation) {
		CodeSource src = PhotoBackdrop.class.getProtectionDomain().getCodeSource();
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
					if (entryName.startsWith(ProcessingModule.IMAGES_LOCATION + slideShowLocation)) {
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
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static PImage scaleImage(PImage img, double maxPixelsX, double maxPixelsY) {
		PImage newImage = img;
		//get the ratio of height to width to conserve scaling
		float ratio = (float) newImage.width / newImage.height;
		//if statements to check which will hit cap first
		if (maxPixelsY * ratio > maxPixelsX) {
			//width is limiting factor
			newImage.resize((int) maxPixelsX, (int) (maxPixelsX / ratio));
		} else {
			//height is limiting factor
			newImage.resize((int) (maxPixelsY * ratio), (int) maxPixelsY);
		}
		return newImage;
	}
}