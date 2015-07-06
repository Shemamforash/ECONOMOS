package economos;

import java.util.*;

public class AI extends User{
	private float aggressiveness, focus, intelligence, wariness, personalityTotal;
	public ResourceMap<AIResource> aiMap = new ResourceMap<AIResource>("AI");
	private ArrayList<AIResource> aiResources = new ArrayList<AIResource>();
	private Random rnd = new Random();
	
	public AI(String name, String company){
		super(name, company);
		int personalityPoints = rnd.nextInt(75) + 50;
		personalityTotal = 0.01f * personalityPoints;
		while(personalityPoints > 0){
			int trait = rnd.nextInt(4);
			switch(trait){
			case 0:
				if(aggressiveness < 1f){
					aggressiveness += 0.02f;
				}
				break;
			case 1:
				if(focus < 1f){
					focus += 0.02f;
				}
				break;
			case 2:
				if(intelligence < 1f){
					intelligence += 0.02f;
				}
				break;
			case 3:
				if(wariness < 1f){
					wariness += 0.02f;
				}
				break;
			default:
				break;
			}
			--personalityPoints;
		}
		money = 100000;
		setPreferredResource();
		Timer t = new Timer();
		t.schedule(new AITimer(this), 0, EconomosMain.timeStep);
	}
	
	public void tick(){
		Collections.sort(aiResources);
		if(getName().equals("Sam's AI")){
			System.out.println(money);
		}
		float threshold = rnd.nextFloat() * personalityTotal - aggressiveness;
		boolean hasBought = false, hasSold = false;
		
		for(int i = 0; i < aiResources.size(); ++i){
			int aiTakeAction = aiResources.get(i).updateAggressiveCounter(aggressiveness * intelligence * rnd.nextFloat());
			float passVal = aiTakeAction * (rnd.nextFloat() * aiResources.get(i).getPreferenceValue());
			if(passVal > threshold && !hasBought){
				int quantity = (int)(rnd.nextFloat() * wariness * (getMoney() / aiResources.get(i).getMarketResource().getBuyPrice(1)));
				if(quantity > aiResources.get(i).getMarketResource().getQuantity()){
					quantity = aiResources.get(i).getMarketResource().getQuantity();
				}
				if(quantity > 0) {
					if(getName().equals("Sam's AI")){
						System.out.println(MarketController.buyResource(quantity, aiResources.get(i), this));
					} else {
						MarketController.buyResource(quantity, aiResources.get(i), this);
					}
					hasBought = true;
				}
			} else if (passVal < -threshold && aiResources.get(i).getMarketResource().getSellPrice(1) > aiResources.get(i).getAverageCost() && !hasSold){
				int quantity = (int)(rnd.nextFloat() * wariness * aiResources.get(i).getQuantity());
				if(quantity > 0) {
					if(getName().equals("Sam's AI")){
						System.out.println(MarketController.sellResource(quantity, aiResources.get(i), this));
					} else {
						MarketController.sellResource(quantity, aiResources.get(i), this);
					}
					hasSold = true;
				}
			}
		}
	}
	
	public void setPreferredResource(){
		ArrayList<MarketResource> resources = DataParser.getAllMarketResources();
		int chosenResource = new Random().nextInt(resources.size());
		float multiplier = (float)(1f/Math.sqrt(2 * Math.PI));
		for(int i = 0; i < resources.size(); ++i){
			int distanceToChosen = chosenResource - i;
			float eExponent = (float)(Math.pow(Math.E, (-(0.1f * distanceToChosen * distanceToChosen) / 2f)));
			float resourcePreferenceValue = multiplier * eExponent;
			resourcePreferenceValue = 1f / 0.396f * resourcePreferenceValue * focus;
			if(resourcePreferenceValue > 1){
				resourcePreferenceValue = 1;
			}
			aiResources.add(aiMap.getResource(resources.get(i).getType(), resources.get(i).getName()));
			aiMap.getResource(resources.get(i).getType(), resources.get(i).getName()).setPreferenceValue(resourcePreferenceValue);
		}
	}
	
	class AITimer extends TimerTask{
		AI ai;
		
		public AITimer(AI ai){
			this.ai = ai;
		}
		
		public void run(){
			ai.tick();
		}
	}
}
