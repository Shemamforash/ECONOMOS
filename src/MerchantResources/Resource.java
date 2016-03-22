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
	
	public ResourceType getType(){
		return type;
	}
	
	public void unlock() {
		unlocked = true;
	}
	
	public String getRarity(){
		return rarity;
	}
	
	public String getGuild(){
		return guild;
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
