import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Creation time: 02:43
 * Created by Dominik on 04.08.2015.
 */
public class LinkHandler {
    private static List<String> currentMp4Files = new ArrayList<>();

    public static void AddURLToTable(String URL, String hoster, FreshUI window){
        if(DownloadPage.valueOf(hoster.toString()) == DownloadPage.RE_Explorer){
            new REExplorer();
        }

        if(hoster.toLowerCase().equals("facebook")){
            URL = URL.replace("photos_stream?tab=photos_stream", "");
            URL = URL.replace("photos_stream?tab=photos_albums", "");
            URL = URL.replace("photos_stream?tab=photos", "");


            // determine if real fb link
            if(URL.contains("/?type") && URL.contains(("&theater"))){
                FacebookDownloader fb = new FacebookDownloader(URL);
                String[] urls = fb.GetDownloadLinks();
                for (int i = 0; i < urls.length; i++) {
                    AddToTableModel(window, urls[i], window.tlDownloadDomain.getSelectedItem().toString(),
                            0, window.settingsManager.GetRemoveGEMA(), window.settingsManager.GetRemoveVidFiles(),
                            window.settingsManager.GetStandardSavePath(), window.settingsManager.GetConvertToMP3());
                }
            }
            else if(URL.contains("facebook")) {
                FacebookDownloader fb = new FacebookDownloader(URL);
                String[] pic = fb.GetDownloadLinks();

                for (int i = 0; i < pic.length; i++) {
                    AddToTableModel(window, pic[i], window.tlDownloadDomain.getSelectedItem().toString(),
                            0, window.settingsManager.GetRemoveGEMA(), window.settingsManager.GetRemoveVidFiles(),
                            window.settingsManager.GetStandardSavePath(), window.settingsManager.GetConvertToMP3());
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "No facebook link", "FacebookDownloader - Not a valid link", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(hoster.toLowerCase().equals("instagram")) {
            // Single Picture
            if(URL.contains("/p/") || URL.contains("?taken-by=")){
                InstagramDownloader ig = new InstagramDownloader(URL, window.settingsManager.GetStandardSavePath(),
                        false);
                String url = ig.GetURLsAndPreview();
                AddToTableModel(window, url, window.tlDownloadDomain.getSelectedItem().toString(),
                        0, window.settingsManager.GetRemoveGEMA(), window.settingsManager.GetRemoveVidFiles(),
                        window.settingsManager.GetStandardSavePath(), window.settingsManager.GetConvertToMP3());
            } else {
                // Crawl Profile
                String[] username = URL.split("/");
                InstagramDownloader insta = new InstagramDownloader(username[3], window.settingsManager.GetStandardSavePath());
                String userID = insta.fetchUserID("https://api.instagram.com/v1/users/" +
                        "search?q={user}&client_id=21ae9c8b9ebd4183adf0d0602ead7f05");
                System.out.println("Found userID: " + userID);
                insta.setSavePath(window.settingsManager.GetStandardSavePath() + "/" + userID);
                String[] urls = insta.fetchAllImageURLs(userID, "");

                for (int i = 0; i < urls.length; i++) {
                    AddToTableModel(window, urls[i], window.tlDownloadDomain.getSelectedItem().toString(),
                            0, window.settingsManager.GetRemoveGEMA(), window.settingsManager.GetRemoveVidFiles(),
                            window.settingsManager.GetStandardSavePath(), window.settingsManager.GetConvertToMP3());
                }
            }
        }
        else if(hoster.toLowerCase().equals("mixcloud") || hoster.toLowerCase().equals("nowvideo")
                || hoster.toLowerCase().equals("sharedsx") || hoster.toLowerCase().equals("soundcloud")
                || hoster.toLowerCase().equals("streamcloud") || hoster.toLowerCase().equals("vimeo")) {

            AddToTableModel(window, URL, window.tlDownloadDomain.getSelectedItem().toString(),
                    0, window.settingsManager.GetRemoveGEMA(), window.settingsManager.GetRemoveVidFiles(),
                    window.settingsManager.GetStandardSavePath(), window.settingsManager.GetConvertToMP3());

        }
        else if(hoster.toLowerCase().equals("youtube")) {
            // if not a correct youtube name assume that text is a youtube username
            if(URL.contains("youtube.com/") && !URL.contains("youtube.com/watch?v=")
                    || URL.contains("youtube.com/user/"))
            {
                // now add all videos from a channel if it is a channel
                String username = URL.replace("youtube.com/", "")
                        .replace("https://", "").replace("http://", "").replace("wwww.", "")
                        .replace("youtube.com/user/", "");

                YouTubeGetChannelVideos channelVideos = new YouTubeGetChannelVideos(username);
                String[] list = channelVideos.GetVideoList();

                if(list.length <= 0) {
                    JOptionPane.showMessageDialog(null, "No YouTube User matching your criteria",
                            "YouTubeDownloaderEngine - No user found!", JOptionPane.ERROR_MESSAGE);
                }
                else{
                    for (int i = 0; i < list.length; i++) {
                        AddToTableModel(window, "https://youtube.com/watch?v=" + list[i],
                                window.tlDownloadDomain.getSelectedItem().toString(),
                            0, window.settingsManager.GetRemoveGEMA(), window.settingsManager.GetRemoveVidFiles(),
                            window.settingsManager.GetStandardSavePath(), window.settingsManager.GetConvertToMP3());
                    }
                    JOptionPane.showMessageDialog(window,
                            "Added " + list.length + " videos recording to your download request: " +
                            username, "MediaDownloader - Added all channel videos", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else if(URL.contains("list=")){
                YouTubeRetrievePlaylist retrievePlaylist = new YouTubeRetrievePlaylist(URL);
                String[] elements = retrievePlaylist.getAllVideosFromPlaylist("");
                for (int i = 0; i < elements.length; i++) {
                    String seperator = "/";

                    if(System.getProperty("os.name").contains("Windows"))
                        seperator = "\\\\";

                    AddToTableModel(window, elements[i], window.tlDownloadDomain.getSelectedItem().toString(),
                            0, window.settingsManager.GetRemoveGEMA(), window.settingsManager.GetRemoveVidFiles(),
                            window.settingsManager.GetStandardSavePath() + seperator +
                                    retrievePlaylist.getPlayListTitle(), window.settingsManager.GetConvertToMP3());

                }

                JOptionPane.showMessageDialog(null, "Added " + elements.length + " links to the downloader"
                        , "MediaDownloader - Added all playlist videos", JOptionPane.INFORMATION_MESSAGE);
            }
            else if(URL.contains("youtube.com/"))
                AddToTableModel(window, URL, window.tlDownloadDomain.getSelectedItem().toString(),
                        0, window.settingsManager.GetRemoveGEMA(), window.settingsManager.GetRemoveVidFiles(),
                        window.settingsManager.GetStandardSavePath(), window.settingsManager.GetConvertToMP3());
            else{
                JOptionPane.showMessageDialog(null, "No youtube link", "YouTubeDownloaderEngine - Not a valid link",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        else
            return; // nothing i can do here cuz unknown host! should never happen (handled earlier in code)
    }

    public static void StartDownloading(FreshUI window) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < window.dTableModel.getRowCount(); i++) {
                    // Retrieve which hoster we have
                    DownloadPage hoster = DownloadPage.valueOf(window.dTableModel.getValueAt(i, 1).toString());

                    if (hoster == DownloadPage.Facebook) {
                        String url = window.dTableModel.getValueAt(i, 0).toString();
                        FacebookDownloader fbDownloader = new FacebookDownloader();
                        int size = fbDownloader.getDownloadSize(url);
                        fbDownloader.DownloadFile(url, size, i, window.dTableModel,
                                window.settingsManager.GetStandardSavePath());

                    } else if (hoster == DownloadPage.Instagram) {
                        String url = window.dTableModel.getValueAt(i, 0).toString();
                        InstagramDownloader instagramDownloader = new
                                InstagramDownloader(window.settingsManager.GetStandardSavePath());

                        int size = instagramDownloader.getDownloadSize(url);
                        instagramDownloader.DownloadFile(url, i, size, window.dTableModel);

                    } else if (hoster == DownloadPage.MixCloud) {
                        String url = window.dTableModel.getValueAt(i, 0).toString();
                        MixCloudDownloader mcDownloader = new MixCloudDownloader(url,
                                window.settingsManager.GetStandardSavePath());

                        String toDL = mcDownloader.GetMediaURL();
                        int size = mcDownloader.getDownloadSize(toDL);
                        mcDownloader.DownloadFile(toDL, size, i, window.dTableModel);

                    } else if (hoster == DownloadPage.NowVideo) {
                        NowVideoDownloader nwDownloader = new NowVideoDownloader(window.dTableModel.getValueAt(i, 0)
                                .toString(), window.settingsManager.GetStandardSavePath());

                        String url = nwDownloader.getVideoURL();
                        int size = nwDownloader.getDownloadSize(url);

                        String seperator = "/";
                        if (System.getProperty("os.name").contains("Windows"))
                            seperator = "\\";

                        nwDownloader.DownloadFile(url, window.settingsManager.GetStandardSavePath() + seperator
                                + "video_" + System.currentTimeMillis(), size, i, window.dTableModel);
                    } else if (hoster == DownloadPage.SharedSX) {
                        SharedSXDownloader sxDownloader = new SharedSXDownloader(window.dTableModel.
                                getValueAt(i, 0).toString());

                        String filename;

                        if (System.getProperty("os.name").contains("Windows"))
                            filename = window.settingsManager.GetStandardSavePath() + "\\" + sxDownloader.getFilename();
                        else
                            filename = window.settingsManager.GetStandardSavePath() + "/" + sxDownloader.getFilename();

                        String dlUrl = sxDownloader.getStreamURL();

                        sxDownloader.DownloadFile(dlUrl, filename, sxDownloader.getDownloadSize(dlUrl),
                                i, window.dTableModel);

                    } else if (hoster == DownloadPage.SoundCloud) {
                        String url = window.dTableModel.getValueAt(i, 0).toString();
                        SoundcloudDownloader scDownloader = new SoundcloudDownloader(url,
                                window.settingsManager.GetStandardSavePath());

                        String toDL = scDownloader.getAudioURL();
                        int size = scDownloader.getDownloadSize(toDL);
                        scDownloader.DownloadFile(toDL, size, i, window.dTableModel);

                    } else if (hoster == DownloadPage.StreamCloud) {
                        StreamCloudEUDownloader sceDownloader = new StreamCloudEUDownloader(window.dTableModel.
                                getValueAt(i, 0).toString());

                        String filename;

                        if (System.getProperty("os.name").contains("Windows"))
                            filename = window.settingsManager.GetStandardSavePath() + "\\" + sceDownloader.getFilename();
                        else
                            filename = window.settingsManager.GetStandardSavePath() + "/" + sceDownloader.getFilename();

                        String dlUrl = sceDownloader.getStreamURL();

                        sceDownloader.DownloadFile(dlUrl, filename, sceDownloader.getDownloadSize(dlUrl),
                                i, window.dTableModel);

                    } else if (hoster == DownloadPage.Vimeo) {
                        String vimURL = window.dTableModel.getValueAt(i, 0).toString();
                        String savePath = window.dTableModel.getValueAt(i, window.dTableModel.getColumnCount() - 1).toString();
                        VimeoDownloader vimDL = new VimeoDownloader(vimURL, savePath);
                        String vidURL = vimDL.getFileUrl();
                        int size = vimDL.getDownloadSize(vidURL);
                        vimDL.DownloadFile(vidURL, size, i, window.dTableModel);

                        if (window.dTableModel.getValueAt(i, window.dTableModel.getColumnCount() - 2).toString().equals("true"))
                            vimDL.StartConvert();

                        // remove mp4 files downloaded

                        boolean shallRemoved = Boolean.valueOf(window.dTableModel.getValueAt(i,
                                window.dTableModel.getColumnCount() - 3).toString());

                        if (System.getProperty("os.name").contains("Windows")) {
                            if (shallRemoved) {
                                String line;
                                String pidInfo = "";
                                try {
                                    boolean isRunning = true;
                                    while (isRunning) {
                                        Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");

                                        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

                                        while ((line = input.readLine()) != null) {
                                            pidInfo += line;
                                        }

                                        input.close();

                                        if (!pidInfo.contains("ffmpeg")) {
                                            isRunning = false;
                                            // now remove all mp4 files contained
                                            for (int j = 0; j < currentMp4Files.size(); j++) {
                                                try {
                                                    File delFile = new File(currentMp4Files.get(j));
                                                    if (delFile.exists())
                                                        delFile.delete();

                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                            currentMp4Files.clear();
                                        }

                                        pidInfo = "";
                                        line = "";
                                    }
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(null, "Error while checking for mp4 files. " +
                                                    "Please contact: admin@r3d-soft.de in order to fix this error!" +
                                                    " Error message: " + ex.getMessage(),
                                            "VimeoDownloaderEngine - Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        } else if (System.getProperty("os.name").contains("nux")) {
                            if (shallRemoved) {
                                String lines = "";
                                try {
                                    boolean isRunning = true;
                                    while (isRunning) {
                                        // Execute command
                                        String command = "ps aux";
                                        Process child = Runtime.getRuntime().exec(command);

                                        // Get the input stream and read from it
                                        InputStream in = child.getInputStream();
                                        int c;
                                        while ((c = in.read()) != -1) {
                                            lines += c;
                                        }
                                        in.close();

                                        if (!lines.contains("ffmpeg")) {
                                            isRunning = false;
                                            // now remove all mp4 files contained
                                            for (int j = 0; j < currentMp4Files.size(); j++) {
                                                try {
                                                    File delFile = new File(currentMp4Files.get(j));
                                                    if (delFile.exists())
                                                        delFile.delete();

                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                            currentMp4Files.clear();
                                        }
                                    }

                                } catch (IOException ex) {
                                    JOptionPane.showMessageDialog(null, "Error while checking for mp4 files. " +
                                                    "Please contact: admin@r3d-soft.de in order to fix this error!" +
                                                    " Error message: " + ex.getMessage(),
                                            "VimeoDownloaderEngine - Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        } else {
                            //
                            JOptionPane.showMessageDialog(null, "Error determine OS in order to delete mp4 files." +
                                            "Please contact: admin@r3d-soft.de in order to fix this error!" +
                                            " Error message: Unkown OS!",
                                    "VimeoDownloaderEngine - Error", JOptionPane.ERROR_MESSAGE);
                        }

                    } else if (hoster == DownloadPage.YouTube) {
                        String ytURL = window.dTableModel.getValueAt(i, 0).toString();
                        String savePath = window.dTableModel.getValueAt(i, window.dTableModel.getColumnCount() - 1).toString();
                        boolean isGema = Boolean.valueOf(window.dTableModel.
                                getValueAt(i, window.dTableModel.getColumnCount() - 4).toString());
                        YouTubeDownloader ytDL = new YouTubeDownloader(ytURL, savePath, isGema);
                        String vidURL = ytDL.getVideoURL();
                        int size = ytDL.getDownloadSize(vidURL);
                        ytDL.DownloadFile(vidURL, size, i, window.dTableModel);

                        if (window.dTableModel.getValueAt(i, window.dTableModel.getColumnCount() - 2).toString().equals("true"))
                            ytDL.StartConvert();

                        // remove mp4 files downloaded

                        boolean shallRemoved = Boolean.valueOf(window.dTableModel.getValueAt(i,
                                window.dTableModel.getColumnCount() - 3).toString());

                        if (System.getProperty("os.name").contains("Windows")) {
                            if (shallRemoved) {
                                String line;
                                String pidInfo = "";
                                try {
                                    boolean isRunning = true;
                                    while (isRunning) {
                                        Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");

                                        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

                                        while ((line = input.readLine()) != null) {
                                            pidInfo += line;
                                        }

                                        input.close();

                                        if (!pidInfo.contains("ffmpeg")) {
                                            isRunning = false;
                                            // now remove all mp4 files contained
                                            for (int j = 0; j < currentMp4Files.size(); j++) {
                                                try {
                                                    File delFile = new File(currentMp4Files.get(j));
                                                    if (delFile.exists())
                                                        delFile.delete();

                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                            currentMp4Files.clear();
                                        }

                                        pidInfo = "";
                                        line = "";
                                    }
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(null, "Error while checking for mp4 files. " +
                                                    "Please contact: admin@r3d-soft.de in order to fix this error!" +
                                                    " Error message: " + ex.getMessage(),
                                            "YouTubeDownloaderEngine - Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        } else if (System.getProperty("os.name").contains("nux")) {
                            if (shallRemoved) {
                                String lines = "";
                                try {
                                    boolean isRunning = true;
                                    while (isRunning) {
                                        // Execute command
                                        String command = "ps aux";
                                        Process child = Runtime.getRuntime().exec(command);

                                        // Get the input stream and read from it
                                        InputStream in = child.getInputStream();
                                        int c;
                                        while ((c = in.read()) != -1) {
                                            lines += c;
                                        }
                                        in.close();

                                        if (!lines.contains("ffmpeg")) {
                                            isRunning = false;
                                            // now remove all mp4 files contained
                                            for (int j = 0; j < currentMp4Files.size(); j++) {
                                                try {
                                                    File delFile = new File(currentMp4Files.get(j));
                                                    if (delFile.exists())
                                                        delFile.delete();

                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                            currentMp4Files.clear();
                                        }
                                    }

                                } catch (IOException ex) {
                                    JOptionPane.showMessageDialog(null, "Error while checking for mp4 files. " +
                                                    "Please contact: admin@r3d-soft.de in order to fix this error!" +
                                                    " Error message: " + ex.getMessage(),
                                            "YouTubeDownloaderEngine - Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        } else {
                            //
                            JOptionPane.showMessageDialog(null, "Error determine OS in order to delete mp4 files." +
                                            "Please contact: admin@r3d-soft.de in order to fix this error!" +
                                            " Error message: Unkown OS!",
                                    "YouTubeDownloaderEngine - Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                // JOB Finished Display
                JOptionPane.showMessageDialog(window, "Downloaded every URL, you entered, successfully. When you dispose this message the download list will be cleared.",
                        "MediaDownloader - Job finished", JOptionPane.INFORMATION_MESSAGE);

                for (int j = window.dTableModel.getRowCount() - 1; j > -1; j--) {
                    window.dTableModel.removeRow(j);
                }
            }
        });
        t.start();
    }

    private static void AddToTableModel(FreshUI window, Object url, Object hoster, Object progress,
                                        Object removeGema, Object removeVideo, Object SavePath, Object convertAudio){
        window.dTableModel.addRow(new Object[]{
                url,
                hoster, progress,
                removeGema,
                removeVideo,
                convertAudio,
                SavePath
        });
    }

    public static void AddMp4ToList(String s) {
        currentMp4Files.add(s);
    }
}
//
// Facebook, Instagram, MixCloud, NowVideo, SharedSX, SoundCloud, StreamCloud, Vimeo, YouTube