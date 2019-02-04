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

/**
  * The GitHandler class handles a GitEvent object (see GitEvent class for more info).
  * It is used for managing continuous integration tasks.
  * The class include methods to pull branches from Github,
  * building the code, testing the code and sending status emails.
  *
  */

public class GitHandler{
  private GitEvent G; // GitEvent object containing all information about the event.

  GitHandler(GitEvent G){
    this.G = G;
  }

  /**
  * Handles an push event by calling all individual functions handling a step each.
  * Meaning, pull, build, test and notify.
  */
  public void request_push(){
        // TODO
      // Called by request_push
      // Try and pull the branch
      // Try and build the branch
      // Execute the Test suite.
      // Send notification
  }

  /**
  * Pulls the branch provided by a GitEvent object and switches to it.
  * @author Andreas Gylling, Philippa Örnell
  * @return - The path to the pulled branch.
  */
  public String pull_branch(){
    // Creates a random number, used for folder name. Prevents duplicate issues.
    Random rn = new Random();
    int identifier = rn.nextInt();
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
      // Checkout the branch command. -t used for fetching remote branches.
      String changeBranch = "git -C "+ Folder +" checkout -t origin/"+G.getBranchName();
      String args2[] = {"bash", "-c", changeBranch};
      Process process2 = Runtime.getRuntime().exec(args2);
      int process2Wait = process2.waitFor();
      if (waitFor == 0 && process2Wait == 0){ // If both commands terminated properly, return the path
        return Folder;
      }

    }catch(Exception e){
      return null;
    }
    return null;
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
    String from = "top-tier-ci@gmail.com";
    String host = "localhost";

    Properties properties = System.getProperties();
    properties.setProperty("smtp.kth.se", host);

    Session session = Session.getDefaultInstance(properties);

    try{
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(from));
        mimeMessage.addRecipient(Message.RecipientType.TO,new InternetAddress(G.getPusherEmail()));
        mimeMessage.setSubject("CI Message");
        mimeMessage.setText(message);

        Transport.send(mimeMessage);
        return true;

    }catch (MessagingException mex) {
        mex.printStackTrace();
        return false;
    }
  }
}
