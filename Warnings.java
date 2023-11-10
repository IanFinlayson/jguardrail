// all warnings issued through a static method in this class
// for consistency of presentation

public class Warnings {
    public static String filename = "";

    public static void setFileName(String f) {
        filename = f;
    }
    
    public static void warn(String message, int line) {
        System.out.println(filename + ":" + line + " " + message);
    }
}

