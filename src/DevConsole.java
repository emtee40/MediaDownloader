import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Creation time: 17:48
 * Created by Dominik on 13.04.2016.
 */
public class DevConsole extends JFrame {
    private FreshUI freshUI;
    private JTextArea txtDev;
    private JScrollPane paneDev;

    public DevConsole(FreshUI freshUI){
        this.freshUI = freshUI;

        InitGUI();
    }

    private void InitGUI() {
        txtDev = new JTextArea("==== MediaDownloader Development Console ====\n");
        paneDev = new JScrollPane(txtDev);
        paneDev.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        getContentPane().add(paneDev, BorderLayout.CENTER);

        setTitle("MediaDownloader - Development Console");
        setSize(400,200);
        setLocationRelativeTo(freshUI);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        //setVisible(true);
    }

    public CustomOutputStream getStream(){
        return new CustomOutputStream(txtDev);
    }

    public void showConsole(){
        setVisible(true);
    }
}
/**
 * This class extends from OutputStream to redirect output to a JTextArrea
 * @author www.codejava.net
 *
 */
class CustomOutputStream extends OutputStream {
    private JTextArea textArea;

    public CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        // redirects data to the text area
        textArea.append(String.valueOf((char)b));
        // scrolls the text area to the end of data
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
