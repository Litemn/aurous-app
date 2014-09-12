package me.aurous.utils.playlist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import me.aurous.ui.UISession;
import me.aurous.utils.Internet;
import me.aurous.utils.media.MediaUtils;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * @author Andrew
 *
 */
public class YouTubeDiscoUtils {
	public static void buildDiscoPlayList(final String query) {
		final String json = getDiscoJson(query);
		if (json.isEmpty()) {
			JOptionPane.showMessageDialog(UISession.getDiscoWidget()
					.getWidget(),
					"There was an error getting some needed data, try again!",
					"Error", JOptionPane.ERROR_MESSAGE);
			UISession.getDiscoWidget().getDiscoProgressBar().setVisible(false);
			return;
		}

		final String url = getPlayListURL(json);
		final String title = getPlayListTitle(url);
		if (title.equals("Oops! Something went wrong.")) {
			JOptionPane
					.showMessageDialog(
							UISession.getDiscoWidget().getWidget(),
							"Unable to locate any playlist for this artist/song \n But don't worry some may show up soon!",
							"Error", JOptionPane.ERROR_MESSAGE);
			UISession.getDiscoWidget().getDiscoProgressBar().setVisible(false);
			return;
		}
		YouTubePlayListImporter.importYoutubePlayList(url, title);

	}

	public static void buildTopPlayList() {
		YouTubePlayListImporter.importYoutubePlayList(TOP_100_URL,
				"YouTube Daily Top Tracks");

	}

	private static String getDiscoJson(final String query) {
		String JSON_URL = String.format(DISCO_API_URL, query);
		JSON_URL = JSON_URL.replace(" ", "%20");
		final String json_data = Internet.text(JSON_URL);

		return json_data;
	}

	private static String getPlayListTitle(final String url) {
		final String html = Internet.text(url);
		final String pattern1 = "<title>";
		final String pattern2 = "- YouTube</title>";
		String title = "";

		final Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)"
				+ Pattern.quote(pattern2));
		final Matcher m = p.matcher(html);
		while (m.find()) {

			title = m.group(1);

		}
		title = StringEscapeUtils.escapeHtml4(title.trim().replaceAll(
				"[^\\x20-\\x7e]", ""));
		title = StringEscapeUtils.unescapeHtml4(title);

		return title;
	}

	private static String getPlayListURL(final String json) {
		final String playListID = MediaUtils.getBetween(json, "\\u0026list=",
				"\\u0026");
		final String discoPlayList = String.format(PLAYLIST_URL, playListID);

		return discoPlayList;
	}

	private static String TOP_100_URL = "https://www.youtube.com/playlist?list=MCUS";

	private static String DISCO_API_URL = "https://www.youtube.com/disco?action_search=1&query=%s";

	private static String PLAYLIST_URL = "https://www.youtube.com/playlist?list=%s";

}
