package GUI;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.*;

import CraftingResources.CraftingController;
import CraftingResources.CraftingResource;
import DataImportExport.DataParser;
import MerchantResources.MarketController;
import MerchantResources.MarketResource;
import MerchantResources.MerchantResource;
import MerchantResources.Resource;
import MerchantResources.ResourceMap;
import economos.Main;
import economos.SelectedResourceCaller;

public class GuildPanel extends GUIElements.MyPanel {
	private GUIElements.MyPanel resourceList = new GUIElements.MyPanel(false);
	private int height, width;
	private JButton selectedResourceButton;
	private JScrollPane resourceScrollPane;
	private ArrayList<? extends Resource> resources;
	private PanelType panelType;

	public enum PanelType {
		CRAFTING, MERCHANT
	};

	public GuildPanel(int height, int width, PanelType panelType) {
		super(true);
		this.height = height;
		this.width = width;
		this.panelType = panelType;
		if (panelType == PanelType.CRAFTING) {
			resources = CraftingController.getCraftingResources().getResources();
		} else {
			resources = MarketController.getMarketResources().getResources();
		}
		setup();
	}

	public void setup() {
		GUIElements.MyPanel guildButtonPanel = new GUIElements.MyPanel(true);
		guildButtonPanel.setLayout(new GridLayout(1, 4));
		guildButtonPanel.add(new JLabel("Filter by:"));
		GUIElements.MyButton sortByGuildButton = new GUIElements.MyButton("Guild", true);
		sortByGuildButton.addActionListener(e -> updateMyList(convertToButtonList(sortByGuild(resources))));
		guildButtonPanel.add(sortByGuildButton);
		
		GUIElements.MyButton sortByRarityButton = new GUIElements.MyButton("Rarity", true);
		sortByRarityButton.addActionListener(e -> updateMyList(convertToButtonList(sortByRarity(resources))));
		guildButtonPanel.add(sortByRarityButton);
		
		GUIElements.MyButton sortByBothButton = new GUIElements.MyButton("Both", true);
		sortByBothButton.addActionListener(e -> updateMyList(sortByBoth()));
		guildButtonPanel.add(sortByBothButton);

		SpringLayout sl_listPanel = new SpringLayout();
		setLayout(sl_listPanel);

		resourceScrollPane = new JScrollPane(resourceList);
		resourceScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		resourceScrollPane.getVerticalScrollBar().setUnitIncrement(10);
		sl_listPanel.putConstraint(SpringLayout.NORTH, resourceScrollPane, 10, SpringLayout.SOUTH, guildButtonPanel);
		sl_listPanel.putConstraint(SpringLayout.SOUTH, resourceScrollPane, 0, SpringLayout.SOUTH, this);
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
		resourceList.setLayout(new GridBagLayout());
		updateMyList(convertToButtonList(sortByGuild(resources)));
	}

	public ArrayList<GUIElements.MyButton> convertToButtonList(ArrayList<SortedCategory> sortedList) {
		ArrayList<GUIElements.MyButton> buttons = new ArrayList<GUIElements.MyButton>();
		for (SortedCategory s : sortedList) {
			buttons.addAll(s.getButtons());
		}
		return buttons;
	}

	public ArrayList<SortedCategory> sortByRarity(ArrayList<? extends Resource> listToSort) {
		ArrayList<String> rarities;
		if (panelType == PanelType.CRAFTING) {
			rarities = DataParser.getCraftingRarities();
		} else {
			rarities = DataParser.getMerchantRarities();
		}
		return sort(rarities, listToSort);
	}

	public ArrayList<SortedCategory> sortByGuild(ArrayList<? extends Resource> listToSort) {
		ArrayList<String> guilds;
		if (panelType == PanelType.CRAFTING) {
			guilds = DataParser.getCraftingTypes();
		} else {
			guilds = DataParser.getMerchantTypes();
		}
		return sort(guilds, listToSort);
	}

	public ArrayList<GUIElements.MyButton> sortByBoth() {
		ArrayList<SortedCategory> superCategories = new ArrayList<SortedCategory>();
		for (SortedCategory s : sortByGuild(resources)) {
			ArrayList<Resource> resourcesInCategory = s.getResources();
			superCategories.add(new SortedCategory(s.getCategory(), sortByRarity(resourcesInCategory)));
		}
		ArrayList<GUIElements.MyButton> buttons = new ArrayList<GUIElements.MyButton>();
		for (SortedCategory s : superCategories) {
			buttons.add(new GUIElements.MyButton(s.getCategory(), true));
			for (SortedCategory subs : s.getSubCategories()) {
				buttons.addAll(subs.getButtons());
			}
		}
		return buttons;
	}

