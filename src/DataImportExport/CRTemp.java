package DataImportExport;

import java.util.ArrayList;

import CraftingResources.CraftingResource;
import MerchantResources.Resource;

public class CRTemp {
	private String recipe;
	private ArrayList<RecipePart> temporaryRecipe = new ArrayList<RecipePart>();
	private CraftingResource craftingResource;

	private class RecipePart {
		private Resource resource;
		private int quantity;

		public RecipePart(Resource resource, int quantity) {
			this.resource = resource;
			this.quantity = quantity;
		}

		public Resource resource() {
			return resource;
		}
		public int quantity() {
			return quantity;
		}
	}

	public CRTemp(String recipe, CraftingResource craftingResource) {
		this.recipe = recipe;
		this.craftingResource = craftingResource;
		assignPrerequisites();
	}

	public void convertRecipe() {
		temporaryRecipe.forEach((p) -> craftingResource.addPrerequisite(p.resource(), p.quantity()));
		if(craftingResource.rarity().equals(DataParser.craftingRarities().get(0))){
			craftingResource.unlock();
		}
	}

	public void assignPrerequisites() {
		for (int i = 0; i < 5; ++i) {
			if (recipe.startsWith("00")) {
				break;
			}
			String lookup = recipe.substring(0, 6);
			String recipePartId = lookup.substring(0, 4);
			int recipePartQuantity = Integer.valueOf(lookup.substring(5, 6));
			recipe = recipe.substring(6);
			Resource r = DataParser.findResource(recipePartId);
			if (r != null) {
				temporaryRecipe.add(new RecipePart(r, recipePartQuantity));
			} else {
				System.out.println(craftingResource.name() + " " + recipePartId + "Recipe part not found. Recipe will fail.");
			}
		}
	}

	public String recipe() {
		return recipe;
	}
}
