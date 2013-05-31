package edu.mines.acmX.exhibit.modules.home_screen.view;

import processing.core.PApplet;
import edu.mines.acmX.exhibit.modules.home_screen.view.inputmethod.VirtualRectClick;

public abstract class ClickableDisplayElement extends DisplayElement {

	VirtualRectClick click;
	
	public ClickableDisplayElement(PApplet par, int originX, int originY,
			double scale, double weight, int width, int height, VirtualRectClick click) {
		super(par, scale, weight, width, height);
		this.click = click;
	}

	@Override
	public abstract void update(int x, int y);

	@Override
	public abstract void draw();

}
