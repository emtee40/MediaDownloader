import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Creation time: 23:14
 * Created by Dominik on 05.08.2015.
 */
public class REExplorer extends MainFrameBase {
    private REExplorerActionListener guiActionListener;

    private JTextField txtURLLine;
    private JTextField txtSavePath;
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public JButton btnAnalyzeURL;
    public JButton btnDownloadFiles;
    public JButton btnRemoveFromList;
    public JButton btnGetWebsiteSource;
    //public JTextArea patternCombo;
    public JComboBox patternCombo;
    public DefaultListModel listOfFoundItems;
    private JList list;
    private SettingsManager man;

    private JButton btnSaveCurrentPattern;

    private String[] objects;

    private String availablePatterns[] = {
            "<a href=\"(.*?)\">",
            "htt(p|ps):\\/\\/(.*?).(rar|zip|tar\\.gz|tar)",
            "\\b(((ht|f)tp(s?)\\:\\/\\/|~\\/|\\/)|www.)" +
                    "(\\w+:\\w+@)?(([-\\w]+\\.)+(com|org|net|gov" +
                    "|mil|biz|info|mobi|name|aero|jobs|museum" +
                    "|travel|[a-z]{2}))(:[\\d]{1,5})?" +
                    "(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?" +
                    "((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" +
                    "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)" +
                    "(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" +
                    "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*" +
                    "(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b" +
                    "(\\.(?i)(jpg|png|gif|bmp|webm|mp4|mp3|ogg|avi|flv))"};

    public REExplorer(SettingsManager manager) {
        man = manager;
        guiActionListener = new REExplorerActionListener(this);

        InitTopContainer();
        InitBottomContainer();
        InitMiddleContainer();
        InitGUI();
    }

    public REExplorer(SettingsManager manager, String url) {
        man = manager;
        guiActionListener = new REExplorerActionListener(this);

        InitTopContainer();
        InitBottomContainer();
        InitMiddleContainer();
        InitGUI();

        txtURLLine.setText(url);
    }

    private void InitBottomContainer() {
        JPanel bottomPanel = new JPanel(new GridLayout(0, 2));
        txtSavePath = new JTextField(man.GetStandardSavePath());
        btnDownloadFiles = new JButton("Download all!");
        btnDownloadFiles.addActionListener(guiActionListener);
        bottomPanel.add(txtSavePath);
        bottomPanel.add(btnDownloadFiles);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }

    private void InitMiddleContainer() {
        JPanel middlePanel = new JPanel(new BorderLayout());
        //patternCombo = new JComboBox(availablePatterns);
        //patternCombo.setEditable(true);
        listOfFoundItems = new DefaultListModel<>();
        //middlePanel.add(patternCombo, BorderLayout.NORTH);
        list = new JList(listOfFoundItems);
        list.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = list.locationToIndex(e.getPoint());
                    ListModel dlm = list.getModel();
                    Object item = dlm.getElementAt(index);
                    list.ensureIndexIsVisible(index);

                    // open dialog to allow editing of this line
                    new REDownloadWindow(item.toString(), txtSavePath.getText());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        JScrollPane scrollPane = new JScrollPane(list);
        //middlePanel.add(listOfFoundItems, BorderLayout.CENTER);
        middlePanel.add(scrollPane, BorderLayout.CENTER);

        getContentPane().add(middlePanel, BorderLayout.CENTER);
    }

