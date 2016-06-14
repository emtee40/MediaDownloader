import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.select.Elements;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Creation time: 03:04
 * Created by Dominik on 23.04.2015.
 */
public class FacebookDownloader extends Downloader {
    private String fbLink;
    private String fbID;
    private boolean isAlbum;
    private boolean isSingleURL;
    private String url;

    public FacebookDownloader(String fbLink) {
        super();
        // determine if link is a album link
        // or complete profile (like: facebook.com/whatsapp)

        if (fbLink.contains("/media/")) {
            // id needed for processing json
            String[] URLArr = fbLink.split("/");
            String[] IDArr = URLArr[URLArr.length - 1].split("\\.");

            this.fbID = (IDArr[1].split("&"))[0];
            isAlbum = true;
        } else if (fbLink.contains("/?type") && fbLink.contains(("&theater"))) {
            // single url
            this.isSingleURL = true;
            this.url = fbLink;
        } else {
            String[] URLArr = fbLink.split("/");

            this.fbID = URLArr[URLArr.length - 1];
            isAlbum = false;
        }

        this.fbLink = fbLink;
        this.url = fbLink;
    }

    public FacebookDownloader() {
        super();
    }

    public void DownloadFile(String urls, long fileSize, int element, DefaultTableModel guiElements, String savePath) throws Exception {
        savePath = CheckSavePath(savePath);
        String[] URL_split = urls.split("/");

        if (!URL_split[URL_split.length - 1].contains("?"))
            super.DownloadFile(urls, savePath + URL_split[URL_split.length - 1], (int) fileSize, element, guiElements);
        else {
            String[] URL_further = (URL_split[URL_split.length - 1]).split("\\?");
            super.DownloadFile(urls, savePath + URL_further[0], (int) fileSize, element, guiElements);
        }
    }

    public String[] GetDownloadLinks() {
        if (isSingleURL) {
            JSoupAnalyze analyze = new JSoupAnalyze(url);
            Elements el = analyze.AnalyzeWithTag("img[class=fbPhotoImage img]");
            String[] urls = new String[el.size()];
            for (int i = 0; i < el.size(); i++) {
                urls[i] = el.get(i).attr("src");
            }
            return urls;
        }

        /*if(isAlbum && isVideo){
            return GetLinksFromAlbum("https://graph.facebook.com/v1.0/" + fbID + "/videos?fields=source");
        }
        else */
        if (isAlbum) {
            String[] allPictures = GetLinksFromAlbum("https://graph.facebook.com/v1.0/" + fbID + "/photos?access_token=CAAT0ftuZAxBABAINQnxJoqFmvzuMlpUmfKZB0dawalmb3f5XmL9U2zmi6LeIZB1x822JLs4Fq7BuX7B8RRghQMr9ZAElGAaQg27OPUFmiTDVVFzjpRNuKWM49QNWWxZCZAr76ljf2Okix74LU9YMQxMZC9b0uz6JdliBlFRkVKmQcM0RkODPETRbI8BbQILiQ4SkT7MPZBbciZB50VjaCefDr&fields=source");
            try {
                String[] videos = GetLinksFromAlbum("https://graph.facebook.com/v1.0/" + fbID + "/videos?access_token=CAAT0ftuZAxBABAINQnxJoqFmvzuMlpUmfKZB0dawalmb3f5XmL9U2zmi6LeIZB1x822JLs4Fq7BuX7B8RRghQMr9ZAElGAaQg27OPUFmiTDVVFzjpRNuKWM49QNWWxZCZAr76ljf2Okix74LU9YMQxMZC9b0uz6JdliBlFRkVKmQcM0RkODPETRbI8BbQILiQ4SkT7MPZBbciZB50VjaCefDr&fields=source");
                return Stream.concat(Arrays.stream(allPictures), Arrays.stream(videos))
                        .toArray(String[]::new);
            } catch (Exception ex) {
                return allPictures;
            }
        } else if (url.contains("/videos/")) {
            JSoupAnalyze webObj = new JSoupAnalyze(url);

            // pattern
            //http(.*?).(mp4)(.*?)\\u002522
            Pattern pattern = Pattern.compile("https(.*?).mp4(.*?)\\\\u002522");
            String site = webObj.GetSiteText();
            Matcher matcher = pattern.matcher(site);

            List<String> result = new ArrayList<String>();

            while (matcher.find()) {
                result.add(matcher.group());
            }

            String[] highest_res = new String[1];
            String url = result.get(result.size() - 1);

            try {
                ScriptEngineManager factory = new ScriptEngineManager();
                ScriptEngine engine = factory.getEngineByName("JavaScript");
                url = (String) engine.eval("unescape('" + url + "');");
            } catch (ScriptException e) {
                e.printStackTrace();
                return null;
            }

            highest_res[0] = url.replace("\\", "").replace("\"", "");
            return highest_res;
        } else if (!isAlbum) {
            String[] album_id = GetAlbumsFromProfile("https://graph.facebook.com/" + fbID + "/albums?access_token=CAAT0ftuZAxBABAINQnxJoqFmvzuMlpUmfKZB0dawalmb3f5XmL9U2zmi6LeIZB1x822JLs4Fq7BuX7B8RRghQMr9ZAElGAaQg27OPUFmiTDVVFzjpRNuKWM49QNWWxZCZAr76ljf2Okix74LU9YMQxMZC9b0uz6JdliBlFRkVKmQcM0RkODPETRbI8BbQILiQ4SkT7MPZBbciZB50VjaCefDr&fields=id");
            List<String> links = new ArrayList<>();

            for (int i = 0; i < album_id.length; i++) {
                String[] tmp = GetLinksFromAlbum("https://graph.facebook.com/v1.0/" + album_id[i] + "/photos?access_token=CAAT0ftuZAxBABAINQnxJoqFmvzuMlpUmfKZB0dawalmb3f5XmL9U2zmi6LeIZB1x822JLs4Fq7BuX7B8RRghQMr9ZAElGAaQg27OPUFmiTDVVFzjpRNuKWM49QNWWxZCZAr76ljf2Okix74LU9YMQxMZC9b0uz6JdliBlFRkVKmQcM0RkODPETRbI8BbQILiQ4SkT7MPZBbciZB50VjaCefDr&fields=source");

                Collections.addAll(links, tmp);
            }

            String[] allPictures = new String[links.size()];
            for (int i = 0; i < links.size(); i++) {
                allPictures[i] = links.get(i);
            }

            // if isVideo == true select all videos
            try {
                String[] videos = GetLinksFromAlbum("https://graph.facebook.com/v1.0/" + fbID + "/videos?access_token=" +
                        "CAAT0ftuZAxBABAINQnxJoqFmvzuMlpUmfKZB0dawalmb3f5XmL9U2zmi6LeIZB1x822JLs4Fq7BuX7B8RRghQMr9ZAElGAaQg27OPUFmiTDVVFzjpRNuKWM49QNWWxZCZAr76ljf2Okix74LU9YMQxMZC9b0uz6JdliBlFRkVKmQcM0RkODPETRbI8BbQILiQ4SkT7MPZBbciZB50VjaCefDr" +
                        "&fields=source");
                return Stream.concat(Arrays.stream(allPictures), Arrays.stream(videos))
                        .toArray(String[]::new);
            } catch (Exception ex) {
                return allPictures;
            }
        } else
            return null;
    }

