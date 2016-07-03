package CraftingResources;

import java.util.ArrayList;

import MerchantResources.Resource;
import economos.Main;
import economos.Player;

public class CraftingResource extends Resource {
	private ArrayList<RequisiteResource>	requisiteResources = new ArrayList<RequisiteResource>();
	private int					value, cost, quantity = 0;

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

	public boolean canCraft(Player p) {
		if (hasResources()) {
			return (p.money() >= cost);
		}
		return false;
	}

	public void craft(Player p) {
		requisiteResources.forEach((r) -> r.reduceResource());
		updateQuantity(1, 0);
		p.updateMoney(-cost);
	}

	public void sell(Player p) {
		updateQuantity(-1, 0);
		p.updateMoney(value);
	}
	
	public int value(){
		return value;
	}

	public class RequisiteResource {
		private Resource			resource;
		private int					quantity;
		private boolean				hasMetRequirements;

		public void reduceResource() {
			Main.getPlayer().findUserResource(resource.name()).consumeResource(quantity);
		}

		public RequisiteResource(Resource resource, int quantity) {
			this.resource = resource;
			this.quantity = quantity;
		}

		public boolean metRequirements() {
			if (hasMetRequirements) {
				return true;
			} else {
				if (Main.getPlayer().findUserResource(resource.name()).quantity() >= quantity) {
					hasMetRequirements = true;
					return true;
				}
				return false;
			}
		}
		
		public Resource resource(){
			return resource;
		}
	}

	public void updateQuantity(int amount, float price) {
		quantity += amount;
	}
}
