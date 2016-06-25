/**
 * Created by dominik on 25.06.16.
 */
public class URLHandler {

    /***
     * Creates an URL package which contains all necessary data about an URL
     * This package is platform independent which means it can be used to display on ANY OS.
     * @param url
     * @return Returns an URLPackage which can be added to the GUI
     */
    public URLPackage CreateURLPackage(String url){
        return new URLPackage(url);
    }
}

class URLPackage {
    private String strHoster;
    private String strURL;

    public URLPackage(String url){
        this.strURL = url;
    }

    public String getURL(){
        return this.strURL;
    }
}
