package economos;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.event.*;

public class EconomosGUI {
	private JFrame frame;
	private GUIElements.MyTextField typeTextField, nameTextField, possessTextField, soldTextField, averageProfitTextField,
			averagePriceTextField, demandTextField, supplyTextField;
	private GUIElements.MyFormattedTextField botBuyQuantityTextField;
	private GUIElements.MyButton setPriceButton;
	private GUIElements.MyFormattedTextField botBuyPriceTextField;
	private GUIElements.MyFormattedTextField botSellQuantityTextField;
	private GUIElements.MyFormattedTextField botSellPriceTextField;
	private GUIElements.MyFormattedTextField sellAmountTextField;
	private GUIElements.MyFormattedTextField buyAmountTextField;
	private JTextArea descriptionTextArea;
	private static Player currentPlayer;
	private JList resourceList, categoryList;
	private GUIElements.MyButton sellButton = new GUIElements.MyButton("Sell");
	private GUIElements.MyButton buyButton = new GUIElements.MyButton("Buy");
	private static PlayerResource selectedResource = null;
	private NumberFormat d = NumberFormat.getIntegerInstance();
	private GraphPanel buyGraph = new GraphPanel();
	private JCheckBox botCheckBox = new JCheckBox("Enable Bot (20C/s)");
	private GUIElements.MyTextField txtBuy;
	private GUIElements.MyTextField txtUnitsAtC;
	private GUIElements.MyTextField txtSell;
	private GUIElements.MyTextField txtUnitsAt;
	public static int timeStep = 10;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EconomosGUI window = new EconomosGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	class AIExecutor extends TimerTask {
		private int aiCount, currentAI = 0;
		private long starttime;

		public AIExecutor(int aiCount) {
			this.aiCount = aiCount;
		}

		public void run() {
			if (currentAI == aiCount) {
				this.cancel();
			} else {
				AI ai = new AI("Potato", "Potato Industries");
				++currentAI;
			}
		}
	}

	public EconomosGUI() {
		load();
		initialize();
		updateCategoryList(categoryList);
		int aiCount = 100;
		Timer t = new Timer();
		t.schedule(new AIExecutor(aiCount), 0, 1000 / aiCount);

		AI ai = new AI("Sam's AI", "Sam's AI");

		GUIElements.MyPanel panel = new GUIElements.MyPanel();
		panel.setBounds(0, 673, 994, 28);
		frame.getContentPane().add(panel);

		t.schedule(new UpdateGUI(), 0, 17);
	}

	private void load() {
		try {
			DataParser parser = new DataParser();
		} catch (IOException e) {
			System.out.println("Failed to load. Terminating");
			System.exit(0);
		}
		currentPlayer = new Player("Sam", "Potatronics");
		d.setMaximumFractionDigits(2);
	}

	class UpdateGUI extends TimerTask {
		public void run() {
			setSelectedResource((String) categoryList.getSelectedValue(),
					String.valueOf(resourceList.getSelectedValue()));
		}
	}

	public void updateCategoryList(JList list) {
		ArrayList<ResourceType<PlayerResource>> arr = new ArrayList<ResourceType<PlayerResource>>(
				currentPlayer.getPlayerResourceMap().getResourceTypes().values());
		String[] strarr = new String[arr.size()];
		for (int i = 0; i < arr.size(); ++i) {
			strarr[i] = arr.get(i).getType();
		}
		list.setListData(strarr);
	}

