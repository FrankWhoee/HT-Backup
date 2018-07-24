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
		String json = ""; 
		int attempts = 25;
		for(int i = 1; i <= attempts; i++) {
			App.executeCommand("rm sign-ups.json");
			App.cloudExec("gsjson "+Ref.spreadsheetId+" >> HT-Backup/sign-ups.json -s HT-Backup/SpreadSheet_API-96ed44bef6bb.json");
			App.cloudExec("chmod +r HT-Backup/sign-ups.json");
			json = App.cloudExec("cat HT-Backup/sign-ups.json");
			try {
				objJson = new JsonParser().parse(json).getAsJsonArray();
				break;
			}catch(Exception e) {
				System.err.println("Spreadsheet failed to download. Trying again... (Attempt " + i + ")");
			}
		}
		try {
			objJson = new JsonParser().parse(json).getAsJsonArray();
		}catch(Exception e) {
			System.err.println("Spreadsheet failed to download after " + attempts + " attempts.");
		}
		
	}

	public ArrayList<Player> scanSheet() {
		ArrayList<Player> sps = new ArrayList<>();
		ArrayList<Player> newPlayers = new ArrayList<Player>();
		for(JsonElement element : objJson){
			
			JsonObject e = element.getAsJsonObject();
			
			String timestamp = e.get("timestamp").getAsString();
			String email = e.get("emailAddress").getAsString();
			boolean participation = e.get("participation").getAsString().equals("Yes") ? true : false;
			
			String username = e.get("username").getAsString();
			boolean submitAgree = e.get("submitagree").getAsString().equals("Yes") ? true : false;
			boolean maliciousAgree = e.get("maliciousagree").getAsString().equals("Yes") ? true : false;
			boolean originalAgree = e.get("originalagree").getAsString().equals("Yes") ? true : false;
			boolean surveyAnswered = e.get("surveyanswered").getAsString().equals("Yes") ? true : false;
			
			int rank = -1;
			String language = null;
			String area = null;
			String education = null;
			String yearsOfCoding = null;
			String attendance = null;
			if(surveyAnswered) {
				language = e.get("language").getAsString();
				area = e.get("area").getAsString();
				education = e.get("education").getAsString();
				yearsOfCoding = e.get("yearsofcoding").getAsString();
				attendance = e.get("attendance").getAsString();
				boolean halite2particip = e.get("halite2particip").getAsString().equals("Yes") ? true : false;
				if(halite2particip) {
					rank = e.get("rank").getAsInt();
				}
			}

			Player p = new Player(timestamp,email, participation,language,username, area,
					rank,education,yearsOfCoding, attendance, submitAgree,
					maliciousAgree, originalAgree, surveyAnswered);
			
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
