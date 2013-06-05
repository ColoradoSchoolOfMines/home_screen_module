package edu.mines.acmX.exhibit.modules.home_screen.view;

import processing.core.PApplet;
import edu.mines.acmX.exhibit.modules.home_screen.view.inputmethod.VirtualRectClick;

public abstract class ClickableDisplayElement extends DisplayElement {

	VirtualRectClick click;
	
	public ClickableDisplayElement(PApplet par, double weight, VirtualRectClick click) {
		super(par, weight);
		this.click = click;
	}

	@Override
	public abstract void update(int x, int y);

	@Override
	public abstract void draw();

}
