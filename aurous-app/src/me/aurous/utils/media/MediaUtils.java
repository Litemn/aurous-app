package me.aurous.utils.media;

import static com.sun.javafx.Utils.convertUnicode;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Platform;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JTable;

import org.apache.commons.lang3.StringEscapeUtils;

import me.aurous.grabbers.SoundCloudGrabber;
import me.aurous.grabbers.VKGrabber;
import me.aurous.grabbers.YouTubeGrabber;
import me.aurous.notifiers.NowPlayingNotification;
import me.aurous.player.Settings;
import me.aurous.player.functions.PlayerFunctions;
import me.aurous.ui.UISession;
import me.aurous.ui.panels.PlayListPanel;
import me.aurous.ui.panels.TabelPanel;
import me.aurous.utils.Internet;
import me.aurous.utils.playlist.YouTubeDataFetcher;

/**
 * @author Andrew
 *
 */
public class MediaUtils {
	public static boolean muted = false;
	public static String activeMedia;
	public static boolean isOutOfFocus;
	

	/**
	 * @author Andrew
	 *
	 *         Gets a string between two givens patterns
	 */

	public static String getBetween(final String match,
			final String patternStart, final String patternEnd) {
		final Pattern p = Pattern.compile(Pattern.quote(patternStart) + "(.*?)"
				+ Pattern.quote(patternEnd));
		final Matcher m = p.matcher(match);
		while (m.find()) {

			return m.group(1);

		}
		return "";
	}

	/**
	 * @author Andrew
	 *
	 *         handles a given site and returns the stream url
	 */
	public static String getMediaURL(final String sourceURL) {
		if (sourceURL == null) {
			 YouTubeGrabber youTube = new YouTubeGrabber();
				return youTube.getYouTubeStream(Internet.text("https://www.youtube.com/watch?v=kGubD7KG9FQ"));
		}
		if (sourceURL.isEmpty()) {
			return "";
		}
		if (sourceURL.contains("youtube")) {
			 YouTubeGrabber youTube = new YouTubeGrabber();
			return youTube.getYouTubeStream(Internet.text(sourceURL));
		} else if (sourceURL.contains("soundcloud")) {
			return SoundCloudGrabber.getCachedURL(sourceURL);
		} else if (sourceURL.contains("vk.me")) {
			if (sourceURL.contains(".mp3")) {
				return sourceURL;
			}
			return VKGrabber.grab(sourceURL);
		} else {
			return sourceURL;
		}
		
	}
	
	public static String cleanString(String dirtyString) {
		dirtyString = StringEscapeUtils.escapeHtml4(dirtyString.replaceAll(
				"[^\\x20-\\x7e]", ""));
		dirtyString = flattenToAscii(dirtyString);
		dirtyString = convertUnicode(dirtyString);

		if (dirtyString.contains(",")) {
			dirtyString = escapeComma(dirtyString);
		}
		dirtyString = StringEscapeUtils.unescapeHtml4(dirtyString);
		final String cleanedString = dirtyString;
		return cleanedString;
	}

	private static String escapeComma(final String str) {
		return str.replace(",", "\\,");
	}

	private static String flattenToAscii(String string) {
		final char[] out = new char[string.length()];
		string = Normalizer.normalize(string, Normalizer.Form.NFD);
		int j = 0;
		for (int i = 0, n = string.length(); i < n; ++i) {
			final char c = string.charAt(i);
			if (c <= '\u007F') {
				out[j++] = c;
			}
		}
		return new String(out);
	}


	/**
	 * @author Andrew
	 *
	 *         handles a given site and returns a built playlist string
	 */
	public static String getBuiltString(final String sourceURL) {
		if (sourceURL.contains("youtube") || sourceURL.contains("youtu.be")) {

			return YouTubeDataFetcher.buildPlayListLine(sourceURL);
		} else if (sourceURL.contains("soundcloud")) {
			return SoundCloudGrabber.buildPlayListLine(sourceURL);
		} else {
			return "";
		}
	}

	
	
	public static void muteToggle() {
		if (muted) {
			muted = false;
			UISession.getControlPanel().volume().setValue(Settings.getVolume());
		} else {
			muted = true;
			Settings.setVolume(UISession.getControlPanel().volume().getValue());
			UISession.getControlPanel().volume().setValue(0);

		}

	}

	/**
	 * @author Andrew
	 *
	 *         calls all needed functions for a switching of media
	 */
	public static void switchMedia(final JTable target) {
		
			
		
		switchMediaStream(target);
		switchMediaMeta(target);
		switchMediaCover(target);
		PlayerFunctions.play(UISession.getControlPanel().play());
		

	}

