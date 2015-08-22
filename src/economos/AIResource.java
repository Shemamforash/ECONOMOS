package economos;

public class AIResource extends UserResource {
	private float preferenceValue;
	private float averageCost;

	public AIResource(String name, String description, String type, String rarity) {
		super(name, description, type, rarity);
	}

	protected void updateQuantity(int amount, float price) {
		super.updateQuantity(amount, price);
		if (amount > 0) {
			averageCost = (averageCost * (sold - amount) + (price * amount)) / sold;
		}
	}

	public float getAverageCost() {
		return averageCost;
	}
}
