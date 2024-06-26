package package4;

import java.util.ArrayList;
import java.util.List;

public class Journal {
    private static List<String> logEntries = new ArrayList<>();

    public static void log(String entry) {
        logEntries.add(entry);
    }
    
    public static List<String> getLogEntries(){
    	return logEntries;
    }
}
    