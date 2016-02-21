package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

import Crafter.MinigameController;
import economos.UpdateListener;

public class EconomosGUI implements UpdateListener{
	private JFrame						frame;
	private static GUIElements.MyTextField		headlineTextField;
	private static NumberFormat				decimalFormatter	= NumberFormat.getIntegerInstance();
	public static int					timeStep			= 9;
	private GUIElements.MyPanel			merchantsPanel, gamePanel, craftersPanel, overviewPanel, infoPanel, currentPanel, framePanel;
	private MinigameController			minigameController;
	private static boolean						resizing			= false;
	private static int							screenWidth, screenHeight;
	private GuildPanel					guildPanelCrafters;
	private static int largePanelGap = 15, smallPanelGap = 7;	
	
	public static int screenWidth(){
		return screenWidth;
	}
	
	public static int screenHeight(){
		return screenHeight;
	}

	public static int largePanelGap(){
		return largePanelGap;
	}
	
	public static int smallPanelGap(){
		return smallPanelGap;
	}

	public static void postNewHeadline(String txt) {
		headlineTextField.setText(txt);
	}

	public EconomosGUI() {
		decimalFormatter.setMaximumFractionDigits(2);
		initialize();
//		Main.addUpdateListener(new GUIListener());
	}

	/**
	 * Initialize the contents of the frame.
	 */

