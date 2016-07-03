package MarketSimulator;

import java.util.*;

import DataImportExport.DataParser;
import MerchantResources.MerchantResource;
import MerchantResources.Resource;
import MerchantResources.ResourceMap;
import economos.Player;

public class MarketController {
	private static ResourceMap<MarketResource>	marketResources			= new ResourceMap<MarketResource>(false);
	private ArrayList<MarketResource>			priceOrderedResources	= new ArrayList<MarketResource>();
	private static int							graphWidth;

	public static ResourceMap<MarketResource> getMarketResources() {
		return marketResources;
	}

	public static synchronized String buyResource(int quantity, MarketResource mr) {
		MerchantResource r = Player.findUserResource(mr.name());
		float pricePerUnit = mr.getBuyPrice(1);
		float price = pricePerUnit * quantity;
		if (Player.money() < price) {
			quantity = (int) Math.floor(Player.money() / pricePerUnit);
		}
		price = pricePerUnit * quantity;
		r.buy(quantity, pricePerUnit);
		Player.updateMoney(-price);
		return "Purchased " + quantity + " units of " + r.name() + " for C" + price;
	}

	/*
 	* Gets the amount of money made from selling the resource.
 	*/
	public static synchronized String sellResource(int quantity, MarketResource mr) {
		MerchantResource r = Player.findUserResource(mr.name());
		if(quantity > r.quantity()){
			quantity = r.quantity();
		}
		float moneyEarned = r.sell(quantity);
		Player.updateMoney(moneyEarned);
		return "Sold " + quantity + " units of " + r.name() + " for C" + moneyEarned;
	}

	public static void setGraphWidth(int width) {
		graphWidth = width;
	}

	public static int graphWidth() {
		return graphWidth;
	}

	public static Resource findResource(String lookup) {
		return marketResources.resource(lookup);
	}

	public ArrayList<MarketResource> getOrderedResources() {
		if (priceOrderedResources.size() == 0) {
			priceOrderedResources = DataParser.marketResources();
		}
		Collections.sort(priceOrderedResources);
		return priceOrderedResources;
	}
}
