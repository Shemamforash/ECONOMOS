package economos;

import MarketSimulator.MarketResource;
import MerchantResources.Resource;

public interface SelectedResourceListener {	
	public void selectedResourceChanged(Resource m);
	public void selectedGuildChanged(String g);
}
