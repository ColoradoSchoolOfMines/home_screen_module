package edu.mines.acmX.exhibit.modules.home_screen.view;

import processing.core.PImage;

import edu.mines.acmX.exhibit.module_manager.ModuleManager;
import edu.mines.acmX.exhibit.modules.home_screen.HomeScreen;
import edu.mines.acmX.exhibit.modules.home_screen.view.inputmethod.VirtualRectClick;

public class ModuleElement extends DisplayElement {

	public static final int MODULE_RUN_SPEED = 500;
	public static final int HINT_SPEED = 1;
	public static final int INFO_SPEED = 1;
	public static final int RECT_CURVE = 6;
	private PImage icon;
	private String packageName;
	private boolean leftEdge;
	private boolean rightEdge;
	private int edgeLength;
	private VirtualRectClick startGame;
	private VirtualRectClick hint;
	private VirtualRectClick info;
	private boolean visible;
	private boolean drawHint;
	private boolean drawInfo;
	
	public ModuleElement(HomeScreen par, double screenScale, PImage image,
			String name, double weight) {
		super(par, screenScale, weight);
		icon = image;
		packageName = name;
		edgeLength = 0;
		startGame = new VirtualRectClick(MODULE_RUN_SPEED, 0, 0, 0, 0);
		hint = new VirtualRectClick(HINT_SPEED, 0, 0, 0, 0);
		info = new VirtualRectClick(INFO_SPEED, 0, 0, 0, 0);
		visible = false;
		drawHint = false;
		drawInfo = false;
	}

	@Override
	public void update(int x, int y) {
		originX = x;
		originY = y;
		startGame.updateCoordinates(originX + (width / 4), originY + (height / 4), width / 2, height / 2);
		hint.updateCoordinates(originX, originY, width, height);
		info.updateCoordinates(originX + ( 3 * width / 4), originY, width / 4, height / 4);
		if (visible) {
			startGame.update(parent.mouseX, parent.mouseY, parent.millis());
			hint.update(parent.mouseX, parent.mouseY, parent.millis());
			info.update(parent.mouseX, parent.mouseY, parent.millis());
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

		// draw hint if need be
		if (drawHint) {
			parent.noFill();
			parent.stroke(0);
			parent.strokeWeight(4);
			parent.rect((float) startGame.getX(), (float) startGame.getY(), (float) startGame.getWidth(), (float) startGame.getHeight(), (float) (startGame.getWidth() / RECT_CURVE), (float) (startGame.getHeight() / RECT_CURVE));
			parent.rect((float) info.getX(), (float) info.getY(), (float) info.getWidth(), (float) info.getHeight(), (float) (info.getWidth() / RECT_CURVE), (float) (info.getHeight() / RECT_CURVE));
			parent.noStroke();
			parent.fill(0);
		}
		if (drawInfo) {
			parent.fill(135, 206, 250);
			parent.rect((float) originX, (float) originY, (float) width, (float) height, (float) (width / RECT_CURVE), (float) (height / RECT_CURVE));
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
		if (hint.durationCompleted(millis)) {
			drawHint = true;
		}
		else {
			drawHint = false;
		}
		if (info.durationCompleted(millis)) {
			drawInfo = true;
		}
		else {
			drawInfo = false;
		}

	}
}
