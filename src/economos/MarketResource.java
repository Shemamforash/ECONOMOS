package economos;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import economos.Main.UpdateListener;

public class MarketResource extends Resource implements Comparable<MarketResource> {
	private ArrayList<MarketSnapshot>	marketHistory	= new ArrayList<MarketSnapshot>();

	private int							baseSupply, basePrice, ticks = 1;
	private int							desiredThisTick, timeSinceEvent;
	private float						maxPrice, minPrice, maxDemand, minDemand, maxSupply, minSupply, averagePrice, supply, demand;
	private float						price, trend;
	float								changeTrend		= 1, changeTrendModifier = 0f;													// between 0 and 1
	int									timer			= 0, timerMax;																	// timer to ensure non constant decrease
	int									trendDirection	= 1;																			// true is up/false is down
	private Random						rnd				= new Random();

	public MarketResource(String name, String description, String type, String rarity, int baseSupplyRate) {
		super(name, description, type, rarity);
		this.baseSupply = rnd.nextInt(75) + 10;
		timerMax = rnd.nextInt(1000) + 500;
		supply = baseSupply;
		demand = baseSupply;
		desiredThisTick = baseSupply;
		quantity = rnd.nextInt(1000);
		basePrice = rnd.nextInt(1000);
		price = getPricePerUnit();
		marketHistory.add(new MarketSnapshot(supply, demand, price));
		maxPrice = getPricePerUnit();
		minPrice = getPricePerUnit();
		averagePrice = price;
		Main.addUpdateListener(new MarketResourceListener());
	}

	private class MarketResourceListener implements UpdateListener {
		public void receiveUpdate() {
			updateResource();
		}
	}

	private void updateResource() {
		putPrice();
		float tempDemand = ((ticks - 1) * demand + desiredThisTick) / ticks;
		++ticks;
		if (tempDemand >= 0) {
			demand = tempDemand;
		}
		desiredThisTick = 0;
	}

	private float getSupplyChange() {
		if (timerMax < timer) {
			changeTrend = -changeTrend;
			timer = 0;
			timerMax = rnd.nextInt(400);
		}

		++timer;

		float temp = (float) (Math.pow(rnd.nextFloat(), 2)) * 2f - 1f;
		if (changeTrendModifier + temp < -1f || changeTrendModifier + temp > 1f) {
			changeTrendModifier = changeTrendModifier - temp;
		} else {
			changeTrendModifier = changeTrendModifier + temp;
		}

		float supplyChange = 0.03f * baseSupply;

		if ((changeTrend * changeTrendModifier > 0 && supply >= baseSupply * 20f) || (changeTrend * changeTrendModifier < 0 && supply <= baseSupply * 0.2f)) {
			changeTrend = -changeTrend;
		}

		changeTrendModifier = changeTrend * changeTrendModifier;

		supplyChange = supplyChange * changeTrendModifier;

		return supply + supplyChange;
	}

	public float getPricePerUnit() {
		if (supply < baseSupply * 0.1f && demand > baseSupply * 0.5f) {
			supply = baseSupply * 0.1f;
		}
		if (demand < supply && supply < baseSupply * 20) {
			supply = demand;
		} else {
			supply = getSupplyChange();
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
		minPrice = marketHistory.get(0).getPrice();
		maxPrice = minPrice;
		minSupply = marketHistory.get(0).getSupply();
		maxSupply = minSupply;
		minDemand = marketHistory.get(0).getDemand();
		maxDemand = minDemand;

		for (int i = 0; i < marketHistory.size(); ++i) {
			float tempPrice = marketHistory.get(i).getPrice();
			float tempSupply = marketHistory.get(i).getSupply();
			float tempDemand = marketHistory.get(i).getDemand();

			if (tempPrice < minPrice) {
				minPrice = tempPrice;
			} else if (tempPrice > maxPrice) {
				maxPrice = tempPrice;
			}

			if (tempDemand < minDemand) {
				minDemand = tempDemand;
			} else if (tempDemand > maxDemand) {
				maxDemand = tempDemand;
			}

			if (tempSupply < minSupply) {
				minSupply = tempSupply;
			} else if (tempSupply > maxSupply) {
				maxSupply = tempSupply;
			}
		}

		minPrice = minPrice - minPrice * 0.1f;
		maxPrice = maxPrice + maxPrice * 0.1f;

		minDemand = minDemand - minDemand * 0.1f;
		maxDemand = maxDemand + maxDemand * 0.1f;

		minSupply = minSupply - minSupply * 0.1f;
		maxSupply = maxSupply + maxSupply * 0.1f;
	}

	public void putPrice() {
		price = getPricePerUnit();
		quantity += supply;

		boolean recordData = false;
		if (ticks % 4 == 0) {
			recordData = true;
		}

		int totalStoredPrices = marketHistory.size();
		float totalPrice = averagePrice * totalStoredPrices;

		if (marketHistory.size() == 4000) {
			totalPrice -= marketHistory.get(0).getPrice();
			if (recordData) {
				marketHistory.remove(0);
			}
		}

		totalPrice += price;
		if (recordData) {
			averagePrice = totalPrice / totalStoredPrices;
			marketHistory.add(new MarketSnapshot(supply, demand, price));
		}

		int dx = marketHistory.size();
		float angle, dy;
		if (dx < 200) {
			dy = marketHistory.get(dx - 1).getPrice() - marketHistory.get(0).getPrice();
		} else {
			dy = marketHistory.get(dx - 1).getPrice() - marketHistory.get(marketHistory.size() - 200).getPrice();
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

	public synchronized float getPriceDiff() {
		return maxPrice - minPrice;
	}

	public synchronized float getMinPrice() {
		return minPrice;
	}

	public synchronized float getMaxPrice() {
		return maxPrice;
	}

	public synchronized float getMaxSupply() {
		return maxSupply;
	}

	public synchronized float getMinSupply() {
		return minSupply;
	}

	public synchronized float getMaxDemand() {
		return maxDemand;
	}

	public synchronized float getMinDemand() {
		return minDemand;
	}

	public synchronized ArrayList<MarketSnapshot> getMarketHistory(int width) {
		int startPoint = marketHistory.size() - width;
		if (startPoint < 0) {
			startPoint = 0;
		}
		ArrayList<MarketSnapshot> arr = new ArrayList<MarketSnapshot>();
		for (int i = startPoint; i < marketHistory.size(); ++i) {
			arr.add(marketHistory.get(i));
		}
		return arr;
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

	public class MarketSnapshot {
		private float	supply, demand, price;

		public MarketSnapshot(float supply, float demand, float price) {
			this.supply = supply;
			this.demand = demand;
			this.price = price;
		}

		public float getSupply() {
			return supply;
		}

		public float getDemand() {
			return demand;
		}

		public float getPrice() {
			return price;
		}
	}
}
