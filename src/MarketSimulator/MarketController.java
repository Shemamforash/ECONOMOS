package MarketSimulator;

import java.util.*;

import DataImportExport.DataParser;
import MerchantResources.MerchantResource;
import MerchantResources.Resource;
import MerchantResources.ResourceMap;
import economos.User;

public class MarketController {
	private static ResourceMap<MarketResource>	marketResources			= new ResourceMap<MarketResource>(false);
	private ArrayList<MarketResource>			priceOrderedResources	= new ArrayList<MarketResource>();
	private static int							graphWidth;

	public static ResourceMap<MarketResource> getMarketResources() {
		return marketResources;
	}

	public static synchronized String buyResource(int quantity, MarketResource mr, User u) {
		MerchantResource r = u.findUserResource(mr.getName());
		float pricePerUnit = mr.getBuyPrice(1);
		float price = pricePerUnit * quantity;
		if (u.getMoney() < price) {
			quantity = (int) Math.floor(u.getMoney() / pricePerUnit);
		}
		price = pricePerUnit * quantity;
		r.buy(quantity, pricePerUnit);
		u.updateMoney(-price);
		return "Purchased " + quantity + " units of " + r.getName() + " for C" + price;
	}

	public static void setGraphWidth(int width) {
		graphWidth = width;
	}

	public static int getGraphWidth() {
		return graphWidth;
	}

	public static Resource findResource(String lookup) {
		return marketResources.getResource(lookup);
	}
	
	/*
	 * Gets the amount of money made from selling the resource.
	 */
	public static synchronized String sellResource(int quantity, MarketResource mr, User u) {
		MerchantResource r = u.findUserResource(mr.getName());
		if(quantity > r.getQuantity()){
			quantity = r.getQuantity();
		}
		float moneyEarned = r.sell(quantity);
		u.updateMoney(moneyEarned);
		return "Sold " + quantity + " units of " + r.getName() + " for C" + moneyEarned;
	}

	public ArrayList<MarketResource> getOrderedResources() {
		if (priceOrderedResources.size() == 0) {
			priceOrderedResources = DataParser.getAllMarketResources();
		}
		Collections.sort(priceOrderedResources);
		return priceOrderedResources;
	}
}
