package economos;

public abstract class User {
	private String name, companyName, playTime;
	private UserResource companyShare;
	protected float money = 100f;
	private float moneyMade;
	private float moneySpent;
	private float value;

	public User(String name, String companyName) {
		this.name = name;
		this.companyName = companyName;
	}

	public float getMoney() {
		return money;
	}

	public void updateMoney(float amount) {
		money += amount;
	}
	
	public String getName(){
		return name;
	}
}
