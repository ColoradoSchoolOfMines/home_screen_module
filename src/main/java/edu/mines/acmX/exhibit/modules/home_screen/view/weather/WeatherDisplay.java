package edu.mines.acmX.exhibit.modules.home_screen.view.weather;

import java.util.List;

import processing.core.PApplet;
import edu.mines.acmX.exhibit.modules.home_screen.view.DisplayElement;

public class WeatherDisplay extends DisplayElement {

	public static final int TIME_TO_REFRESH = 10;
	private WeatherCurrentInfo currentInfo;
	private List<WeatherForecastDayInfo> forecastInfo;
	
	public WeatherDisplay(PApplet parent, double weight) {
		super(parent, weight);
		WeatherLoader.loadWeatherInfo();
		currentInfo = WeatherLoader.getCurrentInfo();
		forecastInfo = WeatherLoader.getForecastInfo();
	}

	@Override
	public void update(int x, int y) {
		originX = x;
		originY = y;
		if (PApplet.minute() % TIME_TO_REFRESH == 0) {
			WeatherLoader.loadWeatherInfo();
			currentInfo = WeatherLoader.getCurrentInfo();
			forecastInfo = WeatherLoader.getForecastInfo();
		}
	}

	@Override
	public void draw() {
		

	}

	
}
