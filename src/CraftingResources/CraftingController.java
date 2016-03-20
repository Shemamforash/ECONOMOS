package CraftingResources;

import java.util.ArrayList;

import MerchantResources.Resource;
import MerchantResources.ResourceMap;

public class CraftingController {
	private static ArrayList<Guild> guilds = new ArrayList<Guild>();
	private static String[] craftingLevels = new String[]{"Dabbler", "Apprentice", "Journeyman", "Master", "Adept"};
	private static ResourceMap<CraftingResource> craftingResources = new ResourceMap<CraftingResource>(true);
	
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

	public static Resource findResource(String lookup) {
		return craftingResources.getResource(lookup);
	}
}
