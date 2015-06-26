package economos;

public class UserResource extends Resource{
	private MarketResource marketResource;
	
	public UserResource(String name, String description, String type) {
		super(name, description, type);
		marketResource = MarketController.getMarketResources().getResourceTypes().get(type).getResourceOfType().get(name);
	}

	protected void updateQuantity(int amount) {
		quantity += amount;		
	}

	public void buyFromMarket(int amount){
		if(marketResource.getQuantity() >= amount){
			updateQuantity(amount);
			marketResource.updateQuantity(-amount);
		}
	}	
	
	public void sellToMarket(int amount){
		if(quantity >= amount){
			updateQuantity(-amount);
			marketResource.updateQuantity(amount);
		}
	}
	
	public MarketResource getMarketResource(){
		return marketResource;
	}
}
