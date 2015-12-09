package economos;

public abstract class UserResource extends Resource{
	protected int sold;
	protected int quantity;
	
	public UserResource(String name, String id, String description, String type, String rarity) {
		super(name, id, description, type, rarity);
	}
	
	public int getSold(){
		return sold;
	}
	
	public int getQuantity(){
		return quantity;
	}
	
	protected abstract void updateQuantity(int amount, float price);
}
