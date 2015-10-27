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
	private MyFrame					frame;
	private GUIElements.MyTextField	typeTextField, nameTextField, possessTextField, soldTextField, averageProfitTextField, averagePriceTextField, demandSupplyTextField, rarityTextField, headlineTextField;
	private GUIElements.MyTextArea	descriptionTextArea;
	private GUIElements.MyPanel		resourceList, resourceDetailPanel;
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

	private void updateGUI() {
		int swlast = screenWidth;
		int shlast = screenHeight;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = (int) screenSize.getWidth();
		screenHeight = (int) screenSize.getHeight();
		if (screenHeight != shlast || screenWidth != swlast) {
			resizing = true;
		} else {
			resizing = false;
		}
		if (selectedGuild != null && selectedResource != null) {
			setSelectedResource(selectedGuild, selectedResource);
		}
		minigameController.repaint();
		if (merchantsPanel.isVisible()) {
			guildPanelMerchants.repaint();
		} else if (craftersPanel.isVisible()) {
			guildPanelCrafters.repaint();
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
	class MyFrame extends JFrame {
		public Dimension getSize() {
			return new Dimension(screenWidth, screenHeight);
		}
	}

	
	
	private void setFullScreen() {
		frame = new MyFrame();
		// frame.setUndecorated(true);
		//
		// GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		// GraphicsDevice gd = ge.getDefaultScreenDevice();
		//
		// if (gd.isFullScreenSupported()) {
		// try {
		// gd.setFullScreenWindow(frame);
		// } finally {
		// gd.setFullScreenWindow(null);
		// }
		// }

		frame.setBackground(new Color(40, 40, 40));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = (int) screenSize.getWidth();
		screenHeight = (int) screenSize.getHeight();
		frame.setBounds(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private void initialize() {
		setFullScreen();

		decimalFormatter.setGroupingUsed(false);
		SpringLayout springLayout = new SpringLayout();
		gamePanel = new GUIElements.MyPanel(true);

		springLayout.putConstraint(SpringLayout.NORTH, gamePanel, 0, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, gamePanel, 0, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, gamePanel, -30, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, gamePanel, 0, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().setLayout(springLayout);

		infoPanel = new GUIElements.MyPanel(true);
		springLayout.putConstraint(SpringLayout.NORTH, infoPanel, -30, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, infoPanel, 0, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, infoPanel, 0, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, infoPanel, 0, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(infoPanel);
		frame.getContentPane().add(gamePanel);
		gamePanel.setLayout(new CardLayout(0, 0));

		merchantsPanel = new GUIElements.MyPanel(true);
		currentPanel = merchantsPanel;
		springLayout.putConstraint(SpringLayout.NORTH, merchantsPanel, 0, SpringLayout.NORTH, gamePanel);
		springLayout.putConstraint(SpringLayout.WEST, merchantsPanel, 0, SpringLayout.WEST, gamePanel);
		springLayout.putConstraint(SpringLayout.SOUTH, merchantsPanel, 0, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, merchantsPanel, 0, SpringLayout.EAST, frame.getContentPane());
		gamePanel.add(merchantsPanel, "Merchants");
		SpringLayout sl_marketPanel = new SpringLayout();
		merchantsPanel.setLayout(sl_marketPanel);

		SpringLayout sl_infoPanel = new SpringLayout();
		infoPanel.setLayout(sl_infoPanel);

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
		guildPanelCrafters = new GuildPanel(new String[] { "Apothecary", "Embroider", "Artificer", "Philosopher", "Smith", "Voyager", "Sage", "Chef" }, this, screenHeight - 50, screenWidth / 4);
		craftersPanel.add(guildPanelCrafters);
		springLayout.putConstraint(SpringLayout.NORTH, craftersPanel, 5, SpringLayout.NORTH, gamePanel);
		springLayout.putConstraint(SpringLayout.WEST, craftersPanel, 15, SpringLayout.WEST, gamePanel);
		gamePanel.add(craftersPanel, "Crafters");
		springLayout.putConstraint(SpringLayout.SOUTH, craftersPanel, 701, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, craftersPanel, 994, SpringLayout.WEST, frame.getContentPane());
		SpringLayout sl_craftersPanel = new SpringLayout();
		sl_craftersPanel.putConstraint(SpringLayout.NORTH, guildPanelCrafters, 10, SpringLayout.NORTH, craftersPanel);
		sl_craftersPanel.putConstraint(SpringLayout.WEST, guildPanelCrafters, 10, SpringLayout.WEST, craftersPanel);
		sl_craftersPanel.putConstraint(SpringLayout.SOUTH, guildPanelCrafters, -40, SpringLayout.SOUTH, craftersPanel);
		sl_craftersPanel.putConstraint(SpringLayout.EAST, guildPanelCrafters, 310, SpringLayout.WEST, craftersPanel);
		craftersPanel.setLayout(sl_craftersPanel);
		craftersPanel.setEnabled(false);

		minigameController = new MinigameController(screenWidth - 330, screenHeight / 2 - 40);
		sl_craftersPanel.putConstraint(SpringLayout.NORTH, minigameController, -screenHeight / 2, SpringLayout.SOUTH, craftersPanel);
		sl_craftersPanel.putConstraint(SpringLayout.WEST, minigameController, 10, SpringLayout.EAST, guildPanelCrafters);
		sl_craftersPanel.putConstraint(SpringLayout.SOUTH, minigameController, -40, SpringLayout.SOUTH, craftersPanel);
		sl_craftersPanel.putConstraint(SpringLayout.EAST, minigameController, -10, SpringLayout.EAST, craftersPanel);
		craftersPanel.add(minigameController);

		overviewPanel = new GUIElements.MyPanel(true);
		springLayout.putConstraint(SpringLayout.NORTH, overviewPanel, 5, SpringLayout.NORTH, gamePanel);
		springLayout.putConstraint(SpringLayout.WEST, overviewPanel, 20, SpringLayout.WEST, gamePanel);
		gamePanel.add(overviewPanel, "Overview");
		springLayout.putConstraint(SpringLayout.SOUTH, overviewPanel, 701, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, overviewPanel, 994, SpringLayout.WEST, frame.getContentPane());
		SpringLayout sl_overviewPanel = new SpringLayout();
		overviewPanel.setLayout(sl_overviewPanel);
		overviewPanel.setEnabled(false);

		guildPanelMerchants = new GuildPanel(new String[] { "Weaver", "Spicer", "Temperer", "Mercer", "Purifier", "Smelter", "Mason", "Crofter" }, this, screenHeight - 50, screenWidth / 4);
		sl_marketPanel.putConstraint(SpringLayout.NORTH, guildPanelMerchants, 10, SpringLayout.NORTH, merchantsPanel);
		sl_marketPanel.putConstraint(SpringLayout.WEST, guildPanelMerchants, 10, SpringLayout.WEST, merchantsPanel);
		sl_marketPanel.putConstraint(SpringLayout.SOUTH, guildPanelMerchants, -40, SpringLayout.SOUTH, merchantsPanel);
		sl_marketPanel.putConstraint(SpringLayout.EAST, guildPanelMerchants, screenWidth / 4, SpringLayout.WEST, merchantsPanel);
		merchantsPanel.add(guildPanelMerchants);
		SpringLayout sl_listPanel = new SpringLayout();

		GUIElements.MyPanel bodyPanel = new GUIElements.MyPanel(true);
		sl_marketPanel.putConstraint(SpringLayout.NORTH, bodyPanel, 10, SpringLayout.NORTH, merchantsPanel);
		sl_marketPanel.putConstraint(SpringLayout.WEST, bodyPanel, 10, SpringLayout.EAST, guildPanelMerchants);
		sl_marketPanel.putConstraint(SpringLayout.SOUTH, bodyPanel, -40, SpringLayout.SOUTH, merchantsPanel);
		sl_marketPanel.putConstraint(SpringLayout.EAST, bodyPanel, -10, SpringLayout.EAST, merchantsPanel);

		merchantsPanel.add(bodyPanel);
		SpringLayout sl_bodyPanel = new SpringLayout();
		bodyPanel.setLayout(sl_bodyPanel);

		int resourcePanelHeight = (screenHeight - 40) / 2;
		resourceDetailPanel = new GUIElements.MyPanel(true);
		sl_bodyPanel.putConstraint(SpringLayout.NORTH, resourceDetailPanel, 0, SpringLayout.NORTH, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.WEST, resourceDetailPanel, 0, SpringLayout.WEST, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.SOUTH, resourceDetailPanel, -resourcePanelHeight, SpringLayout.SOUTH, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.EAST, resourceDetailPanel, 0, SpringLayout.EAST, bodyPanel);
		bodyPanel.add(resourceDetailPanel);

		GUIElements.MyPanel resourceGraphPanel = new GUIElements.MyPanel(true);
		sl_bodyPanel.putConstraint(SpringLayout.NORTH, resourceGraphPanel, 10, SpringLayout.SOUTH, resourceDetailPanel);
		sl_bodyPanel.putConstraint(SpringLayout.WEST, resourceGraphPanel, 0, SpringLayout.WEST, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.SOUTH, resourceGraphPanel, 0, SpringLayout.SOUTH, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.EAST, resourceGraphPanel, 0, SpringLayout.EAST, resourceDetailPanel);
		SpringLayout sl_resourceDetailPanel = new SpringLayout();
		resourceDetailPanel.setLayout(sl_resourceDetailPanel);

		GUIElements.MyPanel topDetailPanel = new GUIElements.MyPanel(true);
		sl_resourceDetailPanel.putConstraint(SpringLayout.NORTH, topDetailPanel, 0, SpringLayout.NORTH, resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.WEST, topDetailPanel, 0, SpringLayout.WEST, resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.SOUTH, topDetailPanel, 0, SpringLayout.SOUTH, resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.EAST, topDetailPanel, resourcePanelHeight / 2, SpringLayout.WEST, resourceDetailPanel);
		resourceDetailPanel.add(topDetailPanel);
		SpringLayout sl_topDetailPanel = new SpringLayout();
		topDetailPanel.setLayout(sl_topDetailPanel);

		GUIElements.MyPanel iconPanel = new GUIElements.MyPanel(true);
		sl_topDetailPanel.putConstraint(SpringLayout.NORTH, iconPanel, 0, SpringLayout.NORTH, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.WEST, iconPanel, 0, SpringLayout.WEST, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.SOUTH, iconPanel, resourcePanelHeight / 2, SpringLayout.NORTH, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.EAST, iconPanel, 0, SpringLayout.EAST, topDetailPanel);
		topDetailPanel.add(iconPanel);
		iconPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		int textFieldHeight = resourcePanelHeight / 6 - 10;
		typeTextField = new GUIElements.MyTextField();
		sl_topDetailPanel.putConstraint(SpringLayout.NORTH, typeTextField, 10, SpringLayout.SOUTH, iconPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.WEST, typeTextField, 0, SpringLayout.WEST, iconPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.SOUTH, typeTextField, textFieldHeight, SpringLayout.SOUTH, iconPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.EAST, typeTextField, 0, SpringLayout.EAST, topDetailPanel);
		topDetailPanel.add(typeTextField);
		typeTextField.setEditable(false);
		typeTextField.setColumns(10);

		nameTextField = new GUIElements.MyTextField();
		sl_topDetailPanel.putConstraint(SpringLayout.NORTH, nameTextField, 10, SpringLayout.SOUTH, typeTextField);
		sl_topDetailPanel.putConstraint(SpringLayout.WEST, nameTextField, 0, SpringLayout.WEST, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.SOUTH, nameTextField, textFieldHeight, SpringLayout.SOUTH, typeTextField);
		sl_topDetailPanel.putConstraint(SpringLayout.EAST, nameTextField, 0, SpringLayout.EAST, topDetailPanel);
		topDetailPanel.add(nameTextField);
		nameTextField.setEditable(false);
		nameTextField.setColumns(10);

		rarityTextField = new GUIElements.MyTextField();
		sl_topDetailPanel.putConstraint(SpringLayout.NORTH, rarityTextField, 10, SpringLayout.SOUTH, nameTextField);
		sl_topDetailPanel.putConstraint(SpringLayout.WEST, rarityTextField, 0, SpringLayout.WEST, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.SOUTH, rarityTextField, textFieldHeight, SpringLayout.SOUTH, nameTextField);
		sl_topDetailPanel.putConstraint(SpringLayout.EAST, rarityTextField, 0, SpringLayout.EAST, topDetailPanel);
		topDetailPanel.add(rarityTextField);
		rarityTextField.setColumns(10);

		GUIElements.MyPanel descriptionPanel = new GUIElements.MyPanel(false);
		sl_resourceDetailPanel.putConstraint(SpringLayout.NORTH, descriptionPanel, 0, SpringLayout.NORTH, resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.WEST, descriptionPanel, 10, SpringLayout.EAST, topDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.SOUTH, descriptionPanel, 0, SpringLayout.SOUTH, resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.EAST, descriptionPanel, resourcePanelHeight / 2 + 10, SpringLayout.EAST, topDetailPanel);
		resourceDetailPanel.add(descriptionPanel);
		descriptionPanel.setLayout(new FlowLayout());
		descriptionTextArea = new GUIElements.MyTextArea();
		descriptionTextArea.setBounds(0, 0, 215, 316);
		descriptionPanel.add(descriptionTextArea);
		sl_topDetailPanel.putConstraint(SpringLayout.NORTH, descriptionTextArea, 10, SpringLayout.NORTH, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.WEST, descriptionTextArea, 343, SpringLayout.WEST, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.SOUTH, descriptionTextArea, 0, SpringLayout.SOUTH, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.EAST, descriptionTextArea, -10, SpringLayout.EAST, topDetailPanel);
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setWrapStyleWord(true);

		descriptionTextArea.setEditable(false);

		GUIElements.MyPanel bottomDetailPanel = new GUIElements.MyPanel(true);
		sl_resourceDetailPanel.putConstraint(SpringLayout.NORTH, bottomDetailPanel, 0, SpringLayout.NORTH, resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.WEST, bottomDetailPanel, 10, SpringLayout.EAST, descriptionPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.SOUTH, bottomDetailPanel, 0, SpringLayout.SOUTH, resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.EAST, bottomDetailPanel, 0, SpringLayout.EAST, resourceDetailPanel);
		resourceDetailPanel.add(bottomDetailPanel);
		SpringLayout sl_bottomDetailPanel = new SpringLayout();
		bottomDetailPanel.setLayout(sl_bottomDetailPanel);

		GUIElements.MyPanel statsPanel = new GUIElements.MyPanel(false);
		sl_bottomDetailPanel.putConstraint(SpringLayout.NORTH, statsPanel, 0, SpringLayout.NORTH, bottomDetailPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.WEST, statsPanel, 0, SpringLayout.WEST, bottomDetailPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.SOUTH, statsPanel, 0, SpringLayout.SOUTH, bottomDetailPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.EAST, statsPanel, 0, SpringLayout.EAST, bottomDetailPanel);
		bottomDetailPanel.add(statsPanel);
		SpringLayout sl_statsPanel = new SpringLayout();
		statsPanel.setLayout(sl_statsPanel);

		int eHeight = resourcePanelHeight / 5;
		System.out.println(resourcePanelHeight + " | " + eHeight);

		int eWidth = (screenWidth - ((screenWidth / 4) + resourcePanelHeight + 50)) / 2;
		possessTextField = new GUIElements.MyTextField();
		sl_statsPanel.putConstraint(SpringLayout.NORTH, possessTextField, 0, SpringLayout.NORTH, statsPanel);
		sl_statsPanel.putConstraint(SpringLayout.WEST, possessTextField, 0, SpringLayout.WEST, statsPanel);
		sl_statsPanel.putConstraint(SpringLayout.SOUTH, possessTextField, eHeight, SpringLayout.NORTH, statsPanel);
		sl_statsPanel.putConstraint(SpringLayout.EAST, possessTextField, eWidth, SpringLayout.WEST, statsPanel);
		possessTextField.setEditable(false);
		statsPanel.add(possessTextField);
		possessTextField.setColumns(10);

		averagePriceTextField = new GUIElements.MyTextField();
		sl_statsPanel.putConstraint(SpringLayout.NORTH, averagePriceTextField, 0, SpringLayout.NORTH, statsPanel);
		sl_statsPanel.putConstraint(SpringLayout.WEST, averagePriceTextField, 0, SpringLayout.EAST, possessTextField);
		sl_statsPanel.putConstraint(SpringLayout.SOUTH, averagePriceTextField, 0, SpringLayout.SOUTH, possessTextField);
		sl_statsPanel.putConstraint(SpringLayout.EAST, averagePriceTextField, 0, SpringLayout.EAST, statsPanel);
		averagePriceTextField.setEditable(false);
		statsPanel.add(averagePriceTextField);
		averagePriceTextField.setColumns(10);

		soldTextField = new GUIElements.MyTextField();
		sl_statsPanel.putConstraint(SpringLayout.NORTH, soldTextField, 0, SpringLayout.SOUTH, possessTextField);
		sl_statsPanel.putConstraint(SpringLayout.WEST, soldTextField, 0, SpringLayout.WEST, statsPanel);
		sl_statsPanel.putConstraint(SpringLayout.SOUTH, soldTextField, eHeight, SpringLayout.SOUTH, possessTextField);
		sl_statsPanel.putConstraint(SpringLayout.EAST, soldTextField, 0, SpringLayout.EAST, possessTextField);
		soldTextField.setEditable(false);
		statsPanel.add(soldTextField);
		soldTextField.setColumns(10);

		boughtTextField = new GUIElements.MyTextField();
		sl_statsPanel.putConstraint(SpringLayout.NORTH, boughtTextField, 0, SpringLayout.NORTH, soldTextField);
		sl_statsPanel.putConstraint(SpringLayout.WEST, boughtTextField, 0, SpringLayout.EAST, soldTextField);
		sl_statsPanel.putConstraint(SpringLayout.SOUTH, boughtTextField, 0, SpringLayout.SOUTH, soldTextField);
		sl_statsPanel.putConstraint(SpringLayout.EAST, boughtTextField, 0, SpringLayout.EAST, statsPanel);
		statsPanel.add(boughtTextField);
		boughtTextField.setColumns(10);

		averageProfitTextField = new GUIElements.MyTextField();
		sl_statsPanel.putConstraint(SpringLayout.NORTH, averageProfitTextField, 0, SpringLayout.SOUTH, soldTextField);
		sl_statsPanel.putConstraint(SpringLayout.WEST, averageProfitTextField, 0, SpringLayout.WEST, statsPanel);
		sl_statsPanel.putConstraint(SpringLayout.SOUTH, averageProfitTextField, eHeight, SpringLayout.SOUTH, soldTextField);
		sl_statsPanel.putConstraint(SpringLayout.EAST, averageProfitTextField, 0, SpringLayout.EAST, soldTextField);
		averageProfitTextField.setEditable(false);
		statsPanel.add(averageProfitTextField);
		averageProfitTextField.setColumns(10);

		demandSupplyTextField = new GUIElements.MyTextField();
		sl_statsPanel.putConstraint(SpringLayout.NORTH, demandSupplyTextField, 0, SpringLayout.NORTH, averageProfitTextField);
		sl_statsPanel.putConstraint(SpringLayout.WEST, demandSupplyTextField, 0, SpringLayout.EAST, averageProfitTextField);
		sl_statsPanel.putConstraint(SpringLayout.SOUTH, demandSupplyTextField, 0, SpringLayout.SOUTH, averageProfitTextField);
		sl_statsPanel.putConstraint(SpringLayout.EAST, demandSupplyTextField, 0, SpringLayout.EAST, statsPanel);
		statsPanel.add(demandSupplyTextField);
		demandSupplyTextField.setEditable(false);
		demandSupplyTextField.setColumns(10);

		buyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentResource != null) {
					String response = MarketController.buyResource(1, currentResource, Main.getPlayer());
					postNewHeadline(response);
				}
			}
		});

		sl_statsPanel.putConstraint(SpringLayout.NORTH, buyButton, 0, SpringLayout.SOUTH, averageProfitTextField);
		sl_statsPanel.putConstraint(SpringLayout.WEST, buyButton, 0, SpringLayout.WEST, statsPanel);
		sl_statsPanel.putConstraint(SpringLayout.SOUTH, buyButton, eHeight, SpringLayout.SOUTH, averageProfitTextField);
		sl_statsPanel.putConstraint(SpringLayout.EAST, buyButton, 0, SpringLayout.EAST, statsPanel);
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

		bodyPanel.add(resourceGraphPanel);
		resourceGraphPanel.setLayout(new GridLayout(0, 1, 0, 0));

		resourceGraphPanel.add(buyGraph);
	}
}