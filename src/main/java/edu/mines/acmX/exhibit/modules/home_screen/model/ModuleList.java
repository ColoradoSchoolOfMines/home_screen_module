package edu.mines.acmX.exhibit.modules.home_screen.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import processing.core.PImage;

public class ModuleList {
	
	// Ideally this would be the ModuleMetaData (so we can pull desc, image, name, handle)
	private List<PImage> moduleImages;
	private String[] packageNames;
	
	public ModuleList(List<PImage> icons, String[] packageNames) {
		moduleImages = icons;
		this.packageNames = packageNames;
	}
	
	public int size() {
		return moduleImages.size();
	}
	
	public PImage getModuleImage(int index) {
		return moduleImages.get(index);
	}
	

}
