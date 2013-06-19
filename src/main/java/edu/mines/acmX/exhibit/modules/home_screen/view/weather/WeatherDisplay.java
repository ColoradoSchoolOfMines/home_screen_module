package edu.mines.acmX.exhibit.modules.home_screen.view.weather;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	
	private static Logger logger = LogManager.getLogger(WeatherDisplay.class);

    //number of pixels to move away from edge
    public static int INITIAL_OFFSET = 10;
    //number of pixels between elements
    public static int PADDING = 20;

    public static float TEMP_RATIO = 0.20f;
    public static float DESCRIPTION_RATIO = 0.20f;
    public static float WIND_RATIO = 0.60f;
    public static float TEXT_RATIO = 0.075f;

	//time (in minutes) to reload weather information
	public static final int TIME_TO_REFRESH = 10;
	//stores current weather information (main display)
	private WeatherCurrentInfo currentInfo;
	//stores forecasted data (currently unused)
	private List<WeatherForecastDayInfo> forecastInfo;
	//stores current weather icon
	private PImage img;
	//stores all icons for the forecasted days
	private List<PImage> forecastImgs;
	//stores millis, to check when to refresh the weather data
	private int lastUpdate;
    private int textSize;

	public WeatherDisplay(PApplet parent, double weight, int textSize) {
		super(parent, weight);
		lastUpdate = parent.millis();
		WeatherLoader.loadWeatherInfo();
		currentInfo = WeatherLoader.getCurrentInfo();
		forecastInfo = WeatherLoader.getForecastInfo();
		forecastImgs = new ArrayList<PImage>();
		img = parent.loadImage(currentInfo.getPicture());
		for (int i = 0; i < forecastInfo.size(); ++i) {
			forecastImgs.add(parent.loadImage(forecastInfo.get(i).getPicture()));
		}
        this.textSize = textSize;
	}

	@Override
	public void update(int x, int y) {
		originX = x;
		originY = y;
		//updates weather info after a set number of minutes (TIME_TO_REFRESH)
		if (parent.millis() - lastUpdate > TIME_TO_REFRESH * 60000) {
			lastUpdate = parent.millis();
			WeatherLoader.loadWeatherInfo();
			currentInfo = WeatherLoader.getCurrentInfo();
			forecastInfo = WeatherLoader.getForecastInfo();
			//if there is a connected image, load images
			if (!currentInfo.getPicture().equals("noConnection.png")) {
				img = parent.loadImage(currentInfo.getPicture());
				forecastImgs.clear();
				for (int i = 0; i < forecastInfo.size(); ++i) {
					//make sure this works
					forecastImgs.add(parent.loadImage(forecastInfo.get(i).getPicture()));
				}
			}
		}
	}

	@Override
	public void draw() {
		//grey background rectangle
		parent.fill(84, 84, 84);
		parent.noStroke();
		parent.rect(originX, originY, width, height);

        // Setup the strings to display
		char deg = "\u00b0".toCharArray()[0]; //degree symbol in Unicode
		String temps = currentInfo.getTempF() + "" + deg + "F (" + currentInfo.getTempC() + "" + deg + "C)";
		String description = currentInfo.getDescription();
		String windString = "Wind Speed: " + currentInfo.getWindspeed() + " mph   Humidity: " + currentInfo.getHumidity() + "%";

        // setup the text alignment
		parent.textAlign(PApplet.LEFT, PApplet.CENTER);
        // TODO change sizing

		parent.textSize(textSize);

		//off-white text
		parent.fill(200, 200, 200);

        float pictureWidth = height; //make the image square
        float remainingWidthAfterImage = width - pictureWidth;
        float tempWidth = TEMP_RATIO * remainingWidthAfterImage;
        float descriptionWidth = DESCRIPTION_RATIO * remainingWidthAfterImage;
        float windWidth = WIND_RATIO * remainingWidthAfterImage;
        float currentX  = originX + INITIAL_OFFSET;

		parent.text(temps, currentX, originY, tempWidth, height);
        currentX += tempWidth + PADDING;

		parent.imageMode(PApplet.CORNERS);
		if( img != null ) { 
			parent.image(img, currentX,	originY, currentX + pictureWidth, originY + height);
		} else {
			logger.warn("Could not load weather icon");
		}

        currentX += pictureWidth + PADDING;
		parent.text(description, currentX, originY, descriptionWidth, height);
        currentX += descriptionWidth + PADDING;


		parent.imageMode(PApplet.CORNER);
		parent.text(windString, currentX, originY, windWidth, height );
	}


}
