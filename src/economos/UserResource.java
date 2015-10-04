package economos;

public abstract class UserResource extends Resource{
	protected int sold;
	
	public UserResource(String name, String description, String type, String rarity) {
		super(name, description, type, rarity);
	}
	
	public int getSold(){
		return sold;
	}
	
	@Override
	protected void updateQuantity(int amount, float price) {
		quantity += amount;
	}
}
