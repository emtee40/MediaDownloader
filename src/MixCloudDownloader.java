import javax.swing.table.DefaultTableModel;

/**
 * Created by Dominik on 12.07.2015.
 * @deprecated
 */
public class MixCloudDownloader extends Downloaderv2 {
    private String mixcloudURL;
    private String previewURL;
    private String savePath;
    private String fileName;

    private final String replacePreview = "c/m4a/64";
    private final String replacePreviewMP3 = ".m4a";
    private final String replaceAudioCDN = "stream";

    private JSoupAnalyze webObj;

    public MixCloudDownloader(String url, String path) {
        this.mixcloudURL = url;
        this.savePath = path;
    }

    public String GetMediaURL() {
        webObj = new JSoupAnalyze(this.mixcloudURL, "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
        previewURL = webObj.AnalyzeWithTag("span[class=play-button play-button-cloudcast-page]")
                .attr("m-preview");

        return previewURL.replace("previews", replacePreview).replace(".mp3", replacePreviewMP3)
                .replace("audiocdn", replaceAudioCDN);
    }

    public void DownloadFile(String urls, int fileSize, int element, DefaultTableModel guiElements) throws Exception {
        webObj = new JSoupAnalyze(this.mixcloudURL, "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
        fileName = this.CheckSavePath(webObj.AnalyzeWithTag("meta[property=og:title]").attr("content") + ".m4a");
        super.DownloadFile(urls, savePath + fileName, fileSize, element, guiElements);
    }
}
