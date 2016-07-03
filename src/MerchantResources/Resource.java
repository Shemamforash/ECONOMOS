package MerchantResources;

public abstract class Resource {
	private String name, id, description, guild, rarity;
	private boolean unlocked = false;	
	private ResourceType type;
	public enum ResourceType { CRAFTING, MERCHANT };
	
	public Resource(String name, String id, String description, String guild, String rarity, ResourceType type){
		this.name = name;
		this.id = id;
		this.description = description;
		this.guild = guild;
		this.rarity = rarity;
		this.type = type;
	}
	
	public boolean unlocked(){
		return unlocked;
	}
	
	public ResourceType type(){
		return type;
	}
	
	public void unlock() {
		unlocked = true;
	}
	
	public String rarity(){
		return rarity;
	}
	
	public String guild(){
		return guild;
	}
	
	public String description(){
		return description;
	}
	
	public String name(){
		return name;
	}

	public String id() {
		return id;
	}
}
