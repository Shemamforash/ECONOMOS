package economos;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MarketResource extends Resource implements Comparable<MarketResource> {
	private ArrayList<Float> previousBuyPrices = new ArrayList<Float>();

	private int baseSupply, supply, demand, basePrice, ticks = 1;
	private int desiredThisTick, timeSinceEvent;
	private float maxPrice, minPrice, averagePrice;
	private float price, trend;

	public MarketResource(String name, String description, String type, int baseSupplyRate) {
		super(name, description, type);
		this.baseSupply = new Random().nextInt(75) + 25;
		supply = this.baseSupply;
		quantity = new Random().nextInt(1000);
		basePrice = new Random().nextInt(1000);
		demand = supply;
		price = getPricePerUnit();
		previousBuyPrices.add(price);
		maxPrice = getPricePerUnit() + 5;
		minPrice = getPricePerUnit() - 5;
		averagePrice = price;
		Timer timer = new Timer();
		timer.schedule(new UpdateResource(this), 0, EconomosMain.timeStep);
	}

	public float getPricePerUnit() {
		if (demand < supply) {
			supply = demand;
		} else if (demand > supply && supply > 0 && new Random().nextInt(20) == 0) {
			int supplyChange = new Random().nextInt(demand - supply + (int) (0.75f * supply));
			supplyChange = supplyChange - (int) (0.75f * supply);
			supply = supply + supplyChange;
		}
		if (supply < 1) {
			supply = 1;
		}
		if (demand / supply * basePrice < 1) {
			return 1;
		}
		return demand / supply * basePrice;
	}

	public void adjustMaxMinPrices() {
		float tempMin = previousBuyPrices.get(0), tempMax = previousBuyPrices.get(0);
		for(float i : previousBuyPrices){
			if(i < tempMin) {
				tempMin = i;
			}
			if(i > tempMax){
				tempMax = i;
			}
		}
		minPrice = tempMin - 10;
		maxPrice = tempMax + 10;
	}

	public void putPrice() {
		price = getPricePerUnit();
		quantity += supply;
		previousBuyPrices.add(price);
		if (previousBuyPrices.size() > 672) {
			averagePrice = (averagePrice + price - previousBuyPrices.get(0)) / previousBuyPrices.size();
			previousBuyPrices.remove(0);
		} else { 
			averagePrice = (averagePrice + price) / previousBuyPrices.size();
		}
		
		int dx = previousBuyPrices.size();
		float angle, dy;
		if (previousBuyPrices.size() < 30) {
			dy = previousBuyPrices.get(dx - 1) - previousBuyPrices.get(0);
//			trend = previousBuyPrices.get(previousBuyPrices.size() - 1) - previousBuyPrices.get(0);
		} else {
			dy = previousBuyPrices.get(dx - 1) - previousBuyPrices.get(previousBuyPrices.size() - 30);
//			trend = previousBuyPrices.get(previousBuyPrices.size() - 1) - previousBuyPrices.get(previousBuyPrices.size() - 30);
		}
		angle = (float) Math.toDegrees(Math.atan(dx/dy));
		trend = angle / 90;
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

	public void updateQuantity(int amount, float price) {
		synchronized (this) {
			quantity += amount;
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
			// if(getName().equals("Ambrosia")){
			// System.out.println("Demand " + demand + " desired " +
			// desiredThisTick + " tick " + ticks);
			// }
			demand = (((ticks - 1) * demand) + (desiredThisTick)) / ticks;
			desiredThisTick = 0;
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

	public float getTrend() {
		return trend;
	}

	public void setSupplyRate(int supplyRate) {
		this.supply += supplyRate;
	}

	public float getAveragePrice() {
		return averagePrice;
	}

	public void updateDesiredThisTick(int amount) {
		desiredThisTick += amount;
	}

	public int compareTo(MarketResource arg0) {
		if (this.price < arg0.price) {
			return -1;
		} else if (this.price > arg0.price) {
			return 1;
		}
		return 0;
	}

	public int getTmeSinceEvent() {
		++timeSinceEvent;
		return timeSinceEvent;
	}
}
