package economos;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

import economos.GUIElements.PercentageSpinner;
import economos.Main.UpdateListener;

public class EconomosGUI {
	private JFrame					frame;
	private GUIElements.MyTextField	typeTextField, nameTextField, possessTextField, soldTextField, averageProfitTextField, averagePriceTextField, demandSupplyTextField, rarityTextField, headlineTextField;
	private GUIElements.MyTextArea	descriptionTextArea;
	private GUIElements.MyPanel		resourceList, resourceDetailPanel, bodyPanel, topDetailPanel, iconPanel, descriptionPanel, statsPanel, framePanel;
	private GUIElements.MyButton	sellButton			= new GUIElements.MyButton("Sell", true);
	private GUIElements.MyButton	buyButton			= new GUIElements.MyButton("Buy", true);
	private static MerchantResource	currentResource		= null;
	private NumberFormat			decimalFormatter	= NumberFormat.getIntegerInstance();
	private GraphPanel				buyGraph			= new GraphPanel();
	public static int				timeStep			= 9;
	private String					selectedResource, selectedGuild;
	private GUIElements.MyPanel		merchantsPanel, gamePanel, craftersPanel, overviewPanel, infoPanel, currentPanel;
	private MinigameController		minigameController;
	private boolean					fieldsReset			= false, resizing = false;
	private int						screenWidth, screenHeight;
	private GuildPanel				guildPanelMerchants, guildPanelCrafters;
	private JTextField				boughtTextField;

	public void setSelectedGuild(String selectedGuild) {
		this.selectedGuild = selectedGuild;
	}

	public String getSelectedGuild() {
		return selectedGuild;
	}

	public void setSelectedResource(String selectedResource) {
		this.selectedResource = selectedResource;
	}

	public String getSelectedResource() {
		return selectedResource;
	}

	public void postNewHeadline(String txt) {
		headlineTextField.setText(txt);
	}

	public EconomosGUI() {
		decimalFormatter.setMaximumFractionDigits(2);
		initialize();
		Main.addUpdateListener(new GUIListener());
	}

	private class GUIListener implements UpdateListener {
		public void receiveUpdate() {
			updateGUI();
		}
	}

	public void setSelectedResource(String type, String name) {
		ResourceMap<MerchantResource> m = Main.getPlayer().getPlayerResourceMap();
		currentResource = m.getResource(type, name);

		if (currentResource != null) {
			MarketResource mr = currentResource.getMarketResource();
			typeTextField.setText(currentResource.getType());
			nameTextField.setText(currentResource.getName());
			rarityTextField.setText(currentResource.getRarity());
			switch (currentResource.getRarity()) {
				case "Commonplace":
					rarityTextField.setColor(new Color(25, 200, 25));
					break;
				case "Unusual":
					rarityTextField.setColor(new Color(0, 200, 100));
					break;
				case "Soughtafter":
					rarityTextField.setColor(new Color(0, 80, 160));
					break;
				case "Coveted":
					rarityTextField.setColor(new Color(140, 0, 230));
					break;
				case "Legendary":
					rarityTextField.setColor(new Color(210, 25, 165));
					break;
				default:
					break;
			}
			descriptionTextArea.setText(currentResource.getDescription());
			demandSupplyTextField.setText("S/D: " + mr.getSupply() / mr.getDemand());
			possessTextField.setText("OWN " + currentResource.getQuantity());
			averagePriceTextField.setText("Average Price: C" + decimalFormatter.format(mr.getAveragePrice()));
			averageProfitTextField.setText("Average Profit: C" + decimalFormatter.format(currentResource.getAverageProfit()));
			soldTextField.setText("Sold " + currentResource.getSold() + " units");
			try {
				buyButton.setText("Buy: C" + decimalFormatter.format(currentResource.getMarketResource().getBuyPrice(1)));
			} catch (NumberFormatException n) {
				// DOSOMETHING
			}

			try {
				sellButton.setText("Sell: C" + decimalFormatter.format(currentResource.getMarketResource().getSellPrice(1)));
			} catch (NumberFormatException n) {
				// DOSOMETHING
			}

			if (resizing == false) {
				buyGraph.repaint();
			}
			fieldsReset = false;
		} else if (!fieldsReset) {
			typeTextField.setText("");
			rarityTextField.setText("");
			rarityTextField.setColor(null);
			nameTextField.setText("");
			possessTextField.setText("");
			soldTextField.setText("");
			averageProfitTextField.setText("");
			averagePriceTextField.setText("");
			demandSupplyTextField.setText("");
			descriptionTextArea.setText("");
			sellButton.setText("Sell");
			buyButton.setText("Buy");
			buyGraph.repaint();
			fieldsReset = true;
		}
	}

