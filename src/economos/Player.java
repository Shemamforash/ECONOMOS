package economos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Player extends User{
	private ArrayList<MerchantResource> userResources = new ArrayList<MerchantResource>();
	public ResourceMap<MerchantResource> resourceMap = new ResourceMap<MerchantResource>("Player");
	
	public Player(String name, String company){
		super(name, company);
		loadUserResources();
	}
	
	public ResourceMap<MerchantResource> getPlayerResourceMap() {
		return resourceMap;
	}
	
	public void loadUserResources(){
		ArrayList<ResourceType<MerchantResource>> rMap = new ArrayList<ResourceType<MerchantResource>>(resourceMap.getResourceTypes().values());
		for(ResourceType<MerchantResource> rType : rMap){
			ArrayList<MerchantResource> resources = new ArrayList<MerchantResource>(rType.getResourcesInType());
			for(int i = 0; i < resources.size(); ++i){
				userResources.add((MerchantResource)resources.get(i));
			}
		}
	}
}
