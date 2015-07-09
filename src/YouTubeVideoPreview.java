import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Creation time: 19:12
 * Created by Dominik on 08.07.2015.
 */
public class YouTubeVideoPreview {
    private String youtubeURL;
    private MediaPlayer mediaPlayer;
    private JFrame frame;

    public void initAndShowGUI(String url) {
        // This method is invoked on the EDT thread
        youtubeURL = url;
        frame = new JFrame("R3DST0RMs YouTube Preview Player");
        final JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel, BorderLayout.CENTER);
        frame.add(ControlPanel(), BorderLayout.SOUTH);
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayer.stop();
                frame.dispose();
            }
        });

        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);

                frame.pack();
            }
        });
    }

    private JPanel ControlPanel(){
        JButton btnPlay = new JButton("Play");
        JButton btnPause = new JButton("Pause");
        JButton btnStop = new JButton("Stop");

        JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        volumeSlider.setPaintLabels(true);
        volumeSlider.setPaintTicks(true);

        volumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double slider = (double)volumeSlider.getValue();
                mediaPlayer.setVolume((slider/100));
            }
        });

        btnPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.play();
            }
        });
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.stop();
            }
        });
        btnPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.pause();
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 3));
        panel.add(btnPlay);
        panel.add(btnPause);
        panel.add(btnStop);
        panel.add(new JLabel("Volume:"));
        panel.add(volumeSlider);
        return panel;
    }

    private void initFX(JFXPanel fxPanel) {
        // This method is invoked on the JavaFX thread
        Scene scene = createScene();
        fxPanel.setScene(scene);
    }

    private Scene createScene() {
        // Create media player
        Media media = new Media((youtubeURL != null) ? youtubeURL : "");
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        //MediaView mediaView = new MediaView(mediaPlayer);
        Group root = new Group();
        Scene scene = new Scene(root, 800, 600);
        //root.getChildren().add(mediaView);

        mediaPlayer.setVolume(0.5);
        scene.setFill(javafx.scene.paint.Color.BLACK);


        final MediaView mv = new MediaView(mediaPlayer);

        final DoubleProperty width = mv.fitWidthProperty();
        final DoubleProperty height = mv.fitHeightProperty();

        width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));

        mv.setPreserveRatio(true);
        root.getChildren().add(mv);

        return (scene);
    }
}