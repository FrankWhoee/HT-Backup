package bot.HT.HT_Backup;

import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class weatherAPI{
	
	public weatherAPI() {
		
	}
	
	public String getWeather(String city, String country, int reportNum) throws Exception{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new URL("http://api.openweathermap.org/data/2.5/forecast?q=burnaby&mode=xml&units=metric&appid=" + Key.weatherAPIKey).openStream());
		
		if(reportNum > (doc.getElementsByTagName("time").getLength() - 1) || reportNum < 0) {
			return ("Forecast report number must be within 0-" + (doc.getElementsByTagName("time").getLength() - 1));
		}
		
		if(country.equals("")) {
			doc = db.parse(new URL("http://api.openweathermap.org/data/2.5/forecast?q="+city+"&mode=xml&units=metric&appid=" + Key.weatherAPIKey).openStream());
		}else {
			doc = db.parse(new URL("http://api.openweathermap.org/data/2.5/forecast?q="+city+","+country+"&mode=xml&units=metric&appid=" + Key.weatherAPIKey).openStream());
		}
		String location = doc.getElementsByTagName("name").item(0).getTextContent();
		String iso3166 = doc.getElementsByTagName("country").item(0).getTextContent();
		
		String timeInit = doc.getElementsByTagName("time").item(reportNum).getAttributes().getNamedItem("from").getTextContent();
		String timeFinal = doc.getElementsByTagName("time").item(reportNum).getAttributes().getNamedItem("to").getTextContent();
		String time = " from " + timeInit + " to " + timeFinal;
		
		String weather = doc.getElementsByTagName("symbol").item(reportNum).getAttributes().getNamedItem("name").getTextContent();
		
		String windDirection = doc.getElementsByTagName("windDirection").item(reportNum).getAttributes().getNamedItem("name").getTextContent();
		double windSpeed = Double.parseDouble(doc.getElementsByTagName("windSpeed").item(reportNum).getAttributes().getNamedItem("mps").getTextContent()) * 1.6;
		
		double temp = Double.parseDouble(doc.getElementsByTagName("temperature").item(reportNum).getAttributes().getNamedItem("value").getTextContent());
		
		String humidity = doc.getElementsByTagName("humidity").item(reportNum).getAttributes().getNamedItem("value").getTextContent();
		
		String forecast = "Here is the weather report for " + location  + "," + iso3166 + time + ":"
				+ "\nWeather: " + weather.substring(0,1).toUpperCase() + weather.substring(1, weather.length())
						+ "\nThe wind is blowing " + windDirection + " at " + ("" + windSpeed).substring(0, 5) + " kilometers per hour, or " + ("" + (windSpeed/1.6)).substring(0,Math.min(5, ("" + (windSpeed/1.6)).length())) + " miles per hour."
								+ "\nThe temperature outside is " + temp + "°C or " + ((temp * 1.8) + 32) + "°F"
										+ "\nThe humidity is at " + humidity + "% today.";
		
		return forecast;
		
	}
	
}