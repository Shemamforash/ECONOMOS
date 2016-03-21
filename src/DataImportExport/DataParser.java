package DataImportExport;

import java.io.*;
import java.util.*;

import CraftingResources.CraftingResource;
import MerchantResources.MarketResource;
import MerchantResources.MerchantResource;
import MerchantResources.Resource;

public class DataParser {
	private File								craftingResourceFile	= new File("CraftingResourceData.csv");
	private File								merchantResourceFile	= new File("MerchantResourceData.csv");
	private static ArrayList<String>			craftingTypes				= new ArrayList<String>();
	private static ArrayList<String>			merchantTypes				= new ArrayList<String>();
	private static ArrayList<MarketResource>	allMarketResources	= new ArrayList<MarketResource>();
	private static ArrayList<CraftingResource>	craftingResources	= new ArrayList<CraftingResource>();

	public void readMerchantData(BufferedReader reader) throws IOException{
		String[] rarities = new String[]{"Commonplace", "Unusual", "Soughtafter", "Coveted", "Legendary"};
		String next = " ";
		int counter = 0, currentRarity = 0;
		String currentType = null;
		while (true) {
			next = reader.readLine();
			if (next == null) {
				break;
			}
			String[] arr = next.split(",");
			if (arr[0].equals("TYPE")) {
				merchantTypes.add(arr[1]);
				currentType = arr[1];
				counter = 0;
				currentRarity = 0;
			} else {
				String name = arr[0];
				String id = arr[1];
				String description = arr[2];
				float price = Float.parseFloat(arr[3]);
				float supply = Float.parseFloat(arr[4]);
				allMarketResources.add(new MarketResource(id, name, currentType, rarities[currentRarity], description, supply, price));
				++counter;
				if(counter % 4 == 0){
					++currentRarity;
				}
			}
		}
	}
	
	public static Resource findResource(String id){
		for(CraftingResource r : craftingResources){
			if(r.getID().equals(id)){
				return r;
			}
		}
		for(MarketResource m : allMarketResources){
			if(m.getID().equals(id)){
				return m;
			}
		}
		return null;
	}
	
	public void readCraftingData(BufferedReader reader) throws IOException{
		String[] rarities = new String[]{"Dabbler", "Apprentice", "Journeyman", "Master", "Adept"};
		String next = " ";
		int counter = 0, currentRarity = 0;
		String currentType = null;
		ArrayList<CRTemp> temporaryCraftingResources = new ArrayList<CRTemp>();
		while (true) {
			next = reader.readLine();
			if (next == null) {
				break;
			}
			String[] arr = next.split(",");
			if (arr[0].equals("TYPE")) {
				craftingTypes.add(arr[1]);
				currentType = arr[1];
				currentRarity = 0;
				counter = 0;
			} else {
				String name = arr[0];
				String id = arr[1];
				String recipe = arr[2];
				int cost = Integer.parseInt(arr[3]);
				CraftingResource c = new CraftingResource(id, name, currentType, rarities[currentRarity], "", cost);
				craftingResources.add(c);
				temporaryCraftingResources.add(new CRTemp(recipe, c));
				++counter;
				if(counter % 8 == 0){
					++currentRarity;
				}
			}
		}
		for(CRTemp t : temporaryCraftingResources){
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

	public static ArrayList<MarketResource> getAllMarketResources() {
		return allMarketResources;
	}
	
	public static ArrayList<CraftingResource> getCraftingResources(){
		return craftingResources;
	}

	public static ArrayList<MerchantResource> getUserResources() {
		ArrayList<MerchantResource> userResources = new ArrayList<MerchantResource>();
		for(MarketResource r : allMarketResources){
			MerchantResource userResource = new MerchantResource(r);
			userResources.add(userResource);
		}
		return userResources;
	}
}
