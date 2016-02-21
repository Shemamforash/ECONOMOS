package Resources;

import java.util.ArrayList;

import economos.Player;

public class CraftingResource extends UserResource {
	private RequisiteResource[]	requisiteResources;
	private int					value, timetocraft, cost;
	private String				recipe;

	public CraftingResource(String name, String id, String type, String rarity, String recipe, int cost) {
		super(name, id, "", type, rarity);
		this.cost = cost;
		this.recipe = recipe;
	}
	
	public void assignPrerequisites(){
		ArrayList<Resource> prerequisites = new ArrayList<Resource>();
		ArrayList<Integer> quantities = new ArrayList<Integer>();
		
		for(int i = 0; i < 5; ++i){
			if(recipe.startsWith("00")){
				break;
			}
			String lookup = recipe.substring(0, 6);
			recipe = recipe.substring(6);
			Resource r = CraftingController.findResource(lookup);
			if(r == null){
				r = MarketController.findResource(lookup);
			}
		}
		requisiteResources = new RequisiteResource[prerequisites.size()];
		for (int i = 0; i < prerequisites.size(); ++i) {
			requisiteResources[i] = new RequisiteResource(prerequisites.get(i), quantities.get(i));
		}
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

	public class RequisiteResource {
		private Resource	resource;
		private int					quantity;
		private boolean				hasMetRequirements;

		public void reduceResource() {
			resource.updateQuantity(quantity, 0);
		}

		public RequisiteResource(Resource resource, int quantity) {
			this.resource = resource;
			this.quantity = quantity;
		}

		public boolean metRequirements() {
			if (hasMetRequirements) {
				return true;
			} else {
				if (resource.getQuantity() >= quantity) {
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
