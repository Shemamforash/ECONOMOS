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

	public HashMap<String, T> getResourceOfType(){
		return resources;
	}
	
	public String getType(){
		return type;
	}
}