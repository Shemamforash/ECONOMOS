package economos;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.event.*;

public class EconomosMain {
	private JFrame frame;
	private JTextField typeTextField, nameTextField, possessTextField,
			soldTextField, averageProfitTextField, averagePriceTextField;
	private JTextField demandTextField, supplyTextField, buyTextField,
			sellTextField, botTextField;
	private JFormattedTextField sellAmountTextField = new JFormattedTextField(NumberFormat.getNumberInstance());
	private JFormattedTextField buyAmountTextField = new JFormattedTextField(NumberFormat.getNumberInstance());
	private JTextArea descriptionTextArea;
	private static Player currentPlayer;
	private JList resourceList, categoryList;
	private JButton sellButton = new JButton("Sell");
	private JButton buyButton = new JButton("Buy");
	private static UserResource selectedResource = null;
	private DecimalFormat d = new DecimalFormat();
	private GraphPanel sellGraph = new GraphPanel();
	private GraphPanel buyGraph = new GraphPanel();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EconomosMain window = new EconomosMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public EconomosMain() {
		load();
		initialize();
		updateCategoryList(currentPlayer, categoryList);

		JPanel panel = new JPanel();
		panel.setBounds(0, 673, 994, 28);
		frame.getContentPane().add(panel);
		
		Timer t = new Timer();
		t.schedule(new UpdateGUI(), 0, 17);
	}

