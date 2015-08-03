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
		averageProfit = getPredictedProfit(amount, price);
		quantity += amount;
	}
	
	public float getPredictedProfit(int amount, float price){
		return averageProfit - price / (float)amount;
	}

	public MarketResource getMarketResource(){
		return marketResource;
	}
	
	public float getAverageProfit(){
		return averageProfit;
	}
	
	public int getSold(){
		return sold;
	}

}
