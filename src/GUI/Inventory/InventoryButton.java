package GUI.Inventory;

/**
 * Created by Sam on 14/07/2016.
 */

import CraftingResources.CraftingResource;
import GUI.GUIElements;
import GUI.GuildPanel.GuildPanel;
import MarketSimulator.MarketResource;
import MerchantResources.Resource;
import economos.UpdateListener;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

/**
 * Created by Sam on 13/07/2016.
 */
public class InventoryButton extends GUIElements.MyPanel implements UpdateListener {
	private GUIElements.MyTextField nameField, categoryField, guildField, tierField, quantityField, valueField;
	private Resource resource;
	private SpringLayout sl;
	private boolean initialised = false;

	public InventoryButton(Resource resource) {
		super();
		this.resource = resource;
		sl = new SpringLayout();
		setLayout(sl);

		nameField = new GUIElements.MyTextField();
		nameField.setText(resource.name());
		add(nameField);

		categoryField = new GUIElements.MyTextField();
		categoryField.setText(resource.type().toString());
		add(categoryField);

		guildField = new GUIElements.MyTextField();
		guildField.setText(resource.guild());
		add(guildField);

		tierField = new GUIElements.MyTextField();
		tierField.setText(resource.rarity());
		add(tierField);

		quantityField = new GUIElements.MyTextField();
		quantityField.setText("TODO");
		add(quantityField);

		valueField = new GUIElements.MyTextField();
		valueField.setText("TODO");
		add(valueField);

		positionComponents();

		initialised = true;
	}

	private void positionComponents() {
		sl.putConstraint(SpringLayout.NORTH, nameField, 0, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.WEST, nameField, 0, SpringLayout.WEST, this);
		sl.putConstraint(SpringLayout.SOUTH, nameField, 0, SpringLayout.SOUTH, this);
		sl.putConstraint(SpringLayout.EAST, nameField, 150, SpringLayout.WEST, this);

		sl.putConstraint(SpringLayout.NORTH, categoryField, 0, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.WEST, categoryField, 0, SpringLayout.EAST, nameField);
		sl.putConstraint(SpringLayout.SOUTH, categoryField, 0, SpringLayout.SOUTH, this);
		sl.putConstraint(SpringLayout.EAST, categoryField, 150, SpringLayout.EAST, nameField);

		sl.putConstraint(SpringLayout.NORTH, guildField, 0, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.WEST, guildField, 0, SpringLayout.EAST, categoryField);
		sl.putConstraint(SpringLayout.SOUTH, guildField, 0, SpringLayout.SOUTH, this);
		sl.putConstraint(SpringLayout.EAST, guildField, 100, SpringLayout.EAST, categoryField);

		sl.putConstraint(SpringLayout.NORTH, tierField, 0, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.WEST, tierField, 0, SpringLayout.EAST, guildField);
		sl.putConstraint(SpringLayout.SOUTH, tierField, 0, SpringLayout.SOUTH, this);
		sl.putConstraint(SpringLayout.EAST, tierField, 150, SpringLayout.EAST, guildField);

		sl.putConstraint(SpringLayout.NORTH, quantityField, 0, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.WEST, quantityField, 0, SpringLayout.EAST, tierField);
		sl.putConstraint(SpringLayout.SOUTH, quantityField, 0, SpringLayout.SOUTH, this);
		sl.putConstraint(SpringLayout.EAST, quantityField, 50, SpringLayout.EAST, tierField);

		sl.putConstraint(SpringLayout.NORTH, valueField, 0, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.WEST, valueField, 0, SpringLayout.EAST, quantityField);
		sl.putConstraint(SpringLayout.SOUTH, valueField, 0, SpringLayout.SOUTH, this);
		sl.putConstraint(SpringLayout.EAST, valueField, 0, SpringLayout.EAST, this);
	}

	@Override
	public void receiveUpdate() {
		positionComponents();
	}

	@Override
	public boolean isInitialised() {
		return initialised;
	}
}

