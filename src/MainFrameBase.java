import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Created by Robin on 09.06.2016.
 * <p>
 * Own base class for all JFrames in this program.
 */
public class MainFrameBase extends JFrame {

    public MainFrameBase(String sTitle) {
        super(sTitle);
    }

    public MainFrameBase() {
        super();
        setProgramIcon();
    }

    private void setProgramIcon(){
        // Program icon
        try {
            setIconImage(ImageIO.read(getClass().getResource("/resources/images/main_icon.png")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
