package economos;

import java.util.ArrayList;

public class CraftingController {
	private ArrayList<Guild> guilds = new ArrayList<Guild>();
	private String[] craftingLevels = new String[]{"Dabbler", "Apprentice", "Journeyman", "Master", "Adept"};
	private ResourceMap<CraftingResource> craftingResources;
	
	public CraftingController(){
		craftingResources = new ResourceMap<CraftingResource>("", "Crafting");
	}
	
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
}
