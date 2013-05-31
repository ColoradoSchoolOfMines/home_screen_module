package edu.mines.acmX.exhibit.modules.home_screen;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import edu.mines.acmX.exhibit.modules.home_screen.view.DisplayElement;

public class ListLayout extends DisplayElement {

	private Orientation orientation;
	private List<DisplayElement> elements;
	private double totalWeight;
	
	public ListLayout(Orientation ori, PApplet par, double scale, double weight) {
		super(par, scale, weight);
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

	@Override
	public void update(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}

}
