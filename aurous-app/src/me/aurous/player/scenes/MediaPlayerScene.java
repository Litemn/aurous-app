package me.aurous.player.scenes;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import me.aurous.ui.UISession;
import me.aurous.ui.panels.ControlPanel;
import me.aurous.utils.media.MediaUtils;



public class MediaPlayerScene {

	private static void updateTime(final long currentTime,
			final long totalDuration) {
		final ControlPanel panel = UISession.getControlPanel();
		final int percentage = (int) (((currentTime * 100.0) / totalDuration) + 0.5); // jesus
		final double seconds = currentTime / 1000.0;
		UISession.getControlPanel().seek().setValue(percentage);
		panel.seek().setValue(percentage);
		panel.current().setText(MediaUtils.calculateTime((int) seconds));
		// god
		if (percentage == 100) {

			MediaUtils.handleEndOfStream();
		}

	}

	private Media media;

	// private static StreamFunctions sFunctions = new StreamFunctions();

	private MediaPlayer player;

	private MediaView view;

	public Scene createScene(final String sourceURL) throws Throwable {
		final ControlPanel panel = UISession.getControlPanel();
		final Group root = new Group();
		root.autosize();
		MediaUtils.activeMedia = sourceURL;
		final String trailer = MediaUtils.getMediaURL(sourceURL);

		media = new Media(trailer.trim());

		player = new MediaPlayer(media);

		view = new MediaView(player);
		view.setFitWidth(1);
		view.setFitHeight(1);
		view.setPreserveRatio(false);

		// System.out.println("media.width: "+media.getWidth());

		final Scene scene = new Scene(root, 1, 1, Color.BLACK);

		player.play();

		player.setOnReady(() -> {
			panel.seek().setValue(0);

		});
		player.currentTimeProperty().addListener(
				(observableValue, duration, current) -> {

					final long currentTime = (long) current.toMillis();

					final long totalDuration = (long) player.getMedia()
							.getDuration().toMillis();
					updateTime(currentTime, totalDuration);

				});

		// PlayerUtils.activeYoutubeVideo = youtubeVideo;
		if (sourceURL.contains("https://www.youtube.com/watch?v=kGubD7KG9FQ")) {
			player.pause();
		}

		UISession.setMediaPlayer(player);
		UISession.setMediaView(view);
		UISession.setMedia(media);

		return (scene);
	}

}
