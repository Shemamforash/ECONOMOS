package Merchant;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SpringLayout;

import GUI.*;
import MerchantResources.*;
import economos.*;

public class MerchantsPanel extends GUIElements.MyPanel implements UpdateListener {
	private GuildPanel guilds;
	private GUIElements.MyPanel resourceList, resourceDetailPanel, bodyPanel;
	private GraphPanel buyGraph = new GraphPanel();
	private LeftDetailPanel leftDetailPanel;
	private RightDetailPanel rightDetailPanel;
	private CenterDetailPanel centerDetailPanel;
	private SpringLayout springLayout;
	private boolean initialised = false;

	public MerchantsPanel() {
		springLayout = new SpringLayout();
		setLayout(springLayout);

		guilds = new GuildPanel(EconomosGUI.screenHeight() - 50, EconomosGUI.screenWidth() / 4, MarketController.getMarketResources());
		add(guilds);

		bodyPanel = new GUIElements.MyPanel(true);
		springLayout.putConstraint(SpringLayout.NORTH, bodyPanel, EconomosGUI.largePanelGap(), SpringLayout.NORTH,
				this);
		springLayout.putConstraint(SpringLayout.WEST, bodyPanel, EconomosGUI.largePanelGap(), SpringLayout.EAST,
				guilds);
		springLayout.putConstraint(SpringLayout.SOUTH, bodyPanel, -40, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, bodyPanel, -EconomosGUI.largePanelGap(), SpringLayout.EAST, this);
		SpringLayout sl_bodyPanel = new SpringLayout();
		bodyPanel.setLayout(sl_bodyPanel);
		add(bodyPanel);

		resourceDetailPanel = new GUIElements.MyPanel(true);
		SpringLayout sl_resourceDetailPanel = new SpringLayout();
		resourceDetailPanel.setLayout(sl_resourceDetailPanel);
		bodyPanel.add(resourceDetailPanel);

		GUIElements.MyPanel resourceGraphPanel = new GUIElements.MyPanel(true);
		sl_bodyPanel.putConstraint(SpringLayout.NORTH, resourceGraphPanel, EconomosGUI.largePanelGap(),
				SpringLayout.SOUTH, resourceDetailPanel);
		sl_bodyPanel.putConstraint(SpringLayout.WEST, resourceGraphPanel, 0, SpringLayout.WEST, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.SOUTH, resourceGraphPanel, 0, SpringLayout.SOUTH, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.EAST, resourceGraphPanel, 0, SpringLayout.EAST, resourceDetailPanel);
		resourceGraphPanel.setLayout(new GridLayout(0, 1, 0, 0));
		bodyPanel.add(resourceGraphPanel);

		leftDetailPanel = new LeftDetailPanel();
		resourceDetailPanel.add(leftDetailPanel);

		centerDetailPanel = new CenterDetailPanel();
		resourceDetailPanel.add(centerDetailPanel);

		rightDetailPanel = new RightDetailPanel();
		resourceDetailPanel.add(rightDetailPanel);

		resourceGraphPanel.add(buyGraph);
		
		UpdateCaller.addListener(this);
		initialised = true;
	}

	public void receiveUpdate() {
		springLayout.putConstraint(SpringLayout.NORTH, guilds, EconomosGUI.largePanelGap(), SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, guilds, EconomosGUI.largePanelGap(), SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, guilds, -40, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, guilds, (int) (EconomosGUI.screenWidth() / 4), SpringLayout.WEST,
				this);

		int resourcePanelHeight = (int) ((EconomosGUI.screenHeight() - 40) / 2.4f);
		springLayout = (SpringLayout) (bodyPanel.getLayout());
		springLayout.putConstraint(SpringLayout.NORTH, resourceDetailPanel, 0, SpringLayout.NORTH, bodyPanel);
		springLayout.putConstraint(SpringLayout.WEST, resourceDetailPanel, 0, SpringLayout.WEST, bodyPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, resourceDetailPanel, resourcePanelHeight, SpringLayout.NORTH,
				bodyPanel);
		springLayout.putConstraint(SpringLayout.EAST, resourceDetailPanel, 0, SpringLayout.EAST, bodyPanel);

		int leftPanelWidth = (int) (resourcePanelHeight / 2 * 1.3f);
		springLayout = (SpringLayout) (resourceDetailPanel.getLayout());
		springLayout.putConstraint(SpringLayout.NORTH, leftDetailPanel, 0, SpringLayout.NORTH, resourceDetailPanel);
		springLayout.putConstraint(SpringLayout.WEST, leftDetailPanel, 0, SpringLayout.WEST, resourceDetailPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, leftDetailPanel, 0, SpringLayout.SOUTH, resourceDetailPanel);
		springLayout.putConstraint(SpringLayout.EAST, leftDetailPanel, leftPanelWidth, SpringLayout.WEST,
				resourceDetailPanel);

		int centerPanelWidth = (int) (resourcePanelHeight * 0.8f);
		springLayout.putConstraint(SpringLayout.NORTH, centerDetailPanel, 0, SpringLayout.NORTH, resourceDetailPanel);
		springLayout.putConstraint(SpringLayout.WEST, centerDetailPanel, EconomosGUI.largePanelGap(), SpringLayout.EAST,
				leftDetailPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, centerDetailPanel, 0, SpringLayout.SOUTH, resourceDetailPanel);
		springLayout.putConstraint(SpringLayout.EAST, centerDetailPanel, centerPanelWidth + EconomosGUI.largePanelGap(),
				SpringLayout.EAST, leftDetailPanel);

		springLayout.putConstraint(SpringLayout.NORTH, rightDetailPanel, 0, SpringLayout.NORTH, resourceDetailPanel);
		springLayout.putConstraint(SpringLayout.WEST, rightDetailPanel, EconomosGUI.largePanelGap(), SpringLayout.EAST,
				centerDetailPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, rightDetailPanel, 0, SpringLayout.SOUTH, resourceDetailPanel);
		springLayout.putConstraint(SpringLayout.EAST, rightDetailPanel, 0, SpringLayout.EAST, resourceDetailPanel);
	}

	@Override
	public boolean isInitialised() {
		return initialised;
	}
}