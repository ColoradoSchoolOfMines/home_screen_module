package edu.mines.acmX.exhibit.modules.home_screen.view;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import processing.core.PImage;
import edu.mines.acmX.exhibit.modules.home_screen.HomeScreen;
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
	private VirtualRectClick startModule;
	private VirtualRectClick infoBox;
	private VirtualRectClick activationBox;

	public ModuleListView(HomeScreen par, int origin_x, int origin_y,
			double screenScale, ModuleList data) {
		
		super(par, origin_x, origin_y, screenScale);
		this.list = data;
		moduleImageCache = new HashMap<Integer, PImage>();
		rightArrowClick = new VirtualRectClick(TRANSITION_MODULE_DELAY, (parent.width - ARROW_WIDTH - ARROW_OFFSETX), originY, ARROW_WIDTH, MODULE_HEIGHT);
		leftArrowClick = new VirtualRectClick(TRANSITION_MODULE_DELAY, originX, originY, ARROW_WIDTH, MODULE_HEIGHT);
		//activationBox = new VirtualRectClick(1, );
	}
	
	public int normalizeModuleOffset(int i) {
		return (modulePanelOffset + i) % list.size();
	}
	
	public void drawModuleElement() {
		
	}

	@Override
	public void draw() {
		for (int i = 0; i < NUM_MODULES_VISIBLE; ++i) {
			parent.fill(255, 255, 255);
			int scaledModuleX = (int) scale(MODULE_OFFSETX + i * (MODULE_WIDTH + MODULE_MARGIN));
			int scaledModuleY = (int) scale(originY);
			int scaledModuleWidth = (int) scale(MODULE_HEIGHT);
			int scaledModuleHeight = (int) scale(MODULE_HEIGHT);
			
			parent.rect(scaledModuleX, scaledModuleY, scaledModuleWidth, scaledModuleHeight);
			normalizeModuleOffset(i);
			parent.image(getModuleImage(normalizeModuleOffset(i)), 
					scaledModuleX, scaledModuleY,
					scaledModuleWidth, scaledModuleHeight);
			parent.fill(50);
//			parent.text("" + (i + modulePanelOffset),
//					(float) (MODULE_OFFSETX + i * (MODULE_WIDTH + MODULE_MARGIN) + .5 * (MODULE_WIDTH)),
//					(float) (originY + .5 * (MODULE_HEIGHT)));
		}
		
		parent.stroke(0, 0, 0);
		parent.fill(255, 255, 255);
		int scaledArrowX1 = (int) scale(10);
		int scaledArrowY1 = (int) scale(originY + (.5 * MODULE_HEIGHT));
		
		int scaledArrowX2 = (int) scale(80);
		int scaledArrowY2 = (int) scale(originY + (.25 * MODULE_HEIGHT));
		
		int scaledArrowY3 = (int) scale(originY + (.75 * MODULE_HEIGHT));
		parent.triangle(scaledArrowX1, scaledArrowY1,
						scaledArrowX2, scaledArrowY2,
						scaledArrowX2, scaledArrowY3);
		parent.triangle(parent.width - scaledArrowX1, scaledArrowY1,
						parent.width - scaledArrowX2, scaledArrowY2,
						parent.width - scaledArrowX2, scaledArrowY3);
	}

	@Override
	public void update() {
		int millis = parent.millis();
		boolean moved = false;
		if (rightArrowClick.durationCompleted(millis)) {
				++modulePanelOffset;
				moved = true;
		}
		if (leftArrowClick.durationCompleted(millis)) {
				--modulePanelOffset;
				moved = true;
		}
		if (moved) {
			while (modulePanelOffset < 0) {
				modulePanelOffset = modulePanelOffset + list.size();
			}
		}
	}
	
	public void mouseMoved() {
		rightArrowClick.update(parent.mouseX, parent.mouseY, parent.millis());
		leftArrowClick.update(parent.mouseX, parent.mouseY, parent.millis());

	}
	
	private PImage getModuleImage(int index) {
//		if (moduleImageCache.containsKey(index)) {
//			return moduleImageCache.get(index);
//		} 
//		
//		PImage loadedImage = parent.loadImage(list.getModuleImageFilename(index));
//		moduleImageCache.put(index, loadedImage);
//		return loadedImage;
		return list.getModuleImage(index);
	}
	
}
