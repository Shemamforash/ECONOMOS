package economos;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.SpringLayout;

public class MerchantsPanel extends MainPanel{
	private GUIElements.MyTextField typeTextField, nameTextField, possessTextField, soldTextField, averageProfitTextField,
	averagePriceTextField, demandTextField, supplyTextField, rarityTextField;
	private GUIElements.MyFormattedTextField sellAmountTextField, buyAmountTextField;
	private GUIElements.MyTextArea descriptionTextArea;
	private GUIElements.MyButton sellButton = new GUIElements.MyButton("Sell", true);
	private GUIElements.MyButton buyButton = new GUIElements.MyButton("Buy", true);
	private NumberFormat decimalFormatter = NumberFormat.getIntegerInstance();
	private GraphPanel buyGraph = new GraphPanel();
	private boolean fieldsReset = false;
	
	public MerchantsPanel(EconomosGUI main) {
		super(main);
		setup();
	}
	
	public String getSelectedGuild(){
		return listPanel.getSelectedGuild();
	}
	
	public String getSelectedResource(){
		return listPanel.getSelectedResource();
	}
	
	public void setup(){
		decimalFormatter.setMaximumFractionDigits(2);
		decimalFormatter.setGroupingUsed(false);

		SpringLayout sl_marketPanel = new SpringLayout();
		sl_marketPanel.putConstraint(SpringLayout.NORTH, main.infoPanel, -30, SpringLayout.SOUTH, main.frame.getContentPane());
		sl_marketPanel.putConstraint(SpringLayout.SOUTH, main.infoPanel, 0, SpringLayout.SOUTH, main.frame.getContentPane());
		sl_marketPanel.putConstraint(SpringLayout.EAST, main.infoPanel, 0, SpringLayout.EAST, main.frame.getContentPane());
		
		this.setLayout(sl_marketPanel);

		listPanel = new GuildPanel(new String[]{"Spicer", "Weaver", "Temperer", "Purifier", "Mercer", "Smelter", "Mason", "Crofter"}, getCurrentPlayer());
		sl_marketPanel.putConstraint(SpringLayout.NORTH, listPanel, 18, SpringLayout.NORTH, this);
		sl_marketPanel.putConstraint(SpringLayout.WEST, listPanel, 6, SpringLayout.WEST, this);
		sl_marketPanel.putConstraint(SpringLayout.SOUTH, listPanel, -6, SpringLayout.SOUTH, this);
		sl_marketPanel.putConstraint(SpringLayout.EAST, listPanel, 300, SpringLayout.WEST, this);
		this.add(listPanel);

		GUIElements.MyPanel bodyPanel = new GUIElements.MyPanel(true);
		sl_marketPanel.putConstraint(SpringLayout.NORTH, bodyPanel, 0, SpringLayout.NORTH, this);
		sl_marketPanel.putConstraint(SpringLayout.WEST, bodyPanel, 12, SpringLayout.EAST, listPanel);
		sl_marketPanel.putConstraint(SpringLayout.SOUTH, bodyPanel, -6, SpringLayout.SOUTH, this);
		sl_marketPanel.putConstraint(SpringLayout.EAST, bodyPanel, -12, SpringLayout.EAST, this);
		
		this.add(bodyPanel);
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
		sl_bottomDetailPanel.putConstraint(SpringLayout.SOUTH, purchasePanel, -6, SpringLayout.SOUTH,
				bottomDetailPanel);
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
				if (main.currentResource != null) {
					String response = MarketController.sellResource(Integer.valueOf(sellAmountTextField.getText()), main.getCurrentResource(), getCurrentPlayer());
					main.postNewHeadline(response);
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
				if (main.getCurrentResource() != null) {
					String response = MarketController.buyResource(Integer.valueOf(buyAmountTextField.getText()),
							main.getCurrentResource(), main.getCurrentPlayer());
					main.postNewHeadline(response);
				}
			}
		});
		bodyPanel.add(resourceGraphPanel);
		resourceGraphPanel.setLayout(new GridLayout(0, 1, 0, 0));

		resourceGraphPanel.add(buyGraph);
	}
	
	public void updateResource(PlayerResource currentResource){
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
		} else if (!fieldsReset) {
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
}
