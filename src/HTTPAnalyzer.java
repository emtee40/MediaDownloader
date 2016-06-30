import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;

/**
 * Rewritten JSounpAnalyze Class in order to support all known platforms.
 * The main purpose of this class it to support Android without any extra effort.
 *
 * Main purpose of this class is to establish an connection to the given website.
 * External libraries are JSoup.
 *
 * To access a website an URLPackage is needed.
 */
public class HTTPAnalyzer {
    private URLPackage _URLPackage;
    private HTTPCredentials credentials;
    private Document site;

    public HTTPAnalyzer(URLPackage url){
        this._URLPackage = url;
    }

    public HTTPAnalyzer(String url){
        this(new URLPackage(url));
    }

    public HTTPAnalyzer(URLPackage url, HTTPCredentials cred){
        this(url);
        credentials = cred;
    }

    public HTTPAnalyzer(String url, HTTPCredentials cred){
        this(url);
        credentials = cred;
    }

    public int parse(){
        try{
            if(credentials == null)
                site = EstablishConnection(this._URLPackage.getURL(),false);
            else
                site = EstablishConnection(this._URLPackage.getURL(),true);
        }
        catch (HttpStatusException ex){
            int statusCode =  ex.getStatusCode();

            // Unauthorized
            if(statusCode == 401){
                // Could do sth here
            }

            return statusCode;
        }
        catch (Exception ex){
            ex.printStackTrace();
            // 0 -> Error
            return 0;
        }
        // 200 -> OK
        return 200;
    }

//    private int parseWithCredentials(){
//        if(credentials == null)
//            return 0;
//    }

    // Public Methods
    public String getSiteHtml(){
        return site.html();
    }

    public Document getDocument(){
        return site;
    }

    public void setSite(Document doc){
        this.site = doc;
    }

    // Public static Methods
    public static Document doFakeSubmit(Document site, String userAgent, Map<String, String> data){
        Document fake = site;


        Document nSite = null;
        return nSite;
    }

    // Helper Methods
    private Document EstablishConnection(String webURL, boolean useCredentials) throws IOException {
        //try {

        /*
            Establish connection without credentials
         */
        if(!useCredentials){
            // Needed since Android is a bit bitchy with creating jsoup connections
            if (CGlobals.CURRENT_OS == OS.Android) {
                return Jsoup.connect(webURL).get();
            } else { // Every other OS like Windows, Linux and Mac
                return Jsoup.connect(webURL).timeout(0).get();
            }
        }

        /*
            Establish connection with credentials
         */
        else {
            if(credentials == null)
                return null;
            // Needed since Android is a bit bitchy with creating jsoup connections
            if (CGlobals.CURRENT_OS == OS.Android) {
                return Jsoup
                        .connect(webURL)
                        .header("Authorization", "Basic " + credentials.getBase64())
                        .get();
            } else { // Every other OS like Windows, Linux and Mac
                return Jsoup
                        .connect(webURL)
                        .header("Authorization", "Basic " + credentials.getBase64())
                        .timeout(0)
                        .get();
            }
        }
        //}
/*        catch (Exception e){
            // print a fancy message
            e.printStackTrace();
            return null;
        }*/
    }

    private Document EstablishConnection(String webURL, String userAgent){
        try {
            // Needed since Android is a bit bitchy with creating jsoup connections
            if (CGlobals.CURRENT_OS == OS.Android) {
                return Jsoup.connect(webURL).userAgent(userAgent).get();
            } else { // Every other OS like Windows, Linux and Mac
                return Jsoup.connect(webURL).timeout(0).userAgent(userAgent).get();
            }
        } catch (Exception e){
            // print a fancy message
            e.printStackTrace();
            return null;
        }
    }
    private Document EstablishConnection(String webURL, Map<String, String> data, String userAgent){
        try {
            // Needed since Android is a bit bitchy with creating jsoup connections
            if (CGlobals.CURRENT_OS == OS.Android) {
                return Jsoup.connect(webURL).userAgent(userAgent).data(data).post();
            } else { // Every other OS like Windows, Linux and Mac
                return Jsoup.connect(webURL).timeout(0).userAgent(userAgent).data(data).post();
            }
        } catch (Exception e){
            // print a fancy message
            e.printStackTrace();
            return null;
        }
    }
}