    private void InitGUI() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);
        setSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setTitle("MediaDownloader - REExplorer (c) R3DST0RM 2015");
        setVisible(true);
    }

    private void InitTopContainer() {
        JPanel topPanel = new JPanel(new GridLayout(0, 3));
        topPanel.setBorder(BorderFactory.createTitledBorder("URL to analyze"));
        //topPanel.add(new JLabel("URL to analyze:"));
        txtURLLine = new JTextField("http://example.com");
        //txtURLLine.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btnAnalyzeURL = new JButton("Analyze");
        btnAnalyzeURL.addActionListener(guiActionListener);
        btnRemoveFromList = new JButton("Remove selected item from list");
        btnRemoveFromList.addActionListener(guiActionListener);
        btnGetWebsiteSource = new JButton("View website source");
        btnGetWebsiteSource.addActionListener(guiActionListener);

        // username password in order to access password protected sites (.htaccess)
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        topPanel.add(new JLabel("Username, Password"));
        topPanel.add(txtUsername);
        topPanel.add(txtPassword);

        topPanel.add(txtURLLine);
        topPanel.add(btnAnalyzeURL);
        topPanel.add(btnGetWebsiteSource);


        btnSaveCurrentPattern = new JButton("Save pattern");
        btnSaveCurrentPattern.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    man.WriteRegexToFile(patternCombo.getSelectedItem().toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        topPanel.add(new JLabel("Regular Expression:"));
        patternCombo = new JComboBox(availablePatterns);
        patternCombo.setEditable(true);
        topPanel.add(patternCombo);
        topPanel.add(btnSaveCurrentPattern);
        topPanel.add(new JLabel(""));
        topPanel.add(new JLabel(""));
        topPanel.add(btnRemoveFromList);

        getContentPane().add(topPanel, BorderLayout.NORTH);
    }

    public String getURL() {
        return txtURLLine.getText();
    }

    public String getSelectedPattern() {
        return patternCombo.getSelectedItem().toString();
    }

    public void setResultURLs(List<String> resultURLs) {
        listOfFoundItems.clear();

        objects = new String[resultURLs.size()];
        for (int i = 0; i < resultURLs.size(); i++) {
            objects[i] = resultURLs.get(i);
            listOfFoundItems.addElement(objects[i]);
            System.out.println(objects[i]);
        }
    }

    public void downloadAllFiles() {
        if (listOfFoundItems.size() <= 0) return;

        try {
            for (int i = 0; i < listOfFoundItems.size(); i++) {
                URL url = new URL(listOfFoundItems.get(i).toString().replace("&amp;", "&")); // DIRTY - Need to decode url right and encode it correctly again!
                //System.out.println("[DOWNLOADING]: " + listOfFoundItems.get(i));
                String[] filename = listOfFoundItems.get(i).toString().split("/");
                InputStream in = new BufferedInputStream(url.openStream());
                OutputStream out = new BufferedOutputStream(new FileOutputStream(txtSavePath.getText() + filename[filename.length - 1]));

                for (int j; (j = in.read()) != -1; ) {
                    out.write(j);
                }
                in.close();
                out.close();
            }

            JOptionPane.showMessageDialog(this, "Finished downloading!",
                    "MediaDownloader - REExplorer - Finished Downloading", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void downloadAllFiles(String username, String password) {
        if (listOfFoundItems.size() <= 0) return;

        try {
            for (int i = 0; i < listOfFoundItems.size(); i++) {
                URL url = new URL(listOfFoundItems.get(i).toString().replace("&amp;", "&"));
                URLConnection connection = url.openConnection();

                String userpass = username + ":" + password;
                String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());

                connection.setRequestProperty("Authorization", basicAuth);
                String[] filename = listOfFoundItems.get(i).toString().split("/");

                InputStream in = new BufferedInputStream(connection.getInputStream());
                OutputStream out = new BufferedOutputStream(new FileOutputStream(txtSavePath.getText() + filename[filename.length - 1]));

                for (int j; (j = in.read()) != -1; ) {
                    out.write(j);
                }
                in.close();
                out.close();
            }

            JOptionPane.showMessageDialog(this, "Finished downloading!",
                    "MediaDownloader - REExplorer - Finished Downloading", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void removeFromList() {
        if (list.getSelectedIndex() >= 0)
            listOfFoundItems.removeElementAt(list.getSelectedIndex());
        else {
            JOptionPane.showMessageDialog(this, "There is currently no item in your list.",
                    "MediaDownloader - REExplorer - Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public SettingsManager getSettingsManager() {
        return man;
    }

    public String getUsername() {
        return txtUsername.getText();
    }

    public char[] getPassword() {
        return txtPassword.getPassword();
    }
}