	public static MerchantResource getCurrentResource() {
		return currentResource;
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void updateGUI() {
		int swlast = screenWidth;
		int shlast = screenHeight;
		screenWidth = framePanel.getWidth();
		screenHeight = framePanel.getHeight();
		if (screenHeight != shlast || screenWidth != swlast) {
			resizing = true;
		} else {
			resizing = false;
		}
		if (selectedGuild != null && selectedResource != null) {
			setSelectedResource(selectedGuild, selectedResource);
		}
		updateSpringConstraints();
		minigameController.repaint();
		if (merchantsPanel.isVisible()) {
			guildPanelMerchants.repaint();
		} else if (craftersPanel.isVisible()) {
			guildPanelCrafters.repaint();
		}
	}

	private void updateSpringConstraints() {
		SpringLayout springLayout = (SpringLayout) (craftersPanel.getLayout());
		springLayout.putConstraint(SpringLayout.NORTH, minigameController, -screenHeight / 2, SpringLayout.SOUTH, craftersPanel);
		springLayout.putConstraint(SpringLayout.WEST, minigameController, 10, SpringLayout.EAST, guildPanelCrafters);
		springLayout.putConstraint(SpringLayout.SOUTH, minigameController, -40, SpringLayout.SOUTH, craftersPanel);
		springLayout.putConstraint(SpringLayout.EAST, minigameController, -10, SpringLayout.EAST, craftersPanel);

		springLayout = (SpringLayout) (merchantsPanel.getLayout());
		springLayout.putConstraint(SpringLayout.NORTH, guildPanelMerchants, 10, SpringLayout.NORTH, merchantsPanel);
		springLayout.putConstraint(SpringLayout.WEST, guildPanelMerchants, 10, SpringLayout.WEST, merchantsPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, guildPanelMerchants, -40, SpringLayout.SOUTH, merchantsPanel);
		springLayout.putConstraint(SpringLayout.EAST, guildPanelMerchants, screenWidth / 4, SpringLayout.WEST, merchantsPanel);

		int resourcePanelHeight = (screenHeight - 40) / 2;
		springLayout = (SpringLayout) (bodyPanel.getLayout());
		springLayout.putConstraint(SpringLayout.NORTH, resourceDetailPanel, 0, SpringLayout.NORTH, bodyPanel);
		springLayout.putConstraint(SpringLayout.WEST, resourceDetailPanel, 0, SpringLayout.WEST, bodyPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, resourceDetailPanel, -resourcePanelHeight, SpringLayout.SOUTH, bodyPanel);
		springLayout.putConstraint(SpringLayout.EAST, resourceDetailPanel, 0, SpringLayout.EAST, bodyPanel);

		springLayout = (SpringLayout) (resourceDetailPanel.getLayout());
		springLayout.putConstraint(SpringLayout.NORTH, topDetailPanel, 0, SpringLayout.NORTH, resourceDetailPanel);
		springLayout.putConstraint(SpringLayout.WEST, topDetailPanel, 0, SpringLayout.WEST, resourceDetailPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, topDetailPanel, 0, SpringLayout.SOUTH, resourceDetailPanel);
		springLayout.putConstraint(SpringLayout.EAST, topDetailPanel, resourcePanelHeight / 2, SpringLayout.WEST, resourceDetailPanel);

		springLayout = (SpringLayout) (topDetailPanel.getLayout());
		springLayout.putConstraint(SpringLayout.NORTH, iconPanel, 0, SpringLayout.NORTH, topDetailPanel);
		springLayout.putConstraint(SpringLayout.WEST, iconPanel, 0, SpringLayout.WEST, topDetailPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, iconPanel, resourcePanelHeight / 2, SpringLayout.NORTH, topDetailPanel);
		springLayout.putConstraint(SpringLayout.EAST, iconPanel, 0, SpringLayout.EAST, topDetailPanel);

		int textFieldHeight = resourcePanelHeight / 6 - 10;

		springLayout.putConstraint(SpringLayout.NORTH, typeTextField, 10, SpringLayout.SOUTH, iconPanel);
		springLayout.putConstraint(SpringLayout.WEST, typeTextField, 0, SpringLayout.WEST, iconPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, typeTextField, textFieldHeight, SpringLayout.SOUTH, iconPanel);
		springLayout.putConstraint(SpringLayout.EAST, typeTextField, 0, SpringLayout.EAST, topDetailPanel);

		springLayout.putConstraint(SpringLayout.NORTH, nameTextField, 10, SpringLayout.SOUTH, typeTextField);
		springLayout.putConstraint(SpringLayout.WEST, nameTextField, 0, SpringLayout.WEST, topDetailPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, nameTextField, textFieldHeight, SpringLayout.SOUTH, typeTextField);
		springLayout.putConstraint(SpringLayout.EAST, nameTextField, 0, SpringLayout.EAST, topDetailPanel);

		springLayout.putConstraint(SpringLayout.NORTH, rarityTextField, 10, SpringLayout.SOUTH, nameTextField);
		springLayout.putConstraint(SpringLayout.WEST, rarityTextField, 0, SpringLayout.WEST, topDetailPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, rarityTextField, textFieldHeight, SpringLayout.SOUTH, nameTextField);
		springLayout.putConstraint(SpringLayout.EAST, rarityTextField, 0, SpringLayout.EAST, topDetailPanel);

		springLayout.putConstraint(SpringLayout.NORTH, descriptionPanel, 0, SpringLayout.NORTH, resourceDetailPanel);
		springLayout.putConstraint(SpringLayout.WEST, descriptionPanel, 10, SpringLayout.EAST, topDetailPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, descriptionPanel, 0, SpringLayout.SOUTH, resourceDetailPanel);
		springLayout.putConstraint(SpringLayout.EAST, descriptionPanel, resourcePanelHeight / 2 + 10, SpringLayout.EAST, topDetailPanel);

		int eHeight = resourcePanelHeight / 5;
		int eWidth = (screenWidth - ((screenWidth / 4) + resourcePanelHeight + 50)) / 2;

		springLayout = (SpringLayout) (statsPanel.getLayout());
		springLayout.putConstraint(SpringLayout.NORTH, possessTextField, 0, SpringLayout.NORTH, statsPanel);
		springLayout.putConstraint(SpringLayout.WEST, possessTextField, 0, SpringLayout.WEST, statsPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, possessTextField, eHeight, SpringLayout.NORTH, statsPanel);
		springLayout.putConstraint(SpringLayout.EAST, possessTextField, eWidth, SpringLayout.WEST, statsPanel);

		springLayout.putConstraint(SpringLayout.NORTH, soldTextField, 0, SpringLayout.SOUTH, possessTextField);
		springLayout.putConstraint(SpringLayout.WEST, soldTextField, 0, SpringLayout.WEST, statsPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, soldTextField, eHeight, SpringLayout.SOUTH, possessTextField);
		springLayout.putConstraint(SpringLayout.EAST, soldTextField, 0, SpringLayout.EAST, possessTextField);

		springLayout.putConstraint(SpringLayout.NORTH, averageProfitTextField, 0, SpringLayout.SOUTH, soldTextField);
		springLayout.putConstraint(SpringLayout.WEST, averageProfitTextField, 0, SpringLayout.WEST, statsPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, averageProfitTextField, eHeight, SpringLayout.SOUTH, soldTextField);
		springLayout.putConstraint(SpringLayout.EAST, averageProfitTextField, 0, SpringLayout.EAST, soldTextField);

		springLayout.putConstraint(SpringLayout.NORTH, buyButton, 0, SpringLayout.SOUTH, averageProfitTextField);
		springLayout.putConstraint(SpringLayout.WEST, buyButton, 0, SpringLayout.WEST, statsPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, buyButton, eHeight, SpringLayout.SOUTH, averageProfitTextField);
		springLayout.putConstraint(SpringLayout.EAST, buyButton, 0, SpringLayout.EAST, statsPanel);
	}

	private void initialize() {
		System.out.println("potato");
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		framePanel = new GUIElements.MyPanel(true);
		boolean fullscreen = true;
		if (fullscreen) {
			frame.setUndecorated(true);

			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice gd = ge.getDefaultScreenDevice();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			screenWidth = (int)screenSize.getWidth();
			screenHeight = (int)screenSize.getHeight();

			System.out.println(screenWidth + " | " + screenHeight);
			if (gd.isFullScreenSupported()) {
				try {
					gd.setFullScreenWindow(frame);
				} finally {
					gd.setFullScreenWindow(null);
				}
			}
			frame.setBounds(0, 0, screenWidth, screenHeight);
			createPanels();
		} else {
			screenWidth = 1000;
			screenHeight = 600;
			framePanel.setPreferredSize(new Dimension(screenWidth, screenHeight));
			createPanels();
			frame.setVisible(true);
			frame.pack();
		}		
	}

	private void createPanels() {
		decimalFormatter.setGroupingUsed(false);

		SpringLayout springLayout = new SpringLayout();
		framePanel.setLayout(springLayout);
		frame.setLayout(new BorderLayout());
		frame.add(framePanel, BorderLayout.CENTER);

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
		SpringLayout sl_marketPanel = new SpringLayout();
		merchantsPanel.setLayout(sl_marketPanel);
		gamePanel.add(merchantsPanel, "Merchants");

		headlineTextField = new GUIElements.MyTextField();
		sl_infoPanel.putConstraint(SpringLayout.NORTH, headlineTextField, 6, SpringLayout.NORTH, infoPanel);
		sl_infoPanel.putConstraint(SpringLayout.WEST, headlineTextField, 6, SpringLayout.WEST, infoPanel);
		sl_infoPanel.putConstraint(SpringLayout.SOUTH, headlineTextField, -6, SpringLayout.SOUTH, infoPanel);
		sl_infoPanel.putConstraint(SpringLayout.EAST, headlineTextField, -248, SpringLayout.EAST, infoPanel);
		infoPanel.add(headlineTextField);
		headlineTextField.setColumns(10);

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
		sl_infoPanel.putConstraint(SpringLayout.WEST, overviewPanelButton, 10, SpringLayout.EAST, craftersPanelButton);
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

		guildPanelCrafters = new GuildPanel(new String[] { "Apothecary", "Embroider", "Artificer", "Philosopher", "Smith", "Voyager", "Sage", "Chef" }, this, screenHeight - 50, screenWidth / 4);
		sl_craftersPanel.putConstraint(SpringLayout.NORTH, guildPanelCrafters, 10, SpringLayout.NORTH, craftersPanel);
		sl_craftersPanel.putConstraint(SpringLayout.WEST, guildPanelCrafters, 10, SpringLayout.WEST, craftersPanel);
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

		guildPanelMerchants = new GuildPanel(new String[] { "Weaver", "Spicer", "Temperer", "Mercer", "Purifier", "Smelter", "Mason", "Crofter" }, this, screenHeight - 50, screenWidth / 4);
		merchantsPanel.add(guildPanelMerchants);

		bodyPanel = new GUIElements.MyPanel(true);
		sl_marketPanel.putConstraint(SpringLayout.NORTH, bodyPanel, 10, SpringLayout.NORTH, merchantsPanel);
		sl_marketPanel.putConstraint(SpringLayout.WEST, bodyPanel, 10, SpringLayout.EAST, guildPanelMerchants);
		sl_marketPanel.putConstraint(SpringLayout.SOUTH, bodyPanel, -40, SpringLayout.SOUTH, merchantsPanel);
		sl_marketPanel.putConstraint(SpringLayout.EAST, bodyPanel, -10, SpringLayout.EAST, merchantsPanel);
		SpringLayout sl_bodyPanel = new SpringLayout();
		bodyPanel.setLayout(sl_bodyPanel);
		merchantsPanel.add(bodyPanel);

		resourceDetailPanel = new GUIElements.MyPanel(true);
		SpringLayout sl_resourceDetailPanel = new SpringLayout();
		resourceDetailPanel.setLayout(sl_resourceDetailPanel);
		bodyPanel.add(resourceDetailPanel);

		GUIElements.MyPanel resourceGraphPanel = new GUIElements.MyPanel(true);
		sl_bodyPanel.putConstraint(SpringLayout.NORTH, resourceGraphPanel, 10, SpringLayout.SOUTH, resourceDetailPanel);
		sl_bodyPanel.putConstraint(SpringLayout.WEST, resourceGraphPanel, 0, SpringLayout.WEST, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.SOUTH, resourceGraphPanel, 0, SpringLayout.SOUTH, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.EAST, resourceGraphPanel, 0, SpringLayout.EAST, resourceDetailPanel);
		resourceGraphPanel.setLayout(new GridLayout(0, 1, 0, 0));
		bodyPanel.add(resourceGraphPanel);

		topDetailPanel = new GUIElements.MyPanel(true);
		SpringLayout sl_topDetailPanel = new SpringLayout();
		topDetailPanel.setLayout(sl_topDetailPanel);
		resourceDetailPanel.add(topDetailPanel);

		iconPanel = new GUIElements.MyPanel(true);
		iconPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		topDetailPanel.add(iconPanel);

		typeTextField = new GUIElements.MyTextField();
		typeTextField.setEditable(false);
		typeTextField.setColumns(10);
		topDetailPanel.add(typeTextField);

		nameTextField = new GUIElements.MyTextField();
		nameTextField.setEditable(false);
		nameTextField.setColumns(10);
		topDetailPanel.add(nameTextField);

		rarityTextField = new GUIElements.MyTextField();
		rarityTextField.setColumns(10);
		topDetailPanel.add(rarityTextField);

		descriptionPanel = new GUIElements.MyPanel(false);
		descriptionPanel.setLayout(new BorderLayout());
		resourceDetailPanel.add(descriptionPanel);

		descriptionTextArea = new GUIElements.MyTextArea();
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setWrapStyleWord(true);
		descriptionTextArea.setEditable(false);
		descriptionPanel.add(descriptionTextArea, BorderLayout.CENTER);

		GUIElements.MyPanel bottomDetailPanel = new GUIElements.MyPanel(true);
		sl_resourceDetailPanel.putConstraint(SpringLayout.NORTH, bottomDetailPanel, 0, SpringLayout.NORTH, resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.WEST, bottomDetailPanel, 10, SpringLayout.EAST, descriptionPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.SOUTH, bottomDetailPanel, 0, SpringLayout.SOUTH, resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.EAST, bottomDetailPanel, 0, SpringLayout.EAST, resourceDetailPanel);
		SpringLayout sl_bottomDetailPanel = new SpringLayout();
		bottomDetailPanel.setLayout(sl_bottomDetailPanel);
		resourceDetailPanel.add(bottomDetailPanel);

		statsPanel = new GUIElements.MyPanel(false);
		SpringLayout sl_statsPanel = new SpringLayout();
		statsPanel.setLayout(sl_statsPanel);
		bottomDetailPanel.add(statsPanel);

		possessTextField = new GUIElements.MyTextField();
		possessTextField.setEditable(false);
		possessTextField.setColumns(10);
		statsPanel.add(possessTextField);

		averagePriceTextField = new GUIElements.MyTextField();
		sl_statsPanel.putConstraint(SpringLayout.NORTH, averagePriceTextField, 0, SpringLayout.NORTH, statsPanel);
		sl_statsPanel.putConstraint(SpringLayout.WEST, averagePriceTextField, 0, SpringLayout.EAST, possessTextField);
		sl_statsPanel.putConstraint(SpringLayout.SOUTH, averagePriceTextField, 0, SpringLayout.SOUTH, possessTextField);
		sl_statsPanel.putConstraint(SpringLayout.EAST, averagePriceTextField, 0, SpringLayout.EAST, statsPanel);
		averagePriceTextField.setEditable(false);
		averagePriceTextField.setColumns(10);
		statsPanel.add(averagePriceTextField);

		soldTextField = new GUIElements.MyTextField();
		soldTextField.setEditable(false);
		soldTextField.setColumns(10);
		statsPanel.add(soldTextField);

		boughtTextField = new GUIElements.MyTextField();
		sl_statsPanel.putConstraint(SpringLayout.NORTH, boughtTextField, 0, SpringLayout.NORTH, soldTextField);
		sl_statsPanel.putConstraint(SpringLayout.WEST, boughtTextField, 0, SpringLayout.EAST, soldTextField);
		sl_statsPanel.putConstraint(SpringLayout.SOUTH, boughtTextField, 0, SpringLayout.SOUTH, soldTextField);
		sl_statsPanel.putConstraint(SpringLayout.EAST, boughtTextField, 0, SpringLayout.EAST, statsPanel);
		boughtTextField.setColumns(10);
		statsPanel.add(boughtTextField);

		averageProfitTextField = new GUIElements.MyTextField();
		averageProfitTextField.setEditable(false);
		averageProfitTextField.setColumns(10);
		statsPanel.add(averageProfitTextField);

		demandSupplyTextField = new GUIElements.MyTextField();
		sl_statsPanel.putConstraint(SpringLayout.NORTH, demandSupplyTextField, 0, SpringLayout.NORTH, averageProfitTextField);
		sl_statsPanel.putConstraint(SpringLayout.WEST, demandSupplyTextField, 0, SpringLayout.EAST, averageProfitTextField);
		sl_statsPanel.putConstraint(SpringLayout.SOUTH, demandSupplyTextField, 0, SpringLayout.SOUTH, averageProfitTextField);
		sl_statsPanel.putConstraint(SpringLayout.EAST, demandSupplyTextField, 0, SpringLayout.EAST, statsPanel);
		demandSupplyTextField.setEditable(false);
		demandSupplyTextField.setColumns(10);
		statsPanel.add(demandSupplyTextField);

		buyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentResource != null) {
					String response = MarketController.buyResource(1, currentResource, Main.getPlayer());
					postNewHeadline(response);
				}
			}
		});
		statsPanel.add(buyButton);

		sellButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentResource != null) {
					String response = MarketController.sellResource(1, currentResource, Main.getPlayer());
					postNewHeadline(response);
				}
			}
		});

		sl_statsPanel.putConstraint(SpringLayout.NORTH, sellButton, 0, SpringLayout.SOUTH, buyButton);
		sl_statsPanel.putConstraint(SpringLayout.WEST, sellButton, 0, SpringLayout.WEST, statsPanel);
		sl_statsPanel.putConstraint(SpringLayout.SOUTH, sellButton, 0, SpringLayout.SOUTH, statsPanel);
		sl_statsPanel.putConstraint(SpringLayout.EAST, sellButton, 0, SpringLayout.EAST, statsPanel);
		statsPanel.add(sellButton);

		resourceGraphPanel.add(buyGraph);

		updateSpringConstraints();
	}
}