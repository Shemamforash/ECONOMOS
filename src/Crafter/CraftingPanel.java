package Crafter;

import java.awt.GridLayout;

import javax.swing.SpringLayout;

import GUI.EconomosGUI;
import GUI.GUIElements;
import GUI.GuildPanel;
import economos.UpdateCaller;
import economos.UpdateListener;

public class CraftingPanel extends GUIElements.MyPanel implements UpdateListener {
	private GuildPanel guilds;
	private GUIElements.MyPanel resourceDetailPanel, bodyPanel;
	private SpringLayout springLayout;
	private boolean initialised = false;
	private MinigameController minigameController;

	public CraftingPanel() {
		springLayout = new SpringLayout();
		setLayout(springLayout);

		guilds = new GuildPanel(EconomosGUI.screenHeight() - 50, EconomosGUI.screenWidth() / 4,
				GuildPanel.PanelType.CRAFTING);
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

		resourceGraphPanel.add(new MinigameController());

		UpdateCaller.addListener(this);
		initialised = true;
	}

	public void receiveUpdate() {
		if (isVisible()) {
			springLayout.putConstraint(SpringLayout.NORTH, guilds, EconomosGUI.largePanelGap(), SpringLayout.NORTH,
					this);
			springLayout.putConstraint(SpringLayout.WEST, guilds, EconomosGUI.largePanelGap(), SpringLayout.WEST, this);
			springLayout.putConstraint(SpringLayout.SOUTH, guilds, -40, SpringLayout.SOUTH, this);
			springLayout.putConstraint(SpringLayout.EAST, guilds, (int) (EconomosGUI.screenWidth() / 4),
					SpringLayout.WEST, this);

			int resourcePanelHeight = (int) ((EconomosGUI.screenHeight() - 40) / 2.4f);
			springLayout = (SpringLayout) (bodyPanel.getLayout());
			springLayout.putConstraint(SpringLayout.NORTH, resourceDetailPanel, 0, SpringLayout.NORTH, bodyPanel);
			springLayout.putConstraint(SpringLayout.WEST, resourceDetailPanel, 0, SpringLayout.WEST, bodyPanel);
			springLayout.putConstraint(SpringLayout.SOUTH, resourceDetailPanel, resourcePanelHeight, SpringLayout.NORTH,
					bodyPanel);
			springLayout.putConstraint(SpringLayout.EAST, resourceDetailPanel, 0, SpringLayout.EAST, bodyPanel);

			revalidate();
//			minigameController.repaint();
		}
	}

	@Override
	public boolean isInitialised() {
		return initialised;
	}
}