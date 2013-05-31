package edu.mines.acmX.exhibit.modules.home_screen.view;

import processing.core.PApplet;

public abstract class DisplayElement extends Scalable {

	protected PApplet parent;
	protected int originX;
	protected int originY;
	protected double weight;
	protected double scale;
	protected int width;
	protected int height;
	
	public DisplayElement(PApplet par, double scale, double weight, int width, int height) {
		super(scale);
		this.parent = par;
		this.weight = weight;
		this.scale = scale;
		this.width = width;
		this.height = height;
	}
	
	public abstract void update(int x, int y);
	public abstract void draw();
	
	public void setOriginX(int originX) {
		this.originX = originX;
	}

	public void setOriginY(int originY) {
		this.originY = originY;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public PApplet getParent() {
		return parent;
	}

	public int getOriginX() {
		return originX;
	}

	public int getOriginY() {
		return originY;
	}

	public double getWeight() {
		return weight;
	}
	
	public double scale(double d) {
		return super.scale(d);
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
