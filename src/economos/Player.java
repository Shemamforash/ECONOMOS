package economos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import Resources.MerchantResource;
import Resources.ResourceMap;

public class Player extends User{
	private ArrayList<MerchantResource> userResources = new ArrayList<MerchantResource>();
	public ResourceMap<MerchantResource> resourceMap = new ResourceMap<MerchantResource>("Player", "Merchant");
	
	public Player(String name, String company){
		super(name, company);
		loadUserResources();
	}
	
	public ResourceMap<MerchantResource> getPlayerResourceMap() {
		return resourceMap;
	}
	
	public void loadUserResources(){
		ArrayList<MerchantResource> resources = resourceMap.getResourcesInType(null);
		for(MerchantResource r : resources){
			userResources.add(r);
		}
	}
}
