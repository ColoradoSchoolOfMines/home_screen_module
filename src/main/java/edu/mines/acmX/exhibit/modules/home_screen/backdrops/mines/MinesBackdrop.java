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

	//folder where all module images are contained
	public static final String IMAGES_LOCATION = "images/";
	//file folder holding slideshow pictures
	public static final String SLIDE_SHOW_LOCATION = "SlideShowImages/";
	//time (in millis) to wait before loading the next picture
	public static final int TIME_TO_ADVANCE = 15000;

	private PImage logo;
	private PImage banner;
	private PImage blaster;
	private PImage spinLogo;

	private List<PImage> slideShowImages;
	private int currentImage;
	private int lastRefreshTime;
	private int alpha;

	public static final float ROTATION_INCREMENT = (float) 0.05;
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
		lastRefreshTime = parent.millis();

		//TODO documentation about adding pics/note to submissions tester (check before approving)
	}

	/**
	 * Increments the rotation for the top right image.
	 */
	@Override
	public void update() {
		rotationTotal += ROTATION_INCREMENT;
		if (parent.millis() - lastRefreshTime >= TIME_TO_ADVANCE) {
			lastRefreshTime = parent.millis(); //reset timer
			alpha = 0;
			currentImage++;
			if (currentImage >= slideShowImages.size()) currentImage = 0;
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
	@Override
	public void alternateDrawFaded() {
		//will do nothing if no pictures are loaded
		if (!slideShowImages.isEmpty()) {
			PImage currentImg = slideShowImages.get(currentImage);
			if (parent.millis() - lastRefreshTime < 5000 && alpha < 255) {
				alpha+= 3;
				parent.tint(255, 255, 255, alpha);
				System.out.println("Time since shift:" + (parent.millis() - lastRefreshTime));
			} else {
				parent.noTint();
			}
			parent.imageMode(PApplet.CENTER);
			parent.image(currentImg, parent.width/2, parent.height/2);
			parent.noTint();
			parent.imageMode(PApplet.CORNER);
		}
	}

	/**
	 * Private function that opens the class jar and loads all pictures
	 * associated with the backdrop slide show. 
	 */
	private void loadSlideShowImages() {
		CodeSource src = MinesBackdrop.class.getProtectionDomain().getCodeSource();
		if (src != null) {
			URL jarLoc = src.getLocation();
			try {
				ZipInputStream jarStream = new ZipInputStream(jarLoc.openStream());
				ZipEntry ze = null;
				List<String> fileNames = new ArrayList<String>();
				while ((ze = jarStream.getNextEntry()) != null) {
					String entryName = ze.getName();
					if (entryName.startsWith(IMAGES_LOCATION + SLIDE_SHOW_LOCATION)) {
						fileNames.add(entryName);
					}
				}
				for (int i = 0; i < fileNames.size(); ++i) {
					if (fileNames.get(i).contains("jpg") || 
							fileNames.get(i).contains("png")) {
						String truePath = fileNames.get(i).substring("images/".length());
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
}
