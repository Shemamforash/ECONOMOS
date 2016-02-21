package economos;

import java.io.IOException;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import GUI.EconomosGUI;
import Resources.DataParser;

public class Main {
	private boolean running = true;
	private float optimumFrameTime = 1000000000f / 60f;
	private static Player			player;
	
	public static void main(String[] args) {
		Main m = new Main();
		m.start();
	}
	
	private void start(){
		loadData();
		createAI();
		startGUI();
		loop();
	}
	
	//Start the GUI in the event dispatch thread.
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
	
	
	//Load in all the resource data
	private void loadData() {
		try {
			DataParser parser = new DataParser();
		} catch (IOException e) {
			System.out.println("Failed to load. Terminating");
			System.exit(0);
		}
		player = new Player("Sam", "Potatronics");
	}
	
	//Create some AI
	private void createAI(){
		for(int i = 0; i < 5; ++i){
			AI ai = new AI("Some ai" + i, "A company");
		}
	}
	
	
	//Start the gameloop- this will try to run at 60fps.
	//Calls the relevant method in the UpdateCaller so that all classes that need to be updated per frame are told when the frame is updated.
	public void loop(){
		int fps = 0;
		long lastFrameTime = System.nanoTime(), time = 0;
		while(running){
			++fps;
			time += System.nanoTime() - lastFrameTime;
			if(time >= 1000000000){
				System.out.println("FPS" + fps);
				fps = 0;
				time = time - 1000000000;
			}
			
			float delta = (System.nanoTime() - lastFrameTime) / optimumFrameTime;
			lastFrameTime = System.nanoTime();
			UpdateCaller.callUpdate();
			
			try{
				Thread.sleep((long) ((lastFrameTime - System.nanoTime() + optimumFrameTime) / 1000000));
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static Player getPlayer() {
		return player;
	}
}
