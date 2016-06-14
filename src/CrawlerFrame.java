import javax.swing.*;
import java.awt.*;

/**
 * Creation time: 20:17
 * Created by Dominik on 09.04.2016.
 */
public class CrawlerFrame extends MainFrameBase {
    private Crawler crawlerLogic;
    private FreshUI freshUIMain;

    private JTextField txtURL;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnCrawl;

    public CrawlerFrame(FreshUI freshUIMain) {
        this.freshUIMain = freshUIMain;

        InitGUI();
    }

    private void InitGUI() {
        //
        // Init logic
        crawlerLogic = new Crawler(this);

        //
        // Init gui components
        txtURL = new JTextField();
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        btnCrawl = new JButton("Crawl");
        btnCrawl.addActionListener(e -> {
            //
            // Decide what todo now
        });

        //
        // Top Panel
        JPanel topPanel = new JPanel(new GridLayout(0, 2));
        topPanel.add(new JLabel("URL:"));
        topPanel.add(txtURL);
        topPanel.add(new JLabel("Username:"));
        topPanel.add(txtUsername);
        topPanel.add(new JLabel("Password:"));
        topPanel.add(txtPassword);
        topPanel.add(new JLabel(""));
        topPanel.add(btnCrawl);
        getContentPane().add(topPanel, BorderLayout.NORTH);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("MediaDownloader - Index of Crawler");
        //setSize(800,600);
        pack();
        setLocationRelativeTo(freshUIMain);
    }

    public void showWindow() {
        setVisible(true);
    }
}
