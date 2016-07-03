package MerchantResources;

import java.util.*;

import DataImportExport.DataParser;

public class ResourceMap<T extends Resource> {
	private HashMap<String, T>	resourceMap	= new HashMap<String, T>();
	private ArrayList<String>	types;

	// private String[] rarities = new String[] { "Commonplace", "Unusual", "Soughtafter", "Coveted", "Legendary" };

	@SuppressWarnings("unchecked")
	public ResourceMap(boolean isCraftingMap) {
		ArrayList<T> resources;
		if (isCraftingMap) {
			types = DataParser.craftingTypes();
			resources = (ArrayList<T>) DataParser.craftingResources();
		} else {
			types = DataParser.merchantTypes();
			resources = (ArrayList<T>) DataParser.marketResources();
		}

		for (int j = 0; j < resources.size(); ++j) {
			resourceMap.put(resources.get(j).name(), (T) resources.get(j));
		}
	}

	public ArrayList<String> getResourceTypes() {
		return types;
	}

	public T resource(String name) {
		if (resourceMap.containsKey(name)) {
			return resourceMap.get(name);
		}
		return null;
	}
	
	public ArrayList<T> resources(){
		return new ArrayList<T>(resourceMap.values());
	}
}
