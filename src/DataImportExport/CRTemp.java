package DataImportExport;

import java.util.ArrayList;

import CraftingResources.CraftingController;
import CraftingResources.CraftingResource;
import CraftingResources.CraftingResource.RequisiteResource;
import MerchantResources.MarketController;
import MerchantResources.MarketResource;
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

		public Resource getResource() {
			return resource;
		}

		public int getQuantity() {
			return quantity;
		}
	}

	public CRTemp(String recipe, CraftingResource craftingResource) {
		this.recipe = recipe;
		this.craftingResource = craftingResource;
		assignPrerequisites();
	}

	public void convertRecipe() {
		for (RecipePart p : temporaryRecipe) {
			craftingResource.addPrerequisite(p.getResource(), p.getQuantity());
		}
		if(craftingResource.getRarity().equals(DataParser.getCraftingRarities().get(0))){
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
				System.out.println(craftingResource.getName() + " " + recipePartId + "Recipe part not found. Recipe will fail.");
			}
		}
	}

	public String getRecipe() {
		return recipe;
	}
}
