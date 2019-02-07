package CI;
import java.util.List;
import java.util.ArrayList;
import java.io.*;

/** 
 *  @author Gustaf Pihl
 *  Static class used for setting and getting logs
 *  related to various builds. Logs are stored in the 'log/'
 *  directory in the root of the project.
 */  
public class BuildLogger {

    public BuildLogger() {      
    }
    
    public static String[] listBuilds() {
        String dirName = "./log/";
        File directory = new File(dirName);
        return directory.list();
    }
    
    public static String getBuildStatus(String build_id) {
        String status = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader("./log/" + build_id));
            String line;
            while ((line = reader.readLine()) != null) {
                    status += line + '\n';
            }
            reader.close();
            return status;
        } catch (Exception e) {
            System.err.format("Exception occurred trying to read '%s'.", build_id);
            e.printStackTrace();
            return null;
        }
    }

    public static void setBuildStatus(String build_id, String status) {
        String directoryName = "./log/";
        String fileName = build_id;

        File directory = new File(directoryName);
        if (! directory.exists()){
            directory.mkdir();
        }

        File file = new File(directoryName + "/" + fileName);
        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(status);
            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
