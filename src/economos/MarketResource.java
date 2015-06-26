package economos;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MarketResource extends Resource{
	private ArrayList<Integer> previousBuyPrices = new ArrayList<Integer>();
	private ArrayList<Integer> previousSellPrices = new ArrayList<Integer>();

	private int baseSupplyRate, supply, demand, baseCost;
	private int maxSell, minSell, maxBuy, minBuy;
	private int buyPrice, sellPrice;
	
	public MarketResource(String name, String description, String type, int baseSupplyRate) {
		super(name, description, type);
		this.baseSupplyRate = 100;
		supply = this.baseSupplyRate;
		quantity = new Random().nextInt(1000);
		baseCost = 100;
		demand = 250;
		Timer timer = new Timer();
		timer.schedule(new UpdateResource(this), 0, 1000);
		sellPrice = (demand / supply) * baseCost;
		buyPrice = (quantity * baseSupplyRate) / supply;
		maxBuy = getBuyPrice() + 5;
		minBuy = getBuyPrice() - 10;
		maxSell = getSellPrice() + 5;
		minSell = getSellPrice() - 10;
	}
	
	public void putPrice(){
		sellPrice = (demand / supply) * baseCost;
		if(supply > 0){
			buyPrice = (quantity * baseSupplyRate) / supply;
		} else {
			supply = 0;
			buyPrice = quantity * baseSupplyRate;
		}
				
		if(buyPrice < minBuy){
			minBuy = buyPrice;
		}
		else if(buyPrice > maxBuy){
			maxBuy = buyPrice;
		}
		if(sellPrice < minSell){
			minSell = sellPrice;
		}
		else if(sellPrice > maxSell){
			maxSell = sellPrice;
		}
		
		previousBuyPrices.add(buyPrice);
		if(previousBuyPrices.size() > 672){
			previousBuyPrices.remove(0);
		}
		
		previousSellPrices.add(sellPrice);
		if(previousSellPrices.size() > 672){
			previousSellPrices.remove(0);
		}
	}
	
	public int getBuyPrice(){
		return buyPrice;
	}
	
	public int getSellPrice(){
		return sellPrice;
	}
	
	class UpdateResource extends TimerTask{	
		private MarketResource marketResource;
		
		public UpdateResource(MarketResource marketResource){
			this.marketResource = marketResource;
		}
		
		public void run(){
			marketResource.putPrice();
		}
	}
	
	public float getBuyDiff(){
		return maxBuy - minBuy;
	}
	
	public float getSellDiff() {
		return maxSell - minSell;
	}
	
	public int getMinBuy(){
		return minBuy;
	}
	
	public int getMaxBuy(){
		return maxBuy;
	}
	
	public int getMinSell(){
		return minSell;
	}
	
	public int getMaxSell(){
		return maxSell;
	}
	
	public ArrayList<Integer> getBuyPrices(){
		return previousBuyPrices;
	}
	
	public ArrayList<Integer> getSellPrices(){
		return previousSellPrices;
	}
	
	public void setSupplyRate(int supplyRate){
		this.supply += supplyRate;
	}

	public void updateQuantity(int amount) {
		quantity += supply;		
	}
}