	public void receiveUpdate() {
		int swlast = screenWidth;
		int shlast = screenHeight;
		screenWidth = framePanel.getWidth();
		screenHeight = framePanel.getHeight();
		if (screenWidth < 600) {
			screenWidth = 600;
		}
		if (screenHeight < 500) {
			screenHeight = 500;
		}
		if (screenHeight != shlast || screenWidth != swlast) {
			resizing = true;
		} else {
			resizing = false;
		}
//		setSelectedResource(selectedGuild, selectedResource);
		updateSpringConstraints();
		minigameController.repaint();
		if (craftersPanel.isVisible()) {
			guildPanelCrafters.repaint();
		}
	}

	
	// used for windowbuilder
	private void setFullScreen() {
		frame = new JFrame();
		frame.setUndecorated(true);

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();

		if (gd.isFullScreenSupported()) {
			try {
				gd.setFullScreenWindow(frame);
			} finally {
				gd.setFullScreenWindow(null);
			}
		}

		frame.setBackground(new Color(40, 40, 40));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = (int) screenSize.getWidth();
		screenHeight = (int) screenSize.getHeight();
		frame.setBounds(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void updateSpringConstraints() {
		SpringLayout springLayout = (SpringLayout) (craftersPanel.getLayout());
		springLayout.putConstraint(SpringLayout.NORTH, minigameController, (int)(-screenHeight / 2), SpringLayout.SOUTH, craftersPanel);
		springLayout.putConstraint(SpringLayout.WEST, minigameController, largePanelGap, SpringLayout.EAST, guildPanelCrafters);
		springLayout.putConstraint(SpringLayout.SOUTH, minigameController, -40, SpringLayout.SOUTH, craftersPanel);
		springLayout.putConstraint(SpringLayout.EAST, minigameController, -largePanelGap, SpringLayout.EAST, craftersPanel);
	}
	
	private void initialize() {
		decimalFormatter.setGroupingUsed(false);
		setFullScreen();

		SpringLayout springLayout = new SpringLayout();
		framePanel = new GUIElements.MyPanel(true);
		framePanel.setLayout(springLayout);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(framePanel, BorderLayout.CENTER);

		gamePanel = new GUIElements.MyPanel(true);
		springLayout.putConstraint(SpringLayout.NORTH, gamePanel, 0, SpringLayout.NORTH, framePanel);
		springLayout.putConstraint(SpringLayout.WEST, gamePanel, 0, SpringLayout.WEST, framePanel);
		springLayout.putConstraint(SpringLayout.SOUTH, gamePanel, -30, SpringLayout.SOUTH, framePanel);
		springLayout.putConstraint(SpringLayout.EAST, gamePanel, 0, SpringLayout.EAST, framePanel);
		gamePanel.setLayout(new CardLayout(0, 0));
		framePanel.add(gamePanel);

		infoPanel = new GUIElements.MyPanel(true);
		springLayout.putConstraint(SpringLayout.NORTH, infoPanel, -30, SpringLayout.SOUTH, framePanel);
		springLayout.putConstraint(SpringLayout.WEST, infoPanel, 0, SpringLayout.WEST, framePanel);
		springLayout.putConstraint(SpringLayout.SOUTH, infoPanel, 0, SpringLayout.SOUTH, framePanel);
		springLayout.putConstraint(SpringLayout.EAST, infoPanel, 0, SpringLayout.EAST, framePanel);
		SpringLayout sl_infoPanel = new SpringLayout();
		infoPanel.setLayout(sl_infoPanel);
		framePanel.add(infoPanel);

		merchantsPanel = new GUIElements.MyPanel(true);
		currentPanel = merchantsPanel;
		springLayout.putConstraint(SpringLayout.NORTH, merchantsPanel, 0, SpringLayout.NORTH, gamePanel);
		springLayout.putConstraint(SpringLayout.WEST, merchantsPanel, 0, SpringLayout.WEST, gamePanel);
		springLayout.putConstraint(SpringLayout.SOUTH, merchantsPanel, 0, SpringLayout.SOUTH, framePanel);
		springLayout.putConstraint(SpringLayout.EAST, merchantsPanel, 0, SpringLayout.EAST, framePanel);
		gamePanel.add(merchantsPanel, "Merchants");

		headlineTextField = new GUIElements.MyTextField();
		sl_infoPanel.putConstraint(SpringLayout.NORTH, headlineTextField, 6, SpringLayout.NORTH, infoPanel);
		sl_infoPanel.putConstraint(SpringLayout.WEST, headlineTextField, 6, SpringLayout.WEST, infoPanel);
		sl_infoPanel.putConstraint(SpringLayout.SOUTH, headlineTextField, -6, SpringLayout.SOUTH, infoPanel);
		sl_infoPanel.putConstraint(SpringLayout.EAST, headlineTextField, -248, SpringLayout.EAST, infoPanel);
		infoPanel.add(headlineTextField);
		headlineTextField.setColumns(largePanelGap);

		GUIElements.MyButton merchantsPanelButton = new GUIElements.MyButton("Merchants", true);
		merchantsPanelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cardLayout = (CardLayout) gamePanel.getLayout();
				cardLayout.show(gamePanel, "Merchants");
			}
		});
		sl_infoPanel.putConstraint(SpringLayout.NORTH, merchantsPanelButton, 0, SpringLayout.NORTH, headlineTextField);
		sl_infoPanel.putConstraint(SpringLayout.WEST, merchantsPanelButton, 6, SpringLayout.EAST, headlineTextField);
		sl_infoPanel.putConstraint(SpringLayout.SOUTH, merchantsPanelButton, 0, SpringLayout.SOUTH, headlineTextField);
		sl_infoPanel.putConstraint(SpringLayout.EAST, merchantsPanelButton, 80, SpringLayout.EAST, headlineTextField);
		infoPanel.add(merchantsPanelButton);

