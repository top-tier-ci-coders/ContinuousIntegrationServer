package CI;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.charset.Charset;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;

public class GitEventTest {

	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	@Test
	public void Test1() {
		try {
			String jsonStr = readFile("./src/test/java/CI/jsonStr", Charset.defaultCharset());
			String eventType = "push";
			GitEvent gitEvent = new GitEvent(eventType, jsonStr);
			assertEquals(gitEvent.getBranchName(), "master");
			assertEquals(gitEvent.getPusherName(), "Codertocat");
			assertEquals(gitEvent.getPusherEmail(), "Codertocat@users.noreply.github.com");
				
		} catch (IOException e) {
			System.err.println(e);
			assertEquals(1 + 1, 3);
		}
	}    
}
