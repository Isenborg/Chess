package se.liu.marfr380;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;

public class AudioUtils {
    public static Clip loadClip(String fileName) {
	Clip clip = null;
	try {
	    // Load the sound file from the resources folder
	    URL url = AudioUtils.class.getClassLoader().getResource(fileName);
	    AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);

	    // Open the clip and return it
	    clip = AudioSystem.getClip();
	    clip.open(audioIn);

	} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
	    e.printStackTrace();
	}
	return clip;
    }
}
