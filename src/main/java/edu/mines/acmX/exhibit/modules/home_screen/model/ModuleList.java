package edu.mines.acmX.exhibit.modules.home_screen.model;

import java.util.List;

import processing.core.PImage;
import edu.mines.acmX.exhibit.modules.home_screen.view.ModuleElement;

public class ModuleList {
	
	// Ideally moduleElements would store ModuleMetaData (so we can pull desc, image, name, handle.
	private List<ModuleElement> moduleElements;
	
	public ModuleList(List<ModuleElement> list) {
		moduleElements = list;
	}
	
	public int size() {
		return moduleElements.size();
	}
	
	public PImage getModuleImage(int index) {
		return moduleElements.get(index).getIcon();
	}
	

}
