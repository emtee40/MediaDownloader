import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Creation time: 15:16
 * Created by Dominik on 04.06.2015.
 */
public class NowVideoDownloader extends Downloader {
    private String nowVideoURL;
    private String keyPartToFind = "var fkzd=";
    private String key = "";
    private String videoID = "";
    private String apiCallURL = "http://www.nowvideo.sx/api/player.api.php?file={video_id}" +
            "&cid3=undefined&numOfErrors=0&user=undefined&pass=undefined&key={url_encoded_fkzd}" +
            "&cid=undefined&cid2=undefined";
    private JSoupAnalyze webObj;
    private int size;
    private String savePath;
    private NowVideoDownloadWindow downloadWindow;

    public NowVideoDownloader(String nowVideoURL, String savePath) {
        this.nowVideoURL = nowVideoURL;
        this.savePath = savePath;

        if (this.nowVideoURL.contains("embed")) {
            String[] idSplit = this.nowVideoURL.split("=");
            videoID = idSplit[idSplit.length - 1];
        } else {
            String[] idSplit = this.nowVideoURL.split("/");
            videoID = idSplit[idSplit.length - 1];
        }

        Document site = JSoupAnalyze.performFakeSubmit(this.nowVideoURL, "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0");
        //webObj = new JSoupAnalyze(this.nowVideoURL, "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0");
        //Elements scriptTags = webObj.AnalyzeWithTag("script");
        Elements scriptTags = site.select("script");
        String[] scriptSplit = null;

        for (int i = 0; i < scriptTags.size(); i++) {
            if (scriptTags.get(i).data().contains(keyPartToFind)) {
                scriptSplit = scriptTags.get(i).data().split("\n");
                break;
            }
        }

        if (scriptSplit == null)
            throw new NullPointerException("no script tag found!");

        for (int i = 0; i < scriptSplit.length; i++) {
            if (scriptSplit[i].contains(keyPartToFind)) {
                String trimmed = scriptSplit[i];
                key = trimmed.replace("var fkzd=", "").replace("\"", "").replace(";", "").trim();
            }
        }
    }

    public String getVideoURL() {
        try {
            apiCallURL = apiCallURL.replace("{video_id}", videoID).replace("{url_encoded_fkzd}", key);
            Document apiDoc = Jsoup.connect(apiCallURL).ignoreContentType(true).get();
            String split = (apiDoc.body().text().split("&"))[0].replace("url=", "").trim();
            return split + "?client=FLASH";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    @Override
    public int getDownloadSize(String urls) {
        size = super.getDownloadSize(urls);
        return size;
    }
}
