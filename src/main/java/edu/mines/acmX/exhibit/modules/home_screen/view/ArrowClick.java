package edu.mines.acmX.exhibit.modules.home_screen.view;

import edu.mines.acmX.exhibit.modules.home_screen.view.inputmethod.VirtualRectClick;
import processing.core.PApplet;

public class ArrowClick extends ClickableDisplayElement {

	private Side side;
	
	public ArrowClick(PApplet par, double scale, double weight, VirtualRectClick click, Side side) {
		super(par, scale, weight, click);
		this.side = side;
	}

	@Override
	public void update(int x, int y) {
		originX = x;
		originY = y;

	}

	@Override
	public void draw() {
		parent.fill(255, 0, 0);
		if (side == Side.LEFT) {
			float x1 = (float) (originX + (width / 6.0));
			float y1 = (float) (originY + (height / 2.0));
			float x2 = (float) (originX + (5 * (width / 6.0)));
			float y2 = (float) (originY + (height / 6.0));
			float x3 = (float) (originX + (5 * (width / 6.0)));
			float y3 = (float) (originY + (5 * (height / 6.0)));
			parent.triangle(x1, y1, x2, y2, x3, y3);
		} else if (side == Side.RIGHT) {
			float x1 = (float) (originX + (5 * (width / 6.0)));
			float y1 = (float) (originY + (height / 2.0));
			float x2 = (float) (originX + (width / 6.0));
			float y2 = (float) (originY + (height / 6.0));
			float x3 = (float) (originX + (width / 6.0));
			float y3 = (float) (originY + (5 * (height / 6.0)));
			parent.triangle(x1, y1, x2, y2, x3, y3);
		}

	}

}
