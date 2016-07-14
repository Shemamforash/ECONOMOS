package GUI.GuildPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.*;

import CraftingResources.CraftingController;
import DataImportExport.DataParser;
import GUI.GUIElements;
import GUI.SubListPanel;
import MarketSimulator.Debug;
import MarketSimulator.MarketController;
import MerchantResources.Resource;
import economos.SelectedResourceCaller;

public class GuildPanel extends GUIElements.MyPanel {
	private ArrayList<? extends Resource> resources;
	private PanelType panelType;
	private SubListPanel subListPanel;
	private GuildPanel guildPanel;
	private JButton selectedResourceButton;

	public enum PanelType {
		CRAFTING, MERCHANT
	};

	public GuildPanel(int height, PanelType panelType) {
		super(true);
		guildPanel = this;
		subListPanel = new SubListPanel(height, false);
		this.panelType = panelType;
		if (panelType == PanelType.CRAFTING) {
			resources = CraftingController.getCraftingResources().resources();
		} else {
			resources = MarketController.getMarketResources().resources();
		}
		setup();
	}

	public ArrayList<Resource> getUnlockedResources(){
		ArrayList<Resource> unlockedResources = new ArrayList<Resource>();
		for(Resource r : resources){
			if(r.unlocked()){
				unlockedResources.add(r);
			}
		}
		return unlockedResources;
	}
	
	public void setup() {
		GUIElements.MyPanel guildButtonPanel = new GUIElements.MyPanel(true);
		guildButtonPanel.setLayout(new GridLayout(1, 4));
		guildButtonPanel.add(new JLabel("Filter by:"));
		GUIElements.MyButton sortByGuildButton = new GUIElements.MyButton("Guild", true);
		sortByGuildButton.addActionListener(e -> updateMyList(convertToButtonList(sortByGuild(getUnlockedResources()))));
		guildButtonPanel.add(sortByGuildButton);
		
		GUIElements.MyButton sortByRarityButton = new GUIElements.MyButton("Rarity", true);
		sortByRarityButton.addActionListener(e -> updateMyList(convertToButtonList(sortByRarity(getUnlockedResources()))));
		guildButtonPanel.add(sortByRarityButton);
		
		GUIElements.MyButton sortByBothButton = new GUIElements.MyButton("Both", true);
		sortByBothButton.addActionListener(e -> updateMyList(sortByBoth()));
		guildButtonPanel.add(sortByBothButton);

		SpringLayout sl_listPanel = new SpringLayout();
		setLayout(sl_listPanel);

		sl_listPanel.putConstraint(SpringLayout.NORTH, guildButtonPanel, 0, SpringLayout.NORTH, this);
		sl_listPanel.putConstraint(SpringLayout.WEST, guildButtonPanel, 0, SpringLayout.WEST, this);
		sl_listPanel.putConstraint(SpringLayout.SOUTH, guildButtonPanel, 30, SpringLayout.NORTH, this);
		sl_listPanel.putConstraint(SpringLayout.EAST, guildButtonPanel, 0, SpringLayout.EAST, this);
		add(guildButtonPanel);

		sl_listPanel.putConstraint(SpringLayout.NORTH, subListPanel, 0, SpringLayout.SOUTH, guildButtonPanel);
		sl_listPanel.putConstraint(SpringLayout.WEST, subListPanel, 0, SpringLayout.WEST, this);
		sl_listPanel.putConstraint(SpringLayout.SOUTH, subListPanel, 0, SpringLayout.SOUTH, this);
		sl_listPanel.putConstraint(SpringLayout.EAST, subListPanel, 0, SpringLayout.EAST, this);
		add(subListPanel);

		updateMyList(convertToButtonList(sortByGuild(getUnlockedResources())));
	}

	public void updateMyList(ArrayList<GUIElements.MyButton> buttonList){
		subListPanel.clear();
		SubListPanel.MainPanel p = null;
		for(GUIElements.MyButton b : buttonList) {
			if(b.getClass().getTypeName().equals("GUI.GuildPanel.ResourceButton") && p != null) {
				subListPanel.addSubPanel(p, b);
			} else {
				p = subListPanel.addMainPanelAsButton(b.getText());
			}
		}
		subListPanel.update();
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
			rarities = DataParser.craftingRarities();
		} else {
			rarities = DataParser.merchantRarities();
		}
		return sort(rarities, listToSort);
	}

	public ArrayList<SortedCategory> sortByGuild(ArrayList<? extends Resource> listToSort) {
		ArrayList<String> guilds;
		if (panelType == PanelType.CRAFTING) {
			guilds = DataParser.craftingTypes();
		} else {
			guilds = DataParser.merchantTypes();
		}
		return sort(guilds, listToSort);
	}

	public ArrayList<GUIElements.MyButton> sortByBoth() {
		ArrayList<SortedCategory> superCategories = new ArrayList<SortedCategory>();
		for (SortedCategory s : sortByGuild(getUnlockedResources())) {
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
				if (r.rarity().equals(category)) {
					sorted.addResource(r);
				} else if (r.guild().equals(category)) {
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
			if(resourcesInCategory.size() > 0){
				buttons.add(new GUIElements.MyButton(category, true));
				buttons.addAll(subsortAlphabetically(resourcesInCategory));
			}
			return buttons;
		}

		private ArrayList<GUIElements.MyButton> subsortAlphabetically(ArrayList<? extends Resource> sublist) {
			ArrayList<GUIElements.MyButton> buttons = new ArrayList<GUIElements.MyButton>();
			Comparator<Resource> alphabetComparator = (Resource one, Resource other) -> one.name().compareTo(other.name());
			Collections.sort(sublist, alphabetComparator);
			boolean setDarker = true;
			for (Resource r : sublist) {
				buttons.add(new ResourceButton(r, true, setDarker, guildPanel));
				setDarker = !setDarker;
			}
			return buttons;
		}
	}

	public void setSelectedResource(ResourceButton b) {
		if (selectedResourceButton != null) {
			selectedResourceButton.setSelected(false);
		}
		SelectedResourceCaller.updateResource(b.getResourceName());
		selectedResourceButton = b;
		selectedResourceButton.setSelected(true);
	}

	public PanelType getPanelType(){
		return panelType;
	}
}