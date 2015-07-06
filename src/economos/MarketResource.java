package economos;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MarketResource extends Resource implements Comparable<MarketResource> {
	private ArrayList<Float> previousBuyPrices = new ArrayList<Float>();
	private ArrayList<Integer> previousSupply = new ArrayList<Integer>();

	private int baseSupply, supply, demand, basePrice, ticks = 1;
	private int boughtThisTick, soldThisTick;
	private float maxPrice, minPrice, averagePrice;
	private float price, trend;

	public MarketResource(String name, String description, String type, int baseSupplyRate) {
		super(name, description, type);
		this.baseSupply = new Random().nextInt(75) + 25;
		supply = this.baseSupply;
		quantity = new Random().nextInt(1000);
		basePrice = new Random().nextInt(1000);
		demand = new Random().nextInt(200);
		price = getPricePerUnit();
		previousBuyPrices.add(price);
		maxPrice = getPricePerUnit() + 5;
		minPrice = getPricePerUnit() - 5;
		averagePrice = price;
		Timer timer = new Timer();
		timer.schedule(new UpdateResource(this), 0, EconomosMain.timeStep);
	}

	public int getQuantity() {
		return quantity;
	}

	public float getAveragePrice() {
		return averagePrice;
	}

	public float getPricePerUnit() {
		float supplyDemandModifier;
		if (demand < supply) {
			supplyDemandModifier = demand / supply;
		} else {
			supplyDemandModifier = 0;
		}
		float supplyBaseModifier;
		if (baseSupply > supply) {
			supplyBaseModifier = 1 - (supply / baseSupply);
		} else {
			supplyBaseModifier = 0;
		}
		float surplusModifier;
		if (quantity > 0 && quantity < 1000000000f) {
			surplusModifier = 1f - (float) Math.log10(quantity) / 10f;
		} else if (quantity > 0) {
			surplusModifier = 0.01f;
		} else {
			surplusModifier = 1;
		}
		if (supplyDemandModifier == 0 && supplyBaseModifier == 0) {
			return surplusModifier * basePrice;
		}
		return (supplyDemandModifier + supplyBaseModifier) * surplusModifier * basePrice;
	}

	public void adjustMaxMinPrices() {
		if (price < minPrice) {
			minPrice = price;
		} else if (price > maxPrice) {
			maxPrice = price;
		}
	}

	public void putPrice() {
		price = getPricePerUnit();
		quantity += supply;
		averagePrice = ((averagePrice * previousBuyPrices.size()) - previousBuyPrices.get(0) + price) / previousBuyPrices.size();
		previousBuyPrices.add(price);
		if (previousBuyPrices.size() > 672) {
			previousBuyPrices.remove(0);
		}
		int supplyChange = new Random().nextInt(10) - 5;
		if(supply + supplyChange > baseSupply * 1.1f){
			supply = (int)(baseSupply * 1.1f);
		} else if (supply + supplyChange < baseSupply * 0.9f){
			supply = (int)(baseSupply * 0.9f);
		} else {
			supply = supply + supplyChange;
		}
		if(previousBuyPrices.size() < 10){
			trend = previousBuyPrices.get(previousBuyPrices.size() - 1) - previousBuyPrices.get(0);
		} else {
			trend = previousBuyPrices.get(previousBuyPrices.size() - 1) - previousBuyPrices.get(previousBuyPrices.size() - 10);
		}
		adjustMaxMinPrices();
	}

	public float getBuyPrice(int amount) {
		return price * amount;
	}

	public float getSellPrice(int quantity) {
		if (quantity <= 0) {
			return -1;
		}
		if (quantity < 100) {
			return (price * quantity * 0.90f);
		} else if (quantity < 1000) {
			return ((price * 90) + (price * (quantity - 100) * 0.95f));
		} else {
			return ((price * 90) + (price * 855) + (price * (quantity - 1000) * 0.98f));
		}
	}

	class UpdateResource extends TimerTask {
		private MarketResource marketResource;

		public UpdateResource(MarketResource marketResource) {
			this.marketResource = marketResource;
		}

		public void run() {
			marketResource.putPrice();
			++ticks;
			demand = (((ticks - 1) * demand) + (soldThisTick)) / ticks;
			soldThisTick = 0;
		}
	}

	public float getPriceDiff() {
		return maxPrice - minPrice;
	}

	public float getMinPrice() {
		return minPrice;
	}

	public float getMaxPrice() {
		return maxPrice;
	}

	public ArrayList<Float> getPrices() {
		return previousBuyPrices;
	}

	public int getDemand() {
		return demand;
	}

	public int getSupply() {
		return supply;
	}

	public float getTrend(){
		return trend;
	}
	
	public void setSupplyRate(int supplyRate) {
		this.supply += supplyRate;
	}

	public void updateQuantity(int amount, float price) {
		synchronized(this){
			if (amount > 0) {
				boughtThisTick += amount;
			} else {
				soldThisTick += Math.abs(amount);
			}
			quantity += amount;
		}
	}

	public int compareTo(MarketResource arg0) {
		if (this.price < arg0.price) {
			return -1;
		} else if (this.price > arg0.price) {
			return 1;
		}
		return 0;
	}
}
