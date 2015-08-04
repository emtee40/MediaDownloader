import javax.swing.*;

/**
 * Creation time: 02:43
 * Created by Dominik on 04.08.2015.
 */
public class LinkHandler {

    public static void AddURLToTable(String URL, String hoster, FreshUI window){
        if(hoster.toLowerCase().equals("facebook")){

        }
        else if(hoster.toLowerCase().equals("instagram")) {

        }
        else if(hoster.toLowerCase().equals("mixcloud")) {

        }
        else if(hoster.toLowerCase().equals("nowvideo")) {

        }
        else if(hoster.toLowerCase().equals("sharedsx")) {

        }
        else if(hoster.toLowerCase().equals("soundcloud")) {

        }
        else if(hoster.toLowerCase().equals("streamcloud")) {

        }
        else if(hoster.toLowerCase().equals("vimeo")) {

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
                            "YouTubeDownloader - No user found!", JOptionPane.ERROR_MESSAGE);
                }
                else{
                    for (int i = 0; i < list.length; i++) {
                        window.dTableModel.addRow(new Object[]{
                                "https://youtube.com/watch?v=" + list[i],
                                window.tlDownloadDomain.getSelectedItem().toString(), 0,
                                window.settingsManager.GetRemoveGEMA(),
                                window.settingsManager.GetRemoveVidFiles(),
                                window.settingsManager.GetStandardSavePath()}
                        );
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

                    window.dTableModel.addRow(new Object[]{
                                    elements[i],
                                    window.tlDownloadDomain.getSelectedItem().toString(), 0,
                                    window.settingsManager.GetRemoveGEMA(),
                                    window.settingsManager.GetRemoveVidFiles(),
                                    window.settingsManager.GetStandardSavePath() + seperator +
                                            retrievePlaylist.getPlayListTitle()}
                    );
                }

                JOptionPane.showMessageDialog(null, "Added " + elements.length + " links to the downloader"
                        , "MediaDownloader - Added all playlist videos", JOptionPane.INFORMATION_MESSAGE);
            }
            else if(URL.contains("youtube.com/"))
                window.dTableModel.addRow(new Object[]{
                                URL,
                                window.tlDownloadDomain.getSelectedItem().toString(), 0,
                                window.settingsManager.GetRemoveGEMA(),
                                window.settingsManager.GetRemoveVidFiles(),
                                window.settingsManager.GetStandardSavePath()}
                );
            else{
                JOptionPane.showMessageDialog(null, "No youtube link", "YouTubeDownloaderEngine - Not a valid link",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        else
            return; // nothing i can do here cuz unknown host! should never happen (handled earlier in code)
    }

    public static void StartDownloading() {

    }
}
//
// Facebook, Instagram, MixCloud, NowVideo, SharedSX, SoundCloud, StreamCloud, Vimeo, YouTube