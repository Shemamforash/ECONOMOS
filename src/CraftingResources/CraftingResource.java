package CraftingResources;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import MerchantResources.Resource;
import economos.Main;
import economos.Player;

public class CraftingResource extends Resource {
	private ArrayList<RequisiteResource>	requisiteResources = new ArrayList<RequisiteResource>();
	private int					value, cost, quantity = 0, sold = 0;
	private Quality quality;

	public enum Quality{
		BROKEN("Broken", 0),
		POOR("Poor", 1),
		STANDARD("Standard", 2),
		GOOD("Good", 3),
		EXCEPTIONAL("Exceptional", 4),
		PERFECTED("Perfected", 5);

		private final String text;
		private final int level;

		private Quality(final String text, final int level){
			this.text = text;
			this.level = level;
		}

		public String toString(){
			return text;
		}

		public int toLevel() {
			return level;
		}

		public static Quality randomQuality() {
			int n = new Random().nextInt(6);
			switch(n) {
				case 0:
					return Quality.BROKEN;
				case 1:
					return Quality.POOR;
				case 2:
					return Quality.STANDARD;
				case 3:
					return Quality.GOOD;
				case 4:
					return Quality.EXCEPTIONAL;
				case 5:
					return Quality.PERFECTED;
			}
			return null;
		}
	}

	public CraftingResource(String name, String id, String guild, String rarity, String description, int cost) {
		super(name, id, description, guild, rarity, ResourceType.CRAFTING);
		this.cost = cost;
	}
	
	public void unlock(){
		super.unlock();
		requisiteResources.forEach((r) -> {
			if(r.resource().type() == ResourceType.MERCHANT){
				r.resource().unlock();
			}
		});
	}
	
	public void addPrerequisite(Resource r, int quantity){
		requisiteResources.add(new RequisiteResource(r, quantity));
	}

	private boolean hasResources() {
		for (RequisiteResource r : requisiteResources) {
			if (!r.metRequirements()) {
				return false;
			}
		}
		return true;
	}

	public boolean canCraft() {
		if (hasResources()) {
			return (Player.money() >= cost);
		}
		return false;
	}

	public void craft() {
		requisiteResources.forEach((r) -> r.reduceResource());
		updateQuantity(1, 0);
		Player.updateMoney(-cost);
		quality = Quality.randomQuality();
	}

	public void sell() {
		updateQuantity(-1, 0);
		Player.updateMoney(value);
	}
	
	public int value(){
		return value;
	}

	public int quantity() { return quantity; }

	public int sold() { return sold; }

	public class RequisiteResource {
		private Resource			resource;
		private int					quantity;
		private boolean				hasMetRequirements;

		public void reduceResource() {
			Player.findUserResource(resource.name()).consumeResource(quantity);
		}

		public RequisiteResource(Resource resource, int quantity) {
			this.resource = resource;
			this.quantity = quantity;
		}

		public boolean metRequirements() {
			if (hasMetRequirements) {
				return true;
			} else {
				if (Player.findUserResource(resource.name()).quantity() >= quantity) {
					hasMetRequirements = true;
					return true;
				}
				return false;
			}
		}
		
		public Resource resource(){
			return resource;
		}

		public int quantity() {
			return quantity;
		}
	}

	public ArrayList<RequisiteResource> requisiteResources() {
		return requisiteResources;
	}

	public void updateQuantity(int amount, float price) {
		quantity += amount;
	}
}
