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
    public static final int INFO_VERT_PADDING = 20;
    public static final int INFO_HOR_PADDING = 20;
    public static final int INFO_SIZE = 8;
	private static final String LAUNCH_TEXT = "LAUNCH";
	private PImage icon;
    private PImage infoImage;
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

	public ModuleElement(HomeScreen par, PImage image, PImage infoImage, String name,
			ModuleMetaData data, double weight) {
		super(par, weight);
		icon = image;
		packageName = name;
		this.data = data;
        this.infoImage = infoImage;
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

        info.updateCoordinates(originX + width - width / INFO_HOR_PADDING - width / INFO_SIZE,
                originY + height / INFO_VERT_PADDING, width / INFO_SIZE, height
                / INFO_SIZE);
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
		if (resize) {
			icon.resize((int) (width - 2 * width / IMAGE_PADDING),
					(int) (height - 2 * height / IMAGE_PADDING));
			resize = false;
		}

		visible = true;
		// show part of an image if the full image doesn't fit.
		float heightRatio = (float) icon.height / height;
		float widthRatio = (float) icon.width / width;
		if (leftEdge) {
			drawModuleIconWithLeftSideCut();
		} else if (rightEdge) {

			drawModuleIconWithRightSideCut();
		}
		// else show the icon normally
		else {
            int moduleTint;
            // set alpha for module icon
            if( drawHint ) {
                moduleTint = 135;
            } else {
                moduleTint = 255;
            }
			drawModuleIcon(moduleTint);
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

	/**
	 * TODO comment
	 * 
	 * @param x
	 * @param y
	 * @param cutWidth
	 * @param cutHeight
	 */
	private void drawModuleIconWithRightSideCut() {
		int cutWidth = (int) (width - edgeLength);
		// set the drawing parameters for the border
		parent.stroke(153, 153);
		parent.noFill();

		float xRadius = width / BORDER_CURVE;
		float yRadius = height / BORDER_CURVE;
		
		float originXAtCut = originX + cutWidth; //modified from other
		float originYAtCut = originY;
		float originToDrawLine = originXAtCut;
		
		// this ensures that the top and bottom lines are not drawn over the right most arcs. 
		if (edgeLength <= xRadius) {
			originToDrawLine = originXAtCut - (xRadius - edgeLength); //modified
		}

		
		// draw line on the top if in view
		if (cutWidth >= xRadius) {
			parent.line(originToDrawLine, originYAtCut, originXAtCut - cutWidth
					+ xRadius, originYAtCut); //modified from other
		}

		// draw line on the bottom if in view
		if (cutWidth >= xRadius) {
			parent.line(originToDrawLine, originYAtCut + height, originXAtCut
					- cutWidth + xRadius, originYAtCut + height); //modified from other
		}
		
		// draw line on the left side
		parent.line(originXAtCut - cutWidth, originYAtCut + yRadius,
				originXAtCut - cutWidth, originYAtCut + height - yRadius); //modifed from other
		
		// calculate the theta to make the left side arcs come in smoothly
		float thetaForLeft = calculateThetaForArc(cutWidth, xRadius); // modified
		
		// draw the corner for the top-left corner
		// we need to multiply radius by two because we are dictating the elipse
		// diameter
		parent.arc(originXAtCut - cutWidth + xRadius, originYAtCut + yRadius,
				xRadius * 2, yRadius * 2, PApplet.PI , thetaForLeft + PApplet.PI); //sign flip
		
		// draw the corner for the bottom-left corner
		parent.arc(originXAtCut - cutWidth + xRadius, originYAtCut + height
				- yRadius, xRadius * 2, yRadius * 2, PApplet.PI - thetaForLeft, PApplet.PI); //sign flip
		
		// calculate the theta for the right side arc to display smoothly.
		float thetaForRight = calculateThetaForArc(edgeLength, xRadius);
		// Only display the right arcs if the edge length is small enough.
		if (edgeLength < xRadius) {
			// draw the top right corner
			parent.arc(originXAtCut + (edgeLength - xRadius), originYAtCut
					+ yRadius, xRadius * 2, yRadius * 2, -PApplet.HALF_PI, -thetaForRight );
			// draw the bottom left corner
			parent.arc(originXAtCut + (edgeLength - xRadius), originYAtCut
					+ height - yRadius, xRadius * 2, yRadius * 2,
					thetaForRight, PApplet.HALF_PI);
		}
		
		float adjustedImageX, adjustedImageWidth, adjustedCutWidth, adjustedCutHeight, imageOffsetXFromZeroZero, imageWidth, imageHeight;
		imageHeight = height - 2 * height / IMAGE_PADDING;
		if (edgeLength < width / IMAGE_PADDING) {
			adjustedImageX = originX + width / IMAGE_PADDING;
			// adjustedImageWidth = cutWidth - width / IMAGE_PADDING;
			imageOffsetXFromZeroZero = 0;
			imageWidth = width - 2 * width / IMAGE_PADDING;
		} else {
			adjustedImageX = originX + width / IMAGE_PADDING;
			// adjustedImageWidth = cutWidth - 2 * width / IMAGE_PADDING;
			imageOffsetXFromZeroZero = 0;
			imageWidth = cutWidth - 1 * width / IMAGE_PADDING;
		}

		// The final case is when the cut is on the image padding on the left
		// of the image. Here we will not render the image at all.
		if (cutWidth > width / IMAGE_PADDING) {
			PImage temp = icon.get((int) imageOffsetXFromZeroZero, 0,
					(int) imageWidth, (int) imageHeight);
			parent.image(temp, adjustedImageX,
					originY + height / IMAGE_PADDING, imageWidth, imageHeight);
		}

		
	}

	/**
	 * TODO comment
	 */
	private void drawModuleIconWithLeftSideCut() {
		int cutWidth = (int) (width - edgeLength);
		// set the drawing parameters for the border
		parent.stroke(153, 153);
		parent.noFill();

		float xRadius = width / BORDER_CURVE;
		float yRadius = height / BORDER_CURVE;
		float originXAtCut = originX + edgeLength;
		float originYAtCut = originY;
		float originToDrawLine = originXAtCut;

		// this ensures that the top and bottom lines are not drawn over the left most arcs. 
		if (edgeLength <= xRadius) {
			originToDrawLine = xRadius - edgeLength + originXAtCut;
		}

		// draw line on the top if in view
		if (cutWidth >= xRadius) {
			parent.line(originToDrawLine, originYAtCut, originXAtCut + cutWidth
					- xRadius, originYAtCut);
		}

		// draw line on the bottom if in view
		if (cutWidth >= xRadius) {
			parent.line(originToDrawLine, originYAtCut + height, originXAtCut
					+ cutWidth - xRadius, originYAtCut + height);
		}

		// draw line on the right side
		parent.line(originXAtCut + cutWidth, originYAtCut + yRadius,
				originXAtCut + cutWidth, originYAtCut + height - yRadius);

		// calculate the theta to make the right side arcs come in smoothly
		float theataForRight = calculateThetaForArc(cutWidth, xRadius);

		// draw the corner for the top-right corner
		// we need to multiply radius by two because we are dictating the elipse
		// diameter
		parent.arc(originXAtCut + cutWidth - xRadius, originYAtCut + yRadius,
				xRadius * 2, yRadius * 2, -theataForRight, 0);

		// draw the corner for the bottom-right corner
		parent.arc(originXAtCut + cutWidth - xRadius, originYAtCut + height
				- yRadius, xRadius * 2, yRadius * 2, 0, theataForRight);

		// calculate the theta for the left side arc to display smoothly.
		float thetaForLeft = calculateThetaForArc(edgeLength, xRadius);
		// Only display the left arcs if the edge length is small enough.
		if (edgeLength < xRadius) {
			// draw the top left corner
			parent.arc(originXAtCut + (xRadius - edgeLength), originYAtCut
					+ yRadius, xRadius * 2, yRadius * 2, -PApplet.PI
					+ thetaForLeft, -PApplet.HALF_PI);
			// draw the bottom left corner
			parent.arc(originXAtCut + (xRadius - edgeLength), originYAtCut
					+ height - yRadius, xRadius * 2, yRadius * 2,
					PApplet.HALF_PI, PApplet.PI - thetaForLeft);
		}
		
		// There are a couple cases to deal with when drawing an image. One,
		// when the image is being cut itself. This is the else in the following
		// code. The second case is when the module is being cut in between the
		// edge of the module and the image (i.e. in the image padding area of
		// the left hand side). This second case is what the if statement
		// catches.
		float adjustedImageX, adjustedImageWidth, adjustedCutWidth, adjustedCutHeight, imageOffsetXFromZeroZero, imageWidth, imageHeight;
		imageHeight = height - 2 * height / IMAGE_PADDING;
		if (edgeLength < width / IMAGE_PADDING) {
			adjustedImageX = originX + width / IMAGE_PADDING;
			// adjustedImageWidth = cutWidth - width / IMAGE_PADDING;
			imageOffsetXFromZeroZero = 0;
			imageWidth = width - 2 * width / IMAGE_PADDING;
		} else {
			adjustedImageX = originX + edgeLength;
			// adjustedImageWidth = cutWidth - 2 * width / IMAGE_PADDING;
			imageOffsetXFromZeroZero = edgeLength - width / IMAGE_PADDING;
			imageWidth = cutWidth - 1 * width / IMAGE_PADDING;
		}

		// The final case is when the cut is on the image padding on the right
		// of the image. Here we will not render the image at all.
		if (cutWidth > width / IMAGE_PADDING) {
			PImage temp = icon.get((int) imageOffsetXFromZeroZero, 0,
					(int) imageWidth, (int) imageHeight);
			parent.image(temp, adjustedImageX,
					originY + height / IMAGE_PADDING, imageWidth, imageHeight);
		}
	}

	/**
	 * This will calculate the angle at which an arc should be drawn to make it
	 * fade in with a hard edge.
	 */
	private float calculateThetaForArc(float cutArcFromHorizontalEdge, float arcRadi) {
		float tempX = cutArcFromHorizontalEdge;
		if (cutArcFromHorizontalEdge > arcRadi) {
			 tempX = arcRadi;
		}
		return (float) Math.acos((arcRadi - tempX) / arcRadi);
	}

	/**
	 * This draws a full module icon with no extra stuff.
	 */
	private void drawModuleIcon(int tint) {
		parent.stroke(153, 153);
		parent.noFill();
		parent.rect(originX, originY, width, height,
				(float) (width / BORDER_CURVE), (float) (height / BORDER_CURVE));
        parent.tint(255, tint);
		parent.image(icon, originX + width / IMAGE_PADDING, originY + height
				/ IMAGE_PADDING, width - 2 * width / IMAGE_PADDING, height - 2
				* height / IMAGE_PADDING);
        parent.noTint();
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
	private void drawHintForInfo() {
		parent.stroke(0);
		parent.noFill();
		parent.strokeWeight(4);
        parent.image(this.infoImage, info.getX(), info.getY(), info.getWidth(), info.getHeight());
	}

	/**
	 * This draws an area that can be hand over to show information about a
	 * module.
	 */
	private void drawLaunchArea() {

		parent.fill(114,114,114,183);
		parent.stroke(0);
		parent.strokeWeight(4);
		// draw the start module hint rect
		parent.rect((float) startGame.getX(), (float) startGame.getY(),
				(float) startGame.getWidth(), (float) startGame.getHeight(),
				(float) (startGame.getWidth() / RECT_CURVE),
				(float) (startGame.getHeight() / RECT_CURVE));
		parent.textSize(48);
		parent.fill(0, 102, 153, 220);
		parent.textAlign(PApplet.CENTER, PApplet.CENTER);
		parent.text(LAUNCH_TEXT, (float)(startGame.getX() + startGame.getWidth() / 2.0), (float)(startGame.getY() + startGame.getHeight() / 2.0));
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
