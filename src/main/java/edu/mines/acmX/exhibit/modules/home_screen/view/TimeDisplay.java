package edu.mines.acmX.exhibit.modules.home_screen.view;

import processing.core.PApplet;

public class TimeDisplay extends DisplayElement {

	private int hour;
	private int minute;
	private int second;
	private int day;
	private int month;
	private int year;
	
	public TimeDisplay(PApplet parent, double weight) {
		super(parent, weight);
		hour = PApplet.hour();
		minute = PApplet.minute();
		second = PApplet.second();
		
		day = PApplet.day();
		month = PApplet.month();
		year = PApplet.year();
	}

	@Override
	public void update(int x, int y) {
		originX = x;
		originY = y;
		hour = PApplet.hour();
		minute = PApplet.minute();
		second = PApplet.second();
		
		day = PApplet.day();
		month = PApplet.month();
		year = PApplet.year();
	}

	@Override
	public void draw() {
		parent.fill(84, 84, 84);
		parent.rect(originX, originY, width, height);
		parent.fill(0, 0, 0);
		parent.textAlign(PApplet.CENTER, PApplet.CENTER);
		parent.textSize(32);
		String minuteString = "" + PApplet.nf(minute, 2);
		String secondString = "" + PApplet.nf(second, 2);
		String time = hour + ":" + minuteString + ":" + secondString;
		String date = day + "/" + month + "/" + year;
		parent.text(time + "     " + date, originX + width / 2, originY + height / 2);
	}

}
