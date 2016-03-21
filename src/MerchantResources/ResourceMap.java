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
			types = DataParser.getCraftingTypes();
			resources = (ArrayList<T>) DataParser.getCraftingResources();
		} else {
			types = DataParser.getMerchantTypes();
			resources = (ArrayList<T>) DataParser.getAllMarketResources();
		}

		for (int j = 0; j < resources.size(); ++j) {
			resourceMap.put(resources.get(j).getName(), (T) resources.get(j));
		}
	}

	public ArrayList<String> getResourceTypes() {
		return types;
	}

	public T getResource(String name) {
		if (resourceMap.containsKey(name)) {
			return resourceMap.get(name);
		}
		return null;
	}
	
	public ArrayList<T> getResources(){
		return new ArrayList<T>(resourceMap.values());
	}

//	public ArrayList<T> getResourcesInType(String type) {
//		ArrayList<T> vals = new ArrayList<T>(resourceMap.values());
//		ArrayList<T> tempArr = new ArrayList<T>();
//
//		if (type == null) {
//			return vals;
//		} else {
//			for (T r : vals) {
//				if (r.getType().equals(type)) {
//					tempArr.add(r);
//				}
//			}
//		}
//
//		// int current = 0, ctr = 0;
//		// while (!vals.isEmpty()) {
//		// String resourceRarity = vals.get(current).getRarity();
//		// if (resourceRarity.equals(rarities[ctr])) {
//		// tempArr.add(vals.remove(current));
//		// } else {
//		// ++current;
//		// }
//		// if (current == vals.size()) {
//		// current = 0;
//		// ++ctr;
//		// }
//		// }
//
//		return tempArr;
//	}
}