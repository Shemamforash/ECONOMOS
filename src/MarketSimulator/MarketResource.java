package MarketSimulator;

import java.util.ArrayList;

import MerchantResources.Resource;
import economos.UpdateCaller;
import economos.UpdateListener;

public class MarketResource extends Resource implements Comparable<MarketResource> {
	private ArrayList<MarketSnapshot>	marketHistory	= new ArrayList<MarketSnapshot>();

	private float						baseSupply, basePrice;
	private int							ticks			= 1,
												stockPile = 0;
	private float						maxPrice, minPrice, maxDemand, minDemand, maxSupply, minSupply, averagePrice, supply, demand;
	private float						price;
	
	private NoisyCurveGenerator supplyCurve = new NoisyCurveGenerator(0.001f, 0.8f, 4);
	private NoisyCurveGenerator demandCurve = new NoisyCurveGenerator(0.0045f, 0.8f, 4);

	public MarketResource(String id, String name, String guild, String rarity, String description, float baseSupply, float basePrice) {
		super(name, id, description, guild, rarity, ResourceType.MERCHANT);
		this.baseSupply = baseSupply;
		this.basePrice = basePrice;
		supply = baseSupply;
		demand = baseSupply;
		price = getPricePerUnit();
		marketHistory.add(new MarketSnapshot(supply, demand, price));
		maxPrice = getPricePerUnit();
		minPrice = getPricePerUnit();
		averagePrice = price;
		UpdateCaller.addListener(new MarketResourceListener());
	}

	private class MarketResourceListener implements UpdateListener {
		public void receiveUpdate() {
			updateResource();
		}

		@Override
		public boolean isInitialised() {
			return true;
		}
	}

	private void updateResource() {
		putPrice();
		++ticks;
	}
	
	private float clampToRange(float num){
		if(num > 1){
			return 1;
		} else if(num < -1){
			return -1;
		}
		return num;
	}

	public float getPricePerUnit() {
		supply = (supplyCurve.getPoint());
		clampToRange(supply);
		supply = supply * baseSupply / 4f + baseSupply;
		
		demand = (demandCurve.getPoint());
		clampToRange(demand);
		demand = demand * baseSupply / 4f + baseSupply;
		
		if(supply > demand){
			float difference = supply - demand;
			stockPile += difference;
		}
		if(demand > supply){
			float difference = demand - supply;
			if(stockPile >= difference && stockPile > 0){
				supply += difference;
				stockPile -= difference;
			}
		}
		return demand / supply * basePrice;
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

		boolean recordData = false;
		if (ticks % 1 == 0) {
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
		adjustMaxMinPrices();

		totalPrice += price;
		if (recordData) {
			averagePrice = totalPrice / totalStoredPrices;
			marketHistory.add(new MarketSnapshot(supply, demand, price));
		}
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

	public float getPriceDiff() {
		return maxPrice - minPrice;
	}

	public float getMinPrice() {
		return minPrice;
	}

	public float getMaxPrice() {
		return maxPrice;
	}

	public float getMaxSupply() {
		return maxSupply;
	}

	public float getMinSupply() {
		return minSupply;
	}

	public float getMaxDemand() {
		return maxDemand;
	}

	public float getMinDemand() {
		return minDemand;
	}

	public ArrayList<MarketSnapshot> getMarketHistory(int width) {
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

	public int getDemand() {
		return (int) demand;
	}

	public int getSupply() {
		return (int) supply;
	}

	public void setSupplyRate(int supplyRate) {
		this.supply += supplyRate;
	}

	public float getAveragePrice() {
		return averagePrice;
	}

	public int compareTo(MarketResource arg0) {
		if (this.price < arg0.price) {
			return -1;
		} else if (this.price > arg0.price) {
			return 1;
		}
		return 0;
	}

	public class MarketSnapshot {
		private float supply, demand, price;

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
