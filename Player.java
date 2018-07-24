package bot.HT.HT_Backup;

public class Player {
	
	//When this player signed up
	public String timestamp;
	
	//Player's email address
	public String email;
	
	//Player's participation status (whether they will play or not)
	public boolean participation;
	
	//Player's preferred language for bot
	public String language;
	
	//Player's username in the Discord server
	public String username;
	
	//Player's region
	public String area;
	
	//Player's rank in Halite II
	public int rank;
	
	//Player's level of education
	public String education;
	
	//Player's years of experience coding 
	public String yearsOfCoding;
	
	//Whether this player will attend the tournament
	public String attendance;
	
	//Agreement to submit a bot by deadline.
	public boolean submitAgree;
	
	//Agreement to not submit malicious code.
	public boolean maliciousAgree;
	
	//Agreement to submit an original bot.
	public boolean originalAgree;
	
	//Agreement to join the Discord server.
	public boolean surveyAnswered;

	public Player(String timestamp, String email, boolean participation, String language, String username, String area,
			int rank, String education, String yearsOfCoding, String attendance, boolean submitAgree,
			boolean maliciousAgree, boolean originalAgree, boolean surveyAnswered) {
		this.timestamp = timestamp;
		this.email = email;
		this.participation = participation;
		this.language = language;
		this.username = username;
		this.area = area;
		this.rank = rank;
		this.education = education;
		this.yearsOfCoding = yearsOfCoding;
		this.attendance = attendance;
		this.submitAgree = submitAgree;
		this.maliciousAgree = maliciousAgree;
		this.originalAgree = originalAgree;
		this.surveyAnswered = surveyAnswered;
	}
	
	
}
