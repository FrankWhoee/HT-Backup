package bot.HT.HT_Backup;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import com.google.gson.*;

public class Spreadsheet {
	
	public static JsonArray objJson;
	public static ArrayList<Player> players = new ArrayList<>();
	public static ArrayList<Player> prevPlayers = new ArrayList<>();
	
	public Spreadsheet() {
		updateLocalSheet();
	}
	
	public void updateLocalSheet() {
		App.cloudExec("gsjson "+Ref.spreadsheetId+" >> sign-ups.json -s SpreadSheet_API-96ed44bef6bb.json");
		App.cloudExec("chmod +r " + Ref.storageName + "/sign-ups.json");
		String json = App.cloudExec("cat " + Ref.storageName + "/sign-ups.json");
		objJson = new JsonParser().parse(json).getAsJsonArray();
	}

	public ArrayList<Player> scanSheet() {
		ArrayList<Player> sps = new ArrayList<>();
		ArrayList<Player> newPlayers = new ArrayList<Player>();
		for(JsonElement element : objJson){
			JsonObject e = element.getAsJsonObject();
			
			String timestamp = e.get("timestamp").getAsString();
			String email = e.get("emailAddress").getAsString();
			boolean participation = e.get("participation").getAsString() == "Yes" ? true : false;
			String language = e.get("language").getAsString();
			String username = e.get("username").getAsString();
			String area = e.get("area").getAsString();
			int rank = e.get("rank").getAsInt();
			String education = e.get("education").getAsString();
			String yearsOfCoding = e.get("yearsofcoding").getAsString();
			String attendance = e.get("attendance").getAsString();
			boolean submitAgree = e.get("submitagree").getAsString() == "Yes" ? true : false;
			boolean maliciousAgree = e.get("maliciousagree").getAsString() == "Yes" ? true : false;
			boolean originalAgree = e.get("originalagree").getAsString() == "Yes" ? true : false;
			boolean joinAgree = e.get("joinagree").getAsString() == "Yes" ? true : false;
			
			Player p = new Player(timestamp,email, participation,language,username, area,
					rank,education,yearsOfCoding, attendance, submitAgree,
					maliciousAgree, originalAgree, joinAgree);
			
			sps.add(p);
			if(!prevPlayers.contains(p)) {
				newPlayers.add(p);
			}
		}
		prevPlayers = players;
		players = sps;
		return newPlayers;
	}
}
