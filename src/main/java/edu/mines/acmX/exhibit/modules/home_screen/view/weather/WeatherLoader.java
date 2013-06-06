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

/**
 * The loader for the Weather display. This polls an external URL and parses 
 * an XML file for various weather data, i.e. temperature, wind, etc. 
 * Stores data in container classes. 
 * 
 * @author Ryan Stauffer
 * @author Matthew Stech
 *
 *@see {@link WeatherDisplay} {@link WeatherCurrentInfo} {@link WeatherForecastDayInfo}
 */
public class WeatherLoader {
	
	//API key- don't know how it's generated
	private static final String API_KEY = "b665b08214224103123010";
	//ZIP code to be checked
	private static final String ZIP = "80401";
	//main site for getting information
	private static final String BASE_URL = "http://free.worldweatheronline.com/feed/weather.ashx";
	//number of forecast days to collect
	private static final String NUM_DAYS = "5";
	
	private static WeatherCurrentInfo currentInfo;
	private static List<WeatherForecastDayInfo> forecastInfo;
	
	/**
	 * Static method. How the WeatherDisplay will gather all data for display.
	 * Only function to be called externally (in WeatherDisplay)
	 */
	public static void loadWeatherInfo() {
		//instantiate list if it doesn't exist
		if (forecastInfo == null) {
			forecastInfo = new ArrayList<WeatherForecastDayInfo>();
		} else {
			//dump the current info if update is called
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
	
	/**
	 * The main function for parsing the XML once the document has been 
	 * generated. 
	 * 
	 * @param doc the document generated by the URL InputStream
	 */
	private static void parseInfo(Document doc) {
		//enter data main tag
		Element data = (Element) doc.getElementsByTagName("data").item(0);
		//get current condition tag
		Element currentTag = (Element) data.getElementsByTagName("current_condition").item(0);
		//get forecast weather tags
		NodeList forecastList = data.getElementsByTagName("weather");
		//parse the current tag
		parseCurrentCondition(currentTag);
		for (int i = 0; i < forecastList.getLength(); ++i) {
			Element weatherForecast = (Element) forecastList.item(i);
			//parse each forecast tag
			forecastInfo.add(parseWeatherTag(weatherForecast));
		}
	}
	/**
	 * Takes the current_condition tag and parses the relevant info. 
	 * Stores in WeatherCurrentInfo format. 
	 * 
	 * @param currentTag the <current_condition> tag being parsed
	 * 
	 * @see {@link WeatherCurrentInfo}
	 */
	private static void parseCurrentCondition(Element currentTag) {
		int tempC = Integer.parseInt(parseTag("temp_C", currentTag));
		int tempF = Integer.parseInt(parseTag("temp_F", currentTag));
		String picture = (parseTag("weatherIconUrl", currentTag));
		String description = (parseTag("weatherDesc", currentTag));
		int windspeed = Integer.parseInt(parseTag("windspeedMiles", currentTag));
		double precipitation = Double.parseDouble(parseTag("precipMM", currentTag));
		int humidity = Integer.parseInt(parseTag("humidity", currentTag));
		
		//currentInfo is stored locally and uses a getter to be shared
		currentInfo = new WeatherCurrentInfo(tempF, tempC, picture, description, windspeed, precipitation, humidity);
		
	}
	
	/**
	 * Parses one forecast <weather> tag. 
	 * 
	 * @param weatherTag the <weather> tag to be parsed
	 * @return one WeatherForecastDayInfo object
	 * 
	 * @see {@link WeatherForecastDayInfo}
	 */
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
	
	/**
	 * Searches the given element for a tag, and returns its value
	 * @param tagName the tag name with desired data
	 * @param e the element that has the tag within it
	 * @return a String of data
	 */
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
