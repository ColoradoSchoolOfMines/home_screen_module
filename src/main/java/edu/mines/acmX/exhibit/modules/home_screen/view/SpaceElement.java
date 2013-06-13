package edu.mines.acmX.exhibit.modules.home_screen.view;

import processing.core.PApplet;

public class SpaceElement extends DisplayElement {

	public SpaceElement(PApplet par, double weight) {
		super(par, weight);
	}

	@Override
	public void update(int x, int y) {
		originX = x;
		originY = y;

	}

	@Override
	public void draw() {
		// SpaceElements don't draw anything, they just take up space
	}

}
