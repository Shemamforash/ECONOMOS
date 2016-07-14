package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.*;

import GUI.Crafter.CraftingPanel;
import GUI.Inventory.InventoryPanel;
import GUI.Merchant.MerchantsPanel;
import economos.UpdateCaller;
import economos.UpdateListener;

public class EconomosGUI extends JFrame implements UpdateListener {
	private static GUIElements.MyTextField headlineTextField;
	private static NumberFormat decimalFormatter = NumberFormat.getIntegerInstance();
	public static int timeStep = 9;
	private GUIElements.MyPanel gamePanel, overviewPanel, infoPanel;
	private MerchantsPanel merchantsPanel;
	private CraftingPanel craftersPanel;
	private InventoryPanel inventoryPanel;
	private static boolean resizing = false;
	private static int screenWidth = 800, screenHeight = 600;
	private static int largePanelGap = 15, smallPanelGap = 7;
	private boolean initialised;

	public static int screenWidth() {
		return screenWidth;
	}

	public static int screenHeight() {
		return screenHeight;
	}

	public static int largePanelGap() {
		return largePanelGap;
	}

	public static int smallPanelGap() {
		return smallPanelGap;
	}

	public static void postNewHeadline(String txt) {
		headlineTextField.setText(txt);
	}

	public EconomosGUI() {
		decimalFormatter.setMaximumFractionDigits(2);
		initialize();
		UpdateCaller.addListener(this);
		initialised = true;
	}

	/**
	 * Initialize the contents of the frame.
	 */

	public void receiveUpdate() {
		int swlast = screenWidth;
		int shlast = screenHeight;
		screenWidth = this.getWidth();
		screenHeight = this.getHeight();
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
	}

