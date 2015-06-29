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

	public MarketResource getMarketResource(){
		return marketResource;
	}
}
