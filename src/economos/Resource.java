package economos;

public abstract class Resource {
	private String name, id, description, type, rarity;
		
	public Resource(String name, String id, String description, String type, String rarity){
		this.name = name;
		this.id = id;
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

	public String getID() {
		return id;
	}
}
