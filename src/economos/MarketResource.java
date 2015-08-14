package economos;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MarketResource extends Resource implements Comparable<MarketResource> {
	private ArrayList<Float> previousBuyPrices = new ArrayList<Float>();

	private int baseSupply, basePrice, ticks = 1;
	private int desiredThisTick, timeSinceEvent;
	private float maxPrice, minPrice, averagePrice, supply, demand;
	private float price, trend;
	float changeTrend = 1, changeTrendModifier = 0.1f; // between 0 and 1
	int timer = 0, timerMax; // timer to ensure non constant decrease
	int trendDirection = 1; // true is up/false is down
	private Random rnd = new Random();

	public MarketResource(String name, String description, String type, int baseSupplyRate) {
		super(name, description, type);
		this.baseSupply = rnd.nextInt(75) + 10;
		timerMax = rnd.nextInt(1000) + 500;
		supply = baseSupply;
		demand = baseSupply;
		desiredThisTick = baseSupply;
		quantity = rnd.nextInt(1000);
		basePrice = rnd.nextInt(1000);
		price = getPricePerUnit();
		previousBuyPrices.add(price);
		maxPrice = getPricePerUnit();
		minPrice = getPricePerUnit();
		averagePrice = price;
		Timer timer = new Timer();
		timer.schedule(new UpdateResource(this), 0, EconomosGUI.timeStep);
	}

	private float getSupplyChange() {
		if (timerMax < timer) {
			changeTrend = -changeTrend / (rnd.nextFloat() * 5f);
			timer = 0;
			timerMax = rnd.nextInt(400);
		}
		
		++timer;
		
		float temp = (float)(Math.pow(rnd.nextFloat(), 2)) * 0.2f - 0.1f;
		if(changeTrendModifier + temp < 0f || changeTrendModifier + temp > 2f){
			changeTrendModifier = changeTrendModifier - temp;
		} else {
			changeTrendModifier = changeTrendModifier + temp;
		}
		temp = changeTrendModifier;
		
		float supplyChange = 0.03f * baseSupply;
		
		if ((changeTrend > 0 && supply >= baseSupply * 20f) || (changeTrend < 0 && supply <= baseSupply * 0.2f)) {
			changeTrend = -changeTrend / (rnd.nextFloat() * 5f);
		} 
		
		supplyChange = supplyChange * changeTrend * temp / (rnd.nextInt(5) + 1);

		return supply + supplyChange;
	}

	public float getPricePerUnit() {
		if (supply < baseSupply * 0f && demand > baseSupply * 0.5f) {
			supply = baseSupply;
		}
		if (demand < supply && supply < baseSupply * 20) {
			supply = demand;
		} else {
			supply = getSupplyChange();
		}
		if (supply < 1) {
			supply = 1;
		}
		if (demand == 0) {
			demand = 1;
		}
		if (demand / supply * basePrice < 1) {
			return 1;
		}
		return (demand / supply * basePrice) / 100;
	}

	public void adjustMaxMinPrices() {
		float tempMin = previousBuyPrices.get(0), tempMax = previousBuyPrices.get(0);
		for (int i = 0; i < previousBuyPrices.size(); ++i) {
			if (previousBuyPrices.get(i) < tempMin) {
				tempMin = previousBuyPrices.get(i);
				continue;
			}
			if (previousBuyPrices.get(i) > tempMax) {
				tempMax = previousBuyPrices.get(i);
			}
		}

		minPrice = tempMin - tempMin * 0.1f;
		maxPrice = tempMax + tempMax * 0.1f;

		// minPrice = price - price;
		// maxPrice = price + price;
	}

	public void putPrice() {
		price = getPricePerUnit();
		quantity += supply;

		boolean recordData = false;
		if (ticks % 4 == 0) {
			recordData = true;
		}

		int totalStoredPrices = previousBuyPrices.size();
		float totalPrice = averagePrice * totalStoredPrices;

		if (previousBuyPrices.size() == 672) {
			totalPrice -= previousBuyPrices.get(0);
			if (recordData) {
				previousBuyPrices.remove(0);
			}
		}

		totalPrice += price;
		if (recordData) {
			averagePrice = totalPrice / totalStoredPrices;
			previousBuyPrices.add(price);
		}

		int dx = previousBuyPrices.size();
		float angle, dy;
		if (previousBuyPrices.size() < 200) {
			dy = previousBuyPrices.get(dx - 1) - previousBuyPrices.get(0);
			// trend = previousBuyPrices.get(previousBuyPrices.size() - 1) -
			// previousBuyPrices.get(0);
		} else {
			dy = previousBuyPrices.get(dx - 1) - previousBuyPrices.get(previousBuyPrices.size() - 200);
			// trend = previousBuyPrices.get(previousBuyPrices.size() - 1) -
			// previousBuyPrices.get(previousBuyPrices.size() - 30);
		}
		angle = (float) Math.toDegrees(Math.atan(dx / dy));
		trend = angle / 90;
		adjustMaxMinPrices();
	}

	public synchronized float getBuyPrice(int amount) {
		return price * amount;
	}

	public synchronized float getSellPrice(int quantity) {
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

	public synchronized void updateQuantity(int amount, float price) {
		quantity += amount;
	}

	class UpdateResource extends TimerTask {
		private MarketResource marketResource;

		public UpdateResource(MarketResource marketResource) {
			this.marketResource = marketResource;
		}

		public void run() {
			marketResource.putPrice();
			// if(getName().equals("Ambrosia")){
			// System.out.println("Demand " + demand + " desired " +
			// desiredThisTick + " tick " + ticks);
			// }
			float tempDemand = ((ticks - 1) * demand + desiredThisTick) / ticks;
			++ticks;
			if (tempDemand >= 0) {
				demand = tempDemand;
			}
			desiredThisTick = 0;
		}
	}

	public synchronized float getPriceDiff() {
		return maxPrice - minPrice;
	}

	public synchronized float getMinPrice() {
		return minPrice;
	}

	public synchronized float getMaxPrice() {
		return maxPrice;
	}

	public synchronized ArrayList<Float> getPrices() {
		return previousBuyPrices;
	}

	public synchronized int getDemand() {
		return (int) demand;
	}

	public synchronized int getSupply() {
		return (int) supply;
	}

	public synchronized float getTrend() {
		return trend;
	}

	public synchronized void setSupplyRate(int supplyRate) {
		this.supply += supplyRate;
	}

	public synchronized float getAveragePrice() {
		return averagePrice;
	}

	public synchronized void updateDesiredThisTick(int amount) {
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
