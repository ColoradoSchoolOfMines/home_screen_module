package edu.mines.acmX.exhibit.modules.home_screen.backdrops.bubbles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;
import edu.mines.acmX.exhibit.modules.home_screen.HomeScreen;
import edu.mines.acmX.exhibit.modules.home_screen.backdrops.Backdrop;

public class BubblesBackdrop extends Backdrop {
	
	public static final int RAND_BUBBLE_SPAWN = 10;
	public static final String BUBBLE_IMAGE = "bubble.png"; 
	
	private List<Bubble> bubbles;
	private Random rand;
	private PImage img;
	
	public BubblesBackdrop(HomeScreen par, double screenScale) {
		super(par, screenScale);
		bubbles = new ArrayList<Bubble>();
		rand = new Random();
		img = parent.loadImage(BUBBLE_IMAGE);
	}

	@Override
	public void update() {
		if (rand.nextInt(100) < RAND_BUBBLE_SPAWN) {
			// Spawn a bubble
			int randX = rand.nextInt(parent.screenWidth);
			bubbles.add(new Bubble(randX, parent.screenHeight));
		}
		
		for (Bubble b : bubbles) {
			b.update();
		}
	}

	@Override
	public void draw() {
		for (Bubble b : bubbles) {
			int newX = (int) Math.round(scale(b.getX()));
			int newY = (int) Math.round(scale(b.getY()));
			parent.image(img, newX, newY);
		}
	}

}
