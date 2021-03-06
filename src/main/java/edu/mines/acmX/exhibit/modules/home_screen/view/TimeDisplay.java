package edu.mines.acmX.exhibit.modules.home_screen.view;

import processing.core.PApplet;

/**
 * A simple display that can display the time and date. 
 * Paired with the Weather Display.
 * 
 * @author Ryan Stauffer
 * @author Matthew Stech
 */
public class TimeDisplay extends DisplayElement {

    public static int PADDING = 10;

	private int hour;
	private int minute;
	private int second;
	private int day;
	private int month;
	private int year;
    private int textSize;
	
	public TimeDisplay(PApplet parent, double weight, int textSize) {
		super(parent, weight);
		//load the time
		hour = PApplet.hour();
		minute = PApplet.minute();
		second = PApplet.second();
		
		//load the date
		day = PApplet.day();
		month = PApplet.month();
		year = PApplet.year();

        // set the text size
        this.textSize = textSize;
	}
	/**
	 * Update, called continuously before draw
	 */
	@Override
	public void update(int x, int y) {
		//update layout position
		originX = x;
		originY = y;
		//update time and date
		hour = PApplet.hour();
		minute = PApplet.minute();
		second = PApplet.second();
		
		day = PApplet.day();
		month = PApplet.month();
		year = PApplet.year();
	}

	/**
	 * draw, called continuously
	 */
	@Override
	public void draw() {
		//draw grey rectangle behind text
		parent.fill(84, 84, 84);
		parent.noStroke();
		parent.rect(originX, originY, width, height);
		parent.fill(200, 200, 200);
		//center-align text
		parent.textAlign(PApplet.RIGHT, PApplet.CENTER);
		parent.textSize(textSize);
		//add a 0 to minute/second if it's between 0 and 9
		String minuteString = "" + PApplet.nf(minute, 2);
		String secondString = "" + PApplet.nf(second, 2);
		//concatenate time string
		String time = hour + ":" + minuteString + ":" + secondString;
		//concatenate date
		String date = month + "/" + day + "/" + year;
		parent.text(time + "  " + date, originX, originY, width - PADDING, height);
	}

}