	public ArrayList<SortedCategory> sort(ArrayList<String> sortingCategories,
			ArrayList<? extends Resource> listToSort) {
		ArrayList<SortedCategory> sortedCategories = new ArrayList<SortedCategory>();
		for (String category : sortingCategories) {
			SortedCategory sorted = new SortedCategory(category);
			for (Resource r : listToSort) {
				if (r.getRarity().equals(category)) {
					sorted.addResource(r);
				} else if (r.getType().equals(category)) {
					sorted.addResource(r);
				}
			}
			sortedCategories.add(sorted);
		}
		return sortedCategories;
	}

	private class SortedCategory {
		private String category;
		private ArrayList<Resource> resourcesInCategory = new ArrayList<Resource>();
		private ArrayList<SortedCategory> subCategories = new ArrayList<SortedCategory>();

		public SortedCategory(String category) {
			this.category = category;
		}

		public SortedCategory(String category, ArrayList<SortedCategory> subCategories) {
			this.category = category;
			this.subCategories = subCategories;
		}

		public ArrayList<SortedCategory> getSubCategories() {
			return subCategories;
		}

		public void addResource(Resource r) {
			resourcesInCategory.add(r);
		}

		public String getCategory() {
			return category;
		}

		public ArrayList<Resource> getResources() {
			return resourcesInCategory;
		}

		public ArrayList<GUIElements.MyButton> getButtons() {
			ArrayList<GUIElements.MyButton> buttons = new ArrayList<GUIElements.MyButton>();
			buttons.add(new GUIElements.MyButton(category, true));
			buttons.addAll(subsortAlphabetically(resourcesInCategory));
			return buttons;
		}

		private ArrayList<GUIElements.MyButton> subsortAlphabetically(ArrayList<? extends Resource> resources) {
			ArrayList<GUIElements.MyButton> buttons = new ArrayList<GUIElements.MyButton>();
			Comparator<Resource> alphabetComparator = new Comparator<Resource>() {
				public int compare(Resource one, Resource other) {
					return one.getName().compareTo(other.getName());
				}
			};
			Collections.sort(resources, alphabetComparator);
			boolean setDarker = true;
			for (Resource r : resources) {
				buttons.add(new ResourceButton(r, true, setDarker));
				setDarker = !setDarker;
			}
			return buttons;
		}
	}

	public void updateMyList(ArrayList<GUIElements.MyButton> buttonList) {
		resourceList.removeAll();
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = 0;
		gc.ipady = height / 30 + 10;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.weightx = 1;
		for (GUIElements.MyButton b : buttonList) {
			resourceList.add(b, gc);
			++gc.gridy;
		}
		resourceList.validate();
		resourceList.repaint();
		resourceScrollPane.getViewport().revalidate();
	}

	private void setSelectedResource(ResourceButton b) {
		if (selectedResourceButton != null) {
			selectedResourceButton.setSelected(false);
		}
		SelectedResourceCaller.updateResource(b.getResourceName());
		selectedResourceButton = b;
		selectedResourceButton.setSelected(true);
	}

	private class ResourceButton extends GUIElements.MyButton {
		private ResourceButton thisButton;
		private Resource resource;

		public ResourceButton(Resource resource, boolean enabled, boolean darker) {
			super("", enabled, new Color(30, 30, 30), new Color(25, 25, 25), true);
			this.resource = resource;
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
			return resource.getName();
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

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
			g.drawString(resource.getName(), 10, getHeight() / 2 + yOffset);
			g.setFont(thisButton.getFont().deriveFont(10f));
			g.setColor(new Color(200, 200, 200));
			String str;
			if (panelType == PanelType.MERCHANT) {
				BigDecimal price = new BigDecimal(((MarketResource) resource).getAveragePrice());
				BigDecimal roundedPrice = price.setScale(2, BigDecimal.ROUND_HALF_UP);
				str = "C" + roundedPrice + " (D" + ((MarketResource) resource).getDemand() + "/ S"
						+ ((MarketResource) resource).getSupply() + ")";
			} else {
				str = "C" + ((CraftingResource) resource).getValue();
			}
			int stringLength = (int) g.getFontMetrics().getStringBounds(str, g).getWidth();
			g.drawString(str, getWidth() - (stringLength + 10), getHeight() / 2 + 5);
		}
	}
}