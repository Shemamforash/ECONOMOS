package economos;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MarketResource extends Resource{
	private ArrayList<Integer> previousPrices = new ArrayList<Integer>();
	private int baseSupplyRate, supplyRate;
	private int maxPrice, minPrice;
	
	public MarketResource(String name, String description, String type, int baseSupplyRate) {
		super(name, description, type);
		this.baseSupplyRate = 100;
		supplyRate = this.baseSupplyRate;
		quantity = new Random().nextInt(1000);
		Timer timer = new Timer();
		timer.schedule(new UpdateResource(this), 0, 100);
		maxPrice = getCurrentPrice() + 5;
		minPrice = maxPrice - 10;
	}
	
	public void putPrice(){
		previousPrices.add(getCurrentPrice());
		if(previousPrices.size() > GUIManager.getGraphWidth() / 10){
			previousPrices.remove(0);
		}
	}
	
	public int getCurrentPrice(){
		int price = 0;
//		supplyRate += new Random().nextInt(10) - 5;
		if(supplyRate > 0){
			price = (quantity * baseSupplyRate) / supplyRate;
		}
		if(supplyRate < 0){
			supplyRate = 0;
		}
		if(supplyRate > baseSupplyRate * 10){
			supplyRate = baseSupplyRate * 10;
		}
		
		if(price < minPrice){
			minPrice = price;
		}
		else if(price > maxPrice){
			maxPrice = price;
		}
		
		return price;
	}
	
	class UpdateResource extends TimerTask{	
		private MarketResource marketResource;
		
		public UpdateResource(MarketResource marketResource){
			this.marketResource = marketResource;
		}
		
		public void run(){
			marketResource.putPrice();
			GUIManager.repaintGraph();
		}
	}
	
	public int getPriceDiff(){
		return maxPrice - minPrice;
	}
	
	public int getMinPrice(){
		return minPrice;
	}
	
	public ArrayList<Integer> getPrices(){
		return previousPrices;
	}
	
	public void setSupplyRate(int supplyRate){
		this.supplyRate += supplyRate;
	}

	public void updateQuantity(int amount) {
		quantity += supplyRate;		
	}
}
