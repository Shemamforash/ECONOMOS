package economos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Forger extends MinigamePanel {
	private float temperature = 0;
	private boolean increaseTemperature = false;

	public Forger(MinigameController minigameController) {
		super(minigameController);

	}

	public void receiveKey(char c) {
		// TODO Auto-generated method stub

	}

	private void drawTemperatureBar(Graphics bGraphics) {
		int qualityHeight = (int) ((float) height / 100f * temperature);
		float interval = 255f / height;
		for (int i = 0; i < height; ++i) {
			if (i < qualityHeight) {
				int redVal = (int) (interval * i);
				if (redVal > 255) {
					redVal = 255;
				}
				int blueVal = 255 - redVal;
				bGraphics.setColor(new Color(redVal, 0, blueVal));
			} else {
				bGraphics.setColor(new Color(5, 5, 5));
			}
			bGraphics.drawLine(0, height - i, 10, height - i);
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		BufferedImage bImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics bGraphics = bImg.createGraphics();

		if (increaseTemperature) {
			temperature += 2;
			if (temperature > 100) {
				temperature = 100;
			}
		} else {
			temperature -= 0.5f;
			if (temperature < 0) {
				temperature = 0;
			}
		}

		drawTemperatureBar(bGraphics);
		g.drawImage(bImg, 0, 0, null);
	}

	public void holdKey(char c) {
		if (String.valueOf(c).toUpperCase().equals("W")) {
			increaseTemperature = true;
		}
	}

	@Override
	public void releaseKey(char c) {
		if (String.valueOf(c).toUpperCase().equals("W")) {
			increaseTemperature = false;
		}
	}

}
