package edu.mines.acmX.exhibit.modules.home_screen.view.weather;

import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;
import edu.mines.acmX.exhibit.modules.home_screen.view.DisplayElement;

/**
 * This is the main class for the Weather display at the bottom of the home
 * screen. It uses the WeatherLoader to pull data from an online XML and parses
 * accordingly. On the main screen, it's tiled with the time display. Polls
 * for new weather information every 10 minutes.
 * 
 * TODO use forecasting data in the expanded weather dialog
 * 
 * @author Ryan Stauffer
 * @author Matthew Stech
 * 
 * @see {@link WeatherLoader} {@link WeatherCurrentInfo} {@link WeatherForecastDayInfo}
 */
public class WeatherDisplay extends DisplayElement {

	//time (in minutes) to reload weather information
	public static final int TIME_TO_REFRESH = 10;
	//stores current weather information (main display)
	private WeatherCurrentInfo currentInfo;
	//stores forecasted data (currently unused)
	private List<WeatherForecastDayInfo> forecastInfo;
	private PImage img;
	//stores millis, to check when to refresh the weather data
	private int lastUpdate;
	
	public WeatherDisplay(PApplet parent, double weight) {
		super(parent, weight);
		lastUpdate = parent.millis();
		WeatherLoader.loadWeatherInfo();
		currentInfo = WeatherLoader.getCurrentInfo();
		forecastInfo = WeatherLoader.getForecastInfo();
	}

	@Override
	public void update(int x, int y) {
		originX = x;
		originY = y;
		//updates weather info every 10 minutes
		if (parent.millis() - lastUpdate > TIME_TO_REFRESH * 60000) {
			lastUpdate = parent.millis();
			WeatherLoader.loadWeatherInfo();
			currentInfo = WeatherLoader.getCurrentInfo();
			forecastInfo = WeatherLoader.getForecastInfo();
		}
		//System.out.println(currentInfo.getPicture());
		//TODO currently overriding Processing's loadImage- this won't work for a URL path
		//img = loadImage("http://www.worldweatheronline.com/images/wsymbols01_png_64/wsymbol_0002_sunny_intervals", "png");
		//System.out.println(img == null);

	}

	@Override
	public void draw() {
		//grey background rectangle
		parent.fill(84, 84, 84);
		parent.rect(originX, originY, width, height);
		//TODO find degree symbols for temperatures
		String temps = currentInfo.getTempF() + " deg F (" + currentInfo.getTempC() + " deg C)";
		String description = currentInfo.getDescription();
		String windString = "Wind Speed: " + currentInfo.getWindspeed() + " mph    Humidity: " + currentInfo.getHumidity() + "%";
		parent.textAlign(PApplet.CENTER, PApplet.CENTER);
		parent.fill(0, 0, 0);
		parent.textSize(32);
		parent.text(temps, originX + width/8, originY + height/2);
		parent.imageMode(PApplet.CENTER);
		//parent.image(img, originX + width/2, originY + height/2, width/8, height/2);
		parent.imageMode(PApplet.CORNER);
		parent.text(description, originX + width/3, originY + height/2);
		parent.text(windString, originX + width *3/4, originY + height/2);
	}

	
}
