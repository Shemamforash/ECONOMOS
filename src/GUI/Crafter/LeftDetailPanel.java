package GUI.Crafter;

import GUI.EconomosGUI;
import GUI.GUIElements;
import GUI.GuildPanel.GuildPanel;
import GUI.DetailPanel;
import MerchantResources.Resource;

import javax.swing.*;
import java.awt.*;

public class LeftDetailPanel extends DetailPanel {
	private GUIElements.MyPanel iconPanel;
	private GUIElements.MyTextField typeTextField, nameTextField, rarityTextField;

	public LeftDetailPanel() {
		super(GuildPanel.PanelType.CRAFTING);
		iconPanel = new GUIElements.MyPanel(true);
		iconPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		add(iconPanel);

		typeTextField = new GUIElements.MyTextField();
		typeTextField.setEditable(false);
		typeTextField.setColumns(10);
		add(typeTextField);

		nameTextField = new GUIElements.MyTextField();
		nameTextField.setEditable(false);
		nameTextField.setColumns(10);
		add(nameTextField);

		rarityTextField = new GUIElements.MyTextField();
		rarityTextField.setColumns(10);
		add(rarityTextField);
		initialised = true;
	}

	public void selectedResourceChanged(Resource m) {
		if (m != null) {
			typeTextField.setText(m.guild());
			nameTextField.setText(m.name());
			rarityTextField.setText(m.rarity());
			switch (m.rarity()) {
			case "Commonplace":
				rarityTextField.setColor(new Color(165, 215, 230));
				break;
			case "Unusual":
				rarityTextField.setColor(new Color(70, 255, 255));
				break;
			case "Soughtafter":
				rarityTextField.setColor(new Color(65, 30, 255));
				break;
			case "Coveted":
				rarityTextField.setColor(new Color(180, 60, 230));
				break;
			case "Legendary":
				rarityTextField.setColor(new Color(250, 110, 10));
				break;
			default:
				break;
			}
		} else {
			typeTextField.setText("");
			rarityTextField.setText("");
			rarityTextField.setColor(null);
			nameTextField.setText("");
		}
	}

	public void selectedGuildChanged(String g) {
		// TODO Auto-generated method stub

	}

	public void receiveUpdate() {
			int resourcePanelHeight = (int) ((EconomosGUI.screenHeight() - 40) / 2.4f);
			int leftPanelWidth = (int) (resourcePanelHeight / 2 * 1.3f);
			int centerPanelWidth = (int) (resourcePanelHeight * 0.8f);
			springLayout.putConstraint(SpringLayout.NORTH, iconPanel, 0, SpringLayout.NORTH, this);
			springLayout.putConstraint(SpringLayout.WEST, iconPanel, 0, SpringLayout.WEST, this);
			springLayout.putConstraint(SpringLayout.SOUTH, iconPanel, leftPanelWidth, SpringLayout.NORTH, this);
			springLayout.putConstraint(SpringLayout.EAST, iconPanel, 0, SpringLayout.EAST, this);

			int textFieldHeight = (resourcePanelHeight - leftPanelWidth) / 3;

			springLayout.putConstraint(SpringLayout.NORTH, typeTextField, EconomosGUI.smallPanelGap(),
					SpringLayout.SOUTH, iconPanel);
			springLayout.putConstraint(SpringLayout.WEST, typeTextField, 0, SpringLayout.WEST, iconPanel);
			springLayout.putConstraint(SpringLayout.SOUTH, typeTextField, textFieldHeight, SpringLayout.SOUTH,
					iconPanel);
			springLayout.putConstraint(SpringLayout.EAST, typeTextField, 0, SpringLayout.EAST, this);

			springLayout.putConstraint(SpringLayout.NORTH, nameTextField, EconomosGUI.smallPanelGap(),
					SpringLayout.SOUTH, typeTextField);
			springLayout.putConstraint(SpringLayout.WEST, nameTextField, 0, SpringLayout.WEST, this);
			springLayout.putConstraint(SpringLayout.SOUTH, nameTextField, textFieldHeight, SpringLayout.SOUTH,
					typeTextField);
			springLayout.putConstraint(SpringLayout.EAST, nameTextField, 0, SpringLayout.EAST, this);

			springLayout.putConstraint(SpringLayout.NORTH, rarityTextField, EconomosGUI.smallPanelGap(),
					SpringLayout.SOUTH, nameTextField);
			springLayout.putConstraint(SpringLayout.WEST, rarityTextField, 0, SpringLayout.WEST, this);
			springLayout.putConstraint(SpringLayout.SOUTH, rarityTextField, textFieldHeight, SpringLayout.SOUTH,
					nameTextField);
			springLayout.putConstraint(SpringLayout.EAST, rarityTextField, 0, SpringLayout.EAST, this);
	}	
}
