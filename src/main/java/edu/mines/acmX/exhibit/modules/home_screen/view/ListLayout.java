package edu.mines.acmX.exhibit.modules.home_screen.view;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import edu.mines.acmX.exhibit.modules.home_screen.Orientation;

public class ListLayout extends DisplayElement {

	private static final boolean DEBUG = true;
	private Orientation orientation;
	private List<DisplayElement> elements;
	// TODO should the weight of an element have an effect on how the ratio works?
	private double totalWeight;
	// ratio width:height ex 3 = 3px width : 1px height
	private double ratio;
	private int viewLength;
	
	public ListLayout(Orientation ori, PApplet par, double weight, double ratio) {
		super(par, weight);
		this.orientation = ori;
		this.elements = new ArrayList<DisplayElement>();
		this.totalWeight = 0;
		this.ratio = ratio;
		this.viewLength = 0;
	}
	
	public void add (DisplayElement element) {
		elements.add(element);
		totalWeight += element.getWeight();
	}
	
	public void pop() {
		totalWeight -= elements.get(elements.size() - 1).getWeight();
		elements.remove(elements.size() - 1);
	}

	public int size() {
		return elements.size();
	}

	@Override
	public void update(int x, int y) {
		originX = x;
		originY = y;
		int xTemp = x;
		int yTemp = y;
		// TODO debug line, remove
		if (orientation == Orientation.HORIZONTAL) {
			int spacing = (int) (height * ratio);
			float totalLength = spacing * elements.size();
			xTemp -= viewLength;
			for (DisplayElement element: elements) {
				while(xTemp >= totalLength) {
					xTemp -= totalLength;
				}
				while(xTemp < 0) {
					xTemp += totalLength;
				}
				element.setWidth(spacing);
				element.setHeight(height);
				element.update(xTemp, yTemp);
				xTemp += spacing;
			}
		}
		else if (orientation == Orientation.VERTICAL) {
			// TODO use viewLength
			int spacing = (int) (width * ratio); 
			float totalHeight = spacing * elements.size();
			for (DisplayElement element: elements) {
				while(yTemp >= totalHeight) {
					yTemp -= totalHeight;
				}
				while(yTemp < 0) {
					yTemp += totalHeight;
				}
				element.setHeight(spacing);
				element.setWidth(width);
				element.update(xTemp, yTemp);
				yTemp += spacing;
			}
		}
		
	}

	@Override
	public void draw() {
		if (DEBUG) {
			parent.noFill();
			parent.stroke(0);
			parent.strokeWeight(1);
			parent.rect(originX, originY, width, height);
			parent.noStroke();
			parent.fill(0);
		}
		if (orientation == Orientation.HORIZONTAL) {
			for (DisplayElement element: elements) {
				if (element.originX  > this.originX &&
					element.originX + element.width < this.originX + this.width) {
					element.draw();
				}
				/*
				else if (element.originX < this.originX &&
						element.originX + element.width > this.originX) {
					ModuleElement tempElement = (ModuleElement) element;
					tempElement.setLeft(true);
					tempElement.setEdgeLength(originX - element.getOriginX());
					tempElement.draw();
				} */
			}
		}
		else if (orientation == Orientation.VERTICAL) {
			// TODO
		}
	}

	public void incrementViewLength(int inc) {
		viewLength += inc;
	}
}






