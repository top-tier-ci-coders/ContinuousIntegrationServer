package CI;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class GitHandlerTest {

  /**
    * This tests the start_tests function in the GitHandler class.
    * @author Philippa Ö, Andreas G.
    */

   @Test
   public void testStartTests() {
     // We pull a branch and run tests on this one
     GitEvent event = new GitEvent("", "");
     event.setBranchName("buildstestspass");
     GitHandler gitHandler = new GitHandler(event);
     String p = gitHandler.pull_branch();

     //We just pulled a branch so we should be able to run tests. Assert: True
     assertTrue(gitHandler.start_tests(p));
     // We can't run the test from the current path. Assert: False
     assertFalse(gitHandler.start_tests("."));
     // If we try to run tests in a folder that doesn't exist, we should fail. Assert: False
     assertFalse(gitHandler.start_tests("./doesnotexist"));

   }

  /**
  * Tests that the pull_branch function terminates properly by returning
  * a path that isn't null.
  * @author Andreas Gylling, Philippa Örnell
  */
  @Test
  public void testPullBranch(){
   GitEvent event = new GitEvent("", "");
   event.setBranchName("test");
   GitHandler gitHandler = new GitHandler(event);
   String re = gitHandler.pull_branch();
   assertNotSame(null, re);
   // Also make sure the return is a correct path
   assertTrue(re.matches(".*/builds-CI/-?[0-9]+"));
  }

  /**
   * Tests GitHandler.build_branch by a positive test to build
   * the current branch and a negative test to build path that does
   * not exist.
   * @author Marcus Östling
   */
  @Test
  public void testBuildBranch() {
    System.out.println("Test build branch");
    GitEvent event = new GitEvent("","");
    GitHandler gitHandler = new GitHandler(event);
    assertTrue(gitHandler.build_branch("."));
    assertFalse(gitHandler.build_branch("./herpderp"));
  }

  /**
   * Tests GitHandler.send_notification by sending a well-known email
   * @author Kartal Kaan Bozdoğan
   */
  @Test
  public void testSendNotificationSuccess() {
    /**
    GitEvent event = new GitEvent();
    event.pusherName = "kartal";
    event.pusherEmail = "bozdogan@kth.se";
    event.branchName = "Mail test";
    GitHandler gitHandler = new GitHandler(event);
    assertTrue(gitHandler.send_notification("Testing... Testing..."));
    */
 }

  /**
   * Tests GitHandler.send_notification by attempting to send an email to an invalid address.
   * @author Kartal Kaan Bozdoğan
   */
  @Test
  public void testSendNotificationFail() {
    /**
    GitEvent event = new GitEvent("","");
    event.setPusherName("kartal");
    event.setPusherEmail("bozdog ankth.se");
    event.setBranchName("Mail test");
    GitHandler gitHandler = new GitHandler(event);
    assertFalse(gitHandler.send_notification("Testing... Testing..."));
    */
 }

}