		GUIElements.MyButton craftersPanelButton = new GUIElements.MyButton("Crafters", true);
		craftersPanelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cardLayout = (CardLayout) gamePanel.getLayout();
				cardLayout.show(gamePanel, "Crafters");
			}
		});

		sl_infoPanel.putConstraint(SpringLayout.NORTH, craftersPanelButton, 0, SpringLayout.NORTH, merchantsPanelButton);
		sl_infoPanel.putConstraint(SpringLayout.WEST, craftersPanelButton, 6, SpringLayout.EAST, merchantsPanelButton);
		sl_infoPanel.putConstraint(SpringLayout.SOUTH, craftersPanelButton, 0, SpringLayout.SOUTH, merchantsPanelButton);
		sl_infoPanel.putConstraint(SpringLayout.EAST, craftersPanelButton, 80, SpringLayout.EAST, merchantsPanelButton);
		infoPanel.add(craftersPanelButton);

		GUIElements.MyButton overviewPanelButton = new GUIElements.MyButton("Overview", true);
		overviewPanelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cardLayout = (CardLayout) gamePanel.getLayout();
				cardLayout.show(gamePanel, "Overview");
			}
		});
		sl_infoPanel.putConstraint(SpringLayout.NORTH, overviewPanelButton, 0, SpringLayout.NORTH, craftersPanelButton);
		sl_infoPanel.putConstraint(SpringLayout.WEST, overviewPanelButton, largePanelGap, SpringLayout.EAST, craftersPanelButton);
		sl_infoPanel.putConstraint(SpringLayout.SOUTH, overviewPanelButton, 0, SpringLayout.SOUTH, craftersPanelButton);
		sl_infoPanel.putConstraint(SpringLayout.EAST, overviewPanelButton, 80, SpringLayout.EAST, craftersPanelButton);
		infoPanel.add(overviewPanelButton);

		craftersPanel = new GUIElements.MyPanel(true);
		springLayout.putConstraint(SpringLayout.NORTH, craftersPanel, 5, SpringLayout.NORTH, gamePanel);
		springLayout.putConstraint(SpringLayout.WEST, craftersPanel, 15, SpringLayout.WEST, gamePanel);
		springLayout.putConstraint(SpringLayout.SOUTH, craftersPanel, 701, SpringLayout.NORTH, framePanel);
		springLayout.putConstraint(SpringLayout.EAST, craftersPanel, 994, SpringLayout.WEST, framePanel);
		SpringLayout sl_craftersPanel = new SpringLayout();
		craftersPanel.setLayout(sl_craftersPanel);
		craftersPanel.setEnabled(false);
		gamePanel.add(craftersPanel, "Crafters");

		guildPanelCrafters = new GuildPanel(new String[] { "Apothecary", "Embroider", "Artificer", "Philosopher", "Smith", "Voyager", "Sage", "Chef" }, screenHeight - 50, screenWidth / 4);
		sl_craftersPanel.putConstraint(SpringLayout.NORTH, guildPanelCrafters, largePanelGap, SpringLayout.NORTH, craftersPanel);
		sl_craftersPanel.putConstraint(SpringLayout.WEST, guildPanelCrafters, largePanelGap, SpringLayout.WEST, craftersPanel);
		sl_craftersPanel.putConstraint(SpringLayout.SOUTH, guildPanelCrafters, -40, SpringLayout.SOUTH, craftersPanel);
		sl_craftersPanel.putConstraint(SpringLayout.EAST, guildPanelCrafters, 310, SpringLayout.WEST, craftersPanel);
		craftersPanel.add(guildPanelCrafters);

		minigameController = new MinigameController(screenWidth - 330, screenHeight / 2 - 40);
		craftersPanel.add(minigameController);

		overviewPanel = new GUIElements.MyPanel(true);
		springLayout.putConstraint(SpringLayout.NORTH, overviewPanel, 5, SpringLayout.NORTH, gamePanel);
		springLayout.putConstraint(SpringLayout.WEST, overviewPanel, 20, SpringLayout.WEST, gamePanel);
		springLayout.putConstraint(SpringLayout.SOUTH, overviewPanel, 701, SpringLayout.NORTH, framePanel);
		springLayout.putConstraint(SpringLayout.EAST, overviewPanel, 994, SpringLayout.WEST, framePanel);
		SpringLayout sl_overviewPanel = new SpringLayout();
		overviewPanel.setLayout(sl_overviewPanel);
		overviewPanel.setEnabled(false);
		gamePanel.add(overviewPanel, "Overview");

//		updateSpringConstraints();
	}

	public static boolean resizing() {
		return resizing;
	}

	public static NumberFormat decimalFormatter() {
		return decimalFormatter;
	}
}