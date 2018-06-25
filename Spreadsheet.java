package bot.HT.HT_Backup;


import java.util.ArrayList;
import com.google.gson.*;

public class Spreadsheet {
	
	public static JsonArray objJson;
	public static ArrayList<Player> players = new ArrayList<>();
	public static ArrayList<Player> prevPlayers = new ArrayList<>();
	
	public Spreadsheet() {
		updateLocalSheet();
	}
	
	public void updateLocalSheet() {
		for(int i = 1; i <= 50; i++) {
			App.executeCommand("rm sign-ups.json");
			App.cloudExec("gsjson "+Ref.spreadsheetId+" >> HT-Backup/sign-ups.json -s HT-Backup/SpreadSheet_API-96ed44bef6bb.json");
			App.cloudExec("chmod +r HT-Backup/sign-ups.json");
			String json = App.cloudExec("cat HT-Backup/sign-ups.json");
			try {
				objJson = new JsonParser().parse(json).getAsJsonArray();
				break;
			}catch(Exception e) {
				System.err.println("Spreadsheet failed to download. Trying again... (Attempt " + i + ")");
			}
		}
		
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
			int rank = -1;
			try {
				rank = e.get("rank").getAsInt();
			}catch(Exception E) {
				continue;
			}
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
			boolean contains = false;
			for(Player old : prevPlayers) {
				if(old.username.equals(p.username)) {
					contains = true;
					break;
				}
			}
			if(!contains) {
				newPlayers.add(p);
			}
		}
		prevPlayers = players;
		players = sps;
		return newPlayers;
	}
}
