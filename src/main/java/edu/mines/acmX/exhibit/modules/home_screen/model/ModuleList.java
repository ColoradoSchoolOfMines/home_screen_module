package edu.mines.acmX.exhibit.modules.home_screen.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import processing.core.PImage;

public class ModuleList {
	
	// Ideally this would be the ModuleMetaData (so we can pull desc, image, name, handle)
	private List<String> moduleImages;
	
	public ModuleList() {
		moduleImages = new ArrayList<String>();
		
		moduleImages.add("aardvark.jpg");
		moduleImages.add("giraffe_module.jpg");
		moduleImages.add("HumaHumaTrigger.jpg");
		moduleImages.add("hand_cursor.png");
	}
	
	public int size() {
		return moduleImages.size();
	}
	
	public String getModuleImageFilename(int index) {
		return moduleImages.get(index);
	}
	

}
