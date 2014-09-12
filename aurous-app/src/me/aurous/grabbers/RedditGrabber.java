package me.aurous.grabbers;

import java.awt.HeadlessException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.JOptionPane;

import me.aurous.ui.UISession;
import me.aurous.utils.media.MediaUtils;
import me.aurous.utils.playlist.PlayListUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Andrew
 *
 */
public class RedditGrabber {
	private static String addQueryToURL(final String url) {
		final String commentQueryURL = String.format(url + "%s", COMMENT_QUERY);
		return commentQueryURL;
	}

	public static void buildRedditPlayList(String url, final String playListName) {
		if (url.contains("comments")) {
			url = addQueryToURL(url);
		}

		scrapeReddit(url, playListName);

	}

	private static void scrapeReddit(final String url, final String playListName) {
		final Thread thread = new Thread() {
			@Override
			public void run() {

				try {
					if (url.contains("reddit")) {
						System.out.println("dacac");
						// print("Fetching %s...", url);
						String last = "";
						final String out = "data/playlist/" + playListName
								+ ".plist";
						final Document doc = Jsoup.connect(url).get();
						final Elements links = doc.select("a[href]");
						final File playListOut = new File(out);
						final FileOutputStream fos = new FileOutputStream(
								playListOut);
						final BufferedWriter bw = new BufferedWriter(
								new OutputStreamWriter(fos));
						final String header = "Title, Artist, Time, Date Added, User, Album, ALBUMART_INDEX, link";
						bw.write(header);
						bw.newLine();
						int iterations = 0;
						for (final Element link : links) {
							if (UISession.getImporterWidget().isOpen()) {
								if (UISession.getImporterWidget()
										.getImportProgressBar() != null) {

									iterations += 1;

									final int percent = (int) ((iterations * 100.0f) / links
											.size());
									UISession.getImporterWidget()
											.getImportProgressBar()
									.setValue(percent);
									PlayListUtils.disableImporterInterface();
								}
								if (!link.attr("abs:href").equals(last)) {

									final String mediaLine = MediaUtils
											.getBuiltString(link
													.attr("abs:href"));
									if (!mediaLine.isEmpty()) {
										bw.write(mediaLine);
										bw.newLine();
										last = link.attr("abs:href");
									}

								}
							} else {

								bw.close();
								PlayListUtils.deletePlayList(out);
								if (UISession.getImporterWidget()
										.getImportProgressBar() != null) {
									PlayListUtils.resetImporterInterface();
								}
								return;
							}
						}
						bw.close();
						if (UISession.getImporterWidget()
								.getImportProgressBar() != null) {
							PlayListUtils.resetImporterInterface();
						}

					} else {
						JOptionPane.showMessageDialog(null,
								"Invalid URL Detected, is this reddit?",
								"Error", JOptionPane.ERROR_MESSAGE);
						if (UISession.getImporterWidget()
								.getImportProgressBar() != null) {
							PlayListUtils.resetImporterInterface();
						}
					}
				} catch (HeadlessException | IOException e) {
					JOptionPane.showMessageDialog(null,
							"HTTP client error, please try again.", "Error",
							JOptionPane.ERROR_MESSAGE);
					if (UISession.getImporterWidget().getImportProgressBar() != null) {
						PlayListUtils.resetImporterInterface();
					}
					// e.printStackTrace();
				}
			}
		};
		thread.start();

	}

	private static String COMMENT_QUERY = "?limit=1000"; // is it 500 or 1000?

}
