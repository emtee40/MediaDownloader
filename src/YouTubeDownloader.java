import org.jsoup.select.Elements;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Creation time: 03:05
 * Created by Dominik on 20.04.2015.
 */
public class YouTubeDownloader extends Downloader {
    private JSoupAnalyze webObj;

    private String vidUrl;
    private String vidID;
    private String vidTitle;
    private String savePath;

    private boolean isGemaUnblockerChecked;
    private SettingsManager settingsManager;

    public YouTubeDownloader(String ytLink, String savePath, boolean isGema){
        super();
        settingsManager = new SettingsManager();
        try {
            //set isGema to class variable if needed later
            isGemaUnblockerChecked = isGema;

            // get link
            this.vidUrl = ytLink;
            this.vidID = (ytLink.split("="))[1]; // first splitted is the vid id for sure

            if(!isGemaUnblockerChecked) {
                webObj = new JSoupAnalyze(vidUrl);
                vidTitle = webObj.AnalyzeWithTag("meta[property=og:title]").get(0).attr("content");
            }
            else {
                webObj = new JSoupAnalyze("http://video.genyoutube.com/" + vidID,
                        "Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0");
                vidTitle = webObj.AnalyzeWithTag("title").get(0).text().replace("Download ", "").replace(" - GenYoutube.com", "");
            }

            // get video title as file name
            vidTitle = validateFileName(vidTitle);

            // check if save path is correct otherwise fix it
            this.savePath = CheckSavePath(savePath);
        }catch (Exception ex){
            System.err.println("Crashed constructor!");
            System.err.println(vidTitle);
            ex.printStackTrace();
        }
    }

    public String getVideoURL(){
        // if no gema unblocker is checked analyze with my technique
        if(!isGemaUnblockerChecked){
            Elements allScriptTags = webObj.AnalyzeWithTag("script");
            String scriptTag = "";

            for (int i = 0; i < allScriptTags.size(); i++) {
                if(allScriptTags.get(i).outerHtml().contains("var ytplayer"))
                    scriptTag = allScriptTags.get(i).outerHtml();
            }

            String[] splitted = scriptTag.split(",");
            String videoFileLink = null;

            for (int i = 0; i < splitted.length; i++) {
                if(splitted[i].contains("\"url_encoded_fmt_stream_map\":")) {
                    videoFileLink = splitted[i].replace("\"url_encoded_fmt_stream_map\":", "");
                    break;
                }
            }

            if(videoFileLink != null) {
                String[] links = videoFileLink.split("=");
                String[] test = null;
                for (int i = 0; i < links.length; i++) {
                    if (links[i].contains("http"))
                        test = links[i].split("\\\\");
                }
                return decodeJScriptURL(test[0]);
            }
            else
                return "";

        }
        else {
            // 720p vids - always try to get the highest resolution to download for better quality!!
            Elements vidSources = webObj.AnalyzeWithTag("a[data-itag=22]");
            if(vidSources.size() == 0) {
                // 360p vids
                vidSources = webObj.AnalyzeWithTag("a[data-itag=18]");
            }
            String url = vidSources.get(0).attr("href");
            // check if url is correct
            if(url.contains("http"))
                return url;
            else
                return "";
        }
    }

    public void StartConvert() {
        String file = savePath + vidTitle + ".mp4";
        String outputfile = savePath + vidTitle + ".mp3";

        ProcessBuilder pb;
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                pb = new ProcessBuilder(settingsManager.GetFFMPEGDir().replace("{wd}",
                        System.getProperty("user.dir")) + "\\ffmpeg.exe", "-i",
                        file, "-vn", "-ab", "360k", "-acodec", "libmp3lame", outputfile); //or other command....
            } else if (System.getProperty("os.name").contains("nux")) {
                pb = new ProcessBuilder("ffmpeg", "-i", file, "-vn", "-ab", "360k",
                        "-acodec", "libmp3lame", outputfile);
            } else
                pb = null;

            Process p = pb.start();
            // i do not need to wait for this to finish
            // just start always a new ffmpeg instance
            //p.waitFor();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Couldn't start FFMPEG (please check FFMPEG path in the options " +
                            "[for Windows users] / please install FFMPEG for Linux users]",
                    "Error while starting converter - YouTubeDownloader", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void DownloadFile(String urls, int fileSize, int element, DefaultTableModel guiElements){
        if(guiElements != null)
            LinkHandler.AddMp4ToList(savePath + vidTitle + ".mp4");

        super.DownloadFile(urls, savePath + vidTitle + ".mp4", fileSize, element, guiElements);
    }
}
