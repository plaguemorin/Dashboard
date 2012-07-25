package ca.screenshot.dashboard;

import org.junit.Test;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;

/**
 * User: plaguemorin
 * Date: 23/07/12
 * Time: 6:00 PM
 */
public class TestOther {
	@Test
	public void getAvailLines() {
		final Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
		for(final Mixer.Info info : mixerInfo) {
			System.out.println(info.getName() + " " + info.getDescription() + " (" + info.getVendor() + ")");

			final Mixer mixer = AudioSystem.getMixer(info);
			for (final Line line : mixer.getSourceLines()) {
				System.out.println(" = " + line.getLineInfo().toString());
			}

			for (final Line line : mixer.getTargetLines()) {
				System.out.println(" = " + line.getLineInfo().toString());
			}
		}


	}
}
