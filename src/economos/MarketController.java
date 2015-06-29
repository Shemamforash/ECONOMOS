package economos;

public class MarketController {
	private static Object lock = new Object();
	private static ResourceMap<MarketResource> marketResources = new ResourceMap<MarketResource>("Market");
	
	public static ResourceMap<MarketResource> getMarketResources(){
		return marketResources;
	}
	
	public static String buyResource(int quantity, UserResource r, User u){
		synchronized(lock){
			if(r.getMarketResource().getQuantity() >= quantity){
				float price = r.getMarketResource().getBuyPrice(quantity);
				if(u.getMoney() >= price){
					r.updateQuantity(quantity);
					r.getMarketResource().updateQuantity(-quantity);
					u.updateMoney(-price);
					return "Purchased " + quantity + " units of " + r.getName() + " for C" + price;
				}
				return "Insufficient funds for transaction, attain " + (price - u.getMoney()) + " more credits";
			}
			return "Quantity entered exceeds market quantity";
		}
	}
}
