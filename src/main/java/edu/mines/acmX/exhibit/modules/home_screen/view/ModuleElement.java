package edu.mines.acmX.exhibit.modules.home_screen.view;

import processing.core.PImage;

import edu.mines.acmX.exhibit.module_manager.ModuleManager;
import edu.mines.acmX.exhibit.modules.home_screen.HomeScreen;
import edu.mines.acmX.exhibit.modules.home_screen.view.inputmethod.VirtualRectClick;

public class ModuleElement extends DisplayElement {

	public static final int MODULE_RUN_SPEED = 500;
	private PImage icon;
	private String packageName;
	private boolean leftEdge;
	private boolean rightEdge;
	private int edgeLength;
	private VirtualRectClick startGame;
	private boolean visible;
	
	public ModuleElement(HomeScreen par, double screenScale, PImage image,
			String name, double weight) {
		super(par, screenScale, weight);
		icon = image;
		packageName = name;
		edgeLength = 0;
		startGame = new VirtualRectClick(MODULE_RUN_SPEED, 0, 0, 0, 0);
		visible = false;
	}

	@Override
	public void update(int x, int y) {
		originX = x;
		originY = y;
		startGame.updateCoordinates(originX + (width / 4), originY + (height / 4), width / 2, height / 2);
		if (visible) {
			startGame.update(parent.mouseX, parent.mouseY, parent.millis());
		}
		visible = false;
	}

	@Override
	public void draw() {
		visible = true;
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

	public VirtualRectClick getStartGameClick() {
		return startGame;
	}

	public void checkClicks(int millis) {
		if (startGame.durationCompleted(millis)) {
			try {
				ModuleManager manager = ModuleManager.getInstance();
				manager.setNextModule(packageName);
				parent.exit();
			} catch (Exception e) {
				// TODO log
				e.printStackTrace();
			}
		}

	}
}
