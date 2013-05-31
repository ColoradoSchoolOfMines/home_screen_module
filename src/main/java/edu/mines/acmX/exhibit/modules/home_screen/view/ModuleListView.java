package edu.mines.acmX.exhibit.modules.home_screen.view;

import java.util.HashMap;
import java.util.Map;

import processing.core.PApplet;
import processing.core.PImage;
import edu.mines.acmX.exhibit.modules.home_screen.HomeScreen;
import edu.mines.acmX.exhibit.modules.home_screen.model.ModuleList;
import edu.mines.acmX.exhibit.modules.home_screen.view.inputmethod.VirtualRectClick;

public class ModuleListView extends DisplayElement {
	
	
	public static final int ARROW_WIDTH = 70;
	public static final int ARROW_OFFSETX = 10;
	// ARROW_HEIGHT
	public static final int NUM_MODULES_VISIBLE = 3;
	public static final int TRANSITION_MODULE_DELAY = 500;

	
	ModuleList list;
	private Map<Integer, PImage> moduleImageCache;

	private int modulePanelOffset = 0;
	
	private VirtualRectClick rightArrowClick;
	private VirtualRectClick leftArrowClick;
	private VirtualRectClick startModule;
	private VirtualRectClick infoBox;
	private VirtualRectClick activationBox;

	public ModuleListView(PApplet par, double screenScale, ModuleList data, double weight) {
		
		super(par, screenScale, weight);
		this.list = data;
		moduleImageCache = new HashMap<Integer, PImage>();
		rightArrowClick = new VirtualRectClick(TRANSITION_MODULE_DELAY, (parent.width - ARROW_WIDTH - ARROW_OFFSETX), originY, ARROW_WIDTH, HomeScreen.MODULE_HEIGHT);
		leftArrowClick = new VirtualRectClick(TRANSITION_MODULE_DELAY, originX, originY, ARROW_WIDTH, HomeScreen.MODULE_HEIGHT);
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
			int scaledModuleX = (int) scale(HomeScreen.MODULE_OFFSETX + i * (HomeScreen.MODULE_WIDTH + HomeScreen.MODULE_MARGIN));
			int scaledModuleY = (int) scale(originY);
			int scaledModuleWidth = (int) scale(HomeScreen.MODULE_HEIGHT);
			int scaledModuleHeight = (int) scale(HomeScreen.MODULE_HEIGHT);
			
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
	}

	@Override
	public void update(int x, int y) {
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
