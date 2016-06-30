package economos;

import MarketSimulator.MarketResource;

public interface SelectedResourceListener {	
	public void selectedResourceChanged(MarketResource m);
	public void selectedGuildChanged(String g);
}
