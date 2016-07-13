package CraftingResources;

import java.util.ArrayList;

import MerchantResources.Resource;
import MerchantResources.ResourceMap;
import economos.SelectedResourceListener;

public class CraftingController {
	private static ArrayList<Guild> guilds = new ArrayList<Guild>();
	private static String[] craftingLevels = new String[]{"Dabbler", "Apprentice", "Journeyman", "Master", "Adept"};
	private static ResourceMap<CraftingResource> craftingResources = new ResourceMap<CraftingResource>(true);
	private static CraftingResource currentResource;

	private class Guild{
		private String name;
		private int xp = 0;
		
		public Guild(String name){
			this.name = name;
		}
		
		public void increaseXP(int amount){
			xp += amount;
		}
	}

	public static CraftingResource findResource(String lookup) {
		return craftingResources.resource(lookup);
	}
	
	public static ResourceMap<CraftingResource> getCraftingResources(){
		return craftingResources;
	}
}
