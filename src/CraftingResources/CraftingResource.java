package CraftingResources;

import java.util.ArrayList;

import MerchantResources.MarketController;
import MerchantResources.Resource;
import economos.Main;
import economos.Player;

public class CraftingResource extends Resource {
	private ArrayList<RequisiteResource>	requisiteResources = new ArrayList<RequisiteResource>();
	private int					value, cost, quantity = 0;

	public CraftingResource(String name, String id, String type, String rarity, String description, int cost) {
		super(name, id, description, type, rarity);
		this.cost = cost;
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
			if (p.getMoney() >= cost) {
				return true;
			}
		}
		return false;
	}

	public void craft(Player p) {
		for (RequisiteResource r : requisiteResources) {
			r.reduceResource();
		}
		updateQuantity(1, 0);
		p.updateMoney(-cost);
	}

	public void sell(Player p) {
		updateQuantity(-1, 0);
		p.updateMoney(value);
	}
	
	public int getValue(){
		return value;
	}

	public class RequisiteResource {
		private Resource			resource;
		private int					quantity;
		private boolean				hasMetRequirements;

		public void reduceResource() {
			Main.getPlayer().findUserResource(resource.getName()).consumeResource(quantity);
		}

		public RequisiteResource(Resource resource, int quantity) {
			this.resource = resource;
			this.quantity = quantity;
		}

		public boolean metRequirements() {
			if (hasMetRequirements) {
				return true;
			} else {
				if (Main.getPlayer().findUserResource(resource.getName()).getQuantity() >= quantity) {
					hasMetRequirements = true;
					return true;
				}
				return false;
			}
		}
	}

	public synchronized void updateQuantity(int amount, float price) {
		quantity += amount;
	}
}
