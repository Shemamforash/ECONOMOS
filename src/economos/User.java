package economos;

import java.util.ArrayList;

import DataImportExport.DataParser;
import MerchantResources.MerchantResource;

public abstract class User {
	private String name, playTime;
	protected float money = 100f;
	private float moneyMade;
	private float moneySpent;
	private float value;
	protected ArrayList<MerchantResource> userResources = new ArrayList<MerchantResource>();

	public User(String name) {
		this.name = name;
		loadUserResources();
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
	
	public void loadUserResources(){
		userResources = DataParser.getUserResources();
	}
	
	public MerchantResource findUserResource(String name){
		for(MerchantResource r : userResources){
			if(r.getName().equals(name)){
				return r;
			}
		}
		return null;
	}
}
