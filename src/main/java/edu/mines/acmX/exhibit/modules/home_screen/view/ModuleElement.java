package edu.mines.acmX.exhibit.modules.home_screen.view;

import processing.core.PApplet;
import processing.core.PImage;
import edu.mines.acmX.exhibit.module_management.ModuleManager;
import edu.mines.acmX.exhibit.module_management.metas.ModuleMetaData;
import edu.mines.acmX.exhibit.modules.home_screen.HomeScreen;
import edu.mines.acmX.exhibit.modules.home_screen.view.inputmethod.VirtualRectClick;

public class ModuleElement extends DisplayElement {

	public static final int MODULE_RUN_SPEED = 750;
	public static final int HINT_SPEED = 1;
	public static final int INFO_SPEED = 1;
	public static final int RECT_CURVE = 6;
	public static final int BORDER_CURVE = 6;
	public static final int INFO_FADE_SPEED = 5;
	public static final float IMAGE_PADDING = 10;
	private PImage icon;
	private String packageName;
	private ModuleMetaData data;
	private boolean leftEdge;
	private boolean rightEdge;
	private int edgeLength;
	private VirtualRectClick startGame;
	private VirtualRectClick hint;
	private VirtualRectClick info;
	private boolean visible;
	private boolean drawHint;
	private boolean drawInfo;
	private float infoAlpha;
	private boolean resize;

	public ModuleElement(HomeScreen par, PImage image, String name,
			ModuleMetaData data, double weight) {
		super(par, weight);
		icon = image;
		packageName = name;
		this.data = data;
		edgeLength = 0;
		startGame = new VirtualRectClick(MODULE_RUN_SPEED, 0, 0, 0, 0);
		hint = new VirtualRectClick(HINT_SPEED, 0, 0, 0, 0);
		info = new VirtualRectClick(INFO_SPEED, 0, 0, 0, 0);
		visible = false;
		drawHint = false;
		drawInfo = false;
		infoAlpha = (float) 0;
		resize = true;

	}

	@Override
	public void update(int x, int y) {
		originX = x;
		originY = y;
		// update coordinates for all VirtualRectClicks
		startGame.updateCoordinates(originX + (width / 4), originY
				+ (height / 4), width / 2, height / 2);
		hint.updateCoordinates(originX, originY, width, height);
		info.updateCoordinates(originX + (3 * width / 4), originY, width / 4,
				height / 4);
		// if the module is visible, start checking for clicks
		if (visible && !(leftEdge) && !(rightEdge)) {

			startGame.update((int) HomeScreen.getHandX(),
					(int) HomeScreen.getHandY(), parent.millis());
			hint.update((int) HomeScreen.getHandX(),
					(int) HomeScreen.getHandY(), parent.millis());
			info.update((int) HomeScreen.getHandX(),
					(int) HomeScreen.getHandY(), parent.millis());
		}
		visible = false;
		leftEdge = false;
		rightEdge = false;
	}

	@Override
	public void draw() {

		// processing has a bug where continually resizing the image will fade
		// the image. We have to make sure to do this only once.
		if(resize) {
			icon.resize(width, height);
			resize = false;
		}

		visible = true;
		// show part of an image if the full image doesn't fit.
		float heightRatio = (float) icon.height / height;
		float widthRatio = (float) icon.width / width;
		if (leftEdge) {
			drawModuleIconWithLeftSideCut((int) edgeLength, 0,
					(int) (width - edgeLength), icon.height);
		} else if (rightEdge) {

			drawModuleIconWithRightSideCut(0, 0, (int) (width - edgeLength),
					icon.height);
		}
		// else show the icon normally
		else {
			drawModuleIcon();
		}

		// draw hint if need be
		if (drawHint && !drawInfo) {
			drawHintForInfo();
			drawLaunchArea();
		}
		// if a click registers with the info click, draw the info
		if (drawInfo) {
			drawInformationFade(infoAlpha);
			if (infoAlpha < 255) {
				infoAlpha += INFO_FADE_SPEED;
			}
		}
		// if the click was false, but the infoAlpha isn't zero, fade info away
		else if (infoAlpha > 0) {
			drawInformationFade(infoAlpha);
			infoAlpha -= INFO_FADE_SPEED;
		}
	}

