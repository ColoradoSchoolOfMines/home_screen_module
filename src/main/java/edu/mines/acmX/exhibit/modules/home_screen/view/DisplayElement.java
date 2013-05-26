package edu.mines.acmX.exhibit.modules.home_screen.view;

import edu.mines.acmX.exhibit.modules.home_screen.HomeScreen;
import processing.core.PApplet;

public abstract class DisplayElement extends Scalable {
	protected PApplet parent;
	protected int originX;
	protected int originY;
	
	public DisplayElement(HomeScreen par, int originX, int originY, double screenScale) {
		super(screenScale);
		this.parent = par;
		this.originX = originX;
		this.originY = originY;
	}
	
	public abstract void update();
	public abstract void draw();
}
