package economos;

public abstract class User {
	private String name, companyName;
	private float money = 100000f;
	private ResourceMap<UserResource> resourceMap = new ResourceMap<UserResource>("User");
	
	public User(String name, String companyName){
		this.name = name;
		this.companyName = companyName;
	}
	
	public ResourceMap<UserResource> getResourceMap(){
		return resourceMap;
	}
	
	public float getMoney(){
		return money;
	}
	
	public void updateMoney(float amount){
		money += amount;
	}
}
