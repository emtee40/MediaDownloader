import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DLCManager handles the DLC-Files. Which are needed to save downloads for a later purpose
 *
 * @author R3DST0RM
 * @version 0.1
 */
public class DLCManager {
    private FreshUI freshUI;

    public DLCManager(FreshUI freshUI) {
        this.freshUI = freshUI;
    }

    public void ExportDLC() {
        ExportDialog exDia = new ExportDialog(freshUI);
        String path = exDia.askSave();
        if (writeLinksToFile(path)) {
            JOptionPane.showMessageDialog(freshUI, "Successfully exported current download list",
                    "Download list exported", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Print error
            JOptionPane.showMessageDialog(freshUI, "Couldn't write DLC to disk please try again.",
                    "Error while saving", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void ImportDLC() {
        ImportDialog imDia = new ImportDialog(freshUI);
        String[] urlsToAdd = imDia.askLoad();

        if (urlsToAdd == null)
            return;

        for (int i = 0; i < urlsToAdd.length; i++) {
            try {
                // simulate an insertion of the specific url
                freshUI.txtDownloadURL.setText(urlsToAdd[i]);
                freshUI.btnAddToList.doClick();
            } catch (Exception e) {
                // in general there should be no error in doing it
                e.printStackTrace();
            }
        }
    }

    private boolean writeLinksToFile(String filePath) {
        try {
            // prevent from saving an empty set of download links
            if (freshUI.dTableModel.getRowCount() <= 0)
                return false;

            File dlc = new File(filePath);
            PrintWriter out = new PrintWriter(dlc);
            for (int i = 0; i < freshUI.dTableModel.getRowCount(); i++) {
                out.println(freshUI.dTableModel.getValueAt(i, 0));
            }
            out.close();

            // everything was fine
            return true;
        } catch (Exception e) {
            System.err.println("Error while saving DLC");
            return false;
        }
    }
}

class ImportDialog extends JDialog {
    private FreshUI freshUI;

    public ImportDialog(FreshUI freshUI) {
        this.freshUI = freshUI;
    }

    public String[] askLoad() {
        JFileChooser chooser = new JFileChooser();
        int retVal = chooser.showOpenDialog(freshUI);

        if (retVal == JFileChooser.APPROVE_OPTION) {
            String fileName = chooser.getSelectedFile().getAbsolutePath();
            File file = new File(fileName);

            if (!file.canRead() || !file.isFile())
                return null;

            BufferedReader in = null;
            List<String> urlList = new ArrayList<String>();

            try {
                in = new BufferedReader(new FileReader(fileName));
                String line = null;
                while ((line = in.readLine()) != null) {
                    urlList.add(line);
                }

                // provide an array as ret value
                String[] urlArray = new String[urlList.size()];
                urlArray = urlList.toArray(urlArray);
                return urlArray;
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(freshUI, "There was an error opening your dlc - please try again",
                        "Error while opening", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } else {
            return null;
        }
    }
}

class ExportDialog extends JDialog {
    private FreshUI freshUI;

    public ExportDialog(FreshUI freshUI) {
        this.freshUI = freshUI;
    }

    public String askSave() {
        JFileChooser chooser = new JFileChooser();
        chooser.showSaveDialog(freshUI);

        return chooser.getSelectedFile().toString();
    }
}
