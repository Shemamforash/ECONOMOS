package economos;

import java.util.ArrayList;

import MerchantResources.MerchantResource;
import MerchantResources.ResourceMap;

public class SelectedResourceCaller {
	private static ArrayList<SelectedResourceListener> listeners = new ArrayList<SelectedResourceListener>();
	
	public static void addListener(SelectedResourceListener listener){
		listeners.add(listener);
	}
	
	public static void updateResource(String name){
		MerchantResource currentResource = Main.getPlayer().findUserResource(name);
		if(currentResource != null){
			for(SelectedResourceListener l : listeners){
				l.selectedResourceChanged(currentResource.getMarketResource());
			}
		}
	}
	
	public static void updateGuild(String guild){
		for(SelectedResourceListener l : listeners){
			l.selectedGuildChanged(guild);
		}
	}
}
