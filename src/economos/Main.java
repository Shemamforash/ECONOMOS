package economos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

public class Main {
	private boolean running = true;
	private float optimumFrameTime = 1000000000f / 60f;
	private static UpdateCaller loopUpdater;
	private static Player			player;
	
	public static void main(String[] args) {
		Main m = new Main();
		m.start();
	}
	
	private void start(){
		loopUpdater = new UpdateCaller();
		loadData();
		createAI();
		startGUI();
		loop();
	}
	
	private void startGUI(){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					EconomosGUI window = new EconomosGUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void loadData() {
		try {
			DataParser parser = new DataParser();
		} catch (IOException e) {
			System.out.println("Failed to load. Terminating");
			System.exit(0);
		}
		player = new Player("Sam", "Potatronics");
	}
	
	private void createAI(){
		for(int i = 0; i < 30; ++i){
			AI ai = new AI("Some ai" + i, "A company");
		}
	}
	
	public void loop(){
		long lastFrameTime = System.nanoTime();
		while(running){
			float delta = (System.nanoTime() - lastFrameTime) / optimumFrameTime;
			lastFrameTime = System.nanoTime();
			loopUpdater.callUpdate();
			try{
				Thread.sleep((long) ((lastFrameTime - System.nanoTime() + optimumFrameTime) / 1000000));
			} catch (Exception e){
				System.out.println("Something wrong with timer");
			}
		}
	}
	
	class UpdateCaller {
		private ArrayList<UpdateListener> listeners = new ArrayList<UpdateListener>();
		
		public void addListener(UpdateListener listener){
			listeners.add(listener);
		}
		
		public void callUpdate(){
			for(UpdateListener l : listeners){
				l.receiveUpdate();
			}
		}
	}
	
	public static void addUpdateListener(UpdateListener l){
		loopUpdater.addListener(l);
	}
	
	interface UpdateListener {
		public void receiveUpdate();
	}
	
	public static Player getPlayer() {
		return player;
	}
}