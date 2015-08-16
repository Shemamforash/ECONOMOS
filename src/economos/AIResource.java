package economos;

public class AIResource extends UserResource implements Comparable<AIResource>{
	private float preferenceValue;
	private float averageCost;
	
	public AIResource(String name, String description, String type, String rarity) {
		super(name, description, type, rarity);
	}
	
	public void setPreferenceValue(float preferenceValue){
		this.preferenceValue = preferenceValue;
	}
	
	public float getPreferenceValue(){
		return preferenceValue;
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