	private void drawModuleIconWithRightSideCut(int x, int y, int cutWidth,
			int cutHeight) {
		PImage temp = icon.get(x, y, cutWidth, cutHeight);
		parent.image(temp, 
				originX + cutWidth / IMAGE_PADDING,
				originY + cutHeight / IMAGE_PADDING, 
				cutWidth - 2 * cutWidth / IMAGE_PADDING, 
				cutHeight - 2 * cutHeight / IMAGE_PADDING);
	}

	private void drawModuleIconWithLeftSideCut(int x, int y, int cutWidth,
			int cutHeight) {
		PImage temp = icon.get(x, y, cutWidth, cutHeight);
		parent.image(temp, 
				originX + edgeLength + cutWidth / IMAGE_PADDING, 
				originY + cutHeight / IMAGE_PADDING, 
				cutWidth - 2 * cutWidth / IMAGE_PADDING,
				cutHeight - 2 * cutHeight / IMAGE_PADDING);
	}

	/**
	 * This draws a full module icon with no extra stuff.
	 */
	private void drawModuleIcon() {
		parent.stroke(153, 153);
		parent.noFill();
		parent.rect(originX, originY, width, height,
				(float) (width / BORDER_CURVE), (float) (height / BORDER_CURVE));
		parent.image(icon, originX + width / IMAGE_PADDING, originY + height
				/ IMAGE_PADDING, width - 2 * width / IMAGE_PADDING, height - 2
				* height / IMAGE_PADDING);
	}

	/**
	 * This draws an area giving more information about a module. This method
	 * should be called after image drawing so that the information is displayed
	 * on top of the module
	 */
	private void drawInformationFade(float alpha) {
		// set color to blue
		parent.fill(135, 206, 250, alpha);
		// draw info box
		parent.rect((float) originX, (float) originY, (float) width,
				(float) height, (float) (width / RECT_CURVE),
				(float) (height / RECT_CURVE));
		// set color to black
		parent.fill(0, alpha);
		// draw packageName
		parent.textSize(20);
		parent.textAlign(PApplet.LEFT, PApplet.TOP);
		parent.text(data.getTitle(), (float) (originX + (width / 6)),
				(float) (originY + (height / 6)));
		parent.text("By " + data.getAuthor(), (float) (originX + (width / 6)),
				(float) (originY + (height / 6) + 40));
	}

	/**
	 * This draws the area that the user will have to hand over to launch a
	 * module
	 */
	private void drawLaunchArea() {
		parent.rect((float) info.getX(), (float) info.getY(),
				(float) info.getWidth(), (float) info.getHeight(),
				(float) (info.getWidth() / RECT_CURVE),
				(float) (info.getHeight() / RECT_CURVE));
		parent.noStroke();
		parent.fill(0);
	}

	/**
	 * This draws an area that can be hand over to show information about a
	 * module.
	 */
	private void drawHintForInfo() {
		parent.noFill();
		parent.stroke(0);
		parent.strokeWeight(4);
		// draw the start module hint rect
		parent.rect((float) startGame.getX(), (float) startGame.getY(),
				(float) startGame.getWidth(), (float) startGame.getHeight(),
				(float) (startGame.getWidth() / RECT_CURVE),
				(float) (startGame.getHeight() / RECT_CURVE));
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

	// check all clicks registered to this ModuleElement for clicks
	public void checkClicks(int millis) {
		// check for a module start click
		if (startGame.durationCompleted(millis)) {
			try {
				ModuleManager manager = ModuleManager.getInstance();
				manager.setNextModule(packageName);
				((HomeScreen) parent).getReceiver().killHand();
				parent.exit();
			} catch (Exception e) {
				// TODO log
				e.printStackTrace();
			}
		}
		// check for a hint click
		if (hint.durationCompleted(millis)) {
			drawHint = true;
		} else {
			drawHint = false;
		}
		// check for an info click
		if (info.durationCompleted(millis)) {
			drawInfo = true;
		} else {
			drawInfo = false;
		}

	}
}