package economos;

public class CraftingResource extends PlayerResource {
	private RequisiteResource[] requisiteResources;
	private int value, timetocraft;

	public CraftingResource(String name, String description, String type, String rarity, PlayerResource[] prerequisites,
			int[] quantities) {
		super(name, description, type, rarity);
		requisiteResources = new RequisiteResource[prerequisites.length];
		for (int i = 0; i < prerequisites.length; ++i) {
			requisiteResources[i] = new RequisiteResource(prerequisites[i], quantities[i]);
		}
	}

	public class RequisiteResource {
		private PlayerResource resource;
		private int quantity;
		private boolean hasMetRequirements;

		public RequisiteResource(PlayerResource resource, int quantity) {
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
