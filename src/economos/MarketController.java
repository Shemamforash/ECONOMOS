package economos;

import java.util.*;

public class MarketController {
	private static Object lock = new Object();
	private static ResourceMap<MarketResource> marketResources = new ResourceMap<MarketResource>("Market");
	private ArrayList<MarketResource> priceOrderedResources = new ArrayList<MarketResource>();

	public static ResourceMap<MarketResource> getMarketResources() {
		return marketResources;
	}

	public static String buyResource(int quantity, UserResource r, User u) {
		synchronized (lock) {
			if (r.getMarketResource().getQuantity() >= quantity) {
				float price = r.getMarketResource().getBuyPrice(quantity);
				if (u.getMoney() >= price) {
					r.updateQuantity(quantity, price);
					r.getMarketResource().updateQuantity(-quantity, price);
					u.updateMoney(-price);
					return "Purchased " + quantity + " units of " + r.getName() + " for C" + price;
				}
				return "Insufficient funds for transaction, attain " + (price - u.getMoney()) + " more credits";
			}
			return "Quantity entered exceeds market quantity";
		}
	}

	public static String sellResource(int quantity, UserResource r, User u) {
		synchronized (lock) {
			if(r.getQuantity() >= quantity){
				float price = r.getMarketResource().getSellPrice(quantity);
				r.updateQuantity(-quantity, price);
//				r.getMarketResource().updateQuantity(quantity, price);
				u.updateMoney(price);
				return "Sold " + quantity + " units of " + r.getName() + " for C" + price;
			}
			return "Quantity entered exceeds quantity you own.";
		}
	}

	public ArrayList<MarketResource> getOrderedResources() {
		if (priceOrderedResources.size() == 0) {
			priceOrderedResources = DataParser.getAllMarketResources();
		}
		Collections.sort(priceOrderedResources);
		return priceOrderedResources;
	}
}