	public void setSelectedResource(String type, String name) {
		ResourceMap<PlayerResource> m = currentPlayer.getPlayerResourceMap();
		selectedResource = m.getResource(type, name);

		if (selectedResource != null) {
			MarketResource mr = selectedResource.getMarketResource();
			typeTextField.setText(selectedResource.getType());
			nameTextField.setText(selectedResource.getName());
			descriptionTextArea.setText(selectedResource.getDescription());
			supplyTextField.setText("Supply: " + mr.getSupply() + "p/s");
			possessTextField.setText("Owned: " + selectedResource.getQuantity());
			if (!sellAmountTextField.isEnabled()) {
				sellAmountTextField.setEnabled(true);
				sellAmountTextField.setText("1");
				buyAmountTextField.setEnabled(true);
				buyAmountTextField.setText("1");
			}
			demandTextField.setText("Demand: " + mr.getDemand() + "p/s");
			averagePriceTextField.setText("Average Price: C" + d.format(mr.getAveragePrice()));
			averageProfitTextField.setText("Average Profit: C" + d.format(selectedResource.getAverageProfit()));
			soldTextField.setText("Sold " + selectedResource.getSold() + " units");
			botCheckBox.setEnabled(true);
			botCheckBox.setSelected(selectedResource.isBotActive());
			buyButton.setText("Buy: C" + d.format(
					selectedResource.getMarketResource().getBuyPrice(Integer.parseInt(buyAmountTextField.getText()))));
			sellButton.setText("Sell: C" + d.format(selectedResource.getMarketResource()
					.getSellPrice(Integer.parseInt(sellAmountTextField.getText()))));
			if (selectedResource.isBotActive()) {
				botSellPriceTextField.setEnabled(true);
				botSellQuantityTextField.setEnabled(true);
				botBuyPriceTextField.setEnabled(true);
				botBuyQuantityTextField.setEnabled(true);
				setPriceButton.setEnabled(true);
			} else {
				botSellPriceTextField.setEnabled(false);
				botSellQuantityTextField.setEnabled(false);
				botBuyPriceTextField.setEnabled(false);
				botBuyQuantityTextField.setEnabled(false);
				setPriceButton.setEnabled(false);
			}
			buyGraph.repaint();
		} else {
			typeTextField.setText("");
			typeTextField.setText("");
			nameTextField.setText("");
			possessTextField.setText("");
			soldTextField.setText("");
			averageProfitTextField.setText("");
			averagePriceTextField.setText("");
			demandTextField.setText("");
			supplyTextField.setText("");
			setPriceButton.setText("");
			descriptionTextArea.setText("");
			sellButton.setText("Sell");
			buyButton.setText("Buy");
			sellAmountTextField.setValue(1);
			buyAmountTextField.setValue(1);
			sellAmountTextField.setEnabled(false);
			buyAmountTextField.setEnabled(false);
			botSellPriceTextField.setEnabled(false);
			botSellQuantityTextField.setEnabled(false);
			botBuyPriceTextField.setEnabled(false);
			botBuyQuantityTextField.setEnabled(false);
			setPriceButton.setEnabled(false);
			botCheckBox.setEnabled(false);
		}
	}

	public static UserResource getSelectedResource() {
		return selectedResource;
	}

