package bot.HT.HT_Backup;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.*;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.bot.utils.*;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import java.lang.ProcessBuilder;

public class App extends ListenerAdapter
{
	
	static JDA jda;
	static Cloud cloud = new Cloud();
    public static void main( String[] args ) throws Exception
    {
    	
        jda = new JDABuilder(AccountType.BOT).setToken(Ref.token).buildBlocking();
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        //
        jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, "Dusting shelves..."));
        jda.addEventListener(new App());
        jda.getTextChannelById(433319633123147786L).sendMessage("`" + Ref.version + " STATUS: ONLINE`").queue();
    }
       
    @Override
    public void onMessageReceived(MessageReceivedEvent evt) {
    	//Objects
    	User objUser = evt.getAuthor();
    	MessageChannel objMsgCh= evt.getChannel();
    	Message objMsg = evt.getMessage();
    	Guild objGuild = evt.getGuild();
    	
	    	if(objMsg.getContentRaw().startsWith(Ref.prefix+"status")) {
	    		String input = objMsg.getContentRaw();
	    		String botName = input.substring(7);
	    		botName = botName.trim();
	    		if(botName.equalsIgnoreCase("HTBot")) {
	    			Member HTBot = getMemberById(objGuild,"" + Ref.HTBotId);
		    		objMsgCh.sendMessage(objUser.getAsMention() + "` HTBOT STATUS: " + HTBot.getOnlineStatus() + "`");
	    		}else if(botName.equalsIgnoreCase("Vegas")) {
	    			Member vegas = getMemberById(objGuild,"" + Ref.vegasId);
	    			objMsgCh.sendMessage(objUser.getAsMention() + "` VEGAS STATUS: " + vegas.getOnlineStatus() + "`").queue();
	    		}else if(botName.equalsIgnoreCase("HT-Backup") || botName.equalsIgnoreCase("")) {
	    			objMsgCh.sendMessage(objUser.getAsMention() + " `" + Ref.version + " STATUS: ONLINE`").queue();
	    		}else{
	    			objMsgCh.sendMessage("`Invalid bot. Try \">status HTBot\" or \">status Vegas\" or \">status HT-Backup\"`").queue();
	    		}
	    	}else if(objMsg.getContentRaw().startsWith(Ref.prefix + "help")) {
	    		objMsgCh.sendMessage("```Here are the functions you can use for HT-Backup:"
	    				+ "\n!submit: Submit your bot and HT-Backup will back up your file for you. ONLY usable in the channels #season2 and #battles."
	    				+ "\n>weather [CITY_NAME]: Check the weather in a certain city. Don't add the brackets in the command."
	    				+ "\n>retrieve: Use this command to retrieve your bot. This is only usable in a Direct Message/Private Channel with the bot."
	    				+ "\n>status [BOT_NAME]: Returns a bot's online status. Leave empty for default, which is HT-Backup."
	    				+ "\nMore functions to come!```").queue();
	    	}
	    	else if(objMsg.getContentRaw().startsWith(Ref.prefix + "init 0")) {
	    		long timeInMin = 0;
	    		try {
	    			timeInMin = Long.parseLong(objMsg.getContentRaw().substring(7).trim());
	    		}catch (Exception e) {
	    			timeInMin = 30;
	    		}
	    		long timeInMs = (timeInMin * 60000) + (long)2.52e+7;
	    		
	    		DateFormat dateFormat = new SimpleDateFormat("yyy/MM/dd HH:mm:ss");
	    		Date currDate = new Date();
	    		currDate = new Date((long)(currDate.getTime() + (long)timeInMs));
	    		String currentDate = dateFormat.format(currDate);
	    		if(Ref.adminIds.contains(objMsg.getAuthor().getIdLong())){
	    			objMsgCh.sendMessage("HT-Backup is going offline for "+timeInMin+" minutes. It will be back online at " + currentDate + " GMT").queue();
	    			objMsg.delete().queue();
		    		jda.shutdown();
	    		}
	    	}else if(objMsg.getContentRaw().startsWith(Ref.prefix +"echo")){
	    		String input = objMsg.getContentRaw();
	    		String echo = input.substring(6);
	    		objMsgCh.sendMessage(echo).queue();
	    		objMsg.delete().queue();
	    	}else if(objMsg.getContentRaw().startsWith("!submit")) {
	    		if(objMsgCh.getIdLong() == Ref.battlesChId || objMsgCh.getIdLong() == Ref.s2ChId) {
		    		Member HTBot = getMemberById(objGuild,"" + Ref.HTBotId);
		    		if(HTBot.getOnlineStatus() == OnlineStatus.ONLINE) {
		    			objMsgCh.sendMessage("Backing up your bot...").queue(message ->{
		    				MessageChannel logCh = jda.getTextChannelById(Ref.logChId);
		    				jda.getPresence().setStatus(OnlineStatus.ONLINE);
				    		jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, "Organising files..."));
				    		logCh.sendMessage("```User " + objUser.getName() + " submitted a bot called "  + objMsg.getAttachments().get(0).getFileName() + " in channel " + objMsgCh.getName() + " at " + "currentDate" + ""
				    				+ "\nHere is the output: " + cloud.store(objMsg) + ""
				    						+ "You can retrieve this file using >retrieve " + objUser.getId() + " or see the file in the folder using >show " + objUser.getId() + "```").queue();
	
				    		jda.getPresence().setStatus(OnlineStatus.IDLE);
				    		
				    		DateFormat dateFormat = new SimpleDateFormat("yyy/MM/dd HH:mm:ss");
				    		Date currDate = new Date();
				    		String currentDate = dateFormat.format(currDate);
				    		
				    		message.editMessage("Your bot has been backed up at " + currentDate + ". Message will expire in 5 seconds.").queue();
				    		double sentTime = System.currentTimeMillis();
				    		double currTime = 0;
				    		while(currTime - sentTime < 5000) {currTime = System.currentTimeMillis();}
				    		message.delete().queue();
				    		
		    			});
		    		}else {
		    			cloud.store(objMsg);
		    			objMsgCh.sendMessage("HTBot is currently offline. Please try again later.").queue();
		    		
		    		}
	    		}else {
	    			objMsgCh.sendMessage("Please use this command in either #season2 or #battles . Message expires in 10 seconds").queue(message ->{
	    				double sentTime = System.currentTimeMillis();
			    		double currTime = 0;
			    		while(currTime - sentTime < 10000) {currTime = System.currentTimeMillis();}
			    		message.delete().queue();
	    			});
	    		}
	    	}else if(objMsg.getContentRaw().startsWith(Ref.prefix + "store") && Ref.adminIds.contains(objMsg.getAuthor().getIdLong())){
	    		cloud.store(objMsg);
	    		objMsgCh.sendMessage("File stored.").queue();
	    	}
	    	else if(objMsg.getContentRaw().startsWith(Ref.prefix + "empty") && Ref.adminIds.contains(objMsg.getAuthor().getIdLong())){
	    		objMsgCh.sendMessage(cloud.empty(objMsg)).queue();
	    		jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, "Recycling paper..."));
	    		
	    		double sentTime = System.currentTimeMillis();
	    		double currTime = 0;
	    		while(currTime - sentTime < 5000) {currTime = System.currentTimeMillis();}
	    		jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, "Enjoying a snack ..."));
	    	}else if(objMsg.getContentRaw().startsWith(Ref.prefix + "remove") && Ref.adminIds.contains(objMsg.getAuthor().getIdLong())) {
	    		objMsgCh.sendMessage(cloud.remove(objMsg)).queue();
	    		jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, "Throwing away garbage..."));
	    		
	    	}else if(objMsg.getContentRaw().startsWith(Ref.prefix + "show") && Ref.adminIds.contains(objMsg.getAuthor().getIdLong())) {
	    		objMsgCh.sendMessage(cloud.show(objMsg)).queue();
	    		jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, "Dusting shelves..."));
	    		
	    	}else if(objMsg.getContentRaw().startsWith(Ref.prefix + "retrieve")) {
	    		if(Ref.adminIds.contains(objMsg.getAuthor().getIdLong()) || objMsg.isFromType(ChannelType.PRIVATE)){
	    			if(objMsg.isFromType(ChannelType.PRIVATE)) {
	    				objMsgCh.sendMessage(cloud.userVerification(objMsg)).queue();
	    				if(cloud.userVerification(objMsg).equals("Verify complete. Retrieiving file...")) {
	    					objMsgCh.sendFile(cloud.userRetrieve(objMsg)).queue(message -> {
	    						
	    						DateFormat dateFormat = new SimpleDateFormat("yyy/MM/dd HH:mm:ss");
	    			    		Date currDate = new Date();
	    			    		String currentDate = dateFormat.format(currDate);
	    						
	    						MessageChannel logCh = jda.getTextChannelById(Ref.logChId);
	    	    				logCh.sendMessage("`User " + objUser.getName() + " retrieved bot \"" + message.getAttachments().get(0).getFileName() + "\" at " + currentDate + "`").queue();
	    					});
	    				}
	    				
	    			}else {
	    				objMsgCh.sendMessage(cloud.verifyRetrieve(objMsg)).queue();
	    				objMsgCh.sendFile(cloud.retrieve(objMsg)).queue();
	    				jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, "Putting folder back..."));
	    			}
	    		}else if(!objMsg.isFromType(ChannelType.PRIVATE) && !Ref.adminIds.contains(objMsg.getAuthor().getIdLong())) {
	    			objMsgCh.sendMessage("Please use this command in a private message. Sending you a private message for instructions now. Message expires in 10 seconds").queue(message ->{
	    				objUser.openPrivateChannel().queue((channel) ->{
				    		channel.sendMessage("To retrieve your bot that has been backed up, simply reply to me by typing >retrieve").queue();
		    			});
	    				double sentTime = System.currentTimeMillis();
			    		double currTime = 0;
			    		while(currTime - sentTime < 10000) {currTime = System.currentTimeMillis();}
			    		message.delete().queue();
			    		objMsg.delete().queue();
			    		
	    			});
	    		}
	    	}else if(objMsg.getContentRaw().equals(Ref.prefix + "restore") && Ref.adminIds.contains(objUser.getIdLong())) {
	    		objMsgCh.sendFile(cloud.sendZip()).queue(message ->{
	    			cloud.deleteZip();
	    			message.editMessage("`Files sent and zip has been cleared in directory.`").queue();
	    		});
	    		
	    	}
	    	else if(objMsg.getContentRaw().equals(Ref.prefix + "greeting") && Ref.adminIds.contains(objMsg.getAuthor().getIdLong())){
	    		String mention = objGuild.getPublicRole().getAsMention();
	    		objMsgCh.sendMessage(mention.substring(1)).queue();
	    		objMsgCh.sendMessage("Hello "+mention.substring(0, mention.length())+"! I'm the bot that will backup all your bots that you submit onto this server so you don't lose them. I'll be silent most of the time, but if you want to know the weather, just type >weather <LOCATION>. May the best bot win!").queue();
	    		objMsg.delete().queue();
	    	}else if(objMsg.getMentionedUsers().contains(jda.getSelfUser())) {
	    		objMsgCh.sendMessage("Hey there "+objMsg.getAuthor().getAsMention()+" :smile:");
	    	}
	    	else if(objMsg.getContentRaw().startsWith(Ref.prefix + "weather")) {
	    		weatherAPI weather = new weatherAPI();
	    		String input = objMsg.getContentRaw();
	    		String city = "";
	    		String output = "";
	    		int reportNum = 0;
	    		
	    		if(input.indexOf(",") == -1 && input.indexOf("#") == -1) {
	    			city = input.substring(9,input.length());
	    			try {
						output = weather.getWeather(city, "", reportNum);
					} catch (Exception e) {
						objMsgCh.sendMessage("" + e).queue();
					}
	    		}else if(input.indexOf(",") != -1 && input.indexOf("#") == -1){
	    			city = input.substring(9,input.indexOf(","));
	    			String country = input.substring(input.indexOf(",") + 1,input.length());
	    			try {
						output = weather.getWeather(city, country, reportNum);
					} catch (Exception e) {
						objMsgCh.sendMessage("" + e).queue();
					}
	    		}else if(input.indexOf(",") != -1 && input.indexOf("#") != -1){
	    			city = input.substring(9,input.indexOf(","));
	    			String country = input.substring(input.indexOf(",") + 1,input.indexOf("#"));
	    			reportNum = Integer.parseInt(input.substring(input.indexOf("#") + 1,input.length()));
	    			
	    			try {
						output = weather.getWeather(city, country, reportNum);
					} catch (Exception e) {
						objMsgCh.sendMessage("" + e).queue();
					}
	    		}else if(input.indexOf(",") == -1 && input.indexOf("#") != -1) {
	    			city = input.substring(9,input.indexOf("#"));
	    			reportNum = Integer.parseInt(input.substring(input.indexOf("#") + 1,input.length()));
	    			try {
						output = weather.getWeather(city, "", reportNum);
					} catch (Exception e) {
						objMsgCh.sendMessage("" + e).queue();
					}
	    		}
	    		objMsgCh.sendMessage(output).queue();
	    		jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, "Looking outside..."));
	    	}
    }
    
    public static String executeCommand(String command) {
    	//Build command 
    	Process process = null;
		try {
			process = new ProcessBuilder(new String[] {"bash", "-c", command})
			    .redirectErrorStream(true)
			    .directory(new File("."))
			    .start();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        //Read output
        StringBuilder out = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = null, previous = null;
        
        String output = "";
        try {
			while ((line = br.readLine()) != null)
			    if (!line.equals(previous)) {
			        previous = line;
			        out.append(line).append('\n');
			        output += line + "\n";
			    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return output;
    }
    
    public static String cloudExec(String command) {
    	//Build command 
    	Process process = null;
		try {
			process = new ProcessBuilder(new String[] {"bash", "-c", command})
			    .redirectErrorStream(true)
			    .directory(new File("../"))
			    .start();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        //Read output
        StringBuilder out = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = null, previous = null;
        
        String output = "";
        try {
			while ((line = br.readLine()) != null)
			    if (!line.equals(previous)) {
			        previous = line;
			        out.append(line).append('\n');
			        output += line + "\n";
			    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return output;
    }
    
    public Member getUser(Guild guild,String name) {
    	List<Member> members = guild.getMembers();
		int memberIndex = -1;
		for(Member member : members) {
			if(member.getEffectiveName().equals(name)) {
				memberIndex = members.indexOf(member);
			}
		}
		if(memberIndex == -1) {
			return null;
		}else {
			return members.get(memberIndex);
		}
    }
    
    public Member getMemberById(Guild guild,String id) {
    	List<Member> members = guild.getMembers();
		int memberIndex = -1;
		for(Member member : members) {
			if(member.getUser().getId().equals(id)) {
				memberIndex = members.indexOf(member);
			}
		}
		if(memberIndex == -1) {
			return null;
		}else {
			return members.get(memberIndex);
		}
    }
    
    public String getFlag(String command, String input) {
    	String cmdWithPrefix = Ref.prefix + command;
    	String flag = input.substring(cmdWithPrefix.length(), input.length());
    	flag = flag.trim();
		System.out.println(flag + " flagged for command \"" + command + "\"");
		return flag;
    }
}
