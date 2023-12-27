package net.ianfinlayson.jguardrail;

// all warnings issued through a static method in this class
// for consistency of presentation

// we also optionally can log the error messages to a file so that
// we know how many are triggered by actual users on our systems

import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class Warnings {
    // constants used to differentiate warning codes
    public static int INT_DIVIDE = 1;
    public static int STR_EQUALS = 2;
    public static int MISSING_BREAK = 3;
    public static int VOID_CONSTRUCTOR = 4;
    public static int SHADOW_VARIABLE = 5;
    public static int SELF_ASSIGN = 6;
    public static int UNINITIALIZED = 7;

    static class Warning implements Comparable<Warning> {
        public String message;
        public int line;
        public int code;

        public Warning(int code, String message, int line) {
            this.message = message;
            this.line = line;
            this.code = code;
        }

        public int compareTo(Warning other) {
            return line - other.line;
        }
    }

    private static ArrayList<Warning> warnings = new ArrayList<>();
    private static String javaFile = "";
    private static String logFile = "/home/ifinlay/jguardrail-log.csv";

    public static void setupWarnings(String javaFile) {
        Warnings.javaFile = javaFile;
        warnings.clear();
    }

    public static void warn(int code, String message, int line) {
        warnings.add(new Warning(code, message, line));
    }

    public static void printWarnings() {
        // sort them by line number
        Collections.sort(warnings);

        // we also log them to a file, unless the logFile name is empty
        PrintWriter pw = null;
        if (logFile != "") {
            try {
                pw = new PrintWriter(new FileOutputStream(new File(logFile), true));
            } catch (Exception e) {
                pw = null;
            }
        }

        // loop through the warnings
        for (Warning w : warnings) {
            // print it to the screen for the user
            System.out.println(javaFile + ":" + w.line + " " + w.message);

            // log it, if we can
            if (pw != null) {
                try {
                    pw.println(System.getProperty("user.name") + "," + javaFile + "," + w.line + "," + w.code);
                } catch (Exception e) {}
            }
        }

        // close the file if we made one
        if (pw != null) {
            pw.close();
        }
    }
}


