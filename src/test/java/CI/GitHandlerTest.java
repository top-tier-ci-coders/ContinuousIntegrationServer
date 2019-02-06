package CI;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.nio.charset.Charset;

public class GitHandlerTest {

  /**
  * Test that a branch should run all steps when a push comes in.
  * Runs a build on master which should always be successful
  * Runs a build on a branch that fails on build
  * Runs a build on a branch that fails to start the tests.
  * Result is send to the email provided to event
  * @author Andreas Gylling
  */
  @Test
  public void testRequestPush(){
    GitEvent event = new GitEvent("","");
    // Master branch should always build sucessfully
    event.setBranchName("master");
    event.setPusherEmail("toptierci@gmail.com");
    GitHandler gh = new GitHandler(event);
    boolean test1 = gh.request_push();
    assertTrue(test1);
    // Fail already at build.
    event.setBranchName("nobuild");
    boolean test2 = gh.request_push();
    assertFalse(test2);
    // Fail at test after build
    event.setBranchName("testsfail");
    boolean test3 = gh.request_push();
    assertFalse(test3);
  }

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
  * @author Andreas Gylling, Philippa Örnell, Kartal Kaan Bozdoğan
  */
  @Test
  public void testPullBranch(){
   GitEvent event = new GitEvent("", "");
   event.setBranchName("abranchthatdoesntexist"); // We can't pull a branch that doesn't exist
   assertNull(new GitHandler(event).pull_branch());
   event.setBranchName("buildstestspass");
   GitHandler gitHandler = new GitHandler(event);
   String re = gitHandler.pull_branch();
   assertNotSame(null, re);
   // Also make sure the return is a correct path
   assertTrue(re.matches(".*/builds-CI/-?[0-9]+"));
   // Make sure that the repo is pulled correctly by reading the contents of a well-known file
   try {
       assertEquals(GitEventTest.readFile(re + "/well_known", Charset.forName("US-ASCII")), "test\n");
   }
   catch (IOException e) {
         e.printStackTrace();
         assertTrue(false);
   }
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
	/*String jsonStr = null;
	try {
		jsonStr = GitEventTest.readFile("./src/test/java/CI/jsonStr", Charset.defaultCharset());
    }
	catch (IOException e) {
		assertTrue(false);
	}
	String eventType = "push";
	GitEvent event = new GitEvent(eventType, jsonStr);
    event.setPusherName("kartal");
    event.setPusherEmail("bozdogan@kth.se");
    event.setBranchName("Mail test");
    GitHandler gitHandler = new GitHandler(event);
    assertTrue(gitHandler.send_notification("Testing... Testing..."));*/
 }

  /**
   * Tests GitHandler.send_notification by attempting to send an email to an invalid address.
   * @author Kartal Kaan Bozdoğan
   */
  @Test
  public void testSendNotificationFail() {
	String jsonStr = null;
	try {
		jsonStr = GitEventTest.readFile("./src/test/java/CI/jsonStr", Charset.defaultCharset());
    }
	catch (IOException e) {
		assertTrue(false);
	}
	String eventType = "push";
	GitEvent event = new GitEvent(eventType, jsonStr);
    event.setPusherName("kartal");
    event.setPusherEmail("bozdog ankth.se");
    event.setBranchName("Mail test");
    GitHandler gitHandler = new GitHandler(event);
    assertFalse(gitHandler.send_notification("Testing... Testing..."));
 }

}
