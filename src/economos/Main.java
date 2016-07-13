package economos;

import java.io.IOException;

import javax.swing.SwingUtilities;

import DataImportExport.DataParser;
import GUI.EconomosGUI;

public class Main {
	private boolean running = true;
	private float optimumFrameTime = 1000000000f / 60f;
	private EconomosGUI window;

	public static void main(String[] args) {
		Main m = new Main();
		m.start();
	}

	private void start() {
		loadData();
		startGUI();
		loop();
	}

	// Start the GUI in the event dispatch thread.
	private void startGUI() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new EconomosGUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Load in all the resource data
	private void loadData() {
		try {
			DataParser parser = new DataParser();
		} catch (IOException e) {
			System.out.println("Failed to load. Terminating");
			System.exit(0);
		}
	}

	// Start the gameloop- this will try to run at 60fps.
	// Calls the relevant method in the UpdateCaller so that all classes that
	// need to be updated per frame are told when the frame is updated.
	public void loop() {
		int fps = 0;
		long lastFrameTime = System.nanoTime(), time = 0;
		while (running) {
			++fps;
			time += System.nanoTime() - lastFrameTime;
			if (time >= 1000000000) {
				fps = 0;
				time = time - 1000000000;
			}

			float delta = (System.nanoTime() - lastFrameTime) / optimumFrameTime;
			lastFrameTime = System.nanoTime();
			UpdateCaller.callUpdate();
			try {
				if (lastFrameTime - System.nanoTime() + optimumFrameTime >= 0) {
					Thread.sleep((long) ((lastFrameTime - System.nanoTime() + optimumFrameTime) / 1000000));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
