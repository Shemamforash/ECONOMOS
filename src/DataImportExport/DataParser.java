package DataImportExport;

import java.io.*;
import DataStructures.List;

import CraftingResources.CraftingResource;
import MarketSimulator.MarketResource;
import MerchantResources.MerchantResource;
import MerchantResources.Resource;

public class DataParser {
	private static List<String> craftingTypes = new List<String>();
	private static List<String> merchantTypes = new List<String>();
	private static List<String> merchantRarities = new List<String>();
	private static List<String> craftingRarities = new List<String>();
	private static List<MarketResource> marketResources = new List<MarketResource>();
	private static List<CraftingResource> craftingResources = new List<CraftingResource>();

	public void readMerchantData(BufferedReader reader) throws IOException {
		String next = " ";
		while (true) {
			next = reader.readLine();
			if (next == null) {
				break;
			}
			String[] arr = next.split(",");
			String name = arr[0];
			String type = arr[1];
			merchantTypes.addUnique(type);
			String rarity = arr[2];
			merchantRarities.addUnique(type);
			String id = arr[3];
			String description = arr[4];
			float price = Float.parseFloat(arr[5]);
			float supply = Float.parseFloat(arr[6]);
			marketResources.add(new MarketResource(id, name, type, rarity, description, supply, price));
		}
	}

	public static Resource findResource(String id) {
		for (CraftingResource r : craftingResources) {
			if (r.id().equals(id)) {
				return r;
			}
		}
		for (MarketResource m : marketResources) {
			if (m.id().equals(id)) {
				return m;
			}
		}
		return null;
	}

	public void readCraftingData(BufferedReader reader) throws IOException {
		String next = " ";
		List<CRTemp> temporaryCraftingResources = new List<CRTemp>();
		while (true) {
			next = reader.readLine();
			if (next == null) {
				break;
			}
			String[] arr = next.split(",");
			String name = arr[0];
			String type = arr[1];
			craftingTypes.addUnique(type);
			String rarity = arr[2];
			craftingRarities.addUnique(rarity);
			String id = arr[3];
			String recipe = arr[4];
			int cost = Integer.parseInt(arr[5]);
			CraftingResource c = new CraftingResource(name, id, type, rarity, "", cost);
			craftingResources.add(c);
			temporaryCraftingResources.add(new CRTemp(recipe, c));
		}
		temporaryCraftingResources.forEach((t) -> t.convertRecipe());
	}

	/**
	 * Read the resourcedata csv files from an input stream locating the files from the class loader
     */
	private void loadFile(String name) throws IOException {
		InputStream reader = getClass().getClassLoader().getResourceAsStream(name);
		readMerchantData(new BufferedReader(new InputStreamReader(reader)));
		reader.close();
	}

	/**
	 *
	 */
	public DataParser() throws IOException {
		InputStream reader = null;
		try {
			loadFile("MerchantResourceData.csv");
			loadFile("CraftingResourceData.csv");
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find resource data file.");
		}
	}

	public static List<String> merchantTypes() {
		return merchantTypes;
	}

	public static List<String> craftingTypes() {
		return craftingTypes;
	}

	public static List<String> merchantRarities() {
		return merchantRarities;
	}
	
	public static List<String> craftingRarities() {
		return craftingRarities;
	}
	
	public static List<MarketResource> marketResources() {
		return marketResources;
	}

	public static List<CraftingResource> craftingResources() {
		return craftingResources;
	}

	public static List<MerchantResource> userResources() {
		List<MerchantResource> userResources = new List<MerchantResource>();
		for (MarketResource r : marketResources) {
			MerchantResource userResource = new MerchantResource(r);
			userResources.add(userResource);
		}
		return userResources;
	}
}
