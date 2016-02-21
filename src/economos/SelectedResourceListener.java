package economos;

import Resources.MerchantResource;

public interface SelectedResourceListener {	
	public void selectedResourceChanged(MerchantResource m);
	public void selectedGuildChanged(String g);
}
