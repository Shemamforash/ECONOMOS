package economos;

import java.util.ArrayList;

import Resources.MerchantResource;
import Resources.ResourceMap;

public class SelectedResourceCaller {
	private static ArrayList<SelectedResourceListener> listeners = new ArrayList<SelectedResourceListener>();
	
	public static void addListener(SelectedResourceListener listener){
		listeners.add(listener);
	}
	
	public static void updateResource(String resource){
		ResourceMap<MerchantResource> m = Main.getPlayer().getPlayerResourceMap();
		MerchantResource currentResource = m.getResource(resource);
		for(SelectedResourceListener l : listeners){
			l.selectedResourceChanged(currentResource);
		}
	}
	
	public static void updateGuild(String guild){
		for(SelectedResourceListener l : listeners){
			l.selectedGuildChanged(guild);
		}
	}
}
