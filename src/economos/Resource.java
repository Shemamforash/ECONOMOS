package economos;

public abstract class Resource {
	private String name, description, type;
	protected int quantity;
	
	public int getQuantity(){
		return quantity;
	}
	
	protected abstract void updateQuantity(int amount);
	
	public Resource(String name, String description, String type){
		this.name = name;
		this.description = description;
		this.type = type;
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
