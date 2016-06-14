import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;

/**
 * Creation time: 20:54
 * Created by Dominik on 07.07.2015.
 */
public class REDownloadWindow extends JDialog {
    private JLabel labelHowTo;
    private JButton btnDownload;
    private JScrollPane pane;
    private JTextArea txtLine;
    private String savePath;

    public REDownloadWindow(String text, String save) {
        savePath = save;
        labelHowTo = new JLabel("Please edit your line so that it is a valid URL, after that" +
                " you can hit the download button.");
        btnDownload = new JButton("Download");

        btnDownload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    URL url = new URL(txtLine.getText().replace("&amp;", "&")); // DIRTY - Need to decode url right and encode it correctly again!
                    System.out.println("[DOWNLOADING]: " + txtLine.getText());
                    String[] filename = txtLine.getText().toString().split("/");
                    InputStream in = new BufferedInputStream(url.openStream());
                    OutputStream out = new BufferedOutputStream(new FileOutputStream(savePath + filename[filename.length - 1]));

                    for (int j; (j = in.read()) != -1; ) {
                        out.write(j);
                    }
                    in.close();
                    out.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        txtLine = new JTextArea(text, 30, 30);
        pane = new JScrollPane(txtLine);

        getContentPane().add(labelHowTo, BorderLayout.NORTH);
        getContentPane().add(pane, BorderLayout.CENTER);
        getContentPane().add(btnDownload, BorderLayout.SOUTH);

        setTitle("R3DST0RMs Regular Expressions - Line Editor");
        setSize(new Dimension(600, 400));
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

