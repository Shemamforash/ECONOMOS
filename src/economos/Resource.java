package economos;

public abstract class Resource {
	private String name, description, type, rarity;
	protected int quantity;
	
	public int getQuantity(){
		return quantity;
	}
	
	protected abstract void updateQuantity(int amount, float price);
	
	public Resource(String name, String description, String type, String rarity){
		this.name = name;
		this.description = description;
		this.type = type;
		this.rarity = rarity;
	}
	
	public String getRarity(){
		return rarity;
	}
	
	public String getType(){
		return type;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String getName(){
		return name;
	}
}
