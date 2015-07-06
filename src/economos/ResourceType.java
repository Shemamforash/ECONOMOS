package economos;

import java.util.*;

class ResourceType<T extends Resource>{
	private HashMap<String, T> resources = new HashMap<String, T>();
	private String type;
	
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
		return new ArrayList<T>(resources.values());
	}
	
	public String getType(){
		return type;
	}
}