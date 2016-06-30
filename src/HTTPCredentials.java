import java.util.Base64;

/**
 * Created by robin on 30.06.2016.
 *
 * Class for authentication credentials.
 * Could be used for htaccess (indexof)
 */
public class HTTPCredentials {
    public String user;
    public String password;

    public HTTPCredentials(String sUser, String sPassword){
        user = sUser;
        password = sPassword;
    }

    public String getBase64(){
        return new String(Base64.getEncoder().encode((user+":"+password).getBytes()));
    }
}
