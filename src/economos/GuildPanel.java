package economos;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class GuildPanel extends GUIElements.MyPanel {
	private MyGuildButton[] buttons = new MyGuildButton[8];
	private GUIElements.MyPanel resourceList = new GUIElements.MyPanel(false);
	private EconomosGUI main;

	public GuildPanel(String[] guildNames, EconomosGUI main) {
		super(true);
		this.main = main;
		setup(guildNames);
	}

	public void setup(String[] guildNames) {
		GUIElements.MyPanel guildButtonPanel = new GUIElements.MyPanel(true);
		guildButtonPanel.setLayout(new GridLayout(8, 1, 6, 0));

		for (int i = 0; i < buttons.length; ++i) {
			buttons[i] = new MyGuildButton(guildNames[i]);
			guildButtonPanel.add(buttons[i]);
		}

		new GUIElements.MyPanel(true);

		SpringLayout sl_listPanel = new SpringLayout();
		this.setLayout(sl_listPanel);

		JScrollPane resourceScrollPane = new JScrollPane();
		sl_listPanel.putConstraint(SpringLayout.NORTH, resourceScrollPane, 0, SpringLayout.NORTH, this);
		sl_listPanel.putConstraint(SpringLayout.SOUTH, resourceScrollPane, 0, SpringLayout.SOUTH, this);
		resourceScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		sl_listPanel.putConstraint(SpringLayout.WEST, resourceScrollPane, 84, SpringLayout.WEST, this);
		sl_listPanel.putConstraint(SpringLayout.EAST, resourceScrollPane, 300, SpringLayout.WEST, this);
		resourceScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		resourceScrollPane.setBorder(null);
		this.add(resourceScrollPane);

		sl_listPanel.putConstraint(SpringLayout.NORTH, guildButtonPanel, 0, SpringLayout.NORTH, this);
		sl_listPanel.putConstraint(SpringLayout.WEST, guildButtonPanel, 0, SpringLayout.WEST, this);
		sl_listPanel.putConstraint(SpringLayout.SOUTH, guildButtonPanel, 0, SpringLayout.SOUTH, this);
		sl_listPanel.putConstraint(SpringLayout.EAST, guildButtonPanel, -6, SpringLayout.WEST, resourceScrollPane);

		this.add(guildButtonPanel);

		resourceList = new GUIElements.MyPanel(false);
		resourceScrollPane.setViewportView(resourceList);
		resourceList.setLayout(new GridBagLayout());
	}

	public void updateMyList(String guildName) {
		if (main.getCurrentPlayer().getPlayerResourceMap().getResourceTypes().containsKey(guildName)) {
			resourceList.removeAll();
			GridBagConstraints gc = new GridBagConstraints();
			gc.gridx = 0;
			gc.ipady = 10;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.weightx = 1;
			ArrayList<PlayerResource> arr = new ArrayList<PlayerResource>(
					main.getCurrentPlayer().getPlayerResourceMap().getResourceTypes().get(guildName).getResourcesInType());
			String[] rarities = new String[] { "Commonplace", "Unusual", "Soughtafter", "Coveted", "Legendary" };
			int ctr = 0;
			boolean setDarker = false;
			for (int i = 0; i < arr.size(); ++i) {
				setDarker = !setDarker;
				ResourceButton tempButton;
				if (i % 4 == 0) {
					tempButton = new ResourceButton(rarities[ctr], false, setDarker);
					resourceList.add(tempButton, gc);
					++gc.gridy;
					++ctr;
				}
				tempButton = new ResourceButton(arr.get(i).getName(), true, setDarker);
				resourceList.add(tempButton, gc);
				++gc.gridy;
			}
			resourceList.validate();
			resourceList.repaint();
		}
	}

	private class ResourceButton extends GUIElements.MyButton {
		private ResourceButton thisButton;

		public ResourceButton(String text, boolean enabled, boolean darker) {
			super(text, enabled, new Color(30, 30, 30), new Color(25, 25, 25));
			if (!enabled) {
				setForeground(Color.white);
			}
			thisButton = this;
			this.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (main.getSelectedResource() != null) {
						main.getSelectedResource().setSelected(false);
					}
					main.setSelectedResource(thisButton);
					main.getSelectedResource().setSelected(true);
				}
			});
		}
	}

	private class MyGuildButton extends GUIElements.MyButton {
		public MyGuildButton thisButton;

		public MyGuildButton(String text) {
			super(text, true);
			thisButton = this;
			this.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (main.getSelectedGuild() != null) {
						main.getSelectedGuild().setSelected(false);
					}
					main.setSelectedGuild(thisButton);
					main.getSelectedGuild().setSelected(true);
					updateMyList(thisButton.getText());
				}
			});
		}
	}
}