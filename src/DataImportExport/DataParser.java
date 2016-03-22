package DataImportExport;

import java.io.*;
import java.util.*;

import CraftingResources.CraftingResource;
import MerchantResources.MarketResource;
import MerchantResources.MerchantResource;
import MerchantResources.Resource;

public class DataParser {
	private File craftingResourceFile = new File("CraftingResourceData.csv");
	private File merchantResourceFile = new File("MerchantResourceData.csv");
	private static ArrayList<String> craftingTypes = new ArrayList<String>();
	private static ArrayList<String> merchantTypes = new ArrayList<String>();
	private static ArrayList<String> merchantRarities = new ArrayList<String>();
	private static ArrayList<String> craftingRarities = new ArrayList<String>();
	private static ArrayList<MarketResource> allMarketResources = new ArrayList<MarketResource>();
	private static ArrayList<CraftingResource> craftingResources = new ArrayList<CraftingResource>();

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
			if(!merchantTypes.contains(type)){
				merchantTypes.add(type);
			}
			String rarity = arr[2];
			if(!merchantRarities.contains(rarity)){
				merchantRarities.add(rarity);
			}
			String id = arr[3];
			String description = arr[4];
			float price = Float.parseFloat(arr[5]);
			float supply = Float.parseFloat(arr[6]);
			allMarketResources.add(
					new MarketResource(id, name, type, rarity, description, supply, price));
		}
	}

	public static Resource findResource(String id) {
		for (CraftingResource r : craftingResources) {
			if (r.getID().equals(id)) {
				return r;
			}
		}
		for (MarketResource m : allMarketResources) {
			if (m.getID().equals(id)) {
				return m;
			}
		}
		return null;
	}

	public void readCraftingData(BufferedReader reader) throws IOException {
		String next = " ";
		ArrayList<CRTemp> temporaryCraftingResources = new ArrayList<CRTemp>();
		while (true) {
			next = reader.readLine();
			if (next == null) {
				break;
			}
			String[] arr = next.split(",");
			String name = arr[0];
			String type = arr[1];
			if(!craftingTypes.contains(type)){
				craftingTypes.add(type);
			}
			String rarity = arr[2];
			if(!craftingRarities.contains(rarity)){
				craftingRarities.add(rarity);
			}
			String id = arr[3];
			String recipe = arr[4];
			int cost = Integer.parseInt(arr[5]);
			CraftingResource c = new CraftingResource(name, id, type, rarity, "", cost);
			craftingResources.add(c);
			temporaryCraftingResources.add(new CRTemp(recipe, c));
		}
		for (CRTemp t : temporaryCraftingResources) {
			t.convertRecipe();
		}
	}

	public DataParser() throws IOException {
		FileReader reader = null;
		try {
			reader = new FileReader(merchantResourceFile);
			readMerchantData(new BufferedReader(reader));
			reader.close();
			reader = new FileReader(craftingResourceFile);
			readCraftingData(new BufferedReader(reader));
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find resource data file.");
		}
	}

	public static ArrayList<String> getMerchantTypes() {
		return merchantTypes;
	}

	public static ArrayList<String> getCraftingTypes() {
		return craftingTypes;
	}

	public static ArrayList<String> getMerchantRarities() {
		return merchantRarities;
	}
	
	public static ArrayList<String> getCraftingRarities() {
		return craftingRarities;
	}
	
	public static ArrayList<MarketResource> getAllMarketResources() {
		return allMarketResources;
	}

	public static ArrayList<CraftingResource> getCraftingResources() {
		return craftingResources;
	}

	public static ArrayList<MerchantResource> getUserResources() {
		ArrayList<MerchantResource> userResources = new ArrayList<MerchantResource>();
		for (MarketResource r : allMarketResources) {
			MerchantResource userResource = new MerchantResource(r);
			userResources.add(userResource);
		}
		return userResources;
	}
}
