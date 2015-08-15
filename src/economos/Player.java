package economos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Player extends User{
	private ArrayList<PlayerResource> userResources = new ArrayList<PlayerResource>();
	public ResourceMap<PlayerResource> resourceMap = new ResourceMap<PlayerResource>("Player");
	
	public Player(String name, String company){
		super(name, company);
		loadUserResources();
	}
	
	public ResourceMap<PlayerResource> getPlayerResourceMap() {
		return resourceMap;
	}
	
	public void loadUserResources(){
		ArrayList<ResourceType<PlayerResource>> rMap = new ArrayList<ResourceType<PlayerResource>>(resourceMap.getResourceTypes().values());
		for(ResourceType<PlayerResource> rType : rMap){
			ArrayList<PlayerResource> resources = new ArrayList<PlayerResource>(rType.getResourcesInType());
			for(int i = 0; i < resources.size(); ++i){
				userResources.add((PlayerResource)resources.get(i));
			}
		}
	}
}
