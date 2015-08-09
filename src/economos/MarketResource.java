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
	float changeTrend = 0; //between 0 and 1
	int timer = 0; //timer to ensure non constant decrease
	int trendDirection = 1; //true is up/false is down

	public MarketResource(String name, String description, String type, int baseSupplyRate) {
		super(name, description, type);
		this.baseSupply = new Random().nextInt(5) + 3;
		supply = baseSupply;
		demand = baseSupply;
		desiredThisTick = baseSupply;
		quantity = new Random().nextInt(1000);
		basePrice = new Random().nextInt(1000);
		price = getPricePerUnit();
		previousBuyPrices.add(price);
		maxPrice = getPricePerUnit() + 5;
		minPrice = getPricePerUnit() - 5;
		averagePrice = price;
		Timer timer = new Timer();
		timer.schedule(new UpdateResource(this), 0, EconomosGUI.timeStep);
	}

	private float getSupplyChange(){	
		int difference = baseSupply * 10;
		float supplyModifier = (1f / difference) * (supply - difference);
		supplyModifier = supplyModifier * supplyModifier;
		if(supplyModifier > 1){
			supplyModifier = 1;
		}
//		if(supply > baseSupply * 10){
//			supplyModifier = -1 - supplyModifier;
//		} else {
//			supplyModifier = 1 - supplyModifier;
//		}
//		
		if (new Random().nextInt(50) < timer) {
			trendDirection = -trendDirection;
			timer = 0;
		}  
		++timer;

		float supplyChange = 0.01f * supply;
		supplyChange = supplyChange * supplyModifier * new Random().nextFloat();
		if(getName().equals("Ambrosia")){
//			System.out.println("Supply = " + supply + " / supplyChange = " + supplyChange + " / supplymodifier = " + supplyModifier + " / basesupply = " + baseSupply);
		}
		return supply + supplyChange;
	}

	public float getPricePerUnit() {
		if(supply < baseSupply * 0.5f && demand > baseSupply * 0.5f){
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
		if(demand == 0){
			demand = 1;
		}
		if (demand / supply * basePrice < 1) {
			return 1;
		}
		return (demand / supply * basePrice) / 1000;
	}

	public void adjustMaxMinPrices() {
		float tempMin = previousBuyPrices.get(0), tempMax = previousBuyPrices.get(0);
		for (float i : previousBuyPrices) {
			if (i < tempMin) {
				tempMin = i;
			}
			if (i > tempMax) {
				tempMax = i;
			}
		}
		minPrice = price - price;
		maxPrice = price + price;
	}

	public void putPrice() {
		price = getPricePerUnit();
		quantity += supply;

		int totalStoredPrices = previousBuyPrices.size();
		float totalPrice = averagePrice * totalStoredPrices;

		if (previousBuyPrices.size() == 672) {
			totalPrice -= previousBuyPrices.get(0);
			previousBuyPrices.remove(0);
		}

		totalPrice += price;
		averagePrice = totalPrice / totalStoredPrices;

		previousBuyPrices.add(price);

		int dx = previousBuyPrices.size();
		float angle, dy;
		if (previousBuyPrices.size() < 30) {
			dy = previousBuyPrices.get(dx - 1) - previousBuyPrices.get(0);
			// trend = previousBuyPrices.get(previousBuyPrices.size() - 1) -
			// previousBuyPrices.get(0);
		} else {
			dy = previousBuyPrices.get(dx - 1) - previousBuyPrices.get(previousBuyPrices.size() - 30);
			// trend = previousBuyPrices.get(previousBuyPrices.size() - 1) -
			// previousBuyPrices.get(previousBuyPrices.size() - 30);
		}
		angle = (float) Math.toDegrees(Math.atan(dx / dy));
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
			if(tempDemand >= 0){
				demand = tempDemand;
			}
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
		return (int)demand;
	}

	public int getSupply() {
		return (int)supply;
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

	public synchronized void updateDesiredThisTick(int amount) {
		if(getName().equals("Ambrosia")){
//			System.out.println("amount = " + amount + " / tick = " + ticks);
		}
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