	public void updateJList(String type, JList list) {
		if (currentPlayer.getPlayerResourceMap().getResourceTypes().containsKey(type)) {
			ArrayList<PlayerResource> arr = new ArrayList<PlayerResource>(
					currentPlayer.getPlayerResourceMap().getResourceTypes().get(type).getResourcesInType());
			String[] strarr = new String[arr.size()];
			for (int i = 0; i < strarr.length; ++i) {
				strarr[i] = ((Resource) arr.get(i)).getName();
			}
			list.setListData(strarr);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 1000, 730);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		d.setGroupingUsed(false);

		JTabbedPane tabPane = new JTabbedPane(JTabbedPane.TOP);
		tabPane.setBounds(0, 0, 994, 663);
		frame.getContentPane().add(tabPane);

		GUIElements.MyPanel marketPanel = new GUIElements.MyPanel();
		tabPane.addTab("Market", null, marketPanel, "Access the market to buy and sell resources");
		SpringLayout sl_marketPanel = new SpringLayout();
		marketPanel.setLayout(sl_marketPanel);

		GUIElements.MyPanel listPanel = new GUIElements.MyPanel();
		sl_marketPanel.putConstraint(SpringLayout.NORTH, listPanel, 0, SpringLayout.NORTH, marketPanel);
		sl_marketPanel.putConstraint(SpringLayout.WEST, listPanel, 0, SpringLayout.WEST, marketPanel);
		sl_marketPanel.putConstraint(SpringLayout.SOUTH, listPanel, 633, SpringLayout.NORTH, marketPanel);
		sl_marketPanel.putConstraint(SpringLayout.EAST, listPanel, 300, SpringLayout.WEST, marketPanel);
		marketPanel.add(listPanel);
		SpringLayout sl_listPanel = new SpringLayout();
		listPanel.setLayout(sl_listPanel);

		JScrollPane categoryScrollPane = new JScrollPane();
		sl_listPanel.putConstraint(SpringLayout.NORTH, categoryScrollPane, 0, SpringLayout.NORTH, listPanel);
		sl_listPanel.putConstraint(SpringLayout.WEST, categoryScrollPane, 0, SpringLayout.WEST, listPanel);
		sl_listPanel.putConstraint(SpringLayout.SOUTH, categoryScrollPane, 220, SpringLayout.NORTH, listPanel);
		sl_listPanel.putConstraint(SpringLayout.EAST, categoryScrollPane, 300, SpringLayout.WEST, listPanel);
		categoryScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		listPanel.add(categoryScrollPane);

		categoryList = new JList(new String[] { "Empty" });
		categoryList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting()) {
					updateJList((String) categoryList.getSelectedValue(), resourceList);
				}
			}
		});
		categoryScrollPane.setViewportView(categoryList);

		JScrollPane resourceScrollPane = new JScrollPane();
		sl_listPanel.putConstraint(SpringLayout.NORTH, resourceScrollPane, 6, SpringLayout.SOUTH, categoryScrollPane);
		sl_listPanel.putConstraint(SpringLayout.WEST, resourceScrollPane, 0, SpringLayout.WEST, listPanel);
		sl_listPanel.putConstraint(SpringLayout.SOUTH, resourceScrollPane, 632, SpringLayout.NORTH, listPanel);
		sl_listPanel.putConstraint(SpringLayout.EAST, resourceScrollPane, 300, SpringLayout.WEST, listPanel);
		resourceScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		listPanel.add(resourceScrollPane);

		resourceList = new JList(new String[] { "Empty" });
		resourceList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting() && categoryList.getSelectedValue() != "Empty") {
					setSelectedResource((String) categoryList.getSelectedValue(),
							String.valueOf(resourceList.getSelectedValue()));
				}
			}
		});
		resourceScrollPane.setViewportView(resourceList);

		GUIElements.MyPanel bodyPanel = new GUIElements.MyPanel();
		sl_marketPanel.putConstraint(SpringLayout.NORTH, bodyPanel, 0, SpringLayout.NORTH, marketPanel);
		sl_marketPanel.putConstraint(SpringLayout.WEST, bodyPanel, 6, SpringLayout.EAST, listPanel);
		sl_marketPanel.putConstraint(SpringLayout.SOUTH, bodyPanel, 0, SpringLayout.SOUTH, listPanel);
		sl_marketPanel.putConstraint(SpringLayout.EAST, bodyPanel, 0, SpringLayout.EAST, marketPanel);
		marketPanel.add(bodyPanel);
		SpringLayout sl_bodyPanel = new SpringLayout();
		bodyPanel.setLayout(sl_bodyPanel);

		GUIElements.MyPanel resourceDetailPanel = new GUIElements.MyPanel();
		sl_bodyPanel.putConstraint(SpringLayout.NORTH, resourceDetailPanel, 0, SpringLayout.NORTH, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.WEST, resourceDetailPanel, 0, SpringLayout.WEST, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.SOUTH, resourceDetailPanel, -299, SpringLayout.SOUTH, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.EAST, resourceDetailPanel, 672, SpringLayout.WEST, bodyPanel);
		bodyPanel.add(resourceDetailPanel);

		GUIElements.MyPanel resourceGraphPanel = new GUIElements.MyPanel();
		sl_bodyPanel.putConstraint(SpringLayout.NORTH, resourceGraphPanel, 6, SpringLayout.SOUTH, resourceDetailPanel);
		sl_bodyPanel.putConstraint(SpringLayout.WEST, resourceGraphPanel, 0, SpringLayout.WEST, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.SOUTH, resourceGraphPanel, -1, SpringLayout.SOUTH, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.EAST, resourceGraphPanel, 0, SpringLayout.EAST, resourceDetailPanel);
		SpringLayout sl_resourceDetailPanel = new SpringLayout();
		resourceDetailPanel.setLayout(sl_resourceDetailPanel);

		GUIElements.MyPanel topDetailPanel = new GUIElements.MyPanel();
		sl_resourceDetailPanel.putConstraint(SpringLayout.NORTH, topDetailPanel, 0, SpringLayout.NORTH,
				resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.WEST, topDetailPanel, 0, SpringLayout.WEST,
				resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.SOUTH, topDetailPanel, 141, SpringLayout.NORTH,
				resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.EAST, topDetailPanel, 0, SpringLayout.EAST,
				resourceDetailPanel);
		resourceDetailPanel.add(topDetailPanel);
		SpringLayout sl_topDetailPanel = new SpringLayout();
		topDetailPanel.setLayout(sl_topDetailPanel);

		GUIElements.MyPanel iconPanel = new GUIElements.MyPanel();
		sl_topDetailPanel.putConstraint(SpringLayout.NORTH, iconPanel, 10, SpringLayout.NORTH, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.WEST, iconPanel, 10, SpringLayout.WEST, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.EAST, iconPanel, 141, SpringLayout.WEST, topDetailPanel);
		topDetailPanel.add(iconPanel);

		typeTextField = new GUIElements.MyTextField();
		sl_topDetailPanel.putConstraint(SpringLayout.NORTH, typeTextField, 10, SpringLayout.NORTH, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.WEST, typeTextField, 6, SpringLayout.EAST, iconPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.SOUTH, typeTextField, -69, SpringLayout.SOUTH, topDetailPanel);
		topDetailPanel.add(typeTextField);
		typeTextField.setEditable(false);
		typeTextField.setColumns(10);

		nameTextField = new GUIElements.MyTextField();
		sl_topDetailPanel.putConstraint(SpringLayout.NORTH, nameTextField, 6, SpringLayout.SOUTH, typeTextField);
		sl_topDetailPanel.putConstraint(SpringLayout.SOUTH, nameTextField, 0, SpringLayout.SOUTH, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.WEST, nameTextField, 6, SpringLayout.EAST, iconPanel);
		topDetailPanel.add(nameTextField);
		nameTextField.setEditable(false);
		nameTextField.setColumns(10);

		descriptionTextArea = new JTextArea();
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setWrapStyleWord(true);
		sl_topDetailPanel.putConstraint(SpringLayout.EAST, descriptionTextArea, -10, SpringLayout.EAST, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.EAST, nameTextField, -6, SpringLayout.WEST, descriptionTextArea);
		sl_topDetailPanel.putConstraint(SpringLayout.EAST, typeTextField, -6, SpringLayout.WEST, descriptionTextArea);
		sl_topDetailPanel.putConstraint(SpringLayout.WEST, descriptionTextArea, 343, SpringLayout.WEST, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.SOUTH, iconPanel, 0, SpringLayout.SOUTH, descriptionTextArea);
		sl_topDetailPanel.putConstraint(SpringLayout.NORTH, descriptionTextArea, 10, SpringLayout.NORTH,
				topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.SOUTH, descriptionTextArea, 0, SpringLayout.SOUTH, topDetailPanel);
		topDetailPanel.add(descriptionTextArea);
		descriptionTextArea.setEditable(false);

		GUIElements.MyPanel bottomDetailPanel = new GUIElements.MyPanel();
		sl_resourceDetailPanel.putConstraint(SpringLayout.NORTH, bottomDetailPanel, 6, SpringLayout.SOUTH,
				topDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.WEST, bottomDetailPanel, 0, SpringLayout.WEST,
				resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.SOUTH, bottomDetailPanel, 0, SpringLayout.SOUTH,
				resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.EAST, bottomDetailPanel, 0, SpringLayout.EAST,
				topDetailPanel);
		resourceDetailPanel.add(bottomDetailPanel);
		SpringLayout sl_bottomDetailPanel = new SpringLayout();
		bottomDetailPanel.setLayout(sl_bottomDetailPanel);

		GUIElements.MyPanel statsPanel = new GUIElements.MyPanel();
		sl_bottomDetailPanel.putConstraint(SpringLayout.NORTH, statsPanel, 0, SpringLayout.NORTH, bottomDetailPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.WEST, statsPanel, 10, SpringLayout.WEST, bottomDetailPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.SOUTH, statsPanel, 187, SpringLayout.NORTH, bottomDetailPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.EAST, statsPanel, 210, SpringLayout.WEST, bottomDetailPanel);
		bottomDetailPanel.add(statsPanel);
		statsPanel.setLayout(new GridLayout(4, 1, 0, 10));

		possessTextField = new GUIElements.MyTextField();
		possessTextField.setEditable(false);
		statsPanel.add(possessTextField);
		possessTextField.setColumns(10);

		soldTextField = new GUIElements.MyTextField();
		soldTextField.setEditable(false);
		statsPanel.add(soldTextField);
		soldTextField.setColumns(10);

		averageProfitTextField = new GUIElements.MyTextField();
		averageProfitTextField.setEditable(false);
		statsPanel.add(averageProfitTextField);
		averageProfitTextField.setColumns(10);

		averagePriceTextField = new GUIElements.MyTextField();
		averagePriceTextField.setEditable(false);
		statsPanel.add(averagePriceTextField);
		averagePriceTextField.setColumns(10);

		GUIElements.MyPanel purchasePanel = new GUIElements.MyPanel();
		sl_bottomDetailPanel.putConstraint(SpringLayout.NORTH, purchasePanel, 0, SpringLayout.NORTH, statsPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.WEST, purchasePanel, 6, SpringLayout.EAST, statsPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.SOUTH, purchasePanel, 0, SpringLayout.SOUTH, statsPanel);
		bottomDetailPanel.add(purchasePanel);
		purchasePanel.setLayout(new GridLayout(4, 1, 0, 10));

		demandTextField = new GUIElements.MyTextField();
		purchasePanel.add(demandTextField);
		demandTextField.setEditable(false);
		demandTextField.setColumns(10);

		supplyTextField = new GUIElements.MyTextField();
		purchasePanel.add(supplyTextField);
		supplyTextField.setEditable(false);
		supplyTextField.setColumns(10);

		GUIElements.MyPanel botPanel = new GUIElements.MyPanel();
		sl_bottomDetailPanel.putConstraint(SpringLayout.EAST, purchasePanel, -6, SpringLayout.WEST, botPanel);

		GUIElements.MyPanel sellPanel = new GUIElements.MyPanel();
		purchasePanel.add(sellPanel);
		sellPanel.setLayout(null);

		sellButton.setBounds(85, 0, 126, 39);
		sellButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedResource != null) {
					String response = MarketController.sellResource(Integer.valueOf(sellAmountTextField.getText()),
							selectedResource, currentPlayer);
					System.out.println(response);
				}
			}
		});
		sellPanel.add(sellButton);

		sellAmountTextField = new GUIElements.MyFormattedTextField(d);
		sellAmountTextField.setEnabled(false);
		sellAmountTextField.setValue(1);
		sellAmountTextField.setBounds(0, 0, 75, 39);

		sellPanel.add(sellAmountTextField);

		GUIElements.MyPanel buyPanel = new GUIElements.MyPanel();
		purchasePanel.add(buyPanel);
		buyPanel.setLayout(null);

		buyButton.setBounds(85, 0, 126, 39);
		buyPanel.add(buyButton);

		buyAmountTextField = new GUIElements.MyFormattedTextField(d);
		buyAmountTextField.setEnabled(false);
		buyAmountTextField.setValue(1);
		buyAmountTextField.setBounds(0, 0, 75, 39);

		buyPanel.add(buyAmountTextField);
		buyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedResource != null) {
					String response = MarketController.buyResource(Integer.valueOf(buyAmountTextField.getText()),
							selectedResource, currentPlayer);
					System.out.println(response);
				}
			}
		});
		sl_bottomDetailPanel.putConstraint(SpringLayout.SOUTH, botPanel, 0, SpringLayout.SOUTH, statsPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.NORTH, botPanel, 0, SpringLayout.NORTH, statsPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.WEST, botPanel, 433, SpringLayout.WEST, bottomDetailPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.EAST, botPanel, 0, SpringLayout.EAST, bottomDetailPanel);
		bottomDetailPanel.add(botPanel);
		botPanel.setLayout(new GridLayout(4, 1, 0, 0));

		botCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					selectedResource.activeBot(true);
				} else {
					selectedResource.activeBot(false);
				}
			}
		});
		botPanel.add(botCheckBox);

		GUIElements.MyPanel botBuyPanel = new GUIElements.MyPanel();
		botPanel.add(botBuyPanel);
		SpringLayout sl_botBuyPanel = new SpringLayout();
		botBuyPanel.setLayout(sl_botBuyPanel);

		botBuyQuantityTextField = new GUIElements.MyFormattedTextField(d);
		botBuyQuantityTextField.setValue(1);

		sl_botBuyPanel.putConstraint(SpringLayout.NORTH, botBuyQuantityTextField, 10, SpringLayout.NORTH, botBuyPanel);
		sl_botBuyPanel.putConstraint(SpringLayout.SOUTH, botBuyQuantityTextField, -10, SpringLayout.SOUTH, botBuyPanel);
		botBuyPanel.add(botBuyQuantityTextField);
		botBuyQuantityTextField.setColumns(10);

		botBuyPriceTextField = new GUIElements.MyFormattedTextField(d);
		botBuyPriceTextField.setValue(1);

		sl_botBuyPanel.putConstraint(SpringLayout.WEST, botBuyPriceTextField, 161, SpringLayout.WEST, botBuyPanel);
		sl_botBuyPanel.putConstraint(SpringLayout.EAST, botBuyPriceTextField, 0, SpringLayout.EAST, botBuyPanel);
		sl_botBuyPanel.putConstraint(SpringLayout.NORTH, botBuyPriceTextField, 10, SpringLayout.NORTH, botBuyPanel);
		sl_botBuyPanel.putConstraint(SpringLayout.SOUTH, botBuyPriceTextField, -10, SpringLayout.SOUTH, botBuyPanel);
		botBuyPanel.add(botBuyPriceTextField);

		txtBuy = new GUIElements.MyTextField();
		sl_botBuyPanel.putConstraint(SpringLayout.EAST, txtBuy, -207, SpringLayout.EAST, botBuyPanel);
		sl_botBuyPanel.putConstraint(SpringLayout.WEST, botBuyQuantityTextField, 6, SpringLayout.EAST, txtBuy);
		sl_botBuyPanel.putConstraint(SpringLayout.NORTH, txtBuy, 10, SpringLayout.NORTH, botBuyPanel);
		sl_botBuyPanel.putConstraint(SpringLayout.WEST, txtBuy, 0, SpringLayout.WEST, botBuyPanel);
		sl_botBuyPanel.putConstraint(SpringLayout.SOUTH, txtBuy, -10, SpringLayout.SOUTH, botBuyPanel);
		txtBuy.setEditable(false);
		txtBuy.setText("Buy");
		botBuyPanel.add(txtBuy);
		txtBuy.setColumns(10);

		txtUnitsAtC = new GUIElements.MyTextField();
		sl_botBuyPanel.putConstraint(SpringLayout.EAST, botBuyQuantityTextField, -6, SpringLayout.WEST, txtUnitsAtC);
		sl_botBuyPanel.putConstraint(SpringLayout.EAST, txtUnitsAtC, -6, SpringLayout.WEST, botBuyPriceTextField);
		sl_botBuyPanel.putConstraint(SpringLayout.NORTH, txtUnitsAtC, 10, SpringLayout.NORTH, botBuyPanel);
		sl_botBuyPanel.putConstraint(SpringLayout.SOUTH, txtUnitsAtC, -10, SpringLayout.SOUTH, botBuyPanel);
		sl_botBuyPanel.putConstraint(SpringLayout.WEST, txtUnitsAtC, 110, SpringLayout.WEST, botBuyPanel);
		txtUnitsAtC.setText("units at");
		txtUnitsAtC.setEditable(false);
		txtUnitsAtC.setColumns(10);
		botBuyPanel.add(txtUnitsAtC);

		GUIElements.MyPanel botSellPanel = new GUIElements.MyPanel();
		botPanel.add(botSellPanel);
		SpringLayout sl_botSellPanel = new SpringLayout();
		botSellPanel.setLayout(sl_botSellPanel);

		txtSell = new GUIElements.MyTextField();
		txtSell.setText("Sell");
		txtSell.setEditable(false);
		sl_botSellPanel.putConstraint(SpringLayout.NORTH, txtSell, 10, SpringLayout.NORTH, botSellPanel);
		sl_botSellPanel.putConstraint(SpringLayout.WEST, txtSell, 0, SpringLayout.WEST, botSellPanel);
		sl_botSellPanel.putConstraint(SpringLayout.SOUTH, txtSell, -10, SpringLayout.SOUTH, botSellPanel);
		sl_botSellPanel.putConstraint(SpringLayout.EAST, txtSell, 32, SpringLayout.WEST, botSellPanel);
		botSellPanel.add(txtSell);
		txtSell.setColumns(10);

		botSellQuantityTextField = new GUIElements.MyFormattedTextField(d);
		botSellQuantityTextField.setValue(1);

		sl_botSellPanel.putConstraint(SpringLayout.NORTH, botSellQuantityTextField, 0, SpringLayout.NORTH, txtSell);
		sl_botSellPanel.putConstraint(SpringLayout.WEST, botSellQuantityTextField, 6, SpringLayout.EAST, txtSell);
		sl_botSellPanel.putConstraint(SpringLayout.SOUTH, botSellQuantityTextField, 0, SpringLayout.SOUTH, txtSell);
		sl_botSellPanel.putConstraint(SpringLayout.EAST, botSellQuantityTextField, 72, SpringLayout.EAST, txtSell);
		botSellPanel.add(botSellQuantityTextField);

		txtUnitsAt = new GUIElements.MyTextField();
		sl_botSellPanel.putConstraint(SpringLayout.NORTH, txtUnitsAt, 0, SpringLayout.NORTH, txtSell);
		sl_botSellPanel.putConstraint(SpringLayout.WEST, txtUnitsAt, 6, SpringLayout.EAST, botSellQuantityTextField);
		sl_botSellPanel.putConstraint(SpringLayout.SOUTH, txtUnitsAt, 0, SpringLayout.SOUTH, txtSell);
		txtUnitsAt.setEditable(false);
		txtUnitsAt.setText("units at");
		botSellPanel.add(txtUnitsAt);
		txtUnitsAt.setColumns(10);

		botSellPriceTextField = new GUIElements.MyFormattedTextField(d);
		botSellPriceTextField.setValue(1);

		sl_botSellPanel.putConstraint(SpringLayout.WEST, botSellPriceTextField, 161, SpringLayout.WEST, botSellPanel);
		sl_botSellPanel.putConstraint(SpringLayout.EAST, txtUnitsAt, -6, SpringLayout.WEST, botSellPriceTextField);
		sl_botSellPanel.putConstraint(SpringLayout.NORTH, botSellPriceTextField, -26, SpringLayout.SOUTH, txtSell);
		sl_botSellPanel.putConstraint(SpringLayout.SOUTH, botSellPriceTextField, -10, SpringLayout.SOUTH, botSellPanel);
		sl_botSellPanel.putConstraint(SpringLayout.EAST, botSellPriceTextField, 0, SpringLayout.EAST, botSellPanel);
		botSellPanel.add(botSellPriceTextField);

		setPriceButton = new GUIElements.MyButton();
		setPriceButton.setText("Confirm Prices");
		setPriceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectedResource.setBotBuy(Integer.valueOf(botBuyPriceTextField.getText()),
						Integer.valueOf(botBuyQuantityTextField.getText()));
				selectedResource.setBotSell(Integer.valueOf(botSellPriceTextField.getText()),
						Integer.valueOf(botSellQuantityTextField.getText()));
			}
		});

		botPanel.add(setPriceButton);
		bodyPanel.add(resourceGraphPanel);
		resourceGraphPanel.setLayout(new GridLayout(0, 1, 0, 0));

		resourceGraphPanel.add(buyGraph);

		GUIElements.MyPanel companyPanel = new GUIElements.MyPanel();
		tabPane.addTab("Company", null, companyPanel, "View information about your company and player statistics");
		companyPanel.setLayout(null);

		JScrollPane pastTransactionsScrollPane = new JScrollPane();
		pastTransactionsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		pastTransactionsScrollPane.setBounds(0, 0, 989, 153);
		companyPanel.add(pastTransactionsScrollPane);

		JTextArea pastTransactionsTextArea = new JTextArea();
		pastTransactionsTextArea.setEditable(false);
		pastTransactionsScrollPane.setViewportView(pastTransactionsTextArea);

		JScrollPane playerStatsScrollPane = new JScrollPane();
		playerStatsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		playerStatsScrollPane.setBounds(710, 164, 279, 471);
		companyPanel.add(playerStatsScrollPane);

		JTextArea playerStatsTextArea = new JTextArea();
		playerStatsScrollPane.setViewportView(playerStatsTextArea);

		GUIElements.MyPanel constructionPanel = new GUIElements.MyPanel();
		tabPane.addTab("Construction", null, constructionPanel, "Access Construction options from this tab");
	}
}
