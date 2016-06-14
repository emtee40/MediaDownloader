import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Creation time: 23:19
 * Created by Dominik on 05.08.2015.
 */
public class REEngine {
    private String searchText;
    private String PATTERN;

    private Pattern pattern;
    private Matcher matcher;

    public REEngine(String searchText) {
        this.searchText = searchText;
        this.PATTERN = "\\b(((ht|f)tp(s?)\\:\\/\\/|~\\/|\\/)|www.)" +
                "(\\w+:\\w+@)?(([-\\w]+\\.)+(com|org|net|gov" +
                "|mil|biz|info|mobi|name|aero|jobs|museum" +
                "|travel|[a-z]{2}))(:[\\d]{1,5})?" +
                "(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?" +
                "((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" +
                "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)" +
                "(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" +
                "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*" +
                "(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b" +
                "(\\.(?i)(jpg|png|gif|bmp|webm|mp4|mp3|ogg|avi|flv))";
        pattern = pattern.compile(PATTERN);
    }

    public REEngine(String searchText, String PATTERN) {
        this.searchText = searchText;
        this.PATTERN = PATTERN;
        pattern = pattern.compile(PATTERN);
    }

    public List<String> GetMatches() {
        List<String> result = new ArrayList<String>();
        matcher = pattern.matcher(searchText);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }
}
