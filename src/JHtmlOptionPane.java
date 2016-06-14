import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;

/**
 * Created by robin on 08.06.2016.
 * <p>
 * Wrapper Class for Html-Optionpane with clickable links etc.
 */
public class JHtmlOptionPane {

    public static void showMessageDialog(Component parentComponent,
                                         String htmlMessage, String title, int messageType) {
        JEditorPane editorPane = new JEditorPane();
        editorPane.addHyperlinkListener(e -> {
            if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                try {
                    Desktop d = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                    if (d != null)
                        d.browse(e.getURL().toURI());
                } catch (Exception ex) {
                }
            }
        });
        editorPane.setOpaque(false); // transparent background
        editorPane.setHighlighter(null); // text not selectable
        editorPane.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
        editorPane.setEditable(false); // do not edit html
        editorPane.setText(htmlMessage);

        JOptionPane.showMessageDialog(parentComponent, editorPane, title, messageType);
    }
}
