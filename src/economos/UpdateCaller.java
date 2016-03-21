package economos;

import java.util.ArrayList;

public class UpdateCaller {
	private static ArrayList<UpdateListener> listeners = new ArrayList<UpdateListener>();
	
	public static synchronized void addListener(UpdateListener listener){
		listeners.add(listener);
	}
	
	public static synchronized void callUpdate(){
		for(UpdateListener l : listeners){
			if(l.isInitialised()){
				l.receiveUpdate();
			}
		}
	}
}