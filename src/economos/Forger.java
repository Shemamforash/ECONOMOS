package economos;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Forger extends MinigamePanel {
	private float temperature = 0, targetTemperature = 0, accuracy;
	private boolean increaseTemperature = false, striking = false;
	private String currentLetter;
	private int targetStrikes = 10, numberOfStrikes, quality, maximumQuality = 100;
	private long timer, timerTarget, lastTime;
	private ArrayList<FlameSource> flames = new ArrayList<FlameSource>();

	public Forger(MinigameController minigameController) {
		super(minigameController);
		numberOfStrikes = targetStrikes;
		for (int i = 0; i < 10; ++i) {
			FlameSource fs = new FlameSource(i);
			flames.add(fs);
		}
	}

	public void receiveKey(char c) {
		if (striking) {
			String input = String.valueOf(c).toUpperCase();
			if (!input.equals("W")) {
				if (input.equals(currentLetter)) {
					float potentialQuality = maximumQuality / targetStrikes;
					float accuracyModifier = accuracy / 100f;
					float distanceToTargetTemperature = targetTemperature - temperature;// -((0.01*x)^2)
																						// +
																						// 1
					float decimalDistance = 0.01f * distanceToTargetTemperature;
					float temperatureModifier = -(decimalDistance * decimalDistance) + 1;
					quality += accuracyModifier * potentialQuality * temperatureModifier;
				} else {
					maximumQuality -= 20;
				}
				striking = false;
			}
		}
	}

	private class FlameSource {
		private int sourceHeight = 5;
		private ArrayList<FlamePixel> activeEmbers = new ArrayList<FlamePixel>();
		private ArrayList<FlamePixel> inactiveEmbers = new ArrayList<FlamePixel>();
		private int x, y;

		public FlameSource(int x) {
			this.x = x;
		}

		public void update() {
			y = (int) (height - (height / 100f * temperature));

			if (inactiveEmbers.size() <= sourceHeight) {
				if (rand.nextInt(10) == 1) {
					inactiveEmbers.add(new FlamePixel(y - (inactiveEmbers.size()), x));
				}
			}
			if (inactiveEmbers.size() > 0 && rand.nextInt(10) == 1) {
				inactiveEmbers.get(inactiveEmbers.size() - 1).setActive();
				activeEmbers.add(inactiveEmbers.remove(inactiveEmbers.size() - 1));
			}

			for (int i = 0; i < activeEmbers.size(); ++i) {
				activeEmbers.get(i).update();
				if (activeEmbers.get(i).finished()) {
					activeEmbers.remove(activeEmbers.get(i));
					--i;
				}
			}

			for (int i = 0; i < inactiveEmbers.size(); ++i) {
				int yHeight = y - i;
				if (yHeight < 0) {
					yHeight = 0;
				}
				inactiveEmbers.get(i).setY(yHeight);
			}
		}
	}

	private class FlamePixel {
		private Color color;
		private int x, frame = 0, maxFrame, xMove;
		private float y;
		private Boolean active = false, finished = false;

		public FlamePixel(int y, int x) {
			this.x = x;
			this.y = y;
			this.color = new Color(255, 0, 0);
			if (rand.nextBoolean()) {
				xMove = 1;
			} else {
				xMove = -1;
			}
			float val = rand.nextFloat() * 10;
			maxFrame = (int) (val * val);
			frame = 0;
			checkY();
		}

		public Boolean finished() {
			return finished;
		}

		public void setActive() {
			active = true;
		}

		private void checkY() {
			if (y < 0) {
				y = 0;
			}
			if (y + 1 >= height) {
				y = height - 1;
			}
		}
		
		public void setY(int y){
			this.y = y;
		}

		public void update() {
			if (rand.nextFloat() < 0.05) {
				xMove = -xMove;
			}
			if (x + xMove > 10 || x + xMove < 0) {
				xMove = -xMove;
			}
			if (rand.nextFloat() < 0.2) {
				x += xMove;
			}
			y -= rand.nextFloat() * 2;
			checkY();
			if (frame >= maxFrame || height - (100 / height * temperature) < y) {
				finished = true;
			}
			color = getColorAtTemperature((int) (100f - (100f / height * y)));
			++frame;
		}

		public Color color() {
			return color;
		}

		public int y() {
			if (y < 0) {
				return 0;
			}
			return (int) y;
		}

		public int x() {
			return x;
		}
	}

	private Color getColorAtTemperature(int t) {
		int rVal, gVal, bVal;
		if (t <= 75) {
			rVal = 255;
			gVal = (int) (t / 5) * 17;
			bVal = gVal;
		} else {
			rVal = (int) (255 - (float) (51f * (21f - (t / 5f))));
			gVal = rVal;
			bVal = 255;
		}
		if (rVal > 255) {
			rVal = 255;
		}
		if (bVal > 255) {
			bVal = 255;
		}
		if (gVal > 255) {
			gVal = 255;
		}
		if (rVal < 0) {
			rVal = 0;
		}
		if (bVal < 0) {
			bVal = 0;
		}
		if (gVal < 0) {
			gVal = 0;
		}
		return new Color(rVal, gVal, bVal);
	}

	private void drawProgressBar(Graphics bGraphics) {
		int maxPixelWidth = (width - 10) / 100 * maximumQuality;
		if (maximumQuality < 0) {
			maximumQuality = 0;
		}
		if (quality > maximumQuality) {
			quality = maximumQuality;
		}

		int qualityWidth = (int) (width / 100 * quality);

		float interval = 255f / qualityWidth;
		for (int i = 0; i < maxPixelWidth; ++i) {
			if (i < qualityWidth) {
				int greenVal = (int) (interval * i);
				if (greenVal > 255) {
					greenVal = 255;
				}
				bGraphics.setColor(new Color(0, greenVal, 255));
			} else {
				bGraphics.setColor(new Color(5, 5, 5));
			}
			bGraphics.drawLine(i + 10, height - 10, i + 10, height);
		}

		bGraphics.setColor(Color.white);
		bGraphics.drawString(quality + "%", qualityWidth, height - 25);
		bGraphics.drawString(maximumQuality + "%", maxPixelWidth, height - 25);
	}

	private void generateStrike() {
		currentLetter = String.valueOf(alphabet[rand.nextInt(26)]);
		while (currentLetter.equals("W")) {
			currentLetter = String.valueOf(alphabet[rand.nextInt(26)]);
		}
		targetTemperature = rand.nextInt(75) + 25;
		timerTarget = rand.nextInt(3000) + 2000;
		timer = 0;
		striking = true;
		--numberOfStrikes;
		accuracy = 0;
		lastTime = System.currentTimeMillis();
	}

	private void drawTarget(Graphics bGraphics) {
		((Graphics2D) bGraphics).setStroke(new BasicStroke(2));
		bGraphics.setColor(new Color(255, 140, 0));
		if (!striking && numberOfStrikes > 0) {
			generateStrike();
		} else if (numberOfStrikes == 0) {
			minigameController().showSuccessMessage();
		}
		bGraphics.drawString(currentLetter, width / 2 - 5, height / 2 - 5);
		bGraphics.drawOval(width / 2 - 30, height / 2 - 30, 60, 60);

		float maxRadius = (height - 20f) / 2;
		float distanceDraw = maxRadius / timerTarget;
		float timeRemaining = timerTarget - timer;
		int radius = (int) (distanceDraw * timeRemaining);

		int greenVal = (int) (140f / timerTarget * timer);
		if (greenVal > 140) {
			greenVal = 140;
		}
		bGraphics.setColor(new Color(255, 140 - greenVal, 0));
		bGraphics.drawOval(width / 2 - radius, height / 2 - radius, radius * 2, radius * 2);

		int distanceToTarget = Math.abs(radius - 30);

		accuracy = 100f / (maxRadius - 30f) * (float) distanceToTarget;
		accuracy = 100f - accuracy;
		timer += System.currentTimeMillis() - lastTime;
		lastTime = System.currentTimeMillis();
		if (timer > timerTarget) {
			striking = false;
		}
	}

	private void drawTemperatureBar(Graphics bGraphics) {
		int qualityHeight = (int) ((float) height / 100f * temperature);

		float interval = 255f / height;
		for (int i = 0; i < height; ++i) {
			if (i < qualityHeight) {
				bGraphics.setColor(getColorAtTemperature((int) (100f / height * i)));
			} else {
				bGraphics.setColor(new Color(5, 5, 5));
			}
			bGraphics.drawLine(0, height - i, 10, height - i);
		}
		if (targetTemperature != 0) {
			int targetTemperatureLine = height - (int) ((float) height / 100f * targetTemperature);
			bGraphics.setColor(Color.white);
			bGraphics.drawLine(0, targetTemperatureLine, 20, targetTemperatureLine);
			bGraphics.drawString(targetTemperature * 10 + "C", 25, targetTemperatureLine);
			bGraphics.drawString(temperature * 10 + "C", 25, height - qualityHeight);
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		BufferedImage bImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics bGraphics = bImg.createGraphics();
		((Graphics2D) bGraphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

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
		drawTarget(bGraphics);
		drawProgressBar(bGraphics);
		for (FlameSource fs : flames) {
			fs.update();
			for(FlamePixel fp : fs.activeEmbers){
				bImg.setRGB(fp.x(), fp.y(), fp.color().getRGB());
			}
			for(FlamePixel fp : fs.inactiveEmbers){
				bImg.setRGB(fp.x(), fp.y(), fp.color().getRGB());
			}
		}
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
