import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * @deprecated This class is deprecated. Better use - HTTPAnalyzer
 * Creation time: 03:05
 * Created by Dominik on 20.04.2015.
 */
public class JSoupAnalyze {
    private Document site;
    private boolean isFacebookLink;

    public JSoupAnalyze(String webURL) {
        try {
            // set to infinite time out ... may be its better
            site = Jsoup.connect(webURL).timeout(0).get();

            if (!webURL.contains("facebook"))
                return;

            // Notify the user we got a facebook link here and need proceed a bit different
            // (debug purposes)

            //System.out.println("Facebook link found - need to proceed a bit different");
            isFacebookLink = true;
            site = Jsoup.parse(site.toString().replace("<!--", ""));
            site = Jsoup.parse(site.toString().replace("&lt;!--", ""));
            site = Jsoup.parse(site.toString().replace("-->", ""));
            site = Jsoup.parse(site.toString().replace("--&gt;", ""));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSoupAnalyze(String webURL, String userAgent) {
        try {
            site = Jsoup.connect(webURL).timeout(0).userAgent(userAgent).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String GetSiteText() {
        return site.html();
    }

    public Elements AnalyzeWithTag(String analyzeTag) {
        return site.select(analyzeTag);
    }

    public static Document performFakeSubmit(String webURL, String userAgent) {
        try {
            // set to infinite time out ... may be its better
            Document fake = Jsoup.connect(webURL).timeout(0).userAgent(userAgent).get();
            String stepkey = "";
            Elements elements = fake.select("input[name=stepkey]");
            for (int i = 0; i < elements.size(); i++) {
                stepkey = elements.attr("value");
            }
            // should only be one!
            Document site = Jsoup.connect(webURL).timeout(0).userAgent(userAgent).data("stepkey", stepkey).data("submit", "submit").post();
            return site;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
