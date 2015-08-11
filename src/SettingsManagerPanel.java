import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.PrintWriter;

/**
 * Creation time: 20:15
 * Created by Dominik on 04.08.2015.
 */
public class SettingsManagerPanel extends JPanel {
    private JLabel lblSavePath;
    private JLabel lblConvertToMp3;
    private JLabel lblRemoveGEMA;
    private JLabel lblRemoveMp4;
    private JLabel lblFFMPEGFile;
    private JTextField txtSavePath;
    private JTextField txtFFMPEG;
    private JCheckBox checkConvertToMp3;
    private JCheckBox checkRemoveGEMA;
    private JCheckBox checkRemoveMp4;
    private JButton btnSave;
    private JButton btnCancel;
    private String settingsFile;
    private JButton btnSelectFFMPEG;
    private JButton btnSelectStandardSave;
    private JFileChooser dirChooser;
    private JPanel panel;
    private JLabel lblCheckMinSize;
    private JCheckBox checkMinimumSize;

    public SettingsManagerPanel(SettingsManager man, FreshUI win){
        //lblSavePath = new JLabel("Standard save path:");
        btnSelectStandardSave = new JButton("Select standard save path:");
        btnSelectStandardSave.addActionListener(e -> {
            String path = "";

            dirChooser = new JFileChooser();
            dirChooser.setDialogTitle("Select the path where files will be stored ...");
            dirChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            dirChooser.setAcceptAllFileFilterUsed(false);

            if (dirChooser.showOpenDialog(win) == JFileChooser.APPROVE_OPTION)
                path = dirChooser.getSelectedFile().getAbsolutePath();

            if (System.getProperty("os.name").contains("Windows"))
                path = path.replace("\\", "\\\\");

            txtSavePath.setText(path);
        });
        txtSavePath = new JTextField(man.GetStandardSavePath());
        lblConvertToMp3 = new JLabel("Convert to mp3");
        checkConvertToMp3 = new JCheckBox("", man.GetConvertToMP3());
        lblRemoveMp4 = new JLabel("Remove video files after mp3 created");
        checkRemoveMp4 = new JCheckBox("", man.GetRemoveVidFiles());
        lblRemoveGEMA = new JLabel("Remove GEMA");
        checkRemoveGEMA = new JCheckBox("", man.GetRemoveGEMA());
        lblCheckMinSize = new JLabel("Allow window to get smaller than minimum size (restart needed!)");
        checkMinimumSize = new JCheckBox("");
        //lblFFMPEGFile = new JLabel("FFMPEG-Directory");
        btnSelectFFMPEG = new JButton("Select FFMPEG-Directory");
        btnSelectFFMPEG.addActionListener(e -> {
            String path = "";

            dirChooser = new JFileChooser();
            dirChooser.setDialogTitle("Select the path where FFMPEG.exe is located");
            dirChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            dirChooser.setAcceptAllFileFilterUsed(false);

            if (dirChooser.showOpenDialog(win) == JFileChooser.APPROVE_OPTION)
                path = dirChooser.getSelectedFile().getAbsolutePath();

            if (System.getProperty("os.name").contains("Windows"))
                path = path.replace("\\", "\\\\");

            txtFFMPEG.setText(path);
        });
        txtFFMPEG = new JTextField(man.GetFFMPEGDir().replace("{wd}", System.getProperty("user.dir")));

        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> {
            // do nothing
            //vs setVisible(false);
        });
        btnSave = new JButton("Save");
        btnSave.addActionListener(e -> {
            try {
                File settings = new File(settingsFile);
                PrintWriter out = new PrintWriter(settings);
                out.println("savepath:" + txtSavePath.getText());
                out.println("converttomp3:" + checkConvertToMp3.isSelected());
                out.println("removegema:" + checkRemoveGEMA.isSelected());
                out.println("ffmpeg:" + txtFFMPEG.getText().replace(System.getProperty("user.dir"), "{wd}"));
                out.println("removeMp4:" + checkRemoveMp4.isSelected());
                out.println("removeMp4:" + checkRemoveMp4.isSelected());
                out.println("minSize:" + checkMinimumSize.isSelected());
                out.close();
                JOptionPane.showMessageDialog(win, "Successfully saved settings!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        panel = new JPanel(new GridLayout(0,2));
        //panel.add(lblSavePath);
        panel.add(btnSelectStandardSave);
        panel.add(txtSavePath);
        panel.add(lblConvertToMp3);
        panel.add(checkConvertToMp3);
        panel.add(lblRemoveMp4);
        panel.add(checkRemoveMp4);
        panel.add(lblRemoveGEMA);
        panel.add(checkRemoveGEMA);
        panel.add(lblCheckMinSize);
        panel.add(checkMinimumSize);
        //panel.add(lblFFMPEGFile);
        panel.add(btnSelectFFMPEG);
        panel.add(txtFFMPEG);
        panel.add(btnCancel);
        panel.add(btnSave);

        settingsFile = man.GetSettingsFile();
    }

    public JPanel getPanel(){
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return panel;
    }
}
