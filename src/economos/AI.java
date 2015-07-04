package economos;

import java.util.*;

public class AI extends User{
	private int aggressiveness, focus, intelligence, wariness;
	public ResourceMap<AIResource> aiMap = new ResourceMap<AIResource>("AI");
	
	public AI(String name, String company){
		super(name, company);
		Random rnd = new Random();
		int personalityPoints = rnd.nextInt(75) + 50;
		while(personalityPoints > 0){
			int trait = rnd.nextInt(4);
			switch(trait){
			case 0:
				aggressiveness += 2;
				break;
			case 1:
				focus += 2;
				break;
			case 2:
				intelligence += 2;
				break;
			case 3:
				wariness += 2;
				break;
			default:
				break;
			}
			--personalityPoints;
		}
		setPreferredResource();
		Timer t = new Timer();
		t.schedule(new AITimer(this), 0, 100);
	}
	
	public void tick(){
		
	}
	
	public void setPreferredResource(){
		ArrayList<MarketResource> resources = DataParser.getAllMarketResources();
		int chosenResource = new Random().nextInt(resources.size());
		float multiplier = (float)(1f/Math.sqrt(2 * Math.PI));
		for(int i = 0; i < resources.size(); ++i){
			int distanceToChosen = chosenResource - i;
			float eExponent = (float)(Math.pow(Math.E, (-(distanceToChosen * distanceToChosen) / 2f)));
			float resourcePreferenceValue = multiplier * eExponent;
			resourcePreferenceValue = 1f / 0.396f * resourcePreferenceValue;
			if(resourcePreferenceValue > 1){
				resourcePreferenceValue = 1;
			}
			aiMap.getResourceTypes().get(resources.get(i).getType()).getResourceOfType().get((resources.get(i).getName())).setPreferenceValue(resourcePreferenceValue);
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
