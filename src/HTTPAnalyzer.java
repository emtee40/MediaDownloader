import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
    private Document site;

    public HTTPAnalyzer(URLPackage url){
        this._URLPackage = url;
        site = EstablishConnection(this._URLPackage.getURL());
    }

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
    private Document EstablishConnection(String webURL) {
        try {
            // Needed since Android is a bit bitchy with creating jsoup connections
            if (CGlobals.CURRENT_OS == OS.Android) {
                return Jsoup.connect(webURL).get();
            } else { // Every other OS like Windows, Linux and Mac
                return Jsoup.connect(webURL).timeout(0).get();
            }
        } catch (Exception e){
            // print a fancy message
            e.printStackTrace();
            return null;
        }
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
