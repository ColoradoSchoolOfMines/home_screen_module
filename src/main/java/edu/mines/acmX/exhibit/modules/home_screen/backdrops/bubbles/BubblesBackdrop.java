package edu.mines.acmX.exhibit.modules.home_screen.backdrops.bubbles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import processing.core.PImage;
import edu.mines.acmX.exhibit.modules.home_screen.HomeScreen;
import edu.mines.acmX.exhibit.modules.home_screen.backdrops.Backdrop;

public class BubblesBackdrop extends Backdrop {
	
	//odds (in %) of a bubble spawning
	public static final int RAND_BUBBLE_SPAWN = 10;
	public static final String BUBBLE_IMAGE = "bubble.png"; 
	
	private List<Bubble> bubbles;
	private Random rand;
	private PImage img;
	
	public BubblesBackdrop(HomeScreen par) {
		super(par);
		//build a list of bubbles
		bubbles = new ArrayList<Bubble>();
		rand = new Random();
		img = parent.loadImage(BUBBLE_IMAGE);
		if (img == null) {
			System.out.println("BUBBLE LOAD ERROR!");
		}
	}

	@Override
	public void update() {
		if (rand.nextInt(100) < RAND_BUBBLE_SPAWN) {
			// Spawn a bubble if below the spawn threshold
			int randX = rand.nextInt(parent.width);
			//adds at random coordinate along the bottom of the screen
			bubbles.add(new Bubble(randX, parent.height));
		}
		
		for(int i = 0; i < bubbles.size(); i++) {
			Bubble b = bubbles.get(i);
			b.update();
			//if a bubble hits the top of the screen, remove it
			if(b.getY() <= 0) {
				bubbles.remove(i);
				i--;
			}
		}
	}

	@Override
	public void draw() {
		for (Bubble b : bubbles) {
			int newX = b.getX();
			int newY = b.getY();
			parent.tint(255, 128);
			parent.image(img, newX, newY);
			parent.noTint();
		}
	}

}
