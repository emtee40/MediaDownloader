import java.util.List;

/**
 * Creation time: 20:18
 * Created by Dominik on 09.04.2016.
 */
public class Crawler extends Downloader {
    private CrawlerFrame cwFrame;
    private List<String> direcotories;
    private List<String> files;

    public Crawler(CrawlerFrame cwFrame) {
        this.cwFrame = cwFrame;
    }

    /***
     * Crawls everything from the given url.
     *
     * @param webURL Website URL to crawl
     */
    public void Crawl(String webURL) {

    }

    public List<String> getDirecotories() {
        return direcotories;
    }

    public List<String> getFiles() {
        return files;
    }
}