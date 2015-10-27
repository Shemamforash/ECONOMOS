package economos;

import java.util.*;

import economos.Main.UpdateListener;

public class AI extends User {
	private float							greed, patience, intelligence;
	public ResourceMap<MerchantResource>	aiMap		= new ResourceMap<MerchantResource>("AI");
	private ArrayList<MerchantResource>		aiResources	= new ArrayList<MerchantResource>();
	private Random							rnd			= new Random();
	private AIListener listener;
	
	private class AIListener implements UpdateListener{
		public void receiveUpdate() {
			tick();
		}
	}

	public AI(String name, String company) {
		super(name, company);
		int personalityPoints = rnd.nextInt(75) + 75;
		while (personalityPoints > 0) {
			int trait = rnd.nextInt(3);
			switch (trait) {
				case 0:
					if (greed < 1f) { // Greedy AI will try to buy as much as possible
						greed += 0.02f;
					}
					break;
				case 1:
					if (patience < 1f) { // Patient AI will wait on trends (wait if price is increasing to sell, vica versa for buy)
						patience += 0.02f;
					}
					break;
				case 2:
					if (intelligence < 0.6f) { // Stupid AI will make more mistakes
						intelligence += 0.02f;
					}
					break;
				default:
					break;
			}
			--personalityPoints;
		}
		getResources();
		Main.addUpdateListener(new AIListener());
	}

	public void tick() {
		for (MerchantResource r : aiResources) {
			MarketResource mr = r.getMarketResource();
			float predictedProfit = r.getPredictedProfit(-1, mr.getSellPrice(1));
			float percentageProfit = predictedProfit / r.getAverageProfit();
			int action;

			if (mr.getTrend() > 0) {
				action = priceIncreasing(r, percentageProfit, mr.getTrend());
			} else {
				action = priceDecreasing(percentageProfit, mr.getTrend());
			}
			if (action == 1) {
				sell(r);
			} else if (action == 2) {
				buy(r, mr.getTrend());
			}
		}
	}

	public boolean testIntelligence() {
		if (intelligence > rnd.nextFloat()) {
			return true;
		}
		return false;
	}

	public int priceIncreasing(MerchantResource ar, float percentageProfit, float trend) { // If market price is increasing
		int choice = 0;
		if (!testIntelligence()) { // If the intelligence test fails, make a random choice
			choice = rnd.nextInt(3);
		} else if (percentageProfit > 0) { // And the percentage profit of the resource compared to previous purchase prices is above 0
			if (percentageProfit * percentageProfit > greed) { // Test to see if the square of the profit is greater than the greed (why do this)
				if (trend < patience) { // If the trend is decreasing but the price is increasing, sell
					choice = 1;
				} else { // Otherwise buy
					choice = 2;
				}
			}
		}
		return choice;
	}

	public int priceDecreasing(float percentageProfit, float trend) { // If market price is decreasing
		int choice = 0;
		if (!testIntelligence()) { // If the intelligence test fails, make a random choice
			choice = rnd.nextInt(3);
		} else if (-patience < trend) { // If the patience is less (greater than abs) than the trend, we want to sell (price is decreasing we need to make profit)
			choice = 1;
		} else if (-patience > trend) { // Vice versa for buying
			choice = 2;
		}
		return choice;
	}

	public void sell(MerchantResource r) {
		MarketController.sellResource(r.getQuantity(), r, this);
		money = 10000;
	}

	public void buy(MerchantResource r, float trend) {
		float amountToSpend = rnd.nextFloat() * (greed + (1 - intelligence)) * 0.3f;
		int quantity = (int) Math.ceil((amountToSpend * getMoney()) / r.getMarketResource().getBuyPrice(1));
		if (quantity > 0) {
			MarketController.buyResource(quantity, r, this);
		}
	}

	public void getResources() {
		ArrayList<MarketResource> resources = DataParser.getAllMarketResources();
		for (int i = 0; i < resources.size(); ++i) {
			aiResources.add(aiMap.getResource(resources.get(i).getType(), resources.get(i).getName()));
		}
	}
}
