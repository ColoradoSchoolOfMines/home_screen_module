package edu.mines.acmX.exhibit.modules.home_screen.view.weather;

public class WeatherForecastDayInfo {
	
	//TODO use this in mouseover for weather
	private int minTempF;
	private int maxTempF;
	private int minTempC;
	private int maxTempC;
	private int windSpeed;
	private String picture;
	private String description;
	private double precipitation;
	private String date;
	
	public WeatherForecastDayInfo(int minTempF, int maxTempF, int minTempC,
			int maxTempC, int windSpeed, String picture, String description,
			double precipitation, String date) {
		super();
		this.minTempF = minTempF;
		this.maxTempF = maxTempF;
		this.minTempC = minTempC;
		this.maxTempC = maxTempC;
		this.windSpeed = windSpeed;
		this.picture = picture;
		this.description = description;
		this.precipitation = precipitation;
		this.date = date;
	}

	public int getMinTempF() {
		return minTempF;
	}

	public int getMaxTempF() {
		return maxTempF;
	}

	public int getMinTempC() {
		return minTempC;
	}

	public int getMaxTempC() {
		return maxTempC;
	}

	public int getWindSpeed() {
		return windSpeed;
	}

	public String getPicture() {
		return picture;
	}
	
	public String getDescription() {
		return description;
	}

	public double getPrecipitation() {
		return precipitation;
	}
	
	public String getDate() {
		return date;
	}
}
