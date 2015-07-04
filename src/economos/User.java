package economos;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public abstract class User {
	private String name, companyName, playTime;
	private UserResource companyShare;
	private float money = 100000f, moneyMade, moneySpent, value;

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
}
