package me.aurous.ui.widgets;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window.Type;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import me.aurous.player.Settings;
import me.aurous.ui.UISession;
import me.aurous.utils.Utils;

/**
 * @author Andrew
 *
 */
public class SettingsWidget {

	/**
	 * Launch the application.
	 */
	public static void openSettings() {
		if ((UISession.getSettingsWidget() != null)
				&& UISession.getSettingsWidget().isOpen()) {
			UISession.getSettingsWidget().getSettingsWidget().toFront();
			UISession.getSettingsWidget().getSettingsWidget().repaint();
			return;
		}
		EventQueue.invokeLater(() -> {
			try {
				final SettingsWidget window = new SettingsWidget();
				UISession.setSettingsWidget(window);
				UISession.getSettingsWidget().getSettingsWidget()
						.setVisible(true);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}

	private JFrame settingsWidget;

	private JLabel avatarTitleLabel;

	private JLabel profileNameTitleLabel;
	private JLabel avatarLabel;
	private JLabel changeAvatarHypeLink;
	public JTextField userNameField;
	private JLabel viewProfileHyperLink;
	private JSeparator separator;
	private JLabel onlineServicesLabel;
	private JSeparator separator_1;
	private JLabel notficationsLabel;
	public JCheckBox showPlayBackAlertCheckbox;
	private JLabel lblWhenANew;
	private JSeparator separator_2;
	private JLabel playBackSettingsLabel;
	public JCheckBox lowQualityCheckBox;
	private JLabel lblHelpsIfYoure;
	private JButton saveSettingsButton;
	private JButton exitFrameButton;
	private JLabel lblIfYouWant;
	public JCheckBox savePlayBackCheckBox;

	/**
	 * Create the application.
	 */
	public SettingsWidget() {
		initialize();
	}

	public JFrame getSettingsWidget() {
		return settingsWidget;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		settingsWidget = new JFrame();
		settingsWidget.setTitle("Settings");
		settingsWidget.setIconImage(Toolkit.getDefaultToolkit().getImage(
				SettingsWidget.class.getResource("/resources/aurouslogo.png")));
		settingsWidget.setResizable(false);
		settingsWidget.getContentPane().setBackground(new Color(49, 49, 49));
		settingsWidget.getContentPane().setLayout(null);
		settingsWidget.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(
					final java.awt.event.WindowEvent windowEvent) {
				UISession.setSettingsWidget(null);
				settingsWidget.dispose();

			}
		});
		avatarTitleLabel = new JLabel("Avatar");
		avatarTitleLabel.setForeground(Color.WHITE);
		avatarTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		avatarTitleLabel.setBounds(10, 11, 50, 14);
		settingsWidget.getContentPane().add(avatarTitleLabel);

		profileNameTitleLabel = new JLabel("Profile name");
		profileNameTitleLabel.setForeground(Color.WHITE);
		profileNameTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		profileNameTitleLabel.setBounds(70, 12, 83, 14);
		settingsWidget.getContentPane().add(profileNameTitleLabel);

		avatarLabel = new JLabel("");
		avatarLabel.setIcon(new ImageIcon(SettingsWidget.class
				.getResource("/resources/avatar.jpg")));
		avatarLabel.setBounds(10, 36, 37, 37);
		settingsWidget.getContentPane().add(avatarLabel);

		changeAvatarHypeLink = new JLabel(
				"<html><a href=\\\"\\\" style=\"color: #ffffff\">Change</a></html>");
		changeAvatarHypeLink.setForeground(Color.WHITE);
		changeAvatarHypeLink.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		changeAvatarHypeLink.setBounds(10, 82, 50, 24);
		changeAvatarHypeLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
		settingsWidget.getContentPane().add(changeAvatarHypeLink);

		userNameField = new JTextField();
		userNameField.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
		userNameField.setBounds(70, 37, 183, 24);
		settingsWidget.getContentPane().add(userNameField);
		userNameField.setColumns(10);

		viewProfileHyperLink = new JLabel(
				"<html><a href=\\\"\\\" style=\"color: #ffffff\">View my Aurous Community Profile</a></html>");
		viewProfileHyperLink.setForeground(Color.WHITE);
		viewProfileHyperLink.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		viewProfileHyperLink.setBounds(70, 82, 217, 24);
		viewProfileHyperLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
		settingsWidget.getContentPane().add(viewProfileHyperLink);

		separator = new JSeparator();
		separator.setForeground(Color.LIGHT_GRAY);
		separator.setBounds(10, 117, 497, 2);
		settingsWidget.getContentPane().add(separator);

		onlineServicesLabel = new JLabel("Online Service Settings");
		onlineServicesLabel.setForeground(Color.WHITE);
		onlineServicesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		onlineServicesLabel.setBounds(10, 122, 155, 24);
		settingsWidget.getContentPane().add(onlineServicesLabel);

		separator_1 = new JSeparator();
		separator_1.setForeground(Color.LIGHT_GRAY);
		separator_1.setBounds(10, 184, 497, 2);
		settingsWidget.getContentPane().add(separator_1);

		notficationsLabel = new JLabel("Notfications");
		notficationsLabel.setForeground(Color.WHITE);
		notficationsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		notficationsLabel.setBounds(10, 194, 155, 24);
		settingsWidget.getContentPane().add(notficationsLabel);

		showPlayBackAlertCheckbox = new JCheckBox("Display Alert");
		showPlayBackAlertCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		showPlayBackAlertCheckbox.setForeground(Color.WHITE);
		showPlayBackAlertCheckbox.setBackground(new Color(49, 49, 49));
		showPlayBackAlertCheckbox.setBounds(10, 241, 98, 23);
		settingsWidget.getContentPane().add(showPlayBackAlertCheckbox);

		lblWhenANew = new JLabel("When a new song plays while minimized");
		lblWhenANew.setForeground(Color.WHITE);
		lblWhenANew.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblWhenANew.setBounds(10, 218, 222, 24);
		settingsWidget.getContentPane().add(lblWhenANew);

		playBackSettingsLabel = new JLabel("Playback Settings");
		playBackSettingsLabel.setForeground(Color.WHITE);
		playBackSettingsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		playBackSettingsLabel.setBounds(10, 303, 155, 24);
		settingsWidget.getContentPane().add(playBackSettingsLabel);

		lowQualityCheckBox = new JCheckBox("Stream Lower Bitrate");
		lowQualityCheckBox.setBackground(new Color(49, 49, 49));
		lowQualityCheckBox.setForeground(Color.WHITE);
		lowQualityCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lowQualityCheckBox.setBounds(10, 346, 134, 23);
		settingsWidget.getContentPane().add(lowQualityCheckBox);

		lblHelpsIfYoure = new JLabel("If you're experiencing buffering.");
		lblHelpsIfYoure.setForeground(Color.WHITE);
		lblHelpsIfYoure.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblHelpsIfYoure.setBounds(10, 322, 209, 28);
		settingsWidget.getContentPane().add(lblHelpsIfYoure);

		saveSettingsButton = new JButton("APPLY");
		saveSettingsButton.addActionListener(e -> {
			Settings.setUserName(userNameField.getText().trim());
			Settings.setDisplayAlert(showPlayBackAlertCheckbox.isSelected());
			Settings.setStreamLowQuality(lowQualityCheckBox.isSelected());
			Settings.setSavePlayBack(savePlayBackCheckBox.isSelected());
			Settings.saveSettings(false);
		});
		saveSettingsButton.setForeground(Color.WHITE);
		saveSettingsButton.setBounds(323, 425, 89, 23);
		settingsWidget.getContentPane().add(saveSettingsButton);

		exitFrameButton = new JButton("CANCEL");
		exitFrameButton.addActionListener(e -> settingsWidget.dispose());
		exitFrameButton.setForeground(Color.WHITE);
		exitFrameButton.setBounds(422, 425, 89, 23);
		settingsWidget.getContentPane().add(exitFrameButton);

		separator_2 = new JSeparator();
		separator_2.setForeground(Color.LIGHT_GRAY);
		separator_2.setBounds(10, 292, 497, 8);
		settingsWidget.getContentPane().add(separator_2);
		settingsWidget.setType(Type.UTILITY);
		settingsWidget.setBounds(100, 100, 533, 484);
		settingsWidget
				.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		lblIfYouWant = new JLabel("If you want to show a song on twitch");
		lblIfYouWant.setForeground(Color.WHITE);
		lblIfYouWant.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblIfYouWant.setBounds(10, 376, 209, 28);
		settingsWidget.getContentPane().add(lblIfYouWant);

		savePlayBackCheckBox = new JCheckBox("Enable Streaming Features");
		savePlayBackCheckBox.setSelected(false);
		savePlayBackCheckBox.setForeground(Color.WHITE);
		savePlayBackCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		savePlayBackCheckBox.setBackground(new Color(49, 49, 49));
		savePlayBackCheckBox.setBounds(10, 400, 216, 23);
		settingsWidget.getContentPane().add(savePlayBackCheckBox);
		settingsWidget.setLocationRelativeTo(UISession.getPresenter()
				.getAurousFrame());

		showPlayBackAlertCheckbox.setSelected(Settings.isDisplayAlert());
		lowQualityCheckBox.setSelected(Settings.isStreamLowQuality());
		savePlayBackCheckBox.setSelected(Settings.isSavePlayBack());
		userNameField.setText(Settings.getUserName());
	}

	public boolean isOpen() {
		return Utils.isNull(settingsWidget) ? false : settingsWidget
				.isVisible();
	}

	public void setSettingsWidget(final JFrame settingsWidget) {
		this.settingsWidget = settingsWidget;
	}
}
