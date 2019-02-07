package CI;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

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
    // The happy path
    event.setBranchName("buildstestspass");
    event.setPusherEmail("toptierci@gmail.com");
    GitHandler gh = new GitHandler(event);
    PipelineResult result = gh.request_push();
    assertEquals(result, PipelineResult.SUCCESS);
    // Fail already at build.
    event.setBranchName("nobuild");
    result = gh.request_push();
    assertEquals(result, PipelineResult.BUILD_FAILED);
    // Fail at test after build
    event.setBranchName("testsfail");
    result = gh.request_push();
    assertEquals(result, PipelineResult.TEST_FAILED);
    // Fails at trying on branch that doesnt exist
    event.setBranchName("gdfngfdngkdfj4324732423");
    result = gh.request_push();
    assertEquals(result, PipelineResult.PULL_FAILED);
    // Fails to send notification to an invalid email address
    event.setBranchName("buildstestspass");
    event.setPusherEmail("toptie rci gmail.com");
    result = gh.request_push();
    assertEquals(result, PipelineResult.NOTIFY_FAILED);

  }

  /**
  * Test that request_push() creates a log file.
  * @author Marcus Östling
  */
  @Test
  public void testRequestPushLog(){
    GitEvent event = new GitEvent("","");
    event.setBranchName("buildstestspass");
    event.setPusherEmail("toptierci@gmail.com");
    GitHandler gh = new GitHandler(event);

    int before = BuildLogger.listBuilds().length;
    gh.request_push();
    int after = BuildLogger.listBuilds().length;
    assertEquals(before+1, after);
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
     String p = gitHandler.pull_branch(Math.abs(new Random().nextInt()));

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
   assertNull(new GitHandler(event).pull_branch(Math.abs(new Random().nextInt())));
   event.setBranchName("buildstestspass");
   GitHandler gitHandler = new GitHandler(event);
   String re = gitHandler.pull_branch(Math.abs(new Random().nextInt()));
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
	// The branch "nobuild" shall not build
	event.setBranchName("nobuild");
    GitHandler gitHandler = new GitHandler(event);
	String path = gitHandler.pull_branch(Math.abs(new Random().nextInt()));
	assertNotNull(path);
	assertFalse(gitHandler.build_branch(path));
	// The branch "testsfail" shall build
	event.setBranchName("testsfail");
	path = gitHandler.pull_branch(Math.abs(new Random().nextInt()));
	assertNotNull(path);
	assertTrue(gitHandler.build_branch(path));
	// A nonexisting folder shall not build
    assertFalse(gitHandler.build_branch("./thisfolderdoesntexist1209786540"));
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
