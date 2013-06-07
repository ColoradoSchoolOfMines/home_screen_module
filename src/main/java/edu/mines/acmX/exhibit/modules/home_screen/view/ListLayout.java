package edu.mines.acmX.exhibit.modules.home_screen.view;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import edu.mines.acmX.exhibit.modules.home_screen.Orientation;

public class ListLayout extends DisplayElement {

	private static final boolean DEBUG = false;
	private Orientation orientation;
	private List<DisplayElement> elements;
	// TODO should the weight of an element have an effect on how the ratio works?
	private double totalWeight;
	// ratio width:height ex 3 = 3px width : 1px height
	private double ratio;
	private int viewLength;
	private int listSize;
	private int minShown;
	
	//default constructor
	public ListLayout(Orientation ori, PApplet par, double weight, double ratio) {
		super(par, weight);
		this.orientation = ori;
		this.elements = new ArrayList<DisplayElement>();
		this.minShown = minShown;
		this.totalWeight = 0;
		this.ratio = ratio;
		this.viewLength = 0;
		this.listSize = 0;
		this.minShown = 0;
	}
	
	//specialized constructor if the user wants to specify a minimum number of elements to display
	//TODO get the carosel to work when there's fewer than the min specified (eliminate pop-in)
	public ListLayout(Orientation ori, PApplet par, double weight, double ratio, int minShown) {
		super(par, weight);
		this.orientation = ori;
		this.elements = new ArrayList<DisplayElement>();
		this.minShown = minShown;
		this.totalWeight = 0;
		this.ratio = ratio;
		this.viewLength = 0;
		this.listSize = minShown;
	}
	
	public void add (DisplayElement element) {
		elements.add(element);
		totalWeight += element.getWeight();
		if(listSize == elements.size() - 1) {
			listSize++;
		}
	}
	
	public void pop() {
		totalWeight -= elements.get(elements.size() - 1).getWeight();
		elements.remove(elements.size() - 1);
		listSize--;
		if (elements.size() < minShown) {
			listSize = minShown;
		}
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

		if (orientation == Orientation.HORIZONTAL) {
			int spacing = (int) (height * ratio);
			float totalLength = spacing * listSize;
			xTemp -= viewLength;
			for (DisplayElement element: elements) {
				while(xTemp >= totalLength - spacing) {
					xTemp -= totalLength;
				}
				while(xTemp < this.originX - 2 * spacing) {
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
				if(element.originX < this.originX) {
					if(element.originX + element.width >= this.originX) {
						ModuleElement tempElement = (ModuleElement) element;
						tempElement.setLeft(true);
						tempElement.setEdgeLength(this.originX - element.originX);
						tempElement.draw();
					}
				}
				else {
					if(element.originX + element.width < this.originX + this.width) {
						element.draw();
					}
					else if(element.originX < this.originX + this.width) {
						ModuleElement tempElement  = (ModuleElement) element;
						tempElement.setRight(true);
						tempElement.setEdgeLength(element.originX + element.width - this.originX - this.width);
						tempElement.draw();
					}
				}
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






