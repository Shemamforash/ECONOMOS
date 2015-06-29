package economos;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MarketResource extends Resource{
	private ArrayList<Float> previousBuyPrices = new ArrayList<Float>();
	private ArrayList<Integer> previousSupply = new ArrayList<Integer>();

	private int baseSupply, supply, demand, basePrice;
	private int boughtThisTick, soldThisTick;
	private float maxPrice, minPrice;
	private float price;
	
	public MarketResource(String name, String description, String type, int baseSupplyRate) {
		super(name, description, type);
		this.baseSupply = 100;
		supply = this.baseSupply;
		quantity = new Random().nextInt(1000);
		basePrice = 100;
		demand = 250;
		Timer timer = new Timer();
		timer.schedule(new UpdateResource(this), 0, 1000);
		price = getPricePerUnit();
		previousBuyPrices.add(price);
		maxPrice = getPricePerUnit() + 5;
		minPrice = getPricePerUnit() - 5;
	}
	
	public int getQuantity(){
		return quantity;
	}
	
	public float getPricePerUnit(){
		float supplyDemandModifier;
		if(demand < supply){
			supplyDemandModifier =  demand/supply;
		} else {
			supplyDemandModifier = 0;
		}
		float supplyBaseModifier;
		if(baseSupply > supply){
			supplyBaseModifier = 1 - (supply/baseSupply);
		} else {
			supplyBaseModifier = 0;
		}
		float surplusModifier;
		if(quantity > 0 && quantity < 1000000000f){
			surplusModifier = 1f - (float)Math.log10(quantity) / 10f;
		} else if (quantity > 0){
			surplusModifier = 0.01f;
		} else {
			surplusModifier = 1;
		}
		if(supplyDemandModifier == 0 && supplyBaseModifier == 0){
			return surplusModifier * basePrice;
		}
		return (supplyDemandModifier + supplyBaseModifier) * surplusModifier * basePrice;
	}
	
	public void adjustMaxMinPrices(){
		if(price < minPrice){
			minPrice = price;
		}
		else if(price > maxPrice){
			maxPrice = price;
		}
	}
	
	public void putPrice(){		
		price = getPricePerUnit(); 
		if(getName().equals("Ambrosia")){
			System.out.println(price);
		}
		quantity += supply;
		previousBuyPrices.add(price);
		if(previousBuyPrices.size() > 672){
			previousBuyPrices.remove(0);
		}
		adjustMaxMinPrices();
	}
	
	public float getBuyPrice(int quantity){
		return price * quantity;
	}
	
	public float getSellPrice(int quantity){
		if(quantity <= 0){
			return -1;
		}
		if(quantity < 100){
			return (price * quantity * 1.1f);
		} else if (quantity < 1000){
			return ((price * 110) + (price * (quantity - 100) * 1.05f));
		} else {
			return ((price * 110) + (price * 945) + (price * (quantity - 1000) * 1.02f));
		}
	}
	
	class UpdateResource extends TimerTask{	
		private MarketResource marketResource;
		
		public UpdateResource(MarketResource marketResource){
			this.marketResource = marketResource;
		}
		
		public void run(){
			marketResource.putPrice();
			demand = soldThisTick;
			soldThisTick = 0;
		}
	}
	
	public float getPriceDiff(){
		return maxPrice - minPrice;
	}
	
	public float getMinPrice(){
		return minPrice;
	}
	
	public float getMaxPrice(){
		return maxPrice;
	}

	
	public ArrayList<Float> getPrices(){
		return previousBuyPrices;
	}
	
	public void setSupplyRate(int supplyRate){
		this.supply += supplyRate;
	}

	public void updateQuantity(int amount) {
		if(amount > 0){
			boughtThisTick += amount;
		} else {
			soldThisTick += Math.abs(amount);
		}
		quantity += amount;		
	}
}
