package edu.mines.acmX.exhibit.modules.home_screen.view;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import edu.mines.acmX.exhibit.modules.home_screen.Orientation;

public class LinearLayout extends DisplayElement {
	
	private Orientation orientation;
	private List<DisplayElement> elements;
	private double totalWeight;
	
	public LinearLayout( Orientation ori, PApplet parent, double scale, double weight) {
		super(parent, scale, weight);
		this.orientation = ori;
		this.elements = new ArrayList<DisplayElement>();
		this.totalWeight = 0;
	}
	
	public void add (DisplayElement element) {
		elements.add(element);
		totalWeight += element.getWeight();
	}
	
	public void pop() {
		totalWeight -= elements.get(elements.size() - 1).getWeight();
		elements.remove(elements.size() - 1);
	}
	
	public void update(int x, int y) {
		originX = x;
		originY = y;
		int xTemp = x; 
		int yTemp = y;
		if (orientation == Orientation.HORIZONTAL) {
			for (DisplayElement element: elements) {
				int spacing = (int) ((element.getWeight() / totalWeight) * width);
				element.setWidth(spacing);
				element.setHeight(height);
				element.update(xTemp, yTemp);
				xTemp += spacing;
			}
		} 
		else if (orientation == Orientation.VERTICAL) {
			for (DisplayElement element: elements) {
				int spacing = (int) ((element.getWeight() / totalWeight) * height);
				element.setHeight(spacing);
				element.setWidth(width);
				element.update(xTemp, yTemp);
				yTemp += spacing;
			}
		}
	}

	@Override
	public void draw() {
		for (DisplayElement element: elements) {
			element.draw();
		}
	}
	
	
}
