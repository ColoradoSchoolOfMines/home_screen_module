package edu.mines.acmX.exhibit.modules.home_screen.view;

import processing.core.PImage;
import edu.mines.acmX.exhibit.modules.home_screen.HomeScreen;

public class ModuleElement extends DisplayElement {

	private PImage icon;
	private String packageName;
	private boolean leftEdge;
	private boolean rightEdge;
	private int edgeLength;
	
	public ModuleElement(HomeScreen par, double screenScale, PImage image,
			String name, double weight) {
		super(par, screenScale, weight);
		icon = image;
		packageName = name;
		edgeLength = 0;
	}

	@Override
	public void update(int x, int y) {
		originX = x;
		originY = y;
	}

	@Override
	public void draw() {
		if (leftEdge) {
			PImage temp = icon.get(edgeLength, originY, width - edgeLength, height);
			parent.image(temp, originX, originY, width, height);
		}
		else if (rightEdge) {
			parent.image(icon, originX, originY, width, height);
		}
		else {
		parent.image(icon, originX, originY, width, height);
		}
		leftEdge = false;
		rightEdge = false;
	}
	
	public PImage getIcon() {
		return icon;
	}

	public void setLeft(boolean bool) {
		leftEdge = bool;
	}

	public void setRight(boolean bool) {
		rightEdge = bool;
	}

	public void setEdgeLength(int len) {
		edgeLength = len;
	}
}
