package economos;

import java.util.*;

public class ResourceMap<T extends Resource> {
	private HashMap<String, ResourceType<T>> resourceTypes = new HashMap<String, ResourceType<T>>();
	
	public ResourceMap(String owner){
		ArrayList<Resource> resources = DataParser.getResourceData(owner);		
		ArrayList<String> types = DataParser.getTypes();
		
		for(int i = 0; i < types.size(); ++i){
			ResourceType<T> resourcesInType = new ResourceType<T>(types.get(i));
			
			for(int j = 0; j < resources.size(); ++j){
				if(resources.get(j).getType().equals(types.get(i))){
					resourcesInType.put(resources.get(j).getName(), (T)resources.get(j));
				}
			}
			
			resourceTypes.put(types.get(i), resourcesInType);
		}
	}
	
	public HashMap<String, ResourceType<T>> getResourceTypes(){
		return resourceTypes;
	}
}
