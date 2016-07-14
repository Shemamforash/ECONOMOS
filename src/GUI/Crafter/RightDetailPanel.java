package GUI.Crafter;

import CraftingResources.CraftingController;
import CraftingResources.CraftingResource;
import GUI.EconomosGUI;
import GUI.GUIElements;
import GUI.DetailPanel;
import GUI.GuildPanel.GuildPanel;
import MerchantResources.Resource;
import economos.Player;

import javax.swing.*;
import java.util.ArrayList;

public class RightDetailPanel extends DetailPanel {
	private GUIElements.MyTextField createdTextField, soldTextField, averageProfitTextField;
	private GUIElements.MyTextArea ingredientsTextArea;
	private static CraftingResource currentResource		= null;
	
	public RightDetailPanel(){
		super(GuildPanel.PanelType.CRAFTING);
		createdTextField = new GUIElements.MyTextField();
		createdTextField.setEditable(false);
		createdTextField.setColumns(10);
		add(createdTextField);

		soldTextField = new GUIElements.MyTextField();
		soldTextField.setEditable(false);
		soldTextField.setColumns(10);
		add(soldTextField);

		averageProfitTextField = new GUIElements.MyTextField();
		averageProfitTextField.setEditable(false);
		averageProfitTextField.setColumns(10);
		add(averageProfitTextField);

		ingredientsTextArea = new GUIElements.MyTextArea();
		add(ingredientsTextArea);

		initialised = true;
	}

	private String getIngredientsText(){
		ArrayList<CraftingResource.RequisiteResource> ingredients = currentResource.requisiteResources();
		String recipe = "";
		for(CraftingResource.RequisiteResource i : ingredients){
			Resource resource = i.resource();
			int owned = 0;
			if(resource.type() == Resource.ResourceType.CRAFTING){
				owned = CraftingController.findResource(resource.name()).quantity();
			} else {
				owned = Player.findUserResource(resource.name()).quantity();
			}
			recipe += resource.name() + " x" + i.quantity() + " (" + owned + ")" + "\n";

		}
		return recipe;
	}

	private void updateText(){
		if(currentResource != null){
			createdTextField.setText("OWN " + currentResource.quantity());
			averageProfitTextField.setText("Average Profit: C" + EconomosGUI.decimalFormatter().format(currentResource.value()));
			soldTextField.setText("Sold " + currentResource.sold() + " units");
			ingredientsTextArea.setText(getIngredientsText());
		} else {
			createdTextField.setText("");
			soldTextField.setText("");
			averageProfitTextField.setText("");
			ingredientsTextArea.setText("");
		}
	}

	public void selectedResourceChanged(Resource m) {
		currentResource = (CraftingResource) m;
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

		springLayout.putConstraint(SpringLayout.NORTH, createdTextField, 0, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, createdTextField, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, createdTextField, smalleHeight, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, createdTextField, eWidth, SpringLayout.WEST, this);

		springLayout.putConstraint(SpringLayout.NORTH, soldTextField, EconomosGUI.smallPanelGap(), SpringLayout.SOUTH, createdTextField);
		springLayout.putConstraint(SpringLayout.WEST, soldTextField, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, soldTextField, smalleHeight + EconomosGUI.smallPanelGap(), SpringLayout.SOUTH, createdTextField);
		springLayout.putConstraint(SpringLayout.EAST, soldTextField, 0, SpringLayout.EAST, this);

		springLayout.putConstraint(SpringLayout.NORTH, averageProfitTextField, EconomosGUI.smallPanelGap(), SpringLayout.SOUTH, soldTextField);
		springLayout.putConstraint(SpringLayout.WEST, averageProfitTextField, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, averageProfitTextField, smalleHeight + EconomosGUI.smallPanelGap(), SpringLayout.SOUTH, soldTextField);
		springLayout.putConstraint(SpringLayout.EAST, averageProfitTextField, 0, SpringLayout.EAST, this);

		springLayout.putConstraint(SpringLayout.NORTH, ingredientsTextArea, EconomosGUI.smallPanelGap(), SpringLayout.SOUTH, averageProfitTextField);
		springLayout.putConstraint(SpringLayout.WEST, ingredientsTextArea, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, ingredientsTextArea, 0, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, ingredientsTextArea, 0, SpringLayout.EAST, this);
		updateText();
	}

}
