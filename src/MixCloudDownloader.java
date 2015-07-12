import java.io.*;
import java.net.URL;

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
        webObj = new JSoupAnalyze(this.mixcloudURL);
        previewURL = webObj.AnalyzeWithTag("span[class=play-button play-button-cloudcast-page]")
                .attr("m-preview");

        return previewURL.replace("previews", replacePreview).replace(".mp3", replacePreviewMP3);
    }

    public void DownloadFile(String urls, int fileSize, int element, MixCloudDownloaderPanel guiElements){
        try {
            URL url = new URL(urls);
            InputStream in = new BufferedInputStream(url.openStream());
            String[] splitted = urls.split("/");
            OutputStream out = new BufferedOutputStream(new FileOutputStream(savePath + splitted[splitted.length - 1]));

            double sum = 0;
            int count;
            byte data[] = new byte[1024];
            // added a quick fix for downloading >= 0 instead of != -1
            while ((count = in.read(data, 0, 1024)) >= 0) {
                out.write(data, 0, count);
                sum += count;

                if (fileSize > 0 && guiElements != null) {
                    guiElements.setElementPercentage(((int)(sum / fileSize * 100)) + "%", element);
                }
            }


            in.close();
            out.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
