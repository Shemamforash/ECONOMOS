package economos;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

import economos.GUIElements.MyPanel;

public class GuildPanel extends GUIElements.MyPanel {
	private MyGuildButton[]		buttons			= new MyGuildButton[8];
	private GUIElements.MyPanel	resourceList	= new GUIElements.MyPanel(false);
	private EconomosGUI			main;
	private int					height, width;
	private JButton				selectedResourceButton, selectedGuildButton;
	private JScrollPane			resourceScrollPane;

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

		resourceScrollPane = new JScrollPane();
		sl_listPanel.putConstraint(SpringLayout.NORTH, resourceScrollPane, 10, SpringLayout.SOUTH, guildButtonPanel);
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
		if (Main.getPlayer().getPlayerResourceMap().getResourceTypes().containsKey(guildName)) {
			resourceList.removeAll();
			GridBagConstraints gc = new GridBagConstraints();
			gc.gridx = 0;
			gc.gridy = 0;
			gc.ipady = height / 30 + 10;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.weightx = 1;
			ArrayList<MerchantResource> arr = new ArrayList<MerchantResource>(Main.getPlayer().getPlayerResourceMap().getResourceTypes().get(guildName).getResourcesInType());
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

	private void setSelectedResource(ResourceButton b) {
		if (selectedResourceButton != null) {
			selectedResourceButton.setSelected(false);
		}
		main.setSelectedResource(b.getResourceName());
		selectedResourceButton = b;
		selectedResourceButton.setSelected(true);
	}

	private class ResourceButton extends GUIElements.MyButton {
		private ResourceButton	thisButton;
		private String			resourceName;

		public ResourceButton(String text, boolean enabled, boolean darker) {
			super("", enabled, new Color(30, 30, 30), new Color(25, 25, 25));
			resourceName = text;
			if (!enabled) {
				setForeground(Color.white);
			}
			thisButton = this;
			this.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					setSelectedResource(thisButton);
				}
			});
		}

		public String getResourceName() {
			return resourceName;
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

			int yOffset;
			if (!isEnabled()) {
				g.setFont(thisButton.getFont().deriveFont(14f));
				g.setColor(new Color(255, 140, 0));
				yOffset = 7;
			} else {
				g.setFont(thisButton.getFont().deriveFont(12f));
				g.setColor(Color.white);
				yOffset = 6;
			}
			g.drawString(resourceName, 10, getHeight() / 2 + yOffset);

			MarketResource r = MarketController.getMarketResources().getResource(main.getSelectedGuild(), resourceName);
			if (r != null) {
				g.setFont(thisButton.getFont().deriveFont(10f));
				g.setColor(new Color(200, 200, 200));
				BigDecimal price = new BigDecimal(r.getAveragePrice());
				BigDecimal roundedPrice = price.setScale(2, BigDecimal.ROUND_HALF_UP);
				String str = "C" + roundedPrice + " (D" + r.getDemand() + "/ S" + r.getSupply() + ")";
				int stringLength = (int) g.getFontMetrics().getStringBounds(str, g).getWidth();
				g.drawString(str, getWidth() - (stringLength + 10), getHeight() / 2 + 5);
			}
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
					if (selectedGuildButton != null) {
						selectedGuildButton.setSelected(false);
						selectedGuildButton.repaint();
					}
					main.setSelectedGuild(thisButton.getText());
					selectedGuildButton = thisButton;
					selectedGuildButton.setSelected(true);
					updateMyList(thisButton.getText());
					resourceScrollPane.requestFocus();
					thisButton.repaint();
				}
			});
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (iconImage != null) {
				g.drawImage(iconImage, 2, 2, this.getWidth() - 4, this.getHeight() - 4, null);
			}
		}
	}
}