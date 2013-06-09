package edu.mines.acmX.exhibit.modules.home_screen.backdrops;

import edu.mines.acmX.exhibit.modules.home_screen.HomeScreen;
import processing.core.PApplet;


/**
 * The abstract root for designing backdrops. Any backdrop can be added
 * to the main screen for use as an active background behind the other features
 * of the home screen.
 * 
 * @author Aakash Shah
 * @author Matthew Stech
 *
 */
public abstract class Backdrop {
	//enables use of Processing functions
	protected PApplet parent;
	
	public Backdrop(HomeScreen par) {
		this.parent = par;
	}

	/**
	 * Standard update loop. Should be used for calculations or other gruntwork
	 * that is not direct drawing. 
	 */
	public abstract void update();
	/**
	 * Standard draw loop. Will be called regardless of activity. 
	 */
	public abstract void draw();
	
	/**
	 * Optional functionality, if more information could be drawn when
	 * inactive. Only called when home screen is faded. 
	 */
	public void alternateDrawFaded() {}
}
