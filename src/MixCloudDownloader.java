import javax.swing.table.DefaultTableModel;

/**
 * Created by Dominik on 12.07.2015.
 */
public class MixCloudDownloader extends Downloader {
    private String mixcloudURL;
    private String previewURL;
    private String savePath;

    private final String replacePreview = "c/m4a/64";
    private final String replacePreviewMP3 = ".m4a";

    private JSoupAnalyze webObj;

    public MixCloudDownloader(String url, String path){
        this.mixcloudURL = url;
        this.savePath = path;
    }

    public String GetMediaURL(){
        webObj = new JSoupAnalyze(this.mixcloudURL, "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
        previewURL = webObj.AnalyzeWithTag("span[class=play-button play-button-cloudcast-page]")
                .attr("m-preview");

        return previewURL.replace("previews", replacePreview).replace(".mp3", replacePreviewMP3);
    }

    public void DownloadFile(String urls, int fileSize, int element, DefaultTableModel guiElements) throws Exception{
        String[] splitted = urls.split("/");
        super.DownloadFile(urls, savePath + splitted[splitted.length -1], fileSize, element, guiElements);
    }
}
