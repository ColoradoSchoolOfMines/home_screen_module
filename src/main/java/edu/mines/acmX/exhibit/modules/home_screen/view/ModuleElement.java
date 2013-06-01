package edu.mines.acmX.exhibit.modules.home_screen.view;

import processing.core.PImage;
import edu.mines.acmX.exhibit.modules.home_screen.HomeScreen;

public class ModuleElement extends DisplayElement {

	private PImage icon;
	private String packageName;
	
	public ModuleElement(HomeScreen par, double screenScale, PImage image,
			String name, double weight) {
		super(par, screenScale, weight);
		icon = image;
		packageName = name;
	}

	@Override
	public void update(int x, int y) {
		originX = x;
		originY = y;
	}

	@Override
	public void draw() {
		parent.image(icon, originX, originY, width, height);
	}
	
	public PImage getIcon() {
		return icon;
	}

}
