package edu.mines.acmX.exhibit.modules.home_screen.backdrops.mines;

import processing.core.PApplet;
import processing.core.PImage;
import edu.mines.acmX.exhibit.modules.home_screen.HomeScreen;
import edu.mines.acmX.exhibit.modules.home_screen.backdrops.Backdrop;
import edu.mines.acmX.exhibit.stdlib.graphics.Coordinate;

public class MinesBackdrop extends Backdrop {

	private PImage logo;
	private PImage banner;
	private PImage blaster;
	private PImage spinLogo;
	
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
	}

	@Override
	public void update() {
		rotationTotal += ROTATION_INCREMENT;
	}

	@Override
	public void draw() {
		//white background
		parent.background(255, 255, 255);
		parent.imageMode(PApplet.CENTER);
		parent.pushMatrix();
		parent.translate((float) spinLocation.getX(), (float) spinLocation.getY());
		parent.rotate(rotationTotal); //rotate logo
		parent.image(spinLogo, 0, 0, (float) (parent.height * 0.15), (float) (parent.height * 0.15));
		parent.popMatrix();
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

}