	/**
	 * @author Andrew
	 *
	 *         sets all the meta information for the active song
	 */
	private static void switchMediaMeta(final JTable target) {
		final int row = target.getSelectedRow();
		

		final String title = target.getValueAt(row, 0).toString().trim();
		final String artist = target.getValueAt(row, 1).toString().trim();
		final String time = (String) target.getValueAt(row, 2);
		PlayListPanel.setSongInformation(title.replace("\\", ""), artist.replace("\\", ""));
		UISession.getControlPanel().total().setText(time);
		if (Settings.isSavePlayBack()) {
			try {
				final File parentDir = new File("./data/livestream/");
				parentDir.mkdir();
				Files.write(Paths.get("./data/livestream/artist.txt"),
						artist.getBytes());
				Files.write(Paths.get("./data/livestream/title.txt"),
						title.getBytes());
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

	}

	/**
	 * @author Andrew
	 *
	 *         switches the album cover
	 */
	private static void switchMediaCover(final JTable target) {
		try {
			final int row = target.getSelectedRow();
			
			final String albumArt = target.getName().equals("search") ? "http://codeusa.net/apps/poptart/bad.png" : (String) target.getValueAt(row, 6);

			final ImageIcon icon = new ImageIcon(new URL(albumArt));
			PlayListPanel.setAlbumArt(icon.getImage());

			if (Settings.isSavePlayBack()) {
				try {

					final Image img = icon.getImage();

					final BufferedImage bi = new BufferedImage(
							img.getWidth(null), img.getHeight(null),
							BufferedImage.TYPE_INT_RGB);

					final Graphics2D g2 = bi.createGraphics();
					g2.drawImage(img, 0, 0, null);
					g2.dispose();
					ImageIO.write(bi, "jpg", new File(
							"./data/livestream/art.jpg"));
				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
		} catch (final MalformedURLException e) {
			e.printStackTrace();
		}
		
	
	}

	/**
	 * @author Andrew
	 *
	 *         switches the album cover
	 */
	public static void copyMediaURL(final JTable target) {
		final int row = target.getSelectedRow();

		final String link = target.getName().equals("search") ? (String) target.getValueAt(row, 3) :(String) target.getValueAt(row, 7);;
		final StringSelection stringSelection = new StringSelection(link);
		final Clipboard clpbrd = Toolkit.getDefaultToolkit()
				.getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
	}

	/**
	 * @author Andrew
	 *
	 *         Handles when a stream ends
	 */
	public static void handleEndOfStream() {
		
		final JTable table = TabelPanel.table;
		if (table.getRowCount() == 0) {
			return;
		}
		if ((PlayerFunctions.repeat == false)
				&& (PlayerFunctions.shuffle == false)) {
			PlayerFunctions.seekNext();
		} else if (PlayerFunctions.repeat == true) {
			PlayerFunctions.repeat();
		} else if (PlayerFunctions.shuffle == true) {
			PlayerFunctions.shuffle();

		}

		if (isOutOfFocus && Settings.isDisplayAlert()) {
			final int row = table.getSelectedRow();
			final String title = (String) table.getValueAt(row, 0);
			final String artist = (String) table.getValueAt(row, 1);
			final String albumArt = (String) table.getValueAt(row, 6);
			try {

				final NowPlayingNotification np = new NowPlayingNotification(
						title, artist, albumArt);
				np.displayAlert();
			} catch (final MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * @author Andrew
	 *
	 *         switches the active stream with the new selected one
	 */
	private static void switchMediaStream(final JTable target) {
		Platform.runLater(() -> {
			try {
				final int row = target.getSelectedRow();
				
				final String sourceURL = target.getName().equals("search") ? (String) target.getValueAt(row, 3) : (String) target.getValueAt(row, 7);
				UISession.getMediaPlayer().stop();
				UISession.getMediaPlayer().dispose();
				UISession.getJFXPanel().setScene(null);
				UISession.getPresenter().setScene(null);
				System.gc();

				UISession.getPresenter().setScene(UISession
						.getMediaPlayerScene().createScene(sourceURL));
			
				
				UISession.getJFXPanel()
						.setScene(UISession.getPresenter().getScene());

				UISession.getMediaPlayer().setVolume(
						((double) UISession.getControlPanel().volume().getValue() / 100));
				
				
			} catch (final Throwable ei) {
				ei.printStackTrace();
			}
			
		});
	}

	/**
	 * @author Andrew
	 *
	 *         Formats seconds into a readable string Need to change my hard
	 *         code to remove extra 0's and use regex.
	 */
	public static String calculateTime(final int seconds) {
		final int day = (int) TimeUnit.SECONDS.toDays(seconds);
		final long hours = TimeUnit.SECONDS.toHours(seconds)
				- TimeUnit.DAYS.toHours(day);
		final long minute = TimeUnit.SECONDS.toMinutes(seconds)
				- TimeUnit.DAYS.toMinutes(day)
				- TimeUnit.HOURS.toMinutes(hours);
		final long second = TimeUnit.SECONDS.toSeconds(seconds)
				- TimeUnit.DAYS.toSeconds(day)
				- TimeUnit.HOURS.toSeconds(hours)
				- TimeUnit.MINUTES.toSeconds(minute);

		// i must be tired because wtf was i doing
		String duration = String.format("%02d:%02d:%d:%02d", day == 0 ? 5185618
				: day, hours == 0 ? 5185618 : hours, minute, second);
		duration = duration.replace("5185618:", "");
		return duration;
	}

}
