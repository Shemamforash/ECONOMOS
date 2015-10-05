package economos;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

import economos.GUIElements.MyPanel;

public class GuildPanel extends GUIElements.MyPanel {
	private MyGuildButton[]		buttons			= new MyGuildButton[8];
	private GUIElements.MyPanel	resourceList	= new GUIElements.MyPanel(false);
	private EconomosGUI			main;
	private int height, width;

	public GuildPanel(String[] guildNames, EconomosGUI main, int height, int width) {
		super(true);
		this.main = main;
		this.height = height;
		this.width = width;
		setup(guildNames);
	}

	public void setup(String[] guildNames) {
		GUIElements.MyPanel guildButtonPanel = new GUIElements.MyPanel(true);
		guildButtonPanel.setLayout(new GridLayout(1, 8));

		for (int i = 0; i < buttons.length; ++i) {
			buttons[i] = new MyGuildButton(guildNames[i]);
			guildButtonPanel.add(buttons[i]);
		}

		new GUIElements.MyPanel(true);

		SpringLayout sl_listPanel = new SpringLayout();
		this.setLayout(sl_listPanel);

		JScrollPane resourceScrollPane = new JScrollPane();
		sl_listPanel.putConstraint(SpringLayout.NORTH, resourceScrollPane, 0, SpringLayout.SOUTH, guildButtonPanel);
		sl_listPanel.putConstraint(SpringLayout.SOUTH, resourceScrollPane, 0, SpringLayout.SOUTH, this);
		resourceScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		sl_listPanel.putConstraint(SpringLayout.WEST, resourceScrollPane, 0, SpringLayout.WEST, this);
		sl_listPanel.putConstraint(SpringLayout.EAST, resourceScrollPane, 0, SpringLayout.EAST, this);
		resourceScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
		resourceScrollPane.setBorder(null);
		this.add(resourceScrollPane);

		sl_listPanel.putConstraint(SpringLayout.NORTH, guildButtonPanel, 0, SpringLayout.NORTH, this);
		sl_listPanel.putConstraint(SpringLayout.WEST, guildButtonPanel, 0, SpringLayout.WEST, this);
		sl_listPanel.putConstraint(SpringLayout.SOUTH, guildButtonPanel, width / 8, SpringLayout.NORTH, this);
		sl_listPanel.putConstraint(SpringLayout.EAST, guildButtonPanel, 0, SpringLayout.EAST, this);

		this.add(guildButtonPanel);

		resourceScrollPane.setViewportView(resourceList);
		resourceList.setLayout(new GridBagLayout());
	}

	public void updateMyList(String guildName) {
		if (main.getCurrentPlayer().getPlayerResourceMap().getResourceTypes().containsKey(guildName)) {
			resourceList.removeAll();
			GridBagConstraints gc = new GridBagConstraints();
			gc.gridx = 0;
			gc.gridy = 0;
			gc.ipady = (height - width / 8) / 30 - 11;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.weightx = 1;
			ArrayList<MerchantResource> arr = new ArrayList<MerchantResource>(main.getCurrentPlayer().getPlayerResourceMap().getResourceTypes().get(guildName).getResourcesInType());
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
		private ResourceButton	thisButton;

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
		public MyGuildButton	thisButton;
		private BufferedImage	iconImage	= null;

		public MyGuildButton(String text) {
			super(text, true);
			if (text.equals("Spicer")) {
				try {
					iconImage = ImageIO.read(new File("Spicer PNG.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				;
			}
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

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (iconImage != null) {
				g.drawImage(iconImage, 0, 0, this.getWidth(), this.getHeight(), null);
			}
		}
	}
}