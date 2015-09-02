package economos;

import java.util.*;

class ResourceType<T extends Resource>{
	private HashMap<String, T> resources = new HashMap<String, T>();
	private String type;
	private String[] rarities = new String[] { "Commonplace", "Unusual", "Soughtafter", "Coveted", "Legendary" };
	
	public ResourceType(String type){
		this.type = type;
	}
	
	public void put(String name, T resource){
		resources.put(name, resource);
	}

	public T findResource(String name){
		if(resources.containsKey(name)){
			return resources.get(name);
		}
		return null;
	}
	
	public ArrayList<T> getResourcesInType(){
		ArrayList<T> vals = new ArrayList<T>(resources.values());
		ArrayList<T> tempArr = new ArrayList<T>();
		
		int current = 0, ctr = 0;
		while(!vals.isEmpty()){
			String resourceRarity = vals.get(current).getRarity();
			if(resourceRarity.equals(rarities[ctr])){
				tempArr.add(vals.remove(current));
			} else {
				++current;
			}
			if(current == vals.size()){
				current = 0;
				++ctr;
			}
		}
		
		return tempArr;
	}
	
	public String getType(){
		return type;
	}
}