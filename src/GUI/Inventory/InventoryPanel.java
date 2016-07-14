package GUI.Inventory;

import GUI.EconomosGUI;
import GUI.GUIElements;
import GUI.GuildPanel.GuildPanel;
import GUI.Merchant.CenterDetailPanel;
import GUI.Merchant.GraphPanel;
import GUI.Merchant.LeftDetailPanel;
import GUI.Merchant.RightDetailPanel;
import GUI.SubListPanel;
import economos.UpdateCaller;
import economos.UpdateListener;
import java.awt.GridLayout;
import javax.swing.SpringLayout;

/**
 * Created by Sam on 14/07/2016.
 */
public class InventoryPanel extends GUIElements.MyPanel implements UpdateListener {
	private SubListPanel bodyPanel;
	private SpringLayout springLayout;
	private boolean initialised = false;

	public InventoryPanel(int height) {
		springLayout = new SpringLayout();
		setLayout(springLayout);
		bodyPanel = new SubListPanel(height, false);
		springLayout.putConstraint(SpringLayout.NORTH, bodyPanel, EconomosGUI.largePanelGap(), SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, bodyPanel, EconomosGUI.largePanelGap(), SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, bodyPanel, -40, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, bodyPanel, -EconomosGUI.largePanelGap(), SpringLayout.EAST, this);
		add(bodyPanel);
		UpdateCaller.addListener(this);
		initialised = true;
	}

	public void receiveUpdate() {
		if (isVisible()) {
			//DOnothingapparently
		}
	}

	@Override
	public boolean isInitialised() {
		return initialised;
	}
}
