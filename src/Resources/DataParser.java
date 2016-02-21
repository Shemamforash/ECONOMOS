package Resources;

import java.io.*;
import java.util.*;

public class DataParser {
	private File								craftingResourceFile	= new File("CraftingResourceData.csv");
	private File								merchantResourceFile	= new File("MerchantResourceData.csv");
	private static ArrayList<RawResourceData>	rawMerchantResourceData		= new ArrayList<RawResourceData>();
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
			} else {
				String name = arr[0];
				String id = arr[1];
				String description = arr[2];
				float price = Float.parseFloat(arr[3]);
				float supply = Float.parseFloat(arr[4]);
				rawMerchantResourceData.add(new RawResourceData(name, id, currentType, description, price, supply, rarities[currentRarity]));
				++counter;
				if(counter % 4 == 0){
					++currentRarity;
				}
			}
		}
	}
	
	public Resource findResourceWithID(String id){
		for(MarketResource m : allMarketResources){
			if(m.getID().equals(id)){
				return m;
			}
		}
		for(CraftingResource r : craftingResources){
			if(r.getID().equals(id)){
				return r;
			}
		}
		return null;
	}
	
	public void readCraftingData(BufferedReader reader) throws IOException{
		String[] rarities = new String[]{"Dabbler", "Apprentice", "Journeyman", "Master", "Adept"};
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
				craftingTypes.add(arr[1]);
				currentType = arr[1];
				counter = 0;
			} else {
				String name = arr[0];
				String id = arr[1];
				String recipe = arr[2];
				int cost = Integer.parseInt(arr[3]);
				craftingResources.add(new CraftingResource(name, id, currentType, rarities[currentRarity], recipe, cost));
				++counter;
				if(counter % 8 == 0){
					++currentRarity;
				}
			}
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

	public static ArrayList<Resource> getResourceData(String owner) {
		ArrayList<Resource> arr = new ArrayList<Resource>();

		for (int i = 0; i < rawMerchantResourceData.size(); ++i) {
			Resource r = null;
			RawResourceData rawDatum = rawMerchantResourceData.get(i);

			if (owner.equals("Player") || owner.equals("AI")) {
				r = new MerchantResource(rawDatum.name(), rawDatum.id(), rawDatum.description(), rawDatum.type(), rawDatum.rarity());
			} else if (owner.equals("Market")) {
				r = new MarketResource(rawDatum.name(), rawDatum.id(), rawDatum.description(), rawDatum.type(), rawDatum.rarity(), 0);
				allMarketResources.add((MarketResource) r);
			}
			if (r != null) {
				arr.add(r);
			}
		}

		return arr;
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

	class RawResourceData {
		private String	name, description, type, rarity, id;
		private float price, supply;

		public RawResourceData(String name, String id, String type, String description, float price, float supply, String rarity) {
			this.name = name;
			this.id = id;
			this.type = type;
			this.description = description;
			this.price = price;
			this.price = supply;
			this.rarity = rarity;
		}

		public String name() {
			return name;
		}

		public String id(){
			return id;
		}
		
		public String type() {
			return type;
		}
		
		public String description() {
			return description;
		}
		
		public float price(){
			return price;
		}
		
		public float supply(){
			return supply;
		}
		
		public String rarity() {
			return rarity;
		}	
	}
}
