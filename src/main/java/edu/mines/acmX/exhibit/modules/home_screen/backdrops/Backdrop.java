package edu.mines.acmX.exhibit.modules.home_screen.backdrops;

import edu.mines.acmX.exhibit.modules.home_screen.HomeScreen;
import processing.core.PApplet;



public abstract class Backdrop {
	protected PApplet parent;
	
	public Backdrop(HomeScreen par) {
		this.parent = par;
	}

	public abstract void update();
	public abstract void draw();
}
