package GUI.Merchant;

import javax.swing.SpringLayout;

import GUI.DetailPanel;
import GUI.EconomosGUI;
import GUI.GUIElements;
import GUI.GuildPanel.GuildPanel;
import MarketSimulator.MarketController;
import MarketSimulator.MarketResource;
import MerchantResources.MerchantResource;
import MerchantResources.Resource;
import economos.Player;

public class RightDetailPanel extends DetailPanel {
	private GUIElements.MyTextField possessTextField, boughtTextField, soldTextField, averageProfitTextField,
	averagePriceTextField, demandSupplyTextField;
	private GUIElements.BuySellButton sellButton = new GUIElements.BuySellButton("Sell");
	private GUIElements.BuySellButton buyButton = new GUIElements.BuySellButton("Buy");
	private static MarketResource		currentResource		= null;
	
	public RightDetailPanel(){
		super(GuildPanel.PanelType.MERCHANT);
		possessTextField = new GUIElements.MyTextField();
		possessTextField.setEditable(false);
		possessTextField.setColumns(10);
		add(possessTextField);

		averagePriceTextField = new GUIElements.MyTextField();
		springLayout.putConstraint(SpringLayout.NORTH, averagePriceTextField, 0, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, averagePriceTextField, 0, SpringLayout.EAST, possessTextField);
		springLayout.putConstraint(SpringLayout.SOUTH, averagePriceTextField, 0, SpringLayout.SOUTH, possessTextField);
		springLayout.putConstraint(SpringLayout.EAST, averagePriceTextField, 0, SpringLayout.EAST, this);
		averagePriceTextField.setEditable(false);
		averagePriceTextField.setColumns(10);
		add(averagePriceTextField);

		soldTextField = new GUIElements.MyTextField();
		soldTextField.setEditable(false);
		soldTextField.setColumns(10);
		add(soldTextField);

		boughtTextField = new GUIElements.MyTextField();
		springLayout.putConstraint(SpringLayout.NORTH, boughtTextField, 0, SpringLayout.NORTH, soldTextField);
		springLayout.putConstraint(SpringLayout.WEST, boughtTextField, 0, SpringLayout.EAST, soldTextField);
		springLayout.putConstraint(SpringLayout.SOUTH, boughtTextField, 0, SpringLayout.SOUTH, soldTextField);
		springLayout.putConstraint(SpringLayout.EAST, boughtTextField, 0, SpringLayout.EAST, this);
		boughtTextField.setColumns(10);
		add(boughtTextField);

		averageProfitTextField = new GUIElements.MyTextField();
		averageProfitTextField.setEditable(false);
		averageProfitTextField.setColumns(10);
		add(averageProfitTextField);

		demandSupplyTextField = new GUIElements.MyTextField();
		springLayout.putConstraint(SpringLayout.NORTH, demandSupplyTextField, 0, SpringLayout.NORTH, averageProfitTextField);
		springLayout.putConstraint(SpringLayout.WEST, demandSupplyTextField, 0, SpringLayout.EAST, averageProfitTextField);
		springLayout.putConstraint(SpringLayout.SOUTH, demandSupplyTextField, 0, SpringLayout.SOUTH, averageProfitTextField);
		springLayout.putConstraint(SpringLayout.EAST, demandSupplyTextField, 0, SpringLayout.EAST, this);
		demandSupplyTextField.setEditable(false);
		demandSupplyTextField.setColumns(10);
		add(demandSupplyTextField);

		buyButton.addActionListener(e -> {
			if (currentResource != null) {
				String response = MarketController.buyResource(buyButton.getSelectedQuantity(), currentResource);
				EconomosGUI.postNewHeadline(response);
			}
		});
		add(buyButton);

		sellButton.addActionListener(e -> {
			if (currentResource != null) {
				String response = MarketController.sellResource(sellButton.getSelectedQuantity(), currentResource);
				EconomosGUI.postNewHeadline(response);
			}
		});

		springLayout.putConstraint(SpringLayout.NORTH, sellButton, EconomosGUI.smallPanelGap(), SpringLayout.SOUTH, buyButton);
		springLayout.putConstraint(SpringLayout.WEST, sellButton, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, sellButton, 0, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, sellButton, 0, SpringLayout.EAST, this);
		add(sellButton);
		initialised = true;
	}

