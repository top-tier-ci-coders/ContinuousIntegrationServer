package CI;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.charset.Charset;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;

/** 
 *  @author Gustaf Pihl
 *  Tests the functionality of the BuildLogger class.
 */  
public class BuildLoggerTest {

	/** 
	 *  Tests the functionality of the BuildLogger class.
	 */   
    @Test
    public void Test1() {
        // Create new build log
        BuildLogger.setBuildStatus("123456789", "STATUS OK");
        String status = BuildLogger.getBuildStatus("123456789");

        // Make sure log has correct status
        assertEquals("STATUS OK\n", status);

        // Overwrite build log
        BuildLogger.setBuildStatus("123456789", "STATUS FAIL");
        status = BuildLogger.getBuildStatus("123456789");

        // Make sure log has correct status
        assertEquals("STATUS FAIL\n", status);
        
        String[] builds = BuildLogger.listBuilds();
        boolean found = false;
        for (String build : builds) {
            if (build.equals("123456789")) {
                found = true;
            }
        }

        // Make sure listBuilds lists the build
        assertTrue(found);      
    }    
}
