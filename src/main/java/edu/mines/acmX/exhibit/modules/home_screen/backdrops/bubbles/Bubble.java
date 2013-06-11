package edu.mines.acmX.exhibit.modules.home_screen.backdrops.bubbles;

import java.util.Random;

public class Bubble {
	public static final int BOUYANCY = 8;
	public static final int MAX_AMPLITUDE = 30;
	private int x;
	private int y;
	
	private double tick;
	private int rand_amplitude;
	
	public Bubble(int initX, int initY) {
		x = initX;
		y = initY;
		
		tick = 0;
		
		Random r = new Random();
		rand_amplitude = r.nextInt(MAX_AMPLITUDE);
	}
	
	public void update() {
		++tick;
		y -= BOUYANCY;
		x = x + (int) (rand_amplitude * Math.sin(tick) / 2);	
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

}
