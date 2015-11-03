import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * Created by Dominik on 05.06.2015.
 */
public class SharedSXDownloader extends Downloader {
    private String hash = "";
    private String expires = "";
    private String timestamp = "";
    private String streamURL = "";
    private String dataname = "";
    private Map<String, String> cookies;

    public SharedSXDownloader(String sharedURL){

        // fill with input type to send a post request
        try {
            Connection.Response prep = Jsoup.connect(sharedURL).timeout(0)
                    .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0").execute();
            Document preparePost = prep.parse();
            cookies = prep.cookies();



            Elements inputhidden = preparePost.select("input[type=hidden]");
            for (int i = 0; i < inputhidden.size(); i++) {
                if(inputhidden.get(i).attr("name").equals("hash"))
                    hash = inputhidden.get(i).attr("value");
                if(inputhidden.get(i).attr("name").equals("expires"))
                    expires = inputhidden.get(i).attr("value");
                if(inputhidden.get(i).attr("name").equals("timestamp"))
                    timestamp = inputhidden.get(i).attr("value");
            }

            //Document getStream = Jsoup.connect(sharedURL)
            //        .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
            //        .data("hash", hash, "expires", expires, "timestamp", timestamp).post();

            Connection.Response cook = Jsoup.connect(sharedURL)
                    .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                    .data("hash", hash, "expires", expires, "timestamp", timestamp)
                    .method(Connection.Method.POST)
                    .ignoreContentType(true).execute();
            //cookies = cook.cookies();

            Document getStream = cook.parse();

            streamURL = (getStream.select("div[class=stream-content]")).attr("data-url");
            dataname = (getStream.select("div[class=stream-content]")).attr("data-name");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getStreamURL(){
        return this.streamURL;
    }

    public String getFilename(){
        return this.dataname;
    }

    public void DownloadFile(String dlUrl, String filename, int downloadSize, int i, DefaultTableModel dTableModel) {
        try {
            URL url = new URL(dlUrl);
            URLConnection hc = url.openConnection();

            hc.setReadTimeout((100 * 1000));
            hc.setReadTimeout((100 * 1000));
            hc.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

            super.DownloadFile(hc, filename, downloadSize, i, dTableModel);
        }catch (Exception ex){
            System.err.println("Error while parsing download link from SharedSXDownloader to Engine!");
        }
    }
}
