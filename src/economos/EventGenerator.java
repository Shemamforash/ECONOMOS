package economos;

import java.util.Random;

public class EventGenerator {
	private Random rnd = new Random();
	
	public void generateEvent(MarketResource r){
		if(r.getTmeSinceEvent() > rnd.nextInt(100) + 25){
			int severity = rnd.nextInt(4);
			float percentageReduction = 0.01f * rnd.nextInt((severity + 1) * 20);
			int reductionAmount = (int)percentageReduction * r.getSupply();
			System.out.println(getMessage(0, reductionAmount));
			r.setSupplyRate(r.getSupply() - reductionAmount);
		}
	}
	
	public String getMessage(int type, int amount){
		switch(type){
		default:	
			return "Resource production reduced by " + amount + " units";
		}
	}
}
