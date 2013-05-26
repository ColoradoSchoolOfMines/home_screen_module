package edu.mines.acmX.exhibit.modules.home_screen.backdrops;

import edu.mines.acmX.exhibit.modules.home_screen.HomeScreen;
import edu.mines.acmX.exhibit.modules.home_screen.view.Scalable;
import processing.core.PApplet;

public abstract class Backdrop extends Scalable {
	protected PApplet parent;
	
	public Backdrop(HomeScreen par, double screenScale) {
		super(screenScale);
		this.parent = par;
	}

	public abstract void update();
	public abstract void draw();
}
