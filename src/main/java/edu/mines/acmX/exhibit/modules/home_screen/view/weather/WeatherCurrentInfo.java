package edu.mines.acmX.exhibit.modules.home_screen.view.weather;

public class WeatherCurrentInfo {

	private int tempF;
	private int tempC;
	private String picture;
	private String description;
	private int windspeed;
	private double precipitation;
	private int humidity;
	
	public WeatherCurrentInfo(int tempF, int tempC, String picture,
			String description, int windspeed, double precipitation,
			int humidity) {
		super();
		this.tempF = tempF;
		this.tempC = tempC;
		this.picture = picture;
		this.description = description;
		this.windspeed = windspeed;
		this.precipitation = precipitation;
		this.humidity = humidity;
	}
	
	public int getTempF() {
		return tempF;
	}
	
	public int getTempC() {
		return tempC;
	}
	
	public String getPicture() {
		return picture;
	}
	
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getWindspeed() {
		return windspeed;
	}
	
	public double getPrecipitation() {
		return precipitation;
	}
	
	public int getHumidity() {
		return humidity;
	}
	
	
}
