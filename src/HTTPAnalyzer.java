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

    public HTTPAnalyzerCode parse() throws HTTPAnalyzerException {
        try{
            if(credentials == null)
                site = EstablishConnection(this._URLPackage.getURL(),false);
            else
                site = EstablishConnection(this._URLPackage.getURL(),true);

            if(site != null)
                return new HTTPAnalyzerCode(200, "OK", true);
            else
                throw new HTTPAnalyzerException("Empty set", new Throwable("Parsing error"));
        }
        catch (HttpStatusException ex){
            int statusCode =  ex.getStatusCode();

            // Unauthorized
            if(statusCode == 401){
                throw new HTTPAnalyzerException("401 Unauthorized", new Throwable("No or wrong credentials provided"));
            }

            throw new HTTPAnalyzerException(statusCode + " " + ex.getMessage());
        }
        catch (IOException ex){
            throw new HTTPAnalyzerException(ex.getMessage());
        }
    }

    // Public Methods
    public String getSiteHtml(){
        return site.html();
    }

    public Document getDocument(){
        return site;
    }

    public HTTPCredentials getCredentials() { return credentials; }

    public void setSite(Document doc){
        this.site = doc;
    }

    // Public static Methods
    public static Document doFakeSubmit(Document site, String userAgent, Map<String, String> data, boolean useCredentials){
        Document fake = site;


        Document nSite = null;
        return nSite;
    }

    // Helper Methods
    private Document EstablishConnection(String webURL, boolean useCredentials) throws IOException {
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
    }

    private Document EstablishConnection(String webURL, String userAgent, boolean useCredentials) throws IOException {
        /*
            Establish connection without credentials
         */
        if(!useCredentials) {
            // Needed since Android is a bit bitchy with creating jsoup connections
            if (CGlobals.CURRENT_OS == OS.Android) {
                return Jsoup.connect(webURL).userAgent(userAgent).get();
            } else { // Every other OS like Windows, Linux and Mac
                return Jsoup.connect(webURL).timeout(0).userAgent(userAgent).get();
            }
        }

        /*
            Establish connection with credentials
        */
        else {
            if(credentials == null)
                return null; // maybe return custom error here

            if(CGlobals.CURRENT_OS == OS.Android) {
                return Jsoup
                        .connect(webURL)
                        .userAgent(userAgent)
                        .header("Authorization", "Basic " + credentials.getBase64())
                        .get();
            } else {
                return Jsoup
                    .connect(webURL)
                    .userAgent(userAgent)
                    .timeout(0)
                    .header("Authorization", "Basic " + credentials.getBase64())
                    .get();
            }
        }
    }

    private Document EstablishConnection(String webURL, Map<String, String> data, String userAgent, boolean useCredentials) throws IOException {
        /*
            Establish connection without credentials
         */
        if (!useCredentials) {
            // Needed since Android is a bit bitchy with creating jsoup connections
            if (CGlobals.CURRENT_OS == OS.Android) {
                return Jsoup.connect(webURL).userAgent(userAgent).data(data).post();
            } else { // Every other OS like Windows, Linux and Mac
                return Jsoup.connect(webURL).timeout(0).userAgent(userAgent).data(data).post();
            }
        }

        /*
            Establish connection with credentials
         */
        else {
            if (CGlobals.CURRENT_OS == OS.Android) {
                return Jsoup
                        .connect(webURL)
                        .userAgent(userAgent)
                        .data(data)
                        .header("Authorization", "Basic " + credentials.getBase64())
                        .post();
            } else { // Every other OS like Windows, Linux and Mac
                return Jsoup
                        .connect(webURL)
                        .timeout(0)
                        .userAgent(userAgent)
                        .data(data)
                        .header("Authorization", "Basic " + credentials.getBase64())
                        .post();
            }
        }
    }
}

