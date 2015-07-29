import javax.swing.*;
import java.awt.*;

/**
 * Created by Dominik on 12.07.2015.
 */
public class MixCloudDownloaderPanel extends JPanel {
    private MixCloudDownloader mcDownloader;
    private SettingsManager settingsManager;

    private JTextField txtURL;
    private JTextField txtPath;
    private JFileChooser dirChooser;

    private JButton btnAddToList;
    private JButton btnRemoveFromList;
    private JButton btnSelectPath;
    private JButton btnStartDownload;

    private JScrollPane scrollBar;
    private DefaultListModel listModel;
    private JList listLinksGUI;

    private String listTitle;

    public MixCloudDownloaderPanel(){
        settingsManager = new SettingsManager();
        initComponents();
        initFileChooser();
        initActionListeners();
    }

    private void initActionListeners() {
        btnAddToList.addActionListener(e -> {
            // determine if real mc link
            if(txtURL.getText().contains("mixcloud")) {
                listModel.addElement(txtURL.getText());
            }
            else {
                JOptionPane.showMessageDialog(null, "No valid mixcloud link",
                        "MixCloudDownloader - Not a valid link", JOptionPane.ERROR_MESSAGE);
            }
            txtURL.setText("");
        });
        btnRemoveFromList.addActionListener(e -> {
            int index = listLinksGUI.getSelectedIndex();
            if(index > -1)
                listModel.removeElementAt(index);
        });
        btnSelectPath.addActionListener(e -> {
            String path = "";

            if (dirChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                path = dirChooser.getSelectedFile().getAbsolutePath();

            if(System.getProperty("os.name").contains("Windows"))
                path = path.replace("\\", "\\\\") + "\\";
            else
                path = path + "/";

            txtPath.setText(path);
        });
        btnStartDownload.addActionListener(e -> {
            if(txtPath.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Please select a download path",
                        "MixCloudDownloader - Select a valid path", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(listModel.size() <= 0) {
                JOptionPane.showMessageDialog(null,
                        "List is empty. Please add a mixcloud link in order to start the download process",
                        "MixCloudDownloader - List is empty", JOptionPane.ERROR_MESSAGE);
                return;
            }

            txtPath.setEditable(false);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < listModel.size(); i++) {
                        String url = listModel.get(i).toString();
                        mcDownloader = new MixCloudDownloader(url, txtPath.getText());
                        String toDL = mcDownloader.GetMediaURL();
                        long size = mcDownloader.getDownloadSize(toDL);
                        listTitle = "Size: " + (size / 1024) + "KB | " + listModel.get(i).toString();
                        mcDownloader.DownloadFile(toDL, (int) size, i, MixCloudDownloaderPanel.this);
                    }

                    JOptionPane.showMessageDialog(MixCloudDownloaderPanel.this, "Downloaded all audio files to: " + txtPath.getText(),
                            "MixCloudDownloader - Job finished", JOptionPane.INFORMATION_MESSAGE);
                    listModel.clear();
                    txtPath.setEditable(true);
                }
            });
            t.start();
        });
    }

    private void initFileChooser() {
        //
        // Standard File Chooser settings
        //
        dirChooser = new JFileChooser();
        dirChooser.setDialogTitle("Choose a path to save ...");
        dirChooser.setCurrentDirectory(new java.io.File(System.getProperty("user.dir")));
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dirChooser.setAcceptAllFileFilterUsed(false);
    }

    private void initComponents() {
        JPanel panelTop = new JPanel(new GridLayout(0,4));
        JLabel lblURL = new JLabel("MixCloud-Link:");
        txtURL = new JTextField("");
        btnAddToList = new JButton("Add to list");
        btnRemoveFromList = new JButton("Remove selected link");

        panelTop.add(lblURL);
        panelTop.add(txtURL);
        panelTop.add(btnAddToList);
        panelTop.add(btnRemoveFromList);

        scrollBar = new JScrollPane();
        listModel = new DefaultListModel();
        listLinksGUI = new JList(listModel);
        scrollBar.setViewportView(listLinksGUI);

        JPanel panelBottom = new JPanel(new GridLayout(0,3));
        txtPath = new JTextField(settingsManager.GetStandardSavePath());
        btnSelectPath = new JButton("Select Path");
        btnStartDownload = new JButton("Download all!");

        panelBottom.add(txtPath);
        panelBottom.add(btnSelectPath);
        panelBottom.add(btnStartDownload);

        // add all elements to gui
        this.setLayout(new BorderLayout());
        this.add(panelTop, BorderLayout.NORTH);
        this.add(scrollBar, BorderLayout.CENTER);
        this.add(panelBottom, BorderLayout.SOUTH);
    }

    public void setElementPercentage(String s, int element) {
        listModel.setElementAt(s + " | " + listTitle, element);
    }

}
