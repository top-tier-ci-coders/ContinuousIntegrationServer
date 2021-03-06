package CI;
import org.json.*;
import java.nio.charset.Charset;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.stream.Collectors;
import org.json.JSONException;

/** 
 *  @author Gustaf Pihl
 *  Class that stores information from a web hook request.
 */   
public class GitEvent
{
    private String eventType;
    private String branchName;
    private String pusherName;
    private String pusherEmail;
    private Boolean validEvent;

    public GitEvent(String eventType, String jsonStr) throws JSONException {
        this.validEvent = false;
        this.eventType = eventType;         

        if (this.eventType.equals("push")) {
            JSONObject obj = new JSONObject(jsonStr);

            String ref = obj.getString("ref");
            this.branchName = ref.substring(11);

            JSONObject pusher = obj.getJSONObject("pusher");
            this.pusherName = pusher.getString("name");
            this.pusherEmail = pusher.getString("email");

            this.validEvent = true;
        }
    }

    public String getPusherName() {
        return this.pusherName;
    }

    public String getPusherEmail() {
        return this.pusherEmail;
    }
    
    public String getEventType() {
        return this.eventType;
    }

    public String getBranchName() {
        return this.branchName;
    }

    public Boolean getValidEvent() {
        return this.validEvent;
    }

    public void setPusherName(String pusherName) {
        this.pusherName = pusherName;
    }

    public void setPusherEmail(String pusherEmail) {
        this.pusherEmail = pusherEmail;
    }
    
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setValidEvent(Boolean validEvent) {
        this.validEvent = validEvent;
    }
}

