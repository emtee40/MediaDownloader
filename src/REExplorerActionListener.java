import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Creation time: 23:17
 * Created by Dominik on 05.08.2015.
 */
public class REExplorerActionListener implements ActionListener {
    private REExplorer currGUI;

    private String analyzeURL;

    public REExplorerActionListener(REExplorer currentGUI) {
        this.currGUI = currentGUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == currGUI.btnAnalyzeURL) {
            analyzeURL = currGUI.getURL();
            REWebsiteConnection webHTML = new REWebsiteConnection(analyzeURL);

            if (currGUI.getUsername().equals("")) {
                webHTML.CreateConnection();
            } else {
                char[] password = currGUI.getPassword();
                webHTML.CreateConnection(currGUI.getUsername(), String.valueOf(password));
            }

            REEngine regEx = new REEngine(webHTML.GetContent(), currGUI.getSelectedPattern());
            currGUI.setResultURLs(regEx.GetMatches());
        }

        if (e.getSource() == currGUI.btnDownloadFiles) {
            if (currGUI.getUsername().equals("")) {
                currGUI.downloadAllFiles();
            } else {
                char[] password = currGUI.getPassword();
                currGUI.downloadAllFiles(currGUI.getUsername(), String.valueOf(password));
            }
        }

        if (e.getSource() == currGUI.btnRemoveFromList) {
            currGUI.removeFromList();
        }

        if (e.getSource() == currGUI.btnGetWebsiteSource) {
            String website = currGUI.getURL();
            REWebsiteConnection webHTML = new REWebsiteConnection(website);

            if (currGUI.getUsername().equals("")) {
                webHTML.CreateConnection();
            } else {
                char[] password = currGUI.getPassword();
                webHTML.CreateConnection(currGUI.getUsername(), String.valueOf(password));
            }

            String webCode = webHTML.GetContent();
            new REDownloadWindow(webCode.replace(">", ">\n"), currGUI.getSettingsManager().GetStandardSavePath());
        }
    }
}
