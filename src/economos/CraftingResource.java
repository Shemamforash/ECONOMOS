package economos;

public class CraftingResource extends UserResource {
	private RequisiteResource[]	requisiteResources;
	private int					value, timetocraft, cost;

	public CraftingResource(String name, String id, String type, String rarity, String recipe, int cost) {
		super(name, id, "", type, rarity);
		this.cost = cost;
		requisiteResources = new RequisiteResource[prerequisites.length];
		for (int i = 0; i < prerequisites.length; ++i) {
			requisiteResources[i] = new RequisiteResource(prerequisites[i], quantities[i]);
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
		private MerchantResource	resource;
		private int					quantity;
		private boolean				hasMetRequirements;

		public void reduceResource() {
			resource.updateQuantity(quantity, 0);
		}

		public RequisiteResource(MerchantResource resource, int quantity) {
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
}
