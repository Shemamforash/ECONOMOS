package economos;

import MerchantResources.MarketResource;

public interface SelectedResourceListener {	
	public void selectedResourceChanged(MarketResource m);
	public void selectedGuildChanged(String g);
}
