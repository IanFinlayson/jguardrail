// all warnings issued through a static method in this class
// for consistency of presentation

import java.util.ArrayList;
import java.util.Collections;

public class Warnings {
    static class Warning implements Comparable<Warning> {
        public String message;
        public int line;

        public Warning(String message, int line) {
            this.message = message;
            this.line = line;
        }
        
        public int compareTo(Warning other) {
            return line - other.line;
        }
    }

    private static ArrayList<Warning> warnings = new ArrayList<>();
    private static String filename = "";

    public static void setupWarnings(String f) {
        filename = f;
        warnings.clear();
    }
    
    public static void warn(String message, int line) {
        warnings.add(new Warning(message, line));
    }
    
    public static void printWarnings() {
        // sort them by line number
        Collections.sort(warnings);
        for (Warning w : warnings) {
            System.out.println(filename + ":" + w.line + " " + w.message);
        }
    }
}

