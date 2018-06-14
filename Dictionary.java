package bot.HT.HT_Backup;


import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.io.IOUtils;
import com.google.gson.*;


public class Dictionary {
    private final static String googleDictURL = "http://dictionaryapi.herokuapp.com/?define=";
    
    private final static ArrayList<String> wordTypes = new ArrayList<>(Arrays.asList("noun","adjective", "verb","adverb","pronoun","preposition", "conjunction", "determiner", "exclamation"));
    
	public Dictionary() {}
	
	public static String define(String word) throws Exception {
		URL url = new URL(googleDictURL + word.toLowerCase());
		URLConnection con = url.openConnection();
		InputStream in = con.getInputStream();
		String encoding = con.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		JsonObject entireJSON = new JsonParser().parse(body).getAsJsonObject();
		
		String definition = "__**" + word.substring(0,1).toUpperCase() + word.substring(1) + ":**__\n";
		
		for(String type : wordTypes) {
			String def = "";
			String example = "";
			try {
				def = entireJSON.get(type).getAsJsonArray().get(0).getAsJsonObject().get("definition").getAsString();
				example = entireJSON.get(type).getAsJsonArray().get(0).getAsJsonObject().get("example").getAsString();
				definition += type.substring(0,1).toUpperCase() + type.substring(1) + ":\n**" + def.substring(0,1).toUpperCase() + def.substring(1) + "**\nExample: *" + example.substring(0,1).toUpperCase() + example.substring(1) + "*\n\n";
				System.out.println("[SUCCESS]: " + type);
			}catch(Exception e) {
				System.out.println("[ERROR]: " + type);
			}
			
		}
		
		
        return definition;
        		

	}
	
	public static String getUrban(String word, int defNum) throws Exception{
		URL url = new URL("http://api.urbandictionary.com/v0/define?term=" + word);
		URLConnection con = url.openConnection();
		InputStream in = con.getInputStream();
		String encoding = con.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		JsonObject entireJSON = new JsonParser().parse(body).getAsJsonObject();
		JsonArray list = new JsonParser().parse(entireJSON.get("list").getAsJsonArray().toString()).getAsJsonArray();
		
		if(!(list.size() > 0)){
			return "That word is so wild, even the Urban Dictionary doesn't have it!";
		}
		
		if(defNum > list.size()) {
			return "The definition number you entered is too high! Use something between 0 and " + (list.size() - 1);
		}
		
		JsonObject json = list.get(defNum).getAsJsonObject();
		JsonArray suggestions = entireJSON.get("tags").getAsJsonArray();
		
		String definition = json.get("definition").getAsString();
		definition = definition.substring(0, Math.min(1999, definition.length()));
		//definition = definition.replaceAll("[", "");
		//definition = definition.replaceAll("]", "");
		String author = json.get("author").getAsString();
		String actualWord = json.get("word").getAsString();
		actualWord = actualWord.substring(0, 1).toUpperCase() + actualWord.substring(1);
		int thumbsUp = json.get("thumbs_up").getAsInt();
		int thumbsDwn = json.get("thumbs_down").getAsInt();
		String example = json.get("example").getAsString();
		
		String suggest = "";
		int i = 0;
		String prev = "4tlgja3-";
		for(JsonElement suggestion : suggestions) {
			if(i < 4 && !prev.equals(suggestion.getAsString())) {
				suggest += suggestion.getAsString() + ", ";
				i++;
				prev = suggestion.getAsString();
			}
			
		}		
		
		try {
			suggest = suggest.substring(0, suggest.length() - 2);
		}catch(Exception e) {
			suggest = "No suggestions to show!";
		}
		
		
		String output = "**" + actualWord + ":**"
						+ "\n" + ":thumbsup:`" + thumbsUp + "`   :thumbsdown:`" + thumbsDwn + "`\n"
						+ "\n**Definition**: " + definition + "\n"
								+ "\n**Example**: " + example + "\n"
										+ "\n**Suggestions: ** " + suggest;
		
		if(output.length() > 1999) {
			int k = definition.length();
			while(output.length() > 1999) {
				definition = definition.substring(0, Math.min(k, definition.length()));
				output = "**" + actualWord + ":**"
						+ "\n" + ":thumbsup:`" + thumbsUp + "`   :thumbsdown:`" + thumbsDwn + "`\n"
						+ "\n**Definition**: " + definition + "...\n"
								+ "\n**Example**: " + example + "\n"
										+ "\n**Suggestions: ** " + suggest;
				k--;
			}
		}
		
		return output;

	}
	
}
