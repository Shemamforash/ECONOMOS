package economos;

public class AIResource extends UserResource{
	private float priceTrend;
	private float preferenceValue;
	
	public AIResource(String name, String description, String type) {
		super(name, description, type);
		// TODO Auto-generated constructor stub
	}
	
	public void setPreferenceValue(float preferenceValue){
		this.preferenceValue = preferenceValue;
	}
}
