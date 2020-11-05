import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.player.advanced.*;

/**
 * This class is used to open and play MP3 files.
 * @author Casey Devet (casey.devet@sccdsb.net)
 */
public class MP3Player
{
	private String filename;
	private AdvancedPlayer player;

	private boolean repeats = false;
	private String nextFilename;

	private boolean openFlag = false;
	private boolean playing = false;

	/**
	 * Opens an MP3 file and sets up playback.
	 * @param filename The MP3 file to open.
	 */
	public MP3Player (String filename)
	{
		this.filename = filename;
		open();
		player.setPlayBackListener(new FinishedHandler());
	}

	/**
	 * Reopens the MP3 file.  This method is useful when playback has finished.
	 */
	public void open ()
	{
		try {
			FileInputStream fis     = new FileInputStream(filename);
			BufferedInputStream bis = new BufferedInputStream(fis);
			player = new AdvancedPlayer(bis);
		}
		catch (Exception e) {
			System.out.println("Problem loading file " + filename);
			System.out.println(e);
		}
		openFlag = true;
	}

	/**
	 * Plays the MP3 file to the sound card.  If the file is not open, this method
	 * will open it first.
	 */
	public void play ()
	{
		if (!openFlag)
		{
			open();
		}
		// run in new thread to play in background
		new Thread() {
			public void run() {
				try { player.play(); }
				catch (Exception e) { System.out.println(e); }
			}
		}.start();
		playing = true;
	}

	/**
	 * Stops the playback.  The MP3 file is closed and must be opened to be
	 * played again.  Note that this does not pause the sound file, but stops it.
	 */
	public void stop ()
	{
		if (player != null)
		{
			player.close();
			openFlag = false;
		}
		playing = false;
	}

	/**
	 * Sets whether or not the audio file should be repeated when it is finished.
	 * By default this setting is false.
	 * @param repeats Whether the audio should repeat.
	 */
	public void setRepeats (boolean repeats)
	{
		this.repeats = repeats;
	}

	/**
	 * Sets another MP3 file to play after this one is finished.
	 * @param nextFilename The next MP3 file to play.
	 */
	public void playNext (String nextFilename)
	{
		this.nextFilename = nextFilename;
	}

	/**
	 * Returns the state of the playback.
	 * @return true if the audio is playing and false otherwise.
	 */
	public boolean isPlaying ()
	{
		return playing;
	}

	private class FinishedHandler extends PlaybackListener
	{
		public void playbackFinished (PlaybackEvent evt)
		{
			if (nextFilename != null)
			{
				filename = nextFilename;
				nextFilename = null;
				player.close();
				open();
				play();
			}
			else if (repeats)
			{
				player.close();
				open();
				play();
			}
			playing = false;
		}
	}
}
