import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Creation time: 23:18
 * Created by Dominik on 05.08.2015.
 */
public class REWebsiteConnection {
    private String URLs;

    private StringBuilder content;

    public REWebsiteConnection(String URLs) {
        this.URLs = URLs;
    }

    public void CreateConnection() {
        try {
            URL site = new URL(URLs);
            URLConnection connection = site.openConnection();

            /*
            *
            * Maybe add the functionality to provide user data and select them whilst pressing analyze.
            *
            * A working solution would be:
            *
                String userpass = "bla:test";
                String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
                connection.setRequestProperty ("Authorization", basicAuth);
            *
            * Otherwise we had to provide it like http://bla:test@testurl.com/.../
            * Which is not a fancy way to use it
            */

            BufferedReader source = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

            String inputLine;
            content = new StringBuilder();

            while ((inputLine = source.readLine()) != null) {
                content.append(inputLine);
            }

            source.close(); // close the reader to clean up resources
        } catch (Exception msg) {
            System.err.println(msg.getMessage());
            if (msg.getMessage().contains("401 for URL")) {
                JOptionPane.showMessageDialog(null, "401 - Unauthorized please provide username and password",
                        "401 - Unauthorized", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Create Connection overloaded
    public void CreateConnection(String username, String password) {
        try {

            URL site = new URL(URLs);
            URLConnection connection = site.openConnection();
            String userpass = username + ":" + password;
            String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
            connection.setRequestProperty("Authorization", basicAuth);

            BufferedReader source = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

            String inputLine;
            content = new StringBuilder();

            while ((inputLine = source.readLine()) != null) {
                content.append(inputLine);
            }

            source.close(); // close the reader to clean up resources
        } catch (Exception msg) {
            System.err.println(msg.getMessage());
            if (msg.getMessage().contains("401 for URL")) {
                JOptionPane.showMessageDialog(null, "401 - Unauthorized please provide username and password",
                        "401 - Unauthorized", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public String GetContent() {
        try {
            return this.content.toString();
        } catch (NullPointerException e) {
            System.err.println("No data available");
            return "";
        }
    }
}
