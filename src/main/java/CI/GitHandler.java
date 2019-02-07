package CI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import java.io.IOException;
import java.io.File;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.lang.Runtime;
import java.text.SimpleDateFormat;

/**
  * The GitHandler class handles a GitEvent object (see GitEvent class for more info).
  * It is used for managing continuous integration tasks.
  * The class include methods to pull branches from Github,
  * building the code, testing the code and sending status emails.
  *
  */

enum PipelineResult
{
	PULL_FAILED,
	BUILD_FAILED,
	TEST_FAILED,
	NOTIFY_FAILED,
	SUCCESS
};

public class GitHandler{
  private GitEvent G; // GitEvent object containing all information about the event.

  GitHandler(GitEvent G){
    this.G = G;
  }

  /**
  * Handles an push event by calling all individual functions handling a step each.
  * Meaning, pull, build, test and notify.
  * @author Andreas Gylling
  * @return true if everything is succesful, false if anything failed
  */
  public PipelineResult request_push(){
      String message = "";
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS"); 
      String buildstatus = 
        "Pusher: " + G.getPusherName() + 
        " Date: " + sdf.format(new Date()) + "<br>";
      PipelineResult result;

      // Creates a random number, used for folder name. Prevents duplicate issues.
      Random rn = new Random();
      int identifier = Math.abs(rn.nextInt());

      // Try and pull the branch
      System.out.println("Pulling \"" + G.getBranchName() + "\"");
      String path = pull_branch(identifier);
      if(path == null){
        message = "Failed to pull \"" + G.getBranchName() + "\", check that the branch name is correct.";
        send_notification(message);
        System.out.println(message);
        return PipelineResult.PULL_FAILED;
      }
      // Try and build the branch
      System.out.println("Building \"" + G.getBranchName() + "\"");
      // Execute the Test suite.
      if(build_branch(path)){
        // Try and start the test suite
	      System.out.println("Testing \"" + G.getBranchName() + "\"");
        if(start_tests(path)){
          message = "Building the branch \""+ G.getBranchName() 
              + "\" was successful, tests passed, check logs for results";
          result = PipelineResult.SUCCESS;
        }else{
          message = "Building the branch \""+ G.getBranchName() 
              + "\" was successful, but the tests failed, please check logs";
          result = PipelineResult.TEST_FAILED;
        }
      }
      else{
          // Send notification that build failed
          message = "Build failed to complete on branch \""+ G.getBranchName() + "\", please check logs";
	        result = PipelineResult.BUILD_FAILED;
      }
      message += " Build folder: " + path + " Report located in: /build/reports/tests/test/index.html";  
      System.out.println(message);
      System.out.println("Sending notification...");
      if(send_notification(message)){
        System.out.println("Notification was sucessfully send");
      }else{
        System.out.println("Notification could not be sent, failed");
	      if (result == PipelineResult.SUCCESS)
	          result = PipelineResult.NOTIFY_FAILED;
      }

      buildstatus += message;
      BuildLogger.setBuildStatus(""+identifier, buildstatus);
      return result;
  }

  /**
  * Pulls the branch provided by a GitEvent object and switches to it.
  * @author Andreas Gylling, Philippa Örnell
  * @return - The path to the pulled branch.
  */
  public String pull_branch(int identifier){
	System.out.println("Random identifier: " + String.valueOf(identifier));
    // URL to our CI repo
    String URL = "https://github.com/top-tier-ci-coders/ContinuousIntegrationServer.git";
    // Path where we want to build our branch later
    String Folder = System.getProperty("user.home") + "/builds-CI/" + identifier;
    try{
      // Clone the repo to a new folder in the Folder directory
      String args[] = {"bash", "-c", "git clone " + URL + " " + Folder};
      Process process = Runtime.getRuntime().exec(args);
      // Wait for the thread to terminate
      int waitFor = process.waitFor();
      if(waitFor != 0){
        return null;
      }
      // Checkout the branch command. -t used for fetching remote branches.
      if(!G.getBranchName().equals("master")){
        String changeBranch = "git -C "+ Folder +" checkout -t origin/"+G.getBranchName();
        String args2[] = {"bash", "-c", changeBranch};
        Process process2 = Runtime.getRuntime().exec(args2);
        int process2Wait = process2.waitFor();
        if (process2Wait == 0){ // If both commands terminated properly, return the path
          return Folder;
        }else{
          return null;
        }
      }
      return Folder;

    }catch(Exception e){
      return null;
    }
  }

  /**
   * Executes the automated tests in a specified local path folder
   * @param path - The path to the where the cloned branch exists, script will be starting the tests here
   * @return - True if tests went through, false if not. I.e true if ./gradlew test was successful
   * @author Philippa Örnell & Andreas Gylling
   */
   public boolean start_tests(String path){
     // If path is the current path, we will end up with recursive testing. We must prevent this.
     if(path.equals(".")){
       return false;
     }
     try {
       //                                 ./gradlew -p    ~/builds-CI/-1004278601/ test
       String[] command = {"bash", "-c", "./gradlew -p " + path +                " test"};
       Process process = Runtime.getRuntime().exec(command);
       // Wait for the thread to terminate, by convention 0 indicates normal termination
       int waitFor = process.waitFor();
       if (waitFor == 0){
         return true;
       }
       else{
         return false;
       }
     } catch(Exception e) {
       //The test failed
     }
     return false;
   }

  /**
  * Attempts to build the pulled branch locally
  * @param path - The path to where the cloned branch is located
  * @return - True if build was successful, false if not.
  * @author Marcus Östling
  */
  public boolean build_branch(String path){
    try {
      Process process = Runtime.getRuntime().exec(path+"/gradlew build -x test");
      if (process.waitFor() == 0)
        return true;
      else
        return false;
    } catch(Exception e) {
      // Build failed
    }
    return false;
  }

  /**
  * Notifies the pusher of the status of certain requests
  * @author Kartal Kaan Bozdoğan
  * @param message - The message to send
  * @return - True if message was send, false if not.
  */
  public boolean send_notification(String message){
	final String from = "toptierci";
	final String pass = "toptierCI123";
	final String to = G.getPusherEmail();
	final String subject = "CI Notification";
	final String body = message;
	Properties props = System.getProperties();
	String host = "smtp.gmail.com";
	props.put("mail.smtp.starttls.enable", "true");
	props.put("mail.smtp.host", host);
	props.put("mail.smtp.user", from);
	props.put("mail.smtp.password", pass);
	props.put("mail.smtp.port", "587");
	props.put("mail.smtp.auth", "true");

	Session session = Session.getDefaultInstance(props);
	MimeMessage mimeMessage = new MimeMessage(session);

	try {
		mimeMessage.setFrom(new InternetAddress(from));
		InternetAddress toAddress = new InternetAddress(to);

		mimeMessage.addRecipient(Message.RecipientType.TO, toAddress);

		mimeMessage.setSubject(subject);
		mimeMessage.setText(body);
		Transport transport = session.getTransport("smtp");
		transport.connect(host, from, pass);
		transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
		transport.close();
	}
	catch (AddressException ae) {
		//ae.printStackTrace();
		return false;
	}
	catch (MessagingException me) {
		//me.printStackTrace();
		return false;
	}
	return true;
  }
}
