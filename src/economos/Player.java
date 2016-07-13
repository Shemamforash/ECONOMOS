package economos;

import java.util.ArrayList;

import DataImportExport.DataParser;
import MerchantResources.MerchantResource;

public class Player {
	private static String name, playTime;
	private static float money = 100000f, moneyMade, moneySpent, value;
	public static ArrayList<MerchantResource> userResources = DataParser.userResources();

	public Player(String name) {
		this.name = name;
	}

	public static float money() {
		return money;
	}

	public static void updateMoney(float amount) {
		money += amount;
	}

	public static String name(){
		return name;
	}

	public static MerchantResource findUserResource(String name){
		for(MerchantResource r : userResources){
			if(r.name().equals(name)){
				return r;
			}
		}
		return null;
	}	
}
