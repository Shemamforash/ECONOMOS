package Resources;

public class MerchantResource extends Resource {
	private MarketResource marketResource;
	private int bought;
	private float averageSell, averageBuy, averageProfit;
	private int sold;
	private int quantity;
	
	public MerchantResource(String name, String id, String description, String type, String rarity) {
		super(name, id, description, type, rarity);
		marketResource = MarketController.getMarketResources().getResource(name);
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
	
	public int getQuantity(){
		return quantity;
	}
}