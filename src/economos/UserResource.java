package economos;

public abstract class UserResource extends Resource{
	private MarketResource marketResource;
	protected int sold;
	private int bought;
	private float averageSell, averageBuy, averageProfit;
	
	public UserResource(String name, String description, String type) {
		super(name, description, type);
		marketResource = MarketController.getMarketResources().getResource(type, name);
	}

	protected void updateQuantity(int amount, float price) { 
		if(amount < 0){
			averageSell = ((averageSell * sold) + (price * -amount)) / (sold - amount);
			sold -= amount;
		} else {
			averageBuy = ((averageBuy * bought) + (price * amount)) / (bought + amount);
			bought += amount;
		}
		quantity += amount;
	}

	public MarketResource getMarketResource(){
		return marketResource;
	}
	
	public float getAverageProfit(){
		if(sold == 0){
			return 0;
		}
		averageProfit = averageSell - averageBuy;
		return averageProfit;
	}
	
	public int getSold(){
		return sold;
	}

}
