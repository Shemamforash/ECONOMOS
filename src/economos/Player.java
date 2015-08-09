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
		Timer t = new Timer();
		t.schedule(new UpdateUser(), 0, EconomosGUI.timeStep);
	}
	
	class UpdateUser extends TimerTask {
		public void run(){
			updateBots();
		}
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
	
	public void updateBots() {
		for (int i = 0; i < userResources.size(); ++i) {
			if (userResources.get(i).isBotActive()) {
				PlayerResource ur = userResources.get(i);
				int currentQ = ur.getQuantity();
				System.out.println(getMoney() + " | " + ur.getMarketResource().getBuyPrice(ur.getBotBuyQuantity())  + " | " +  ur.getMarketResource().getPricePerUnit()  + " | " + ur.getBotBuyPrice());
				if (currentQ >= ur.getBotSellQuantity() && ur.getMarketResource().getSellPrice(1) >= ur.getBotSellPrice()) {
					MarketController.sellResource(ur.getBotSellQuantity(), ur, this);
				} else if (getMoney() >= ur.getMarketResource().getBuyPrice(ur.getBotBuyQuantity()) && ur.getMarketResource().getPricePerUnit() <= ur.getBotBuyPrice()) {
					MarketController.buyResource(ur.getBotBuyQuantity(), ur, this);
				}
			}
		}
	}
}
