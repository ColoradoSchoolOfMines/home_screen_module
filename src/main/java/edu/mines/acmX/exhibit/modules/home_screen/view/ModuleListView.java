package edu.mines.acmX.exhibit.modules.home_screen.view;

import java.util.HashMap;
import java.util.Map;

import processing.core.PApplet;
import processing.core.PImage;
import edu.mines.acmX.exhibit.modules.home_screen.model.ModuleList;
import edu.mines.acmX.exhibit.modules.home_screen.view.inputmethod.VirtualRectClick;

public class ModuleListView extends DisplayElement {
	
	public static final int MODULE_OFFSETX = 125;
	public static final int MODULE_WIDTH = 500;
	public static final int MODULE_HEIGHT = 500;
	public static final int MODULE_MARGIN = 70;
	public static final int ARROW_WIDTH = 70;
	public static final int ARROW_OFFSETX = 10;
	// ARROW_HEIGHT
	public static final int NUM_MODULES_VISIBLE = 3;
	public static final int TRANSITION_MODULE_DELAY = 2000;

	
	ModuleList list;
	private Map<Integer, PImage> moduleImageCache;

	private int modulePanelOffset = 0;
	
	private VirtualRectClick rightArrowClick;
	private VirtualRectClick leftArrowClick;
	
	private boolean inRightArrowRegion = false;
	private int inRightArrowStartTime = 0;
	
	private boolean inLeftArrowRegion = false;
	private int inLeftArrowStartTime = 0;

	public ModuleListView(PApplet par, int origin_x, int origin_y,
			double screenScale, ModuleList data) {
		
		super(par, origin_x, origin_y, screenScale);
		this.list = data;
		moduleImageCache = new HashMap<Integer, PImage>();
		rightArrowClick = new VirtualRectClick(2000, (parent.screenWidth - ARROW_WIDTH - ARROW_OFFSETX), originY, ARROW_WIDTH, MODULE_HEIGHT);
		leftArrowClick = new VirtualRectClick(2000, originX, originY, ARROW_WIDTH, MODULE_HEIGHT);
		
	}

	@Override
	public void draw() {
		for (int i = 0; i < NUM_MODULES_VISIBLE; ++i) {
			parent.fill(255, 255, 255);
			parent.rect(MODULE_OFFSETX + i * (MODULE_WIDTH + MODULE_MARGIN), originY, MODULE_WIDTH, MODULE_HEIGHT);
			parent.image(getModuleImage(i + modulePanelOffset),
					MODULE_OFFSETX + i * (MODULE_WIDTH + MODULE_MARGIN), originY,
					MODULE_WIDTH, MODULE_HEIGHT);
			parent.fill(50);
//			parent.text("" + (i + modulePanelOffset),
//					(float) (MODULE_OFFSETX + i * (MODULE_WIDTH + MODULE_MARGIN) + .5 * (MODULE_WIDTH)),
//					(float) (originY + .5 * (MODULE_HEIGHT)));
		}
		
		parent.stroke(0, 0, 0);
		parent.fill(255, 255, 255);
		if ((modulePanelOffset - 1) >= 0) {
			parent.triangle((float) 10, (float) (originY + .5 * (MODULE_HEIGHT)),
					(float) 80, (float) (originY + .25 * (MODULE_HEIGHT)),
					(float) 80, (float) (originY + .75 * (MODULE_HEIGHT)));
		}
		
		if ((modulePanelOffset + NUM_MODULES_VISIBLE) < list.size()) {
			parent.triangle((float) (parent.screenWidth - 10), (float) (originY + .5 * (MODULE_HEIGHT)),
					(float) (parent.screenWidth - 80), (float) (originY + .25 * (MODULE_HEIGHT)),
					(float) (parent.screenWidth - 80), (float) (originY + .75 * (MODULE_HEIGHT)));
		}
	}

	@Override
	public void update() {
		int millis = parent.millis();

		if (rightArrowClick.durationCompleted(millis)) {
			if ((modulePanelOffset + NUM_MODULES_VISIBLE) < list.size()) ++modulePanelOffset;
		}
		if (leftArrowClick.durationCompleted(millis)) {
			if ((modulePanelOffset + NUM_MODULES_VISIBLE) >= 0) --modulePanelOffset;
		}
	}
	
	public void mouseMoved() {
		rightArrowClick.update(parent.mouseX, parent.mouseY, parent.millis());
		leftArrowClick.update(parent.mouseX, parent.mouseY, parent.millis());

	}
	
	private PImage getModuleImage(int index) {
		if (moduleImageCache.containsKey(index)) {
			return moduleImageCache.get(index);
		}
		PImage loadedImage = parent.loadImage(list.getModuleImageFilename(index));
		moduleImageCache.put(index, loadedImage);
		return loadedImage;
	}
	
}
