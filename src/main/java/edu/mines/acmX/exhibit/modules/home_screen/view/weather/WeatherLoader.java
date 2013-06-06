package edu.mines.acmX.exhibit.modules.home_screen.view.weather;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WeatherLoader {
	
	private static final String API_KEY = "b665b08214224103123010";
	private static final String ZIP = "80401";
	private static final String BASE_URL = "http://free.worldweatheronline.com/feed/weather.ashx";
	private static final String NUM_DAYS = "5";
	
	private static WeatherCurrentInfo currentInfo;
	private static List<WeatherForecastDayInfo> forecastInfo;
	
	/**
	 * Static method. How the WeatherDisplay will gather all data for display.
	 */
	public static void loadWeatherInfo() {
		if (forecastInfo == null) {
			forecastInfo = new ArrayList<WeatherForecastDayInfo>();
		} else {
			forecastInfo.clear();
		}
		try {
			URL weatherURL = new URL(BASE_URL + "?key=" + API_KEY + "&q=" 
					+ ZIP + "&num_of_days=" + NUM_DAYS + "&format=xml");
			URLConnection weatherConnection = weatherURL.openConnection();
			InputStream weatherStream = weatherConnection.getInputStream();
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = docBuilder.parse(weatherStream);
			doc.getDocumentElement().normalize();
			parseInfo(doc);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void parseInfo(Document doc) {
		Element data = (Element) doc.getElementsByTagName("data").item(0);
		Element currentTag = (Element) data.getElementsByTagName("current_condition").item(0);
		NodeList forecastList = data.getElementsByTagName("weather");
		parseCurrentCondition(currentTag);
		for (int i = 0; i < forecastList.getLength(); ++i) {
			Element weatherForecast = (Element) forecastList.item(i);
			forecastInfo.add(parseWeatherTag(weatherForecast));
		}
	}
	
	private static void parseCurrentCondition(Element currentTag) {
		int tempC = Integer.parseInt(parseTag("temp_C", currentTag));
		int tempF = Integer.parseInt(parseTag("temp_F", currentTag));
		String picture = (parseTag("weatherIconUrl", currentTag));
		String description = (parseTag("weatherDesc", currentTag));
		int windspeed = Integer.parseInt(parseTag("windspeedMiles", currentTag));
		double precipitation = Double.parseDouble(parseTag("precipMM", currentTag));
		int humidity = Integer.parseInt(parseTag("humidity", currentTag));
		
		currentInfo = new WeatherCurrentInfo(tempF, tempC, picture, description, windspeed, precipitation, humidity);
		
	}
	
	private static WeatherForecastDayInfo parseWeatherTag(Element weatherTag) {
		String date = parseTag("date", weatherTag);
		int minTempF = Integer.parseInt(parseTag("tempMinF", weatherTag)); 
		int maxTempF = Integer.parseInt(parseTag("tempMaxF", weatherTag));
		int minTempC = Integer.parseInt(parseTag("tempMinC", weatherTag));
		int maxTempC = Integer.parseInt(parseTag("tempMaxC", weatherTag));
		int windSpeed = Integer.parseInt(parseTag("windspeedMiles", weatherTag));
		String picture = parseTag("weatherIconUrl", weatherTag);
		String description = parseTag("weatherDesc", weatherTag);
		double precipitation = Double.parseDouble(parseTag("precipMM", weatherTag));
		return new WeatherForecastDayInfo(minTempF, maxTempF, minTempC, maxTempC, windSpeed, picture, description, precipitation, date);
	}
	
	private static String parseTag(String tagName, Element e) {
		Element subEle = (Element) e.getElementsByTagName(tagName).item(0);
		return subEle.getTextContent();
	}

	public static WeatherCurrentInfo getCurrentInfo() {
		return currentInfo;
	}

	public static List<WeatherForecastDayInfo> getForecastInfo() {
		return forecastInfo;
	}
}
