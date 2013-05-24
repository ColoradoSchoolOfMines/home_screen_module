package edu.mines.acmX.exhibit.modules.home_screen.view;

import processing.core.PApplet;

public abstract class DisplayElement {
	protected PApplet parent;
	protected int originX;
	protected int originY;
	protected double screenScale;
	
	public DisplayElement(PApplet par, int originX, int originY, double screenScale) {
		this.parent = par;
		this.originX = originX;
		this.originY = originY;
		this.screenScale = screenScale;
	}
	
	public abstract void update();
	public abstract void draw();
}
