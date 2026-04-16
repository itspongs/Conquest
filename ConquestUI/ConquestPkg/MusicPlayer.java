package ConquestPkg;

import javax.sound.sampled.*;
import java.io.File;
import java.net.URL;

public class MusicPlayer {

    private static Clip clip;

    public static void play(String filename) {
        stop(); // stop any currently playing music first
        try {
            URL url = MusicPlayer.class.getClassLoader().getResource("ConquestPkg/music/" + filename);
            if (url == null) {
                File f = new File("music/" + filename);
                if (f.exists()) url = f.toURI().toURL();
            }
            if (url == null) { System.err.println("[MusicPlayer] Not found: " + filename); return; }

            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(ais);

            FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(volume.getMinimum() + (volume.getMaximum() - volume.getMinimum()) * 0.65f);

            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}
