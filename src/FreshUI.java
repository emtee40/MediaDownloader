import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * Creation time: 00:36
 * Created by Dominik on 04.08.2015.
 */
public class FreshUI extends JFrame implements ActionListener {
    private JTable downloadTable;
    public DefaultTableModel dTableModel;
    public JComboBox<DownloadPage> tlDownloadDomain;
    private JButton btnAddToList;
    private JButton btnDownload;
    private JTextField txtDownloadURL;
    public SettingsManager settingsManager;

    private final String[] tableHeader = { "Download URL", "Hoster", "Progress (in %)",
            "Remove GEMA", "Remove MP4", "Convert to MP3", "Local Path" };

    private JMenuBar menuBar;
    private JMenu menuMenu;
    private JMenu menuSettings;
    private JMenu menuHelp;
    private JMenuItem menuItemExit;
    private JMenuItem menuItemSettingsWindow;
    private JMenuItem menuItemHelp;
    private JMenuItem menuItemAbout;

    public FreshUI(){
        InitGUIComponents();
        InitActionListeners();
        InitWindowStandards();
    }

    private void InitActionListeners() {
        menuItemExit.addActionListener(e -> System.exit(0));
        menuItemHelp.addActionListener(e -> {
            String msg = "<html>This tool allows you to download various files from many social/video platforms (eg. YouTube)." +
                    "<br />For the YouTube Downloader following terms are supported:" +
                    "<ul>" +
                    "<li>user:<i>USERNAME</i> (Add all videos from a channel)</li>" +
                    "<li>https://wwww.youtube.com/watch?v=<i>VIDEOID</i> (Just adds this video to the download list)</li>" +
                    "<li>https://www.youtube.com/user/<i>USERNAME</i> (Also add all videos from a channel)</li>" +
                    "</ul>" +
                    "<b>For more information visit: <a href='http://r3d-soft.de/'>http://r3d-soft.de</a></b>" +
                    "</html>";

            JOptionPane.showMessageDialog(null, new JLabel(msg), "Help", JOptionPane.INFORMATION_MESSAGE);
        });
        menuItemAbout.addActionListener(e -> {
            String msg = "<html>" +
                    "Thanks for using <b>MediaDownloader v1.0b</b> - written by R3DST0RM.<br />" +
                    "This software uses ffmpeg as MP3 converter all licenses can be found here: bin/licenses/<br /><br />" +
                    "This software is free software (GNU General Public License v2) - Source Code available at request:<br /><br />" +
                    "E-Mail: <b>admin@r3d-soft.de</b><br />" +
                    "Website: <b>http://r3d-soft.de</b>" +
                    "</html>";

            JOptionPane.showMessageDialog(null, new JLabel(msg), "Help", JOptionPane.INFORMATION_MESSAGE);
        });
        menuItemSettingsWindow.addActionListener(e -> settingsManager.ShowSettingsWindow(this));
    }

    private void InitWindowStandards() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setTitle("MediaDownloader v1.0b (Fresh-UI)");
        setVisible(true);
    }

    private void InitGUIComponents() {
        // add menubar
        menuBar = new JMenuBar();
        menuMenu = new JMenu("Menu");
        menuSettings = new JMenu("Settings");
        menuHelp = new JMenu("Help");

        menuItemExit = new JMenuItem("Exit");
        menuItemSettingsWindow = new JMenuItem("Settings");
        menuItemHelp = new JMenuItem("Help - Usage");
        menuItemAbout = new JMenuItem("? - About this tool");

        menuMenu.add(menuItemExit);
        menuSettings.add(menuItemSettingsWindow);
        menuHelp.add(menuItemHelp);
        menuHelp.add(menuItemAbout);

        menuBar.add(menuMenu);
        menuBar.add(menuSettings);
        menuBar.add(menuHelp);

        settingsManager = new SettingsManager();

        tlDownloadDomain = new JComboBox<>();
        tlDownloadDomain.setModel(new DefaultComboBoxModel<>(DownloadPage.values()));

        downloadTable = new JTable();
        dTableModel = new DefaultTableModel();
        dTableModel.setColumnIdentifiers(tableHeader);
        downloadTable.getTableHeader().setReorderingAllowed(false);
        downloadTable.setModel(dTableModel);

        txtDownloadURL = new JTextField();
        btnAddToList = new JButton("Add to list");
        btnAddToList.addActionListener(this);
        btnDownload = new JButton("Download all!");
        btnDownload.addActionListener(this);

        // panel top
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel midTop = new JPanel(new GridLayout(0,6));
        midTop.add(new JLabel("Download URL: "));
        midTop.add(txtDownloadURL);
        midTop.add(new JLabel("Hoster (automatically set)"));
        midTop.add(tlDownloadDomain);
        midTop.add(btnAddToList);
        midTop.add(btnDownload);
        topPanel.add(midTop, BorderLayout.CENTER);
        topPanel.add(menuBar, BorderLayout.NORTH);
        // panel middle
        getContentPane().add(new JScrollPane(downloadTable), BorderLayout.CENTER);
        getContentPane().add(topPanel, BorderLayout.NORTH);

        SettingsManagerPanel sMP = new SettingsManagerPanel(settingsManager, this);
        getContentPane().add(sMP.getPanel(), BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());

            new FreshUI(); // get a fancy window
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            // handle exception - start with java native ui
            new FreshUI();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == btnAddToList){
            txtDownloadURL.setEditable(false);
            if(tlDownloadDomain.getSelectedItem().toString().equals("Auto_Detect")){
                System.out.println("Auto-Detect URL Host");
                try {
                    URL detectDomain = new URL(txtDownloadURL.getText());
                    int index = getDownloadPage(detectDomain.getHost());
                    if(index != -1) {
                        tlDownloadDomain.setSelectedIndex(index);
                        // add to list with auto detect host!
                        LinkHandler.AddURLToTable(txtDownloadURL.getText(),
                                tlDownloadDomain.getItemAt(index).toString(), this);

                        tlDownloadDomain.setSelectedIndex(0);
                        txtDownloadURL.setText("");
                    } else
                        JOptionPane.showMessageDialog(FreshUI.this, "No supported hoster found please enter a " +
                                        "supported URL.",
                                "Error - Unsupported URL", JOptionPane.ERROR_MESSAGE);
                }catch (Exception ex){
                    System.err.println("Malformed url try again");
                    JOptionPane.showMessageDialog(FreshUI.this, "Malformed URL detected. " +
                            "Please check the URL and try again.", "Error - Malformed URL", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // add to list without auto detect host
                LinkHandler.AddURLToTable(txtDownloadURL.getText(),
                        tlDownloadDomain.getSelectedItem().toString(), this);
            }

            txtDownloadURL.setEditable(true);
        }

        if(e.getSource() == btnDownload){
            // echo error cause no links are found
            if(dTableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(FreshUI.this, "No links provided. " +
                                "Please add links to the crawler (using the 'Add to list' button). " +
                                "If this error persists please contact admin@r3d-soft.de", "Error - No links provided",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Start Downloading!
            LinkHandler.StartDownloading(this);
        }
    }

    private int getDownloadPage(String toDetect){
        DownloadPage[] pages = DownloadPage.values();

        for (int i = 0; i < pages.length; i++) {
            if (toDetect.toLowerCase().contains(pages[i].toString().toLowerCase()))
                return i;
        }

        return -1;
    }
}

enum DownloadPage {
    Auto_Detect, Facebook, Instagram, MixCloud, NowVideo, SharedSX, SoundCloud, StreamCloud, Vimeo, YouTube, RE_Explorer
}