package economos;

public class User {
	private String name, companyName;
	private ResourceMap<UserResource> resourceMap = new ResourceMap<UserResource>("User");
	
	public User(String name, String companyName){
		this.name = name;
		this.companyName = companyName;
	}
	
	public ResourceMap<UserResource> getResourceMap(){
		return resourceMap;
	}
}
