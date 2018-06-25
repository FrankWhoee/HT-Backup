package bot.HT.HT_Backup;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Ref {
	
	
	
	//PREFIX
	public static final String prefix = ">";
	
	//VERSION NUMBER
	public static final String version = "HT-Backup 4.0.4";
	
	//USER IDs
	public static final ArrayList<Long> adminIds = new ArrayList(Arrays.asList(301294798155939840L,417573975347167233L,194857448673247235L,384452291295576065L,406568474979205120L,447530706445533184L));
	public static final long HTBotId = 417573975347167233L;
	public static final long vegasId = 419950820281417739L;
	
	//MISC.
	public static final String[] alphabet = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
	
	public static final String spreadsheetId = "17bMWQiC10jE-xhRv9Lc9WTBSkbKdmgBLi_Xe-7MHixI";
	public static final long VANCOUVERTOUTC = (long)2.52e+7; //UNIT: MILLESECONDS
	public static String inviteLink = "https://discord.gg/H9eYc4H";
	public static final DateFormat dateFormat = new SimpleDateFormat("yyy/MM/dd HH:mm:ss");
	public static final String logoURL = "https://cdn.discordapp.com/attachments/417732882828886019/446493666333294602/HaliteTournamentsPlanetsLogo.png";
	public static final String HTBackupLogoURL = "https://github.com/FrankWhoee/HT-Backup/raw/master/backupBotlogo.png";
	public static final String seasonNum = "III";
	public static final long signupScanInterval = (long)1.8e+6;
	
	//HT COLOURS
	public static final Color HTRed = new Color((float)1.0,(float)0.2666,(float)0.2666);
	public static final Color HTYellow = new Color(1,(float)0.68,0);
	public static final Color HTGreen = new Color(0,(float)0.71,0);
	
	//CHANNEL IDs
	public static final long generalChId = 411263442838880259L;
	public static final long logChId = 447828385834729502L;
	public static final long backupChId = 449407125467168768L;
	public static final long updateChId = 411265410651127809L;
	public static long submitChId = 446330102632939520L;
	public static long battlesChId = 446330102632939520L;
	public static long privateChId = 419831655503495178L;
	public static long dumpChId = 450829169564188742L;
	public static ArrayList<Long> privateChannels = new ArrayList(Arrays.asList(446494159553953802L, 419831655503495178L));
	
	//GUILD IDs
	public static ArrayList<Long> privateGuilds = new ArrayList(Arrays.asList(436768406952476683L, 417732882828886017L, 432695533010681856L));
	public static final long HTGuildId = 411263442838880256L;
	
	//ROLE IDs
	public static final long playerRoleId = 411263996189212680L;
	public static final Map<String,Long> langs = new HashMap<String,Long>(); 
	static {
		langs.put("Java",452604935994081311L);
		langs.put("Python",452604977823875092L);
		langs.put("C#",456309600669663233L);
		langs.put("C++",456309676376719360L);
		langs.put("Dart",456309702704234500L);
		langs.put("Go",456309739219845122L);
		langs.put("Haskell",456309763836215297L);
		langs.put("Javascript",456309784405082123L);
		langs.put("Ruby",456309811676577792L);
		langs.put("Rust",456309831624687628L);
	}
	
	
	//DEV CHANNEL IDs
	public static long devChId = 432695533010681859L;
	public static long dhallstrChId= 419272722753781762L;
	
	//CLOUD
	public static final String storageName = "HT-Backup-s3";
	public static final String previousStorageName = "HT-Backup-s2";
	public static final String vegasFile = "bets.json";
	public static final String backupLogFileName = "lastBackupTime.txt";
	public static long backupInterval = (long)6.048e+8; //UNIT: MILLESECONDS
	
	public static String getTime() {
		Date currDate = new Date();
		currDate = new Date((long)(currDate.getTime() + VANCOUVERTOUTC));
		String currentDate = dateFormat.format(currDate) + " UTC";
		return currentDate;
	}
	
	public static Date getTimeRaw() {
		Date currDate = new Date();
		currDate = new Date((long)(currDate.getTime() + VANCOUVERTOUTC));
		return currDate;
	}
	
	public static String getTime(String time) {
		long timeInMin = 0;
		try {
			timeInMin = Long.parseLong(time);
		}catch (Exception e) {
			timeInMin = 30;
		}
		long timeInMs = (timeInMin * 60000) + Ref.VANCOUVERTOUTC;
		Date currDate = new Date();
		currDate = new Date((long)(currDate.getTime() + (long)timeInMs));
		String currentDate = dateFormat.format(currDate) + " UTC";
		return currentDate;
	}
	
	
}
