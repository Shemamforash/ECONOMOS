package economos;

public class MarketController {
	private static ResourceMap<MarketResource> marketResources = new ResourceMap<MarketResource>("Market");
	
	public static ResourceMap<MarketResource> getMarketResources(){
		return marketResources;
	}
}
