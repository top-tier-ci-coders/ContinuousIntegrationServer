package CI;

/** 
 *  @author Marcus Östling, Kartal Kaan Bozdoğan
 *  Class that stores information from a web hook request.
 */   
public class GitEvent
{
	public EventType eventType;
	public String pusherName;
	public String pusherEmail;
	public String branchName;
}	
