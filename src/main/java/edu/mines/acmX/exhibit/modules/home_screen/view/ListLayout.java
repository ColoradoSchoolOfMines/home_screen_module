package edu.mines.acmX.exhibit.modules.home_screen.view;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import edu.mines.acmX.exhibit.modules.home_screen.Orientation;

public class ListLayout extends DisplayElement {

	private Orientation orientation;
	private List<DisplayElement> elements;
	private double totalWeight;
	// ratio width:height ex 3 = 3px width : 1px height
	private double ratio;
	private int viewLength;
	
	public ListLayout(Orientation ori, PApplet par, double scale, double weight, double ratio) {
		super(par, scale, weight);
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

	@Override
	public void update(int x, int y) {
		int xTemp = x;
		int yTemp = y;
		if (orientation == Orientation.HORIZONTAL) {
			int spacing = (int) (height * ratio);
			for (DisplayElement element: elements) {
				element.setWidth(spacing);
				element.setHeight(height);
				element.update(xTemp, yTemp);
				xTemp += spacing;
			}
		}
		else if (orientation == Orientation.VERTICAL) {
			int spacing = (int) (width * ratio); {
				for (DisplayElement element: elements) {
					element.setHeight(spacing);
					element.setWidth(width);
					element.update(xTemp, yTemp);
					yTemp += spacing;
				}
			}
		}
		
	}

	@Override
	public void draw() {
		if (orientation == Orientation.HORIZONTAL) {
			for (DisplayElement element: elements) {
				if (element.originX - viewLength > this.originX &&
					element.width + element.originX - viewLength < this.originX + this.height) {
					element.setOriginX(element.getOriginX() - viewLength);
					element.setOriginY(element.getOriginY() - viewLength);
					element.draw();
				}
			}
		}
		else if (orientation == Orientation.VERTICAL) {
			// TODO
		}
	}

}






