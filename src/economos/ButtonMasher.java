package economos;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

class ButtonMasher extends MinigamePanel {
	private float quality = 100f;
	private int centerY, numberOfButtons, maxQuality = 100;
	private LetterButton[] buttons;
	private long lastTime = 0, secondCounter = 0;

	public ButtonMasher(final int numberOfButtons, MinigameController minigameController) {
		super(minigameController);
		this.centerY = height / 2;
		this.numberOfButtons = numberOfButtons;
		buttons = new LetterButton[numberOfButtons];
		for (int i = 0; i < numberOfButtons; ++i) {
			LetterButton letterButton = new LetterButton(i);
			buttons[i] = letterButton;
			letterButton.refreshLetter();
		}
		
	}

	public void constructPoints(int letterno) {
		int startX = width / 20;
		int boxWidth = (int) (((width - (2 * startX)) / numberOfButtons) * 0.75f);
		int yOffset = centerY - boxWidth / 2;

		BufferedImage bImg = new BufferedImage(boxWidth + 2, boxWidth + 2, BufferedImage.TYPE_INT_RGB);
		Graphics bGraphics = bImg.createGraphics();

		int xOffset = (int) (startX + letterno * boxWidth * 1.3333f);
		bGraphics.setColor(GUIElements.darkColor);
		bGraphics.fillRect(0, 0, boxWidth, boxWidth);
		bGraphics.setColor(GUIElements.goldenOrange);
		((Graphics2D) bGraphics).setStroke(new BasicStroke(4));
		bGraphics.drawRoundRect(1, 1, boxWidth, boxWidth, boxWidth / 6, boxWidth / 6);
		bGraphics.drawString(buttons[letterno].getLetter(), boxWidth - 25, boxWidth / 2 + 15);
		buttons[letterno].setPoints(getPoints(bImg, xOffset, yOffset, boxWidth / 2));
	}

	public ArrayList<MyPoint> getPoints(BufferedImage bImg, int xOffset, int yOffset, int boxHalfWidth) {
		Random rand = new Random();
		ArrayList<MyPoint> temp = new ArrayList<MyPoint>();
		for (int i = 0; i < bImg.getWidth(); ++i) {
			for (int j = 0; j < bImg.getHeight(); ++j) {
				Color c = new Color(bImg.getRGB(i, j));
				if (c.getRed() > 50) {
					float yDiff = j - boxHalfWidth;
					float xDiff = i - boxHalfWidth;
					float val = 1f / (Math.abs(yDiff) + Math.abs(xDiff));
					float spd = rand.nextFloat();
					val = val * spd;
					yDiff = yDiff * val;
					xDiff = xDiff * val;
					MyPoint p = new MyPoint(i + xOffset, j + yOffset, xDiff, yDiff, c);
					temp.add(p);
				}
			}
		}
		return temp;
	}
	
	public void receiveKey(char c){
		for (int i = 0; i < numberOfButtons; ++i) {
			String charVal = String.valueOf(c).toUpperCase();
			if (!buttons[i].isDissolving()) {
				if (charVal.equals(buttons[i].getLetter())) {
					buttons[i].setDissolve();
					++i;
					quality += 100 / numberOfButtons;
					if(i == numberOfButtons){
						minigameController().showSuccessMessage();
					}
				} else if (i > 0) {
					--i;
					maxQuality -= 10;
					if (maxQuality < 0) {
						maxQuality = 0;
					}
				}
				while (i < numberOfButtons) {
					buttons[i].refreshLetter();
					++i;
				}
			}
		}
		secondCounter = 0;
	}

	private void updateTime() {
		if (lastTime != 0) {
			secondCounter += System.currentTimeMillis() - lastTime;
			quality -= 0.1f;
		}
		if (secondCounter >= 1000) {
			for (int i = 1; i < numberOfButtons; ++i) {
				if (buttons[i - 1].isDissolving() && !buttons[i].isDissolving()) {
					buttons[i - 1].refreshLetter();
				}
			}
			secondCounter = 0;
		}
		lastTime = System.currentTimeMillis();
	}

