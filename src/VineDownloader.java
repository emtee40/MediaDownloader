import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Creation time: 15:27
 * Created by Dominik on 24.11.2015.
 */
public class VineDownloader extends Downloader {
    private String vineLink;
    private String vineUserID;
    private boolean isUser;
    private String[] vineList;

    private String baseURL = "https://vine.co/api/timelines/users/";

    public VineDownloader(String vineLink){
        super();

        if(vineLink.contains("/u/")) {
            this.isUser = true;
            String[] userID = vineLink.split("/");
            this.vineUserID = userID[userID.length - 1];
        }
        else
            this.isUser = false;

        this.vineLink = vineLink;
    }

    public String[] GetVines(){
        vineList = URLsFromJSON("", 1); // 1 because page counter doesn't start from 0
        return vineList;
    }

    private String[] URLsFromJSON(String anchor, int i){
        try{
            String url;

            if(anchor == "")
                url = baseURL + vineUserID;
            else
                url = baseURL + vineUserID + "?page=" + i + "&anchor=" + anchor + "&size=10";

            JSONObject obj = readJSONFromVine(url);
            String nextPage = "";
            try{
                nextPage = obj.getJSONObject("data").getString("anchorStr");
            }catch (Exception ex){
                System.err.println("No next page");
            }

            JSONArray arr = obj.getJSONObject("data").getJSONArray("records");
            List<String> itemlist = new ArrayList<>();

            for (int j = 0; j < arr.length(); j++) {
                try {
                    itemlist.add(arr.getJSONObject(j).getJSONArray("videoUrls").getJSONObject(0).getString("videoUrl"));
                }
                catch (Exception ex){
                    // ignore this case
                }
            }

            String[] items = new String[itemlist.size()];
            for (int j = 0; j < itemlist.size(); j++) {
                items[j] = itemlist.get(j);
            }

            i++; // increment page counter
            if(nextPage.equals(""))
                return items;
            else
                return Stream.concat(Arrays.stream(items), Arrays.stream(URLsFromJSON(nextPage, i)))
                        .toArray(String[]::new);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public void DownloadFile(String savePath, String urls, int element, int fileSize, DefaultTableModel guiElements) throws Exception{
        savePath = CheckSavePath(savePath);
        String[] URL_split = urls.split("/");
        String filename = (URL_split[URL_split.length - 1].split("\\?"))[0];
        super.DownloadFile(urls, savePath + filename, fileSize, element, guiElements);
    }

    public String GetUID() {
        if(this.isUser)
            return this.vineUserID;
        else
            return "";
    }
}
