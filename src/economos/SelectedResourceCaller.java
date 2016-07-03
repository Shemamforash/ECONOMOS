package economos;

import java.util.ArrayList;

import MerchantResources.MerchantResource;

public class SelectedResourceCaller {
	private static ArrayList<SelectedResourceListener> listeners = new ArrayList<SelectedResourceListener>();
	
	public static void addListener(SelectedResourceListener listener){
		listeners.add(listener);
	}
	
	public static void updateResource(String name){
		MerchantResource currentResource = Main.getPlayer().findUserResource(name);
		if(currentResource != null){
			listeners.forEach((l) -> l.selectedResourceChanged(currentResource.marketResource()));
		}
	}
	
	public static void updateGuild(String guild){
		listeners.forEach((l) -> l.selectedGuildChanged(guild));
	}
}