	private void drawProgressBar(Graphics bGraphics) {
		bGraphics.setColor(new Color(25, 25, 25));
		bGraphics.fillRect(0, height - 10, width / 100 * maxQuality, height);
		
		int maxPixelWidth = width / 100 * maxQuality;
		if(maxQuality < 0){
			maxQuality = 0;
		}
		if(quality > maxQuality){
			quality = maxQuality;
		}
		
		int qualityWidth = (int) (width / 100 * quality);
		float interval = 255f / qualityWidth;
		for(int i = 0; i < maxPixelWidth; ++i){
			if(i < qualityWidth){
				int greenVal = (int)(interval * i);
				if(greenVal > 255){
					greenVal = 255;
				}
				bGraphics.setColor(new Color(0, greenVal, 255));
			} else {
				bGraphics.setColor(new Color(5, 5, 5));
			}
			bGraphics.drawLine(i, height - 10, i, height);
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		updateTime();
		BufferedImage bImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics bGraphics = bImg.createGraphics();
		bGraphics.setColor(new Color(15, 15, 15));
		bGraphics.fillRect(0, 0, width, height);

		for (int i = 0; i < numberOfButtons; ++i) {
			ArrayList<MyPoint> temp = buttons[i].getPoints();
			for (MyPoint p : temp) {
				if (buttons[i].dissolve && !p.finished()) {
					p.movePoint();
				}
				if(p.x() > 0 && p.x() < width && p.y() > 0 && p.y() < height){
					bImg.setRGB(p.x(), p.y(), p.color().getRGB());
				}
			}
		}

		drawProgressBar(bGraphics);
		g.drawImage(bImg, 0, 0, null);
	}

	private class MyPoint {
		private float x, y, xDir, yDir;
		private Color color;
		private boolean finished;
		private float steps = 180;
		private float rDif, gDif, bDif, newRed, newGreen, newBlue;

		public MyPoint(float x, float y, float xDir, float yDir, Color color) {
			this.x = x;
			this.y = y;
			this.xDir = xDir;
			this.yDir = yDir;
			this.color = color;
			rDif = ((float)color.getRed() - 15) / steps;
			gDif = ((float)color.getGreen() - 15) / steps;
			bDif = ((float)color.getBlue() - 15) / steps;
			newRed = color.getRed();
			newGreen = color.getGreen();
			newBlue = color.getBlue();
		}

		public Color color() {
			return color;
		}

		public int x() {
			return (int) x;
		}

		public int y() {
			return (int) y;
		}

		public boolean finished() {
			return finished;
		}

		public void movePoint() {
			newRed -= rDif;
			newGreen -= gDif;
			newBlue -= bDif;
			
			if (color.getRed() <= 15) {
				finished = true;
			}
			
			color = new Color((int)newRed, (int)newGreen, (int)newBlue, 255);

			x += xDir;
			y += yDir;			
		}
	}

	private class LetterButton {
		private char currentLetter;
		private ArrayList<MyPoint> points = new ArrayList<MyPoint>();
		private boolean dissolve = false;
		private int letterno;

		public LetterButton(int letterno) {
			this.letterno = letterno;
		}

		public void refreshLetter() {
			currentLetter = alphabet[new Random().nextInt(26)];
			dissolve = false;
			constructPoints(letterno);
		}

		public String getLetter() {
			return String.valueOf(currentLetter);
		}

		public boolean isDissolving() {
			return dissolve;
		}

		public void setPoints(ArrayList<MyPoint> points) {
			this.points = points;
			;
		}

		public void setDissolve() {
			dissolve = true;
		}

		public ArrayList<MyPoint> getPoints() {
			return points;
		}
	}

	@Override
	public void holdKey(char c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void releaseKey(char c) {
		// TODO Auto-generated method stub
		
	}
}