	private void updateText(){
		if(currentResource != null){
			MerchantResource userResource = Player.findUserResource(currentResource.name());
			demandSupplyTextField.setText("S/D: " + currentResource.getSupply() + "/" + currentResource.getDemand());
			possessTextField.setText("OWN " + userResource.quantity());
			averagePriceTextField.setText("Average Price: C" + EconomosGUI.decimalFormatter().format(currentResource.getAveragePrice()));
			averageProfitTextField.setText("Average Profit: C" + EconomosGUI.decimalFormatter().format(userResource.averageProfit()));
			soldTextField.setText("Sold " + userResource.sold() + " units");
			try {
				buyButton.setText("C" + EconomosGUI.decimalFormatter().format(currentResource.getBuyPrice(buyButton.getSelectedQuantity())));
			} catch (NumberFormatException n) {
				// DOSOMETHING
			}

			try {
				sellButton.setText("C" + EconomosGUI.decimalFormatter().format(currentResource.getSellPrice(sellButton.getSelectedQuantity())));
			} catch (NumberFormatException n) {
				// DOSOMETHING
			}
		} else {
			possessTextField.setText("");
			soldTextField.setText("");
			averageProfitTextField.setText("");
			averagePriceTextField.setText("");
			demandSupplyTextField.setText("");
			sellButton.reset();
			buyButton.reset();
		}
	}

	public void selectedResourceChanged(Resource m) {
		currentResource = (MarketResource) m;
		sellButton.reset();
		buyButton.reset();
	}

	public void selectedGuildChanged(String g) {
		// TODO Auto-generated method stub
		
	}

	public void receiveUpdate() {
		int resourcePanelHeight = (int) ((EconomosGUI.screenHeight() - 40) / 2.4f);
		float eHeight = (resourcePanelHeight - (4 * EconomosGUI.smallPanelGap())) / 12f;
		int eWidth = (EconomosGUI.screenWidth() - ((EconomosGUI.screenWidth() / 4) + resourcePanelHeight + 50)) / 2;
		int smalleHeight = (int) (2 * eHeight);
		int largeeHeight = (int) (3 * eHeight);

		springLayout.putConstraint(SpringLayout.NORTH, possessTextField, 0, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, possessTextField, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, possessTextField, smalleHeight, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, possessTextField, eWidth, SpringLayout.WEST, this);

		springLayout.putConstraint(SpringLayout.NORTH, soldTextField, EconomosGUI.smallPanelGap(), SpringLayout.SOUTH, possessTextField);
		springLayout.putConstraint(SpringLayout.WEST, soldTextField, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, soldTextField, smalleHeight + EconomosGUI.smallPanelGap(), SpringLayout.SOUTH, possessTextField);
		springLayout.putConstraint(SpringLayout.EAST, soldTextField, 0, SpringLayout.EAST, possessTextField);

		springLayout.putConstraint(SpringLayout.NORTH, averageProfitTextField, EconomosGUI.smallPanelGap(), SpringLayout.SOUTH, soldTextField);
		springLayout.putConstraint(SpringLayout.WEST, averageProfitTextField, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, averageProfitTextField, smalleHeight + EconomosGUI.smallPanelGap(), SpringLayout.SOUTH, soldTextField);
		springLayout.putConstraint(SpringLayout.EAST, averageProfitTextField, 0, SpringLayout.EAST, soldTextField);

		springLayout.putConstraint(SpringLayout.NORTH, buyButton, EconomosGUI.smallPanelGap(), SpringLayout.SOUTH, averageProfitTextField);
		springLayout.putConstraint(SpringLayout.WEST, buyButton, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, buyButton, largeeHeight + EconomosGUI.smallPanelGap(), SpringLayout.SOUTH, averageProfitTextField);
		springLayout.putConstraint(SpringLayout.EAST, buyButton, 0, SpringLayout.EAST, this);

		buyButton.updateConstraints(largeeHeight);
		sellButton.updateConstraints(largeeHeight);
		updateText();
	}

}
