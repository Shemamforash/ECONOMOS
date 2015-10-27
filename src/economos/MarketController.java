package economos;

import java.util.*;

public class MarketController {
	private static ResourceMap<MarketResource>	marketResources			= new ResourceMap<MarketResource>("Market");
	private ArrayList<MarketResource>			priceOrderedResources	= new ArrayList<MarketResource>();
	private static int							graphWidth;

	public static ResourceMap<MarketResource> getMarketResources() {
		return marketResources;
	}

	public static synchronized String buyResource(int quantity, MerchantResource r, User u) {
		r.getMarketResource().updateDesiredThisTick(quantity);
		MarketResource mr = r.getMarketResource();
		float pricePerUnit = mr.getBuyPrice(1);
		float price = pricePerUnit * quantity;
		if (u.getMoney() < price) {
			quantity = (int) Math.floor(u.getMoney() / pricePerUnit);
			price = pricePerUnit * quantity;
		}
		r.updateQuantity(quantity, price);
		r.getMarketResource().updateQuantity(-quantity, price);
		u.updateMoney(-price);
		return "Purchased " + quantity + " units of " + r.getName() + " for C" + price;
	}

	public static void setGraphWidth(int width) {
		graphWidth = width;
	}

	public static int getGraphWidth() {
		return graphWidth;
	}

	public static synchronized String sellResource(int quantity, MerchantResource r, User u) {
		if (r.getQuantity() < quantity) {
			quantity = r.getQuantity();
		}
		float price = r.getMarketResource().getSellPrice(quantity);
		r.updateQuantity(-quantity, price);
		r.getMarketResource().updateQuantity(quantity, price);
		u.updateMoney(price);
		return "Sold " + quantity + " units of " + r.getName() + " for C" + price;
	}

	public ArrayList<MarketResource> getOrderedResources() {
		if (priceOrderedResources.size() == 0) {
			priceOrderedResources = DataParser.getAllMarketResources();
		}
		Collections.sort(priceOrderedResources);
		return priceOrderedResources;
	}
}
