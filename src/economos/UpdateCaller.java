package economos;

import java.util.ArrayList;

public class UpdateCaller {
	private static ArrayList<UpdateListener> listeners = new ArrayList<UpdateListener>();
	
	public static void addListener(UpdateListener listener){
		listeners.add(listener);
	}
	
	public static void callUpdate(){
		listeners.forEach((l) -> {
			if(l.isInitialised()){
				l.receiveUpdate();
			}
		});
	}
}