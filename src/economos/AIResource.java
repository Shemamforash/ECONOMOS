package economos;

public class AIResource extends UserResource implements Comparable<AIResource>{
	private float preferenceValue, aggressiveCounter;
	private float averageCost;
	
	public AIResource(String name, String description, String type) {
		super(name, description, type);
	}
	
	public void setPreferenceValue(float preferenceValue){
		this.preferenceValue = preferenceValue;
	}
	
	public float getPreferenceValue(){
		return preferenceValue;
	}
	
	public int updateAggressiveCounter(float aggressiveValue){
		if(getMarketResource().getTrend() < 0){						//If the trend is negative
			if(aggressiveCounter <= 0){								//And the aggressiveCounter is also negative
				aggressiveCounter -= 0.02f;							//Decrease the aggressiveCounter
				if(aggressiveCounter < -aggressiveValue){			//Check if it crosses the threshold value
					aggressiveCounter = 0;
					return 1;										//Return 1 for sell
				} else {
					return 0;
				}
			} else {												//Otherwise reset the aggressive counter
				aggressiveCounter = 0f;
				return 0;
			}
		} else {
			if(aggressiveCounter >= 0){
				aggressiveCounter += 0.02f;
				if(aggressiveCounter > aggressiveValue){
					aggressiveCounter = 0;
					return -1;										//Return -1 for buy
				} else {
					return 0;
				}
			} else {
				aggressiveCounter = 0f;
				return 0;											//Return 0 for do nothing
			}
		}
	}

	public int compareTo(AIResource arg0) {
		if(arg0.getPreferenceValue() < preferenceValue){
			return 1;
		} else if(arg0.getPreferenceValue() > preferenceValue){
			return -1;
		}
		return 0;
	}

	protected void updateQuantity(int amount, float price) {
		super.updateQuantity(amount, price);
		if(amount > 0){
			averageCost = (averageCost * (sold - amount) + (price * amount)) / sold;
		}
	}
	
	public float getAverageCost(){
		return averageCost;
	}
}
