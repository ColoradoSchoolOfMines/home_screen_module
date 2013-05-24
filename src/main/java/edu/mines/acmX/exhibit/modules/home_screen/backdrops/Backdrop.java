package edu.mines.acmX.exhibit.modules.home_screen.backdrops;

import processing.core.PApplet;

public abstract class Backdrop {
	protected PApplet parent;
	
	public Backdrop(PApplet par) {
		this.parent = par;
	}
	
	public abstract void update();
	public abstract void draw();
}
