package edu.mines.acmX.exhibit.modules.home_screen.view;

import processing.core.PImage;
import edu.mines.acmX.exhibit.modules.home_screen.HomeScreen;

public class ModuleElement extends DisplayElement {

	private PImage icon;
	private String packageName;
	
	public ModuleElement(HomeScreen par, int originX, int originY,
			double screenScale, PImage image, String name) {
		super(par, originX, originY, screenScale);
		icon = image;
		packageName = name;
	}

	@Override
	public void update() {

	}

	@Override
	public void draw() {

	}
	
	public PImage getIcon() {
		return icon;
	}

}