	public static Player getCurrentUser() {
		return currentPlayer;
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
	
	class UpdateGUI extends TimerTask{		
		public void run(){
			setSelectedResource((String) categoryList.getSelectedValue(), String.valueOf(resourceList.getSelectedValue()));
		}
	}

	public void updateCategoryList(User user, JList list) {
		ArrayList<ResourceType<UserResource>> arr = new ArrayList<ResourceType<UserResource>>(user.getResourceMap().getResourceTypes().values());
		String[] strarr = new String[arr.size()];
		for (int i = 0; i < arr.size(); ++i) {
			strarr[i] = arr.get(i).getType();
		}
		list.setListData(strarr);
	}

	public void setSelectedResource(String type, String name) {
		ResourceMap<UserResource> m = EconomosMain.getCurrentUser().getResourceMap();
		if (m.getResourceTypes().containsKey(type)) {
			ResourceType<UserResource> t = m.getResourceTypes().get(type);
			if (t.getResourceOfType().containsKey(name)) {
				selectedResource = (UserResource) t.getResourceOfType().get(name);
			} else {
				selectedResource = null;
			}
		}

		if (selectedResource != null) {
			MarketResource mr = selectedResource.getMarketResource();
			typeTextField.setText(selectedResource.getType());
			nameTextField.setText(selectedResource.getName());
			descriptionTextArea.setText(selectedResource.getDescription());
			supplyTextField.setText("Supply: " + mr.getSupply() +"p/s");
			possessTextField.setText("Owned: " + selectedResource.getQuantity());
			buyTextField.setText("Buy: C" + d.format(mr.getBuyPrice(1)));
			sellTextField.setText("Sell: C" + d.format(mr.getSellPrice(1)));
			sellAmountTextField.setEnabled(true);
			buyAmountTextField.setEnabled(true);
			demandTextField.setText("Demand: " + mr.getDemand() + "p/s");
			averagePriceTextField.setText("Average Price: C" + d.format(mr.getAveragePrice()));
			averageProfitTextField.setText("Average Profit: C" + d.format(selectedResource.getAverageProfit()));
			soldTextField.setText("Sold " + selectedResource.getSold() + " units");
			buyGraph.repaint();
			sellGraph.repaint();
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
			buyTextField.setText("");
			sellTextField.setText("");
			botTextField.setText("");
			descriptionTextArea.setText("");
			sellButton.setText("Sell");
			buyButton.setText("Buy");
			sellAmountTextField.setValue(1);
			buyAmountTextField.setValue(1);
			sellAmountTextField.setEnabled(false);
			buyAmountTextField.setEnabled(false);
		}
	}

	public static UserResource getSelectedResource() {
		return selectedResource;
	}

	public void updateJList(User user, String type, JList list) {
		if (EconomosMain.getCurrentUser().getResourceMap().getResourceTypes().containsKey(type)) {
			ArrayList<UserResource> arr = new ArrayList<UserResource>(user.getResourceMap().getResourceTypes().get(type).getResourceOfType().values());
			String[] strarr = new String[arr.size()];
			for (int i = 0; i < strarr.length; ++i) {
				strarr[i] = ((Resource) arr.get(i)).getName();
			}
			list.setListData(strarr);
		}
	}

	public int getIntFromString(String str){
		if(!str.equals("")){
			return Integer.parseInt(str);
		} else {
			return 1;
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

		JTabbedPane tabPane = new JTabbedPane(JTabbedPane.TOP);
		tabPane.setBounds(0, 0, 994, 663);
		frame.getContentPane().add(tabPane);

		JPanel marketPanel = new JPanel();
		tabPane.addTab("Market", null, marketPanel, "Access the market to buy and sell resources");
		SpringLayout sl_marketPanel = new SpringLayout();
		marketPanel.setLayout(sl_marketPanel);

		JPanel listPanel = new JPanel();
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
					updateJList(EconomosMain.getCurrentUser(), (String) categoryList.getSelectedValue(), resourceList);
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
					setSelectedResource((String) categoryList.getSelectedValue(), String.valueOf(resourceList.getSelectedValue()));
				}
			}
		});
		resourceScrollPane.setViewportView(resourceList);

		JPanel bodyPanel = new JPanel();
		sl_marketPanel.putConstraint(SpringLayout.NORTH, bodyPanel, 0, SpringLayout.NORTH, marketPanel);
		sl_marketPanel.putConstraint(SpringLayout.WEST, bodyPanel, 6, SpringLayout.EAST, listPanel);
		sl_marketPanel.putConstraint(SpringLayout.SOUTH, bodyPanel, 0, SpringLayout.SOUTH, listPanel);
		sl_marketPanel.putConstraint(SpringLayout.EAST, bodyPanel, 0, SpringLayout.EAST, marketPanel);
		marketPanel.add(bodyPanel);
		SpringLayout sl_bodyPanel = new SpringLayout();
		bodyPanel.setLayout(sl_bodyPanel);

		JPanel resourceDetailPanel = new JPanel();
		sl_bodyPanel.putConstraint(SpringLayout.NORTH, resourceDetailPanel, 0, SpringLayout.NORTH, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.WEST, resourceDetailPanel, 0, SpringLayout.WEST, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.SOUTH, resourceDetailPanel, -299, SpringLayout.SOUTH, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.EAST, resourceDetailPanel, 672, SpringLayout.WEST, bodyPanel);
		bodyPanel.add(resourceDetailPanel);

		JPanel resourceGraphPanel = new JPanel();
		sl_bodyPanel.putConstraint(SpringLayout.NORTH, resourceGraphPanel, 6, SpringLayout.SOUTH, resourceDetailPanel);
		sl_bodyPanel.putConstraint(SpringLayout.WEST, resourceGraphPanel, 0, SpringLayout.WEST, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.SOUTH, resourceGraphPanel, -1, SpringLayout.SOUTH, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.EAST, resourceGraphPanel, 0, SpringLayout.EAST, resourceDetailPanel);
		SpringLayout sl_resourceDetailPanel = new SpringLayout();
		resourceDetailPanel.setLayout(sl_resourceDetailPanel);

		JPanel topDetailPanel = new JPanel();
		sl_resourceDetailPanel.putConstraint(SpringLayout.NORTH, topDetailPanel, 0, SpringLayout.NORTH, resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.WEST, topDetailPanel, 0, SpringLayout.WEST, resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.SOUTH, topDetailPanel, 141, SpringLayout.NORTH, resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.EAST, topDetailPanel, 0, SpringLayout.EAST, resourceDetailPanel);
		resourceDetailPanel.add(topDetailPanel);
		SpringLayout sl_topDetailPanel = new SpringLayout();
		topDetailPanel.setLayout(sl_topDetailPanel);

		JPanel iconPanel = new JPanel();
		sl_topDetailPanel.putConstraint(SpringLayout.NORTH, iconPanel, 10, SpringLayout.NORTH, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.WEST, iconPanel, 10, SpringLayout.WEST, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.EAST, iconPanel, 141, SpringLayout.WEST, topDetailPanel);
		topDetailPanel.add(iconPanel);

		typeTextField = new JTextField();
		sl_topDetailPanel.putConstraint(SpringLayout.NORTH, typeTextField, 10, SpringLayout.NORTH, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.WEST, typeTextField, 6, SpringLayout.EAST, iconPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.SOUTH, typeTextField, -69, SpringLayout.SOUTH, topDetailPanel);
		topDetailPanel.add(typeTextField);
		typeTextField.setEditable(false);
		typeTextField.setColumns(10);

		nameTextField = new JTextField();
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
		sl_topDetailPanel.putConstraint(SpringLayout.NORTH, descriptionTextArea, 10, SpringLayout.NORTH, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.SOUTH, descriptionTextArea, 0, SpringLayout.SOUTH, topDetailPanel);
		topDetailPanel.add(descriptionTextArea);
		descriptionTextArea.setEditable(false);

		JPanel bottomDetailPanel = new JPanel();
		sl_resourceDetailPanel.putConstraint(SpringLayout.NORTH, bottomDetailPanel, 6, SpringLayout.SOUTH, topDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.WEST, bottomDetailPanel, 0, SpringLayout.WEST, resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.SOUTH, bottomDetailPanel, 0, SpringLayout.SOUTH, resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.EAST, bottomDetailPanel, 0, SpringLayout.EAST, topDetailPanel);
		resourceDetailPanel.add(bottomDetailPanel);
		SpringLayout sl_bottomDetailPanel = new SpringLayout();
		bottomDetailPanel.setLayout(sl_bottomDetailPanel);

		JPanel statsPanel = new JPanel();
		sl_bottomDetailPanel.putConstraint(SpringLayout.NORTH, statsPanel, 0, SpringLayout.NORTH, bottomDetailPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.WEST, statsPanel, 10, SpringLayout.WEST, bottomDetailPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.SOUTH, statsPanel, 187, SpringLayout.NORTH, bottomDetailPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.EAST, statsPanel, 210, SpringLayout.WEST, bottomDetailPanel);
		bottomDetailPanel.add(statsPanel);
		statsPanel.setLayout(new GridLayout(4, 1, 0, 10));

		possessTextField = new JTextField();
		possessTextField.setEditable(false);
		statsPanel.add(possessTextField);
		possessTextField.setColumns(10);

		soldTextField = new JTextField();
		soldTextField.setEditable(false);
		statsPanel.add(soldTextField);
		soldTextField.setColumns(10);

		averageProfitTextField = new JTextField();
		averageProfitTextField.setEditable(false);
		statsPanel.add(averageProfitTextField);
		averageProfitTextField.setColumns(10);

		averagePriceTextField = new JTextField();
		averagePriceTextField.setEditable(false);
		statsPanel.add(averagePriceTextField);
		averagePriceTextField.setColumns(10);

		JPanel purchasePanel = new JPanel();
		sl_bottomDetailPanel.putConstraint(SpringLayout.NORTH, purchasePanel, 0, SpringLayout.NORTH, statsPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.WEST, purchasePanel, 6, SpringLayout.EAST, statsPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.SOUTH, purchasePanel, 0, SpringLayout.SOUTH, statsPanel);
		bottomDetailPanel.add(purchasePanel);
		purchasePanel.setLayout(new GridLayout(4, 1, 0, 10));

		demandTextField = new JTextField();
		purchasePanel.add(demandTextField);
		demandTextField.setEditable(false);
		demandTextField.setColumns(10);

		supplyTextField = new JTextField();
		purchasePanel.add(supplyTextField);
		supplyTextField.setEditable(false);
		supplyTextField.setColumns(10);

		JPanel botPanel = new JPanel();
		sl_bottomDetailPanel.putConstraint(SpringLayout.EAST, purchasePanel, -6, SpringLayout.WEST, botPanel);

		JPanel sellPanel = new JPanel();
		purchasePanel.add(sellPanel);
		sellPanel.setLayout(null);

		
		sellButton.setBounds(85, 0, 126, 39);
		sellButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (selectedResource != null) {
					String response = MarketController.sellResource(getIntFromString(sellAmountTextField.getText()), selectedResource, currentPlayer);
					System.out.println(response);
				}
			}
		});
		sellPanel.add(sellButton);
		
		sellAmountTextField.setEnabled(false);
		sellAmountTextField.setValue(1);
		sellAmountTextField.setBounds(0, 0, 75, 39);
		sellAmountTextField.addPropertyChangeListener(new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent arg0) {
				if(selectedResource != null){
					float n = selectedResource.getMarketResource().getSellPrice(getIntFromString(buyAmountTextField.getText()));
					sellButton.setText("Sell (C" + d.format(n) + ")");
				}
			}
		});
		sellPanel.add(sellAmountTextField);

		JPanel buyPanel = new JPanel();
		purchasePanel.add(buyPanel);
		buyPanel.setLayout(null);

		buyButton.setBounds(85, 0, 126, 39);
		buyPanel.add(buyButton);
		
		buyAmountTextField.setEnabled(false);
		buyAmountTextField.setValue(1);
		buyAmountTextField.setBounds(0, 0, 75, 39);
		buyAmountTextField.addPropertyChangeListener(new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent arg0) {
				if(selectedResource != null){
					float n = selectedResource.getMarketResource().getBuyPrice(getIntFromString(buyAmountTextField.getText()));
					buyButton.setText("Buy (C" + d.format(n) + ")");
				}
			}
		});
		
		
		buyPanel.add(buyAmountTextField);
		buyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedResource != null) {
					String response = MarketController.buyResource(getIntFromString(buyAmountTextField.getText()), selectedResource, currentPlayer);
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

		botTextField = new JTextField();
		botTextField.setEnabled(false);
		botPanel.add(botTextField);
		botTextField.setColumns(10);

		JRadioButton activeRadioButton = new JRadioButton("Enable Bot (20C/s)");
		botPanel.add(activeRadioButton);

		JPanel botBuyPanel = new JPanel();
		botPanel.add(botBuyPanel);
		SpringLayout sl_botBuyPanel = new SpringLayout();
		botBuyPanel.setLayout(sl_botBuyPanel);

		JButton decreaseBuyButton = new JButton("-");
		sl_botBuyPanel.putConstraint(SpringLayout.NORTH, decreaseBuyButton, 25, SpringLayout.NORTH, botBuyPanel);
		sl_botBuyPanel.putConstraint(SpringLayout.SOUTH, decreaseBuyButton, 0, SpringLayout.SOUTH, botBuyPanel);
		sl_botBuyPanel.putConstraint(SpringLayout.EAST, decreaseBuyButton, -10, SpringLayout.EAST, botBuyPanel);
		botBuyPanel.add(decreaseBuyButton);

		buyTextField = new JTextField();
		sl_botBuyPanel.putConstraint(SpringLayout.WEST, decreaseBuyButton, 6, SpringLayout.EAST, buyTextField);
		sl_botBuyPanel.putConstraint(SpringLayout.NORTH, buyTextField, 10, SpringLayout.NORTH, botBuyPanel);
		sl_botBuyPanel.putConstraint(SpringLayout.WEST, buyTextField, 0, SpringLayout.WEST, botBuyPanel);
		sl_botBuyPanel.putConstraint(SpringLayout.EAST, buyTextField, -84, SpringLayout.EAST, botBuyPanel);
		sl_botBuyPanel.putConstraint(SpringLayout.SOUTH, buyTextField, -10, SpringLayout.SOUTH, botBuyPanel);
		buyTextField.setEnabled(false);
		botBuyPanel.add(buyTextField);
		buyTextField.setColumns(10);

		JPanel botSellPanel = new JPanel();
		botPanel.add(botSellPanel);
		SpringLayout sl_botSellPanel = new SpringLayout();
		botSellPanel.setLayout(sl_botSellPanel);

		sellTextField = new JTextField();
		sl_botSellPanel.putConstraint(SpringLayout.WEST, sellTextField, 0, SpringLayout.WEST, botSellPanel);
		sellTextField.setEnabled(false);
		sl_botSellPanel.putConstraint(SpringLayout.NORTH, sellTextField, 10, SpringLayout.NORTH, botSellPanel);
		sl_botSellPanel.putConstraint(SpringLayout.SOUTH, sellTextField, -10, SpringLayout.SOUTH, botSellPanel);
		sl_botSellPanel.putConstraint(SpringLayout.EAST, sellTextField, 155, SpringLayout.WEST, botSellPanel);
		botSellPanel.add(sellTextField);
		sellTextField.setColumns(10);

		JButton increaseSellButton = new JButton("+");
		sl_botSellPanel.putConstraint(SpringLayout.NORTH, increaseSellButton, 0, SpringLayout.NORTH, botSellPanel);
		sl_botSellPanel.putConstraint(SpringLayout.WEST, increaseSellButton, 6, SpringLayout.EAST, sellTextField);
		sl_botSellPanel.putConstraint(SpringLayout.EAST, increaseSellButton, -10, SpringLayout.EAST, botSellPanel);
		botSellPanel.add(increaseSellButton);
		bodyPanel.add(resourceGraphPanel);
		resourceGraphPanel.setLayout(new GridLayout(0, 1, 0, 0));

		resourceGraphPanel.add(sellGraph);
		resourceGraphPanel.add(buyGraph);

		JButton increaseBuyButton = new JButton("+");
		sl_botBuyPanel.putConstraint(SpringLayout.NORTH, increaseBuyButton, 0, SpringLayout.NORTH, botBuyPanel);
		sl_botBuyPanel.putConstraint(SpringLayout.WEST, increaseBuyButton, 6, SpringLayout.EAST, buyTextField);
		sl_botBuyPanel.putConstraint(SpringLayout.EAST, increaseBuyButton, -10, SpringLayout.EAST, botBuyPanel);
		botBuyPanel.add(increaseBuyButton);

		JButton decreaseSellButton = new JButton("-");
		sl_botSellPanel.putConstraint(SpringLayout.WEST, decreaseSellButton, 6, SpringLayout.EAST, sellTextField);
		sl_botSellPanel.putConstraint(SpringLayout.SOUTH, decreaseSellButton, 0, SpringLayout.SOUTH, botSellPanel);
		sl_botSellPanel.putConstraint(SpringLayout.EAST, decreaseSellButton, -10, SpringLayout.EAST, botSellPanel);
		botSellPanel.add(decreaseSellButton);

		JPanel companyPanel = new JPanel();
		tabPane.addTab("Company", null, companyPanel, "View information about your company and player statistics");

		JPanel constructionPanel = new JPanel();
		tabPane.addTab("Construction", null, constructionPanel, "Access Construction options from this tab");
	}
}
