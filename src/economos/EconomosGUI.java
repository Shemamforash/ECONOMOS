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
	private GUIElements.MyTextField typeTextField, nameTextField, possessTextField, soldTextField,
			averageProfitTextField, averagePriceTextField, demandTextField, supplyTextField, rarityTextField, headlineTextField;
	private GUIElements.MyFormattedTextField sellAmountTextField, buyAmountTextField;
	private GUIElements.MyTextArea descriptionTextArea;
	private static Player currentPlayer;
	private GUIElements.MyPanel resourceList;
	private GUIElements.MyButton sellButton = new GUIElements.MyButton("Sell", true);
	private GUIElements.MyButton buyButton = new GUIElements.MyButton("Buy", true);
	private static PlayerResource currentResource = null;
	private NumberFormat decimalFormatter = NumberFormat.getIntegerInstance();
	private GraphPanel buyGraph = new GraphPanel();
	public static int timeStep = 4;
	private JButton selectedGuild, selectedResource;
	private SpringLayout springLayout;
	private GUIElements.MyPanel marketPanel;
	private boolean fieldsReset = false;

	public void postNewHeadline(String txt) {
		headlineTextField.setText(txt);
	}

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
		int aiCount = 100;
		Timer t = new Timer();
		t.schedule(new AIExecutor(aiCount), 0, 1000 / aiCount);

		AI ai = new AI("Sam's AI", "Sam's AI");

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
		decimalFormatter.setMaximumFractionDigits(2);
	}

	class UpdateGUI extends TimerTask {
		public void run() {
			if (selectedGuild != null && selectedResource != null) {
				setSelectedResource(selectedGuild.getText(), selectedResource.getText());
			}
		}
	}

	public void setSelectedResource(String type, String name) {
		ResourceMap<PlayerResource> m = currentPlayer.getPlayerResourceMap();
		currentResource = m.getResource(type, name);

		if (currentResource != null) {
			MarketResource mr = currentResource.getMarketResource();
			typeTextField.setText(currentResource.getType());
			nameTextField.setText(currentResource.getName());
			rarityTextField.setText(currentResource.getRarity());
			descriptionTextArea.setText(currentResource.getDescription());
			supplyTextField.setText("Supply: " + mr.getSupply() + "p/s");
			possessTextField.setText("Owned: " + currentResource.getQuantity());
			if (!sellAmountTextField.isEnabled()) {
				sellAmountTextField.setEnabled(true);
				sellAmountTextField.setText("1");
				buyAmountTextField.setEnabled(true);
				buyAmountTextField.setText("1");
			}
			demandTextField.setText("Demand: " + mr.getDemand() + "p/s");
			averagePriceTextField.setText("Average Price: C" + decimalFormatter.format(mr.getAveragePrice()));
			averageProfitTextField
					.setText("Average Profit: C" + decimalFormatter.format(currentResource.getAverageProfit()));
			soldTextField.setText("Sold " + currentResource.getSold() + " units");
			try {
				buyButton.setText("Buy: C" + decimalFormatter.format(currentResource.getMarketResource()
						.getBuyPrice(Integer.parseInt(buyAmountTextField.getText()))));
			} catch (NumberFormatException n) {
				// DOSOMETHING
			}

			try {
				sellButton.setText("Sell: C" + decimalFormatter.format(currentResource.getMarketResource()
						.getSellPrice(Integer.parseInt(sellAmountTextField.getText()))));
			} catch (NumberFormatException n) {
				// DOSOMETHING
			}

			buyGraph.repaint();
			fieldsReset = false;
		} else if(!fieldsReset){
			typeTextField.setText("");
			rarityTextField.setText("");
			nameTextField.setText("");
			possessTextField.setText("");
			soldTextField.setText("");
			averageProfitTextField.setText("");
			averagePriceTextField.setText("");
			demandTextField.setText("");
			supplyTextField.setText("");
			descriptionTextArea.setText("");
			sellButton.setText("Sell");
			buyButton.setText("Buy");
			sellAmountTextField.setValue(1);
			buyAmountTextField.setValue(1);
			sellAmountTextField.setEnabled(false);
			buyAmountTextField.setEnabled(false);
			buyGraph.repaint();
			fieldsReset = true;
		}
	}

	public static UserResource getSelectedResource() {
		return currentResource;
	}

	private class ResourceButton extends GUIElements.MyButton {
		private ResourceButton thisButton;
		
		public ResourceButton(String text, boolean enabled, boolean darker){
			super(text, enabled, new Color(30, 30, 30), new Color(25, 25, 25));
			if(!enabled){
				setForeground(Color.white);
			}
			thisButton = this;
			this.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					if(selectedResource != null){
						selectedResource.setSelected(false);
					}
					selectedResource = thisButton;
					selectedResource.setSelected(true);
				}				
			});
		}
	}
	
	public void updateMyList(String type) {
		if (currentPlayer.getPlayerResourceMap().getResourceTypes().containsKey(type)) {
			resourceList.removeAll();
			ArrayList<PlayerResource> arr = new ArrayList<PlayerResource>(
					currentPlayer.getPlayerResourceMap().getResourceTypes().get(type).getResourcesInType());
			String[] rarities = new String[]{"Commonplace", "Unusual", "Soughtafter", "Coveted", "Legendary"};
			int ctr = 0;
			boolean setDarker = false;
			for (int i = 0; i < arr.size(); ++i) {
				setDarker = !setDarker;
				ResourceButton tempButton;
				if(i % 4 == 0){
					tempButton = new ResourceButton(rarities[ctr], false, setDarker);
					resourceList.add(tempButton);
					++ctr;
				}
				tempButton = new ResourceButton(arr.get(i).getName(), true, setDarker);
				resourceList.add(tempButton);
			}
			resourceList.validate();
			resourceList.repaint();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBackground(new Color(40, 40, 40));
		frame.setBounds(100, 100, 1000, 730);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		decimalFormatter.setGroupingUsed(false);
		springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);

		marketPanel = new GUIElements.MyPanel(true);
		springLayout.putConstraint(SpringLayout.NORTH, marketPanel, 0, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, marketPanel, 0, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, marketPanel, 0, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, marketPanel, 0, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(marketPanel);
		SpringLayout sl_marketPanel = new SpringLayout();
		marketPanel.setLayout(sl_marketPanel);

		GUIElements.MyPanel listPanel = new GUIElements.MyPanel(true);
		sl_marketPanel.putConstraint(SpringLayout.NORTH, listPanel, 18, SpringLayout.NORTH, marketPanel);
		sl_marketPanel.putConstraint(SpringLayout.WEST, listPanel, 6, SpringLayout.WEST, marketPanel);
		sl_marketPanel.putConstraint(SpringLayout.SOUTH, listPanel, -44, SpringLayout.SOUTH, marketPanel);
		sl_marketPanel.putConstraint(SpringLayout.EAST, listPanel, 300, SpringLayout.WEST, marketPanel);
		marketPanel.add(listPanel);
		SpringLayout sl_listPanel = new SpringLayout();
		listPanel.setLayout(sl_listPanel);

		JScrollPane resourceScrollPane = new JScrollPane();
		sl_listPanel.putConstraint(SpringLayout.NORTH, resourceScrollPane, 0, SpringLayout.NORTH, listPanel);
		resourceScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		sl_listPanel.putConstraint(SpringLayout.WEST, resourceScrollPane, 84, SpringLayout.WEST, listPanel);
		sl_listPanel.putConstraint(SpringLayout.SOUTH, resourceScrollPane, -1, SpringLayout.SOUTH, listPanel);
		sl_listPanel.putConstraint(SpringLayout.EAST, resourceScrollPane, 300, SpringLayout.WEST, listPanel);
		resourceScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		resourceScrollPane.setBorder(null);
		listPanel.add(resourceScrollPane);

		resourceList = new GUIElements.MyPanel(false);
//		resourceList.addListSelectionListener(new ListSelectionListener() {
//			public void valueChanged(ListSelectionEvent arg0) {
//				if (!arg0.getValueIsAdjusting() && selectedGuild != null) {
//					setSelectedResource(selectedGuild.getText(), String.valueOf(resourceList.getSelectedValue()));
//				}
//			}
//		});
		resourceScrollPane.setViewportView(resourceList);
		resourceList.setLayout(new GridLayout(25, 1, 0, 0));

		GUIElements.MyPanel bodyPanel = new GUIElements.MyPanel(true);
		sl_marketPanel.putConstraint(SpringLayout.NORTH, bodyPanel, 0, SpringLayout.NORTH, marketPanel);
		sl_marketPanel.putConstraint(SpringLayout.WEST, bodyPanel, 12, SpringLayout.EAST, listPanel);
		sl_marketPanel.putConstraint(SpringLayout.SOUTH, bodyPanel, -44, SpringLayout.SOUTH, marketPanel);
		sl_marketPanel.putConstraint(SpringLayout.EAST, bodyPanel, -12, SpringLayout.EAST, marketPanel);

		GUIElements.MyPanel guildButtonPanel = new GUIElements.MyPanel(true);
		sl_listPanel.putConstraint(SpringLayout.NORTH, guildButtonPanel, 0, SpringLayout.NORTH, listPanel);
		sl_listPanel.putConstraint(SpringLayout.WEST, guildButtonPanel, 0, SpringLayout.WEST, listPanel);
		sl_listPanel.putConstraint(SpringLayout.SOUTH, guildButtonPanel, 0, SpringLayout.SOUTH, listPanel);
		sl_listPanel.putConstraint(SpringLayout.EAST, guildButtonPanel, -6, SpringLayout.WEST, resourceScrollPane);
		listPanel.add(guildButtonPanel);
		guildButtonPanel.setLayout(new GridLayout(8, 1, 6, 0));

		class MyGuildButton extends GUIElements.MyButton {
			public MyGuildButton thisButton;

			public MyGuildButton(String text) {
				super(text, true);
				thisButton = this;
				this.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if(selectedGuild != null){
							selectedGuild.setSelected(false);
						}
						selectedGuild = thisButton;
						selectedGuild.setSelected(true);
						updateMyList(selectedGuild.getText());
					}
				});
			}
		}

		MyGuildButton btnWeaver = new MyGuildButton("Weaver");
		guildButtonPanel.add(btnWeaver);

		MyGuildButton btnSpicer = new MyGuildButton("Spicer");
		guildButtonPanel.add(btnSpicer);

		MyGuildButton btnTemperer = new MyGuildButton("Temperer");
		guildButtonPanel.add(btnTemperer);

		MyGuildButton btnMercer = new MyGuildButton("Mercer");
		guildButtonPanel.add(btnMercer);

		MyGuildButton btnPurifier = new MyGuildButton("Purifier");
		guildButtonPanel.add(btnPurifier);

		MyGuildButton btnSmelter = new MyGuildButton("Smelter");
		guildButtonPanel.add(btnSmelter);

		MyGuildButton btnCrofter = new MyGuildButton("Crofter");
		guildButtonPanel.add(btnCrofter);

		MyGuildButton btnMason = new MyGuildButton("Mason");
		guildButtonPanel.add(btnMason);
		marketPanel.add(bodyPanel);
		SpringLayout sl_bodyPanel = new SpringLayout();
		bodyPanel.setLayout(sl_bodyPanel);

		GUIElements.MyPanel resourceDetailPanel = new GUIElements.MyPanel(true);
		sl_bodyPanel.putConstraint(SpringLayout.NORTH, resourceDetailPanel, 0, SpringLayout.NORTH, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.WEST, resourceDetailPanel, 0, SpringLayout.WEST, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.SOUTH, resourceDetailPanel, -299, SpringLayout.SOUTH, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.EAST, resourceDetailPanel, 672, SpringLayout.WEST, bodyPanel);
		bodyPanel.add(resourceDetailPanel);

		GUIElements.MyPanel resourceGraphPanel = new GUIElements.MyPanel(true);
		sl_bodyPanel.putConstraint(SpringLayout.NORTH, resourceGraphPanel, 6, SpringLayout.SOUTH, resourceDetailPanel);
		sl_bodyPanel.putConstraint(SpringLayout.WEST, resourceGraphPanel, 6, SpringLayout.WEST, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.SOUTH, resourceGraphPanel, -12, SpringLayout.SOUTH, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.EAST, resourceGraphPanel, -12, SpringLayout.EAST, resourceDetailPanel);
		SpringLayout sl_resourceDetailPanel = new SpringLayout();
		resourceDetailPanel.setLayout(sl_resourceDetailPanel);

		GUIElements.MyPanel topDetailPanel = new GUIElements.MyPanel(true);
		sl_resourceDetailPanel.putConstraint(SpringLayout.NORTH, topDetailPanel, 6, SpringLayout.NORTH,
				resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.WEST, topDetailPanel, 0, SpringLayout.WEST,
				resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.SOUTH, topDetailPanel, 141, SpringLayout.NORTH,
				resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.EAST, topDetailPanel, -233, SpringLayout.EAST,
				resourceDetailPanel);
		resourceDetailPanel.add(topDetailPanel);
		SpringLayout sl_topDetailPanel = new SpringLayout();
		topDetailPanel.setLayout(sl_topDetailPanel);

		GUIElements.MyPanel iconPanel = new GUIElements.MyPanel(true);
		sl_topDetailPanel.putConstraint(SpringLayout.NORTH, iconPanel, 10, SpringLayout.NORTH, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.WEST, iconPanel, 10, SpringLayout.WEST, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.EAST, iconPanel, 141, SpringLayout.WEST, topDetailPanel);
		topDetailPanel.add(iconPanel);

		typeTextField = new GUIElements.MyTextField();
		sl_topDetailPanel.putConstraint(SpringLayout.NORTH, typeTextField, 10, SpringLayout.NORTH, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.WEST, typeTextField, 6, SpringLayout.EAST, iconPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.EAST, typeTextField, -6, SpringLayout.EAST, topDetailPanel);
		topDetailPanel.add(typeTextField);
		typeTextField.setEditable(false);
		typeTextField.setColumns(10);

		nameTextField = new GUIElements.MyTextField();
		sl_topDetailPanel.putConstraint(SpringLayout.SOUTH, typeTextField, -6, SpringLayout.NORTH, nameTextField);
		sl_topDetailPanel.putConstraint(SpringLayout.WEST, nameTextField, 6, SpringLayout.EAST, iconPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.EAST, nameTextField, 0, SpringLayout.EAST, typeTextField);
		topDetailPanel.add(nameTextField);
		nameTextField.setEditable(false);
		nameTextField.setColumns(10);

		GUIElements.MyPanel bottomDetailPanel = new GUIElements.MyPanel(true);
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

		GUIElements.MyPanel statsPanel = new GUIElements.MyPanel(false);
		sl_bottomDetailPanel.putConstraint(SpringLayout.NORTH, statsPanel, 6, SpringLayout.NORTH, bottomDetailPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.WEST, statsPanel, 6, SpringLayout.WEST, bottomDetailPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.SOUTH, statsPanel, 181, SpringLayout.NORTH, bottomDetailPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.EAST, statsPanel, 204, SpringLayout.WEST, bottomDetailPanel);
		bottomDetailPanel.add(statsPanel);
		statsPanel.setLayout(new GridLayout(4, 1, 6, 6));

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

		GUIElements.MyPanel purchasePanel = new GUIElements.MyPanel(false);
		sl_bottomDetailPanel.putConstraint(SpringLayout.NORTH, purchasePanel, 6, SpringLayout.NORTH, bottomDetailPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.WEST, purchasePanel, 12, SpringLayout.EAST, statsPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.SOUTH, purchasePanel, -6, SpringLayout.SOUTH, bottomDetailPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.EAST, purchasePanel, -6, SpringLayout.EAST, bottomDetailPanel);
		bottomDetailPanel.add(purchasePanel);
		GridLayout gl_purchasePanel = new GridLayout();
		gl_purchasePanel.setRows(4);
		purchasePanel.setLayout(gl_purchasePanel);

		demandTextField = new GUIElements.MyTextField();
		purchasePanel.add(demandTextField);
		demandTextField.setEditable(false);
		demandTextField.setColumns(10);

		supplyTextField = new GUIElements.MyTextField();
		purchasePanel.add(supplyTextField);
		supplyTextField.setEditable(false);
		supplyTextField.setColumns(10);

		GUIElements.MyPanel sellPanel = new GUIElements.MyPanel(false);
		purchasePanel.add(sellPanel);
		sellPanel.setLayout(null);

		sellButton.setBounds(85, 0, 138, 39);
		sellButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentResource != null) {
					String response = MarketController.sellResource(Integer.valueOf(sellAmountTextField.getText()),
							currentResource, currentPlayer);
					postNewHeadline(response);
				}
			}
		});
		sellPanel.add(sellButton);

		sellAmountTextField = new GUIElements.MyFormattedTextField(decimalFormatter);
		sellAmountTextField.setEnabled(false);
		sellAmountTextField.setValue(1);
		sellAmountTextField.setBounds(0, 0, 75, 39);

		sellPanel.add(sellAmountTextField);

		GUIElements.MyPanel buyPanel = new GUIElements.MyPanel(false);
		purchasePanel.add(buyPanel);
		buyPanel.setLayout(null);

		buyButton.setBounds(85, 0, 138, 39);
		buyPanel.add(buyButton);

		buyAmountTextField = new GUIElements.MyFormattedTextField(decimalFormatter);
		buyAmountTextField.setEnabled(false);
		buyAmountTextField.setValue(1);
		buyAmountTextField.setBounds(0, 0, 75, 39);

		buyPanel.add(buyAmountTextField);
		GUIElements.MyPanel descriptionPanel = new GUIElements.MyPanel(false);
		sl_resourceDetailPanel.putConstraint(SpringLayout.NORTH, descriptionPanel, 12, SpringLayout.NORTH,
				resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.WEST, descriptionPanel, 6, SpringLayout.EAST, topDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.SOUTH, descriptionPanel, -6, SpringLayout.SOUTH,
				bottomDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.EAST, descriptionPanel, -12, SpringLayout.EAST,
				resourceDetailPanel);
		resourceDetailPanel.add(descriptionPanel);
		descriptionPanel.setLayout(null);

		descriptionTextArea = new GUIElements.MyTextArea();
		descriptionTextArea.setBounds(0, 0, 215, 316);
		descriptionPanel.add(descriptionTextArea);
		sl_topDetailPanel.putConstraint(SpringLayout.NORTH, descriptionTextArea, 10, SpringLayout.NORTH,
				topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.WEST, descriptionTextArea, 343, SpringLayout.WEST, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.SOUTH, descriptionTextArea, 0, SpringLayout.SOUTH, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.EAST, descriptionTextArea, -10, SpringLayout.EAST, topDetailPanel);
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setWrapStyleWord(true);
		sl_topDetailPanel.putConstraint(SpringLayout.SOUTH, iconPanel, 0, SpringLayout.SOUTH, descriptionTextArea);
		
		rarityTextField = new GUIElements.MyTextField();
		sl_topDetailPanel.putConstraint(SpringLayout.NORTH, nameTextField, -45, SpringLayout.NORTH, rarityTextField);
		sl_topDetailPanel.putConstraint(SpringLayout.NORTH, rarityTextField, -39, SpringLayout.SOUTH, iconPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.SOUTH, nameTextField, -6, SpringLayout.NORTH, rarityTextField);
		sl_topDetailPanel.putConstraint(SpringLayout.WEST, rarityTextField, 6, SpringLayout.EAST, iconPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.EAST, rarityTextField, 0, SpringLayout.EAST, typeTextField);
		sl_topDetailPanel.putConstraint(SpringLayout.SOUTH, rarityTextField, 0, SpringLayout.SOUTH, iconPanel);
		topDetailPanel.add(rarityTextField);
		rarityTextField.setColumns(10);
		descriptionTextArea.setEditable(false);

		buyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentResource != null) {
					String response = MarketController.buyResource(Integer.valueOf(buyAmountTextField.getText()),
							currentResource, currentPlayer);
					postNewHeadline(response);
				}
			}
		});
		bodyPanel.add(resourceGraphPanel);
		resourceGraphPanel.setLayout(new GridLayout(0, 1, 0, 0));

		resourceGraphPanel.add(buyGraph);

		headlineTextField = new GUIElements.MyTextField();
		sl_marketPanel.putConstraint(SpringLayout.NORTH, headlineTextField, -32, SpringLayout.SOUTH, marketPanel);
		sl_marketPanel.putConstraint(SpringLayout.WEST, headlineTextField, 6, SpringLayout.WEST, marketPanel);
		sl_marketPanel.putConstraint(SpringLayout.SOUTH, headlineTextField, -12, SpringLayout.SOUTH, marketPanel);
		sl_marketPanel.putConstraint(SpringLayout.EAST, headlineTextField, -6, SpringLayout.EAST, marketPanel);
		marketPanel.add(headlineTextField);
		headlineTextField.setColumns(1);
	}
}
