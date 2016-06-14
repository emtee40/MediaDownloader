/**
 * Created by Robin on 09.06.2016.
 * <p>
 * Static class for global variables
 */
public class CGlobals {

    public static final String VERSION_STRING = "1.1b";
    public static OS CURRENT_OS;
    public static String PATH_SEPARATOR;
    public static final String STANDARD_HTML_STYLETAG_OPEN = "<p style='font-family: sans-serif; font: 9px;'>";
    public static final String STANDARD_HTML_STYLETAG_CLOSE = "</p>";

    public static void init() {
        // Get OS
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().contains("windows"))
            CURRENT_OS = OS.Windows;
        else if (osName.toLowerCase().contains("nux"))
            CURRENT_OS = OS.Linux;
            // TODO: Android
        else
            CURRENT_OS = OS.Undefined;

        // Separator
        switch (CURRENT_OS) {
            case Windows:
                PATH_SEPARATOR = "\\";
                break;
            case Linux:
                PATH_SEPARATOR = "/";
                break;
            // TODO: Android
            default:
                PATH_SEPARATOR = "";
        }


    }

}

enum OS {
    Linux,
    Windows,
    Android,
    Undefined
}
