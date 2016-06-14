import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Creation time: 03:05
 * Created by Dominik on 01.06.2015.
 */
public class VimeoDownloader extends Downloader {
    private SettingsManager settingsManager;
    private String vimeoUrl;
    private String savePath;
    private String videoFileUrl;
    private String fileTitle;

    private JSoupAnalyze webObj;

    public VimeoDownloader(String vimeoUrl, String savePath) {
        super();
        settingsManager = new SettingsManager();
        this.vimeoUrl = vimeoUrl;
        this.savePath = CheckSavePath(savePath);

        // check for div & og:title for needed strings
        webObj = new JSoupAnalyze(this.vimeoUrl);
        this.fileTitle = validateFileName(webObj.AnalyzeWithTag("meta[property=og:title]").attr("content"));
        this.videoFileUrl = webObj.AnalyzeWithTag("div[class*=player js-player]").attr("data-config-url");
    }

    public String getFileUrl() {
        JSONObject obj = null;
        try {
            obj = readJsonFromUrl(this.videoFileUrl);
            return obj.getJSONObject("request").getJSONObject("files")
                    .getJSONObject("h264").getJSONObject("hd").getString("url");
        } catch (Exception ex) {
            if (obj != null)
                return obj.getJSONObject("request").getJSONObject("files").getJSONObject("h264").getJSONObject("sd").getString("url");
            else
                return "null";
        }
    }

    public void DownloadFile(String urls, int fileSize, int element, DefaultTableModel guiElements) throws Exception {
        if (guiElements != null)
            LinkHandler.AddMp4ToList(savePath + this.fileTitle + ".mp4");
        super.DownloadFile(urls, savePath + this.fileTitle + ".mp4", fileSize, element, guiElements);
    }

    public void StartConvert() {
        String file = savePath + this.fileTitle + ".mp4";
        String outputfile = savePath + this.fileTitle + ".mp3";

        ProcessBuilder pb;
        try {
            if (CGlobals.CURRENT_OS == OS.Windows) {
                pb = new ProcessBuilder(settingsManager.GetFFMPEGDir().replace("{wd}", System.getProperty("user.dir")) + "\\ffmpeg.exe", "-i", file, "-vn", "-ab", "360k", "-acodec", "libmp3lame", outputfile); //or other command....
            } else if (CGlobals.CURRENT_OS == OS.Linux) {
                pb = new ProcessBuilder("ffmpeg", "-i", file, "-vn", "-ab", "360k", "-acodec", "libmp3lame", outputfile);
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
                    "Error while starting converter - VimeoDownloader", JOptionPane.ERROR_MESSAGE);
        }
    }
}