	// used for windowbuilder
	private void setFullScreen() {
		setUndecorated(true);

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();

		if (gd.isFullScreenSupported()) {
			try {
				gd.setFullScreenWindow(this);
			} finally {
				gd.setFullScreenWindow(null);
			}
		}

		setBackground(new Color(40, 40, 40));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = (int) screenSize.getWidth();
		screenHeight = (int) screenSize.getHeight();
		setBounds(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void initialize() {
		decimalFormatter.setGroupingUsed(false);
		setFullScreen();
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);

		gamePanel = new GUIElements.MyPanel(true);
		springLayout.putConstraint(SpringLayout.NORTH, gamePanel, 0, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, gamePanel, 0, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, gamePanel, -30, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, gamePanel, 0, SpringLayout.EAST, getContentPane());
		gamePanel.setLayout(new CardLayout(0, 0));
		this.add(gamePanel);

		infoPanel = new GUIElements.MyPanel(true);
		springLayout.putConstraint(SpringLayout.NORTH, infoPanel, -30, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, infoPanel, 0, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, infoPanel, 0, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, infoPanel, 0, SpringLayout.EAST, getContentPane());
		SpringLayout sl_infoPanel = new SpringLayout();
		infoPanel.setLayout(sl_infoPanel);
		this.add(infoPanel);

		merchantsPanel = new MerchantsPanel();
		springLayout.putConstraint(SpringLayout.NORTH, merchantsPanel, 0, SpringLayout.NORTH, gamePanel);
		springLayout.putConstraint(SpringLayout.WEST, merchantsPanel, 0, SpringLayout.WEST, gamePanel);
		springLayout.putConstraint(SpringLayout.SOUTH, merchantsPanel, 0, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, merchantsPanel, 0, SpringLayout.EAST, getContentPane());
		gamePanel.add(merchantsPanel, "Merchants");
		
		craftersPanel = new CraftingPanel();
		springLayout.putConstraint(SpringLayout.NORTH, craftersPanel, 0, SpringLayout.NORTH, gamePanel);
		springLayout.putConstraint(SpringLayout.WEST, craftersPanel, 0, SpringLayout.WEST, gamePanel);
		springLayout.putConstraint(SpringLayout.SOUTH, craftersPanel, 0, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, craftersPanel, 0, SpringLayout.EAST, getContentPane());
		gamePanel.add(craftersPanel, "Crafters");

		inventoryPanel = new InventoryPanel(getHeight());
		springLayout.putConstraint(SpringLayout.NORTH, inventoryPanel, 0, SpringLayout.NORTH, gamePanel);
		springLayout.putConstraint(SpringLayout.WEST, inventoryPanel, 0, SpringLayout.WEST, gamePanel);
		springLayout.putConstraint(SpringLayout.SOUTH, inventoryPanel, 0, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, inventoryPanel, 0, SpringLayout.EAST, getContentPane());
		gamePanel.add(inventoryPanel, "Inventory");

		headlineTextField = new GUIElements.MyTextField();
		sl_infoPanel.putConstraint(SpringLayout.NORTH, headlineTextField, 6, SpringLayout.NORTH, infoPanel);
		sl_infoPanel.putConstraint(SpringLayout.WEST, headlineTextField, 6, SpringLayout.WEST, infoPanel);
		sl_infoPanel.putConstraint(SpringLayout.SOUTH, headlineTextField, -6, SpringLayout.SOUTH, infoPanel);
		sl_infoPanel.putConstraint(SpringLayout.EAST, headlineTextField, -248, SpringLayout.EAST, infoPanel);
		infoPanel.add(headlineTextField);
		headlineTextField.setColumns(largePanelGap);

		GUIElements.MyButton merchantsPanelButton = new GUIElements.MyButton("Merchants", true);
		merchantsPanelButton.addActionListener(e -> {
				CardLayout cardLayout = (CardLayout) gamePanel.getLayout();
				cardLayout.show(gamePanel, "Merchants");
				gamePanel.revalidate();
		});

		sl_infoPanel.putConstraint(SpringLayout.NORTH, merchantsPanelButton, 0, SpringLayout.NORTH, headlineTextField);
		sl_infoPanel.putConstraint(SpringLayout.WEST, merchantsPanelButton, 6, SpringLayout.EAST, headlineTextField);
		sl_infoPanel.putConstraint(SpringLayout.SOUTH, merchantsPanelButton, 0, SpringLayout.SOUTH, headlineTextField);
		sl_infoPanel.putConstraint(SpringLayout.EAST, merchantsPanelButton, 80, SpringLayout.EAST, headlineTextField);
		infoPanel.add(merchantsPanelButton);

		GUIElements.MyButton craftersPanelButton = new GUIElements.MyButton("Crafters", true);
		craftersPanelButton.addActionListener(e -> {
				CardLayout cardLayout = (CardLayout) gamePanel.getLayout();
				cardLayout.show(gamePanel, "Crafters");
				gamePanel.revalidate();
		});

		sl_infoPanel.putConstraint(SpringLayout.NORTH, craftersPanelButton, 0, SpringLayout.NORTH,
				merchantsPanelButton);
		sl_infoPanel.putConstraint(SpringLayout.WEST, craftersPanelButton, 6, SpringLayout.EAST, merchantsPanelButton);
		sl_infoPanel.putConstraint(SpringLayout.SOUTH, craftersPanelButton, 0, SpringLayout.SOUTH,
				merchantsPanelButton);
		sl_infoPanel.putConstraint(SpringLayout.EAST, craftersPanelButton, 80, SpringLayout.EAST, merchantsPanelButton);
		infoPanel.add(craftersPanelButton);

		GUIElements.MyButton overviewPanelButton = new GUIElements.MyButton("Inventory", true);
		overviewPanelButton.addActionListener(e -> {
				CardLayout cardLayout = (CardLayout) gamePanel.getLayout();
				cardLayout.show(gamePanel, "Inventory");
				gamePanel.revalidate();
		});
		sl_infoPanel.putConstraint(SpringLayout.NORTH, overviewPanelButton, 0, SpringLayout.NORTH, craftersPanelButton);
		sl_infoPanel.putConstraint(SpringLayout.WEST, overviewPanelButton, largePanelGap, SpringLayout.EAST,
				craftersPanelButton);
		sl_infoPanel.putConstraint(SpringLayout.SOUTH, overviewPanelButton, 0, SpringLayout.SOUTH, craftersPanelButton);
		sl_infoPanel.putConstraint(SpringLayout.EAST, overviewPanelButton, 80, SpringLayout.EAST, craftersPanelButton);
		infoPanel.add(overviewPanelButton);

		overviewPanel = new GUIElements.MyPanel(true);
		springLayout.putConstraint(SpringLayout.NORTH, overviewPanel, 5, SpringLayout.NORTH, gamePanel);
		springLayout.putConstraint(SpringLayout.WEST, overviewPanel, 20, SpringLayout.WEST, gamePanel);
		springLayout.putConstraint(SpringLayout.SOUTH, overviewPanel, 701, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, overviewPanel, 994, SpringLayout.WEST, getContentPane());
		SpringLayout sl_overviewPanel = new SpringLayout();
		overviewPanel.setLayout(sl_overviewPanel);
		overviewPanel.setEnabled(false);
		gamePanel.add(overviewPanel, "Overview");
	}

	public static boolean resizing() {
		return resizing;
	}

	public static NumberFormat decimalFormatter() {
		return decimalFormatter;
	}

	@Override
	public boolean isInitialised() {
		return initialised;
	}
}