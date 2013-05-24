package edu.mines.acmX.exhibit.modules.home_screen.backdrops.bubbles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;
import edu.mines.acmX.exhibit.modules.home_screen.backdrops.Backdrop;

public class BubblesBackdrop extends Backdrop {
	
	public static final int RAND_BUBBLE_SPAWN = 10;
	public static final String BUBBLE_IMAGE = "bubble.png"; 
	
	private List<Bubble> bubbles;
	private Random rand;
	private PImage img;
	
	public BubblesBackdrop(PApplet par) {
		super(par);
		bubbles = new ArrayList<Bubble>();
		rand = new Random();
		img = parent.loadImage(BUBBLE_IMAGE);
	}

	@Override
	public void update() {
		if (rand.nextInt(100) < RAND_BUBBLE_SPAWN) {
			// Spawn a bubble
			int randX = rand.nextInt(parent.screenWidth);
			PApplet.println("RAndom x : " + randX);
			bubbles.add(new Bubble(randX, parent.screenHeight));
			System.out.println("Adding a new bubble");
		}
		
		for (Bubble b : bubbles) {
			b.update();
		}
	}

	@Override
	public void draw() {
		for (Bubble b : bubbles) {
			parent.image(img, b.getX(), b.getY());
		}
	}

}
