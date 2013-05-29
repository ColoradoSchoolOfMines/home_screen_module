package edu.mines.acmX.exhibit.modules.home_screen.view;

public abstract class Scalable {
	protected double scale;
	
	public Scalable(double scale) {
		this.scale = scale;
	}
	
	protected double scale(double d) {
		return scale * d;
	}

}
