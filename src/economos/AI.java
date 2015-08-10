package economos;

import java.util.*;

public class AI extends User {
	private float aggressiveness, focus, intelligence, wariness, personalityTotal;
	public ResourceMap<AIResource> aiMap = new ResourceMap<AIResource>("AI");
	private ArrayList<AIResource> aiResources = new ArrayList<AIResource>();
	private Random rnd = new Random();

	public AI(String name, String company) {
		super(name, company);
		int personalityPoints = rnd.nextInt(75) + 50;
		personalityTotal = 0.01f * personalityPoints;
		while (personalityPoints > 0) {
			int trait = rnd.nextInt(4);
			switch (trait) {
			case 0:
				if (aggressiveness < 1f) {
					aggressiveness += 0.02f;
				}
				break;
			case 1:
				if (focus < 1f) {
					focus += 0.02f;
				}
				break;
			case 2:
				if (intelligence < 0.6f) {
					intelligence += 0.02f;
				}
				break;
			case 3:
				if (wariness < 1f) {
					wariness += 0.02f;
				}
				break;
			default:
				break;
			}
			--personalityPoints;
		}
		setPreferredResource();
		Timer t = new Timer();
		t.schedule(new AITimer(this), 0, EconomosGUI.timeStep);
	}

	public void tick() {
		Collections.sort(aiResources);
		boolean hasSold = false, hasBought = false;

		for (AIResource r : aiResources) {
			if (hasBought == true && hasSold == true) {
				break;
			}
			MarketResource mr = r.getMarketResource();
			float predictedProfit = r.getPredictedProfit(-1, mr.getSellPrice(1));
			float percentageProfit = predictedProfit / r.getAverageProfit();
			int action;
			if (r.getPreferenceValue() > rnd.nextFloat()) {
				if (mr.getTrend() > 0) {
					action = priceIncreasing(r, percentageProfit, mr.getTrend());
				} else {
					action = priceDecreasing(percentageProfit, mr.getTrend());
				}
				if (action == 1 && !hasSold) {
					sell(r);
//					hasSold = true;
				} else if (action == 2 && !hasBought) {
					if (buy(r, mr.getTrend())) {
//						hasBought = true;
					}
				}
			}
		}
	}

	public boolean testIntelligence() {
		if (intelligence > rnd.nextFloat()) {
			return true;
		}
		return false;
	}

	public int priceIncreasing(AIResource ar, float percentageProfit, float trend) {
		if (percentageProfit > 0) {
			if (percentageProfit > aggressiveness) {
				if (trend < wariness || testIntelligence()) {
					return 1;
				}
			}
		}
		if (trend > wariness || trend > aggressiveness || !testIntelligence()) {
			return 2;
		}
		return 0;
	}

	public int priceDecreasing(float percentageProfit, float trend) {
		if (percentageProfit > 0) {
			if (testIntelligence()) {
				return 1;
			}
		}
		if (-wariness > trend) {
			return 1;
		}
		if (trend < 1) {
			if (!testIntelligence()) {
				return 2;
			}
		}
		return 0;
	}

	public void sell(AIResource r) {
		MarketController.sellResource(r.getQuantity(), r, this);
		money = 10000;
	}

	public boolean buy(AIResource r, float trend) {
		float amountToSpend = rnd.nextFloat() * intelligence * (1 - wariness);
		int quantity = (int) Math.ceil((amountToSpend * getMoney()) / r.getMarketResource().getBuyPrice(1));
		if (quantity > 0) {
			MarketController.buyResource(quantity, r, this);
			return true;
		}
		return false;
	}

	public void setPreferredResource() {
		ArrayList<MarketResource> resources = DataParser.getAllMarketResources();
		int chosenResource = new Random().nextInt(resources.size());
		float multiplier = (float) (1f / Math.sqrt(2 * Math.PI));
		for (int i = 0; i < resources.size(); ++i) {
			int distanceToChosen = chosenResource - i;
			float eExponent = (float) (Math.pow(Math.E, (-(0.1f * distanceToChosen * distanceToChosen) / 2f)));
			float resourcePreferenceValue = multiplier * eExponent;
			resourcePreferenceValue = 1f / 0.396f * resourcePreferenceValue * focus;
			if (resourcePreferenceValue > 1) {
				resourcePreferenceValue = 1;
			}
			aiResources.add(aiMap.getResource(resources.get(i).getType(), resources.get(i).getName()));
			aiMap.getResource(resources.get(i).getType(), resources.get(i).getName())
					.setPreferenceValue(resourcePreferenceValue);
		}
	}

	class AITimer extends TimerTask {
		AI ai;

		public AITimer(AI ai) {
			this.ai = ai;
		}

		public void run() {
			ai.tick();
		}
	}
}
