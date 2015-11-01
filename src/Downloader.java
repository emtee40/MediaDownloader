import org.json.JSONException;
import org.json.JSONObject;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Creation time: 03:04
 * Created by Dominik on 01.06.2015.
 */
public abstract class Downloader {

    // Methods any Downloader need
    public boolean isFileExisting(File fileToCheck){
        try {
            if (fileToCheck.exists())
                return true;
            else
                return false;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public int getDownloadSize(String urls){
        URLConnection hUrl;
        try {
            hUrl = new URL(urls).openConnection();
            int size = hUrl.getContentLength();
            return size;

        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String CheckSavePath(String pathToCheck) {
        if(System.getProperty("os.name").contains("Windows")) {
            if (!pathToCheck.endsWith("\\")) {
                pathToCheck = pathToCheck + "\\";
            }

            if (!Files.isDirectory(Paths.get(pathToCheck))) {
                try {
                    Files.createDirectory(Paths.get(pathToCheck));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return pathToCheck;
        }
        else if(System.getProperty("os.name").contains("nux")){
            if(!pathToCheck.endsWith("/"))
                pathToCheck = pathToCheck + "/";

            if(!Files.isDirectory(Paths.get(pathToCheck))){
                try{
                    Files.createDirectory(Paths.get(pathToCheck));
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            return pathToCheck;
        }
        else
            return pathToCheck;
    }

    public String validateFileName(String name){
        if(name.contains("|"))
            name = name.replace("|", "_");

        if(name.contains(">"))
            name = name.replace(">", "_");

        if(name.contains("<"))
            name = name.replace("<", "_");

        if(name.contains("\""))
            name = name.replace("\"", "_");

        if(name.contains("?"))
            name = name.replace("?", "_");

        if(name.contains("*"))
            name = name.replace("*", "_");

        if(name.contains(":"))
            name = name.replace(":", "_");

        if(name.contains("\\\\"))
            name = name.replace("\\\\", "_");

        if(name.contains("/"))
            name = name.replace("/", "_");

        return name;
    }

    public String decodeJScriptURL(String toDecode){
        try {
            ScriptEngineManager factory = new ScriptEngineManager();
            ScriptEngine engine = factory.getEngineByName("JavaScript");
            return (String) engine.eval("unescape('" + toDecode + "')");
        } catch (ScriptException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject readJsonFromUrl(String url) throws JSONException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }catch (Exception ex){
            return null;
        }
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    /*
    public void DownloadFile(String dlUrl, String filename, int downloadSize, int i, DefaultTableModel dTableModel) {
        try {
            URL url = new URL(dlUrl);
            InputStream in = new BufferedInputStream(url.openStream());
            OutputStream out = new BufferedOutputStream(new FileOutputStream(filename + ".mp4"));

            double sum = 0;
            int count;
            byte data[] = new byte[1024];
            // added a quick fix for downloading >= 0 instead of != -1
            while ((count = in.read(data, 0, 1024)) >= 0) {
                out.write(data, 0, count);
                sum += count;

                if (downloadSize > 0 && dTableModel != null) {
                    dTableModel.setValueAt(((int)(sum / downloadSize * 100)) + "%", i, 2);
                }
            }


            in.close();
            out.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    } */

    public void DownloadFile(String dlUrl, String filename, int downloadSize, int i, DefaultTableModel dTableModel) {
        try {
            HttpURLConnection connection = (HttpURLConnection)new URL(dlUrl).openConnection();
            File outputFileCache = new File(filename);
            long downloadedSize = 0;
            long fileLength = 0;
            BufferedInputStream input = null;
            RandomAccessFile output = null;

            if (outputFileCache.exists()) {
                connection.setAllowUserInteraction(true);
                connection.setRequestProperty("Range", "bytes=" + outputFileCache.length() + "-");
            }

            connection.setConnectTimeout(14000);
            connection.setReadTimeout(20000);
            connection.connect();

            if (connection.getResponseCode() / 100 != 2)
                System.err.println("Unknown response code!");
            else {
                String connectionField = connection.getHeaderField("content-range");

                if (connectionField != null) {
                    String[] connectionRanges = connectionField.substring("bytes=".length()).split("-");
                    downloadedSize = Long.valueOf(connectionRanges[0]);
                }

                if (connectionField == null && outputFileCache.exists())
                    outputFileCache.delete();

                fileLength = connection.getContentLength() + downloadedSize;
                input = new BufferedInputStream(connection.getInputStream());
                output = new RandomAccessFile(outputFileCache, "rw");
                output.seek(downloadedSize);

                byte data[] = new byte[1024];
                int count = 0;
                int __progress = 0;

                while ((count = input.read(data, 0, 1024)) != -1
                        && __progress != 100) {
                    downloadedSize += count;
                    output.write(data, 0, count);
                    __progress = (int) ((downloadedSize * 100) / fileLength);

                    dTableModel.setValueAt(__progress + "%", i, 2);
                }

                output.close();
                input.close();
            }
        }catch (Exception ex){
            System.err.println(ex.getMessage() + " Error occured while downloading!");
        }
    }
}