    private String[] GetAlbumsFromProfile(String url) {
        try {
            JSONObject obj = readJsonFromUrl(url);
            String nextPage;
            try {
                nextPage = obj.getJSONObject("paging").getString("next");
            } catch (Exception ex) {
                nextPage = "";
            }

            JSONArray arr = obj.getJSONArray("data");
            List<String> itemlist = new ArrayList<>();

            for (int i = 0; i < arr.length(); i++) {
                try {
                    itemlist.add(arr.getJSONObject(i).getString("id"));
                } catch (Exception ex) {
                    // ignore because if no video id is found move on
                }
            }

            String[] items = new String[itemlist.size()];
            for (int i = 0; i < itemlist.size(); i++) {
                items[i] = itemlist.get(i);
            }

            if (nextPage.equals(""))
                return items;
            else
                return Stream.concat(Arrays.stream(items), Arrays.stream(GetLinksFromAlbum(nextPage)))
                        .toArray(String[]::new);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String[] GetLinksFromAlbum(String url) {
        String requestURL = url;

        try {
            JSONObject obj = readJsonFromUrl(requestURL);
            String nextPage;
            try {
                nextPage = obj.getJSONObject("paging").getString("next");
            } catch (Exception ex) {
                nextPage = "";
            }

            JSONArray arr = obj.getJSONArray("data");
            List<String> itemlist = new ArrayList<>();

            for (int i = 0; i < arr.length(); i++) {
                try {
                    itemlist.add(arr.getJSONObject(i).getString("source"));
                } catch (Exception ex) {
                    // ignore because if no video id is found move on
                }
            }

            String[] items = new String[itemlist.size()];
            for (int i = 0; i < itemlist.size(); i++) {
                items[i] = itemlist.get(i);
            }

            if (nextPage.equals(""))
                return items;
            else
                return Stream.concat(Arrays.stream(items), Arrays.stream(GetLinksFromAlbum(nextPage)))
                        .toArray(String[]::new);
        } catch (Exception ex) {
            System.err.println("No links found or private album!");
            return null;
        }
    }
}
