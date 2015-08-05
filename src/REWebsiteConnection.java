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

    public REWebsiteConnection(String URLs){
        this.URLs = URLs;
    }

    public void CreateConnection(){
        try {
            URL site = new URL(URLs);
            URLConnection connection = site.openConnection();
            BufferedReader source = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

            String inputLine;
            content = new StringBuilder();

            while((inputLine = source.readLine()) != null){
                content.append(inputLine);
            }

            source.close(); // close the reader to clean up resources
        }
        catch (Exception msg){
            System.err.println(msg.getMessage());
        }
    }

    public String GetContent(){
        return this.content.toString();
    }
}
