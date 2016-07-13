package economos;

import java.util.ArrayList;

import CraftingResources.CraftingController;
import CraftingResources.CraftingResource;
import GUI.GuildPanel;
import MerchantResources.MerchantResource;

public class SelectedResourceCaller {
	private static ArrayList<SelectedResourceListener> craftingListeners = new ArrayList<SelectedResourceListener>();
	private static ArrayList<SelectedResourceListener> merchantListeners = new ArrayList<SelectedResourceListener>();
	private static CraftingResource currentCraftingResource = null;
	private static MerchantResource currentMerchantResource = null;

	public static void addListener(SelectedResourceListener listener, GuildPanel.PanelType panelType){
		if(panelType == GuildPanel.PanelType.CRAFTING){
			craftingListeners.add(listener);
		} else {
			merchantListeners.add(listener);
		}
	}

	private static void notifyListeners(){
		if(currentMerchantResource != null) {
			merchantListeners.forEach((l) -> l.selectedResourceChanged(currentMerchantResource.marketResource()));
		}
		if(currentCraftingResource != null) {
			craftingListeners.forEach((l) -> l.selectedResourceChanged(currentCraftingResource));
		}
	}
	
	public static void updateResource(String name){
		MerchantResource tempMerchant = Player.findUserResource(name);
		if(tempMerchant != null){
			currentMerchantResource = tempMerchant;
			notifyListeners();
		}
		CraftingResource tempCrafting = CraftingController.findResource(name);
		if(tempCrafting != null){
			currentCraftingResource = tempCrafting;
			notifyListeners();
		}
	}
	
	public static void updateGuild(String guild){
//		listeners.forEach((l) -> l.selectedGuildChanged(guild));
//		listeners.forEach((l) -> l.selectedGuildChanged(guild));
	}
}
