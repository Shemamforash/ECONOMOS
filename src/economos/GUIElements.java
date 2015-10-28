package economos;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.text.Format;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUIElements {
	private static Color	backgroundColor	= new Color(17, 17, 17);
	static Color			darkColor		= new Color(10, 10, 10);
	private static Border	emptyBorder		= BorderFactory.createEmptyBorder(10, 10, 10, 10);
	static Color			goldenOrange	= new Color(255, 140, 0);

	static class MyButton extends JButton {
		private Color	hoverColor		= new Color(255, 180, 0);
		private Color	pressedColor	= new Color(255, 80, 0);
		private Color	unpressedColor	= new Color(255, 140, 0);
		private boolean	selected		= false, inList = false;

		public MyButton() {
			this(null, false);
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		public MyButton(String text, boolean enabled) {
			super(text);
			setup(enabled);
		}

		public MyButton(String text, boolean enabled, Color hover, Color unpressed, boolean inList) {
			super(text);
			setup(enabled);
			this.hoverColor = hover;
			this.unpressedColor = unpressed;
			this.inList = inList;
		}

		public void setup(boolean enabled) {
			super.setContentAreaFilled(false);
			setEnabled(enabled);
			setForeground(Color.white);
			setFont(new Font("Verdana", Font.BOLD, 12));
			setFocusPainted(false);
		}

		public void setBorder(Border border) {
		}

		protected void paintComponent(Graphics g) {
			if (getModel().isPressed() || selected) {
				if(inList){
					g.setColor(pressedColor);
					g.fillRect(getWidth() - 4, 0, 4, getHeight());
				}
				g.setColor(pressedColor);
			} else if (getModel().isRollover()) {
				g.setColor(hoverColor);
				g.fillRect(0, 0, getWidth(), getHeight());
			} else {
				g.setColor(unpressedColor);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
			if (inList) {
				g.setColor(new Color(40, 40, 40));
				g.drawLine(5, 0, getWidth() - 10, 0);
				g.drawLine(5, getHeight(), getWidth() - 10, getHeight());
			}
			super.paintComponent(g);
		}

		public void setContentAreaFilled(boolean b) {
		}
	}

	static class MyPanel extends JPanel {
		public MyPanel(boolean darker) {
			if (darker) {
				setBackground(darkColor);
			} else {
				setBackground(backgroundColor);
			}
		}

		public void setBorder(Border border) {
		}
	}

	static class EvaporatingButton extends JPanel {
		private BoxTower[]	towers	= new BoxTower[10];
		private boolean		ready	= false;

		public EvaporatingButton() {
			super();
			for (int i = 0; i < 10; ++i) {
				towers[i] = new BoxTower(i * 10, 10, 200);
			}
		}

		private void resetBoxes() {
			for (int i = 0; i < 10; ++i) {
				towers[i].resetTower();
			}
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			BufferedImage bImg = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics bGraphics = bImg.createGraphics();
			bGraphics.setColor(new Color(15, 15, 15));
			bGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());

			boolean finishedDrawing = true;
			for (int i = 0; i < 10; ++i) {
				towers[i].drawBoxes(bGraphics);
				if (!towers[i].isTowerComplete()) {
					finishedDrawing = false;
				}
			}

			if (finishedDrawing) {
				resetBoxes();
				ready = true;
			}

			g.drawImage(bImg, 0, 0, null);
		}

		private class BoxTower {
			private Box[]	tower			= new Box[10];
			private int		x, boxHeight;
			private boolean	towerComplete	= false;

			public BoxTower(int x, int boxHeight, int panelHeight) {
				this.x = x;
				this.boxHeight = boxHeight;
				for (int i = tower.length - 1; i >= 0; --i) {
					tower[i] = new Box(panelHeight - (i * boxHeight));
				}
			}

			public void resetTower() {
				for (int i = tower.length - 1; i >= 0; --i) {
					tower[i].resetY();
				}
				towerComplete = false;
			}

			public boolean isTowerComplete() {
				return towerComplete;
			}

			public void drawBoxes(Graphics g) {
				moveTower();
				for (int i = tower.length - 1; i >= 0; --i) {
					g.setColor(tower[i].getColor());
					g.fillRect(x, tower[i].getY(), boxHeight, boxHeight);
				}
			}

			private void moveTower() {
				towerComplete = true;
				for (int i = tower.length - 1; i >= 0; --i) {
					tower[i].increaseHeight();
					if (!tower[i].movedEnough) {
						for (int j = i - 1; j >= 0; --j) {
							if (tower[j].getY() - tower[j + 1].getY() > 35) {
								tower[j].increaseHeight();
							}
						}
						towerComplete = false;
						break;
					}
				}
			}
		}

		private class Box {
			private Color	color		= new Color(255, 140, 0, 255);
			private int		y, yCounter = 0;
			private boolean	movedEnough	= false;

			public Box(int y) {
				this.y = y;
			}

			public void resetY() {
				yCounter = 0;
				movedEnough = false;
			}

			public int getY() {
				return y - yCounter;
			}

			public boolean getHasMoved() {
				return movedEnough;
			}

			public void increaseHeight() {
				if (color.getAlpha() > 0) {
					yCounter += new Random().nextInt(5);
					if (yCounter >= 51) {
						if (movedEnough != true) {
							movedEnough = true;
						}
						yCounter = 51;
					}
				}
			}

			public Color getColor() {
				return new Color(255, 140, 0, 255 - (5 * yCounter));
			}
		}
	}

	static class PercentageSpinner extends JPanel {
		private int					radius;
		private float				currentVal	= 100;
		private boolean				spinning	= false;
		private Color				buttonColor	= new Color(255, 140, 0);
		private PercentageSpinner	thisSpinner;

		public PercentageSpinner(final int radius) {
			this.radius = radius;
			thisSpinner = this;
			setBackground(darkColor);
			this.addMouseListener(new MouseListener() {
				public boolean isInButton(MouseEvent e) {
					float xPos = e.getX() - thisSpinner.getWidth() / 2;
					float yPos = e.getY() - thisSpinner.getHeight() / 2;
					int distanceToCentre = (int) Math.sqrt(xPos * xPos + yPos * yPos);
					if (distanceToCentre < radius / 2) {
						return true;
					}
					return false;
				}

				public void mouseClicked(MouseEvent e) {
					if (isInButton(e) && !spinning) {
						buttonColor = new Color(255, 80, 0);
						currentVal = 0;
						spinning = true;
					} else if (spinning) {
						buttonColor = new Color(30, 30, 30);
					} else {
						buttonColor = new Color(255, 140, 0);
					}
				}

				public void mouseEntered(MouseEvent e) {
					if (isInButton(e) && !spinning) {
						buttonColor = new Color(255, 180, 0);
					} else if (spinning) {
						buttonColor = new Color(30, 30, 30);
					} else {
						buttonColor = new Color(255, 140, 0);
					}
				}

				public void mouseExited(MouseEvent e) {
					if (spinning) {
						buttonColor = new Color(30, 30, 30);
					} else {
						buttonColor = new Color(255, 140, 0);
					}
				}

				public void mousePressed(MouseEvent e) {
					if (isInButton(e) && !spinning) {
						currentVal = 0;
						spinning = true;
						buttonColor = new Color(255, 80, 0);
					} else if (spinning) {
						buttonColor = new Color(30, 30, 30);
					} else {
						buttonColor = new Color(255, 140, 0);
					}
				}

				public void mouseReleased(MouseEvent e) {
					buttonColor = new Color(255, 140, 0);
				}
			});
		}

		public void drawHollowArc(Graphics bGraphics, int radiusOffset, int value) {
			float curValDifference = currentVal + value;
			if (curValDifference > 100) {
				curValDifference = 100;
			}

			if (curValDifference >= 0) {
				bGraphics.setColor(new Color(255, (int) (curValDifference * 1.8f), 0));

				int xOrigin = this.getWidth() / 2 - (radius - radiusOffset);
				int yOrigin = this.getHeight() / 2 - (radius - radiusOffset);
				int newRadius = (radius - radiusOffset) * 2;

				// speed = distance / time (distance = 360f) (time = 100 -
				// value)
				int noRevolutions = 3;
				float totalDegrees = noRevolutions * 360f;
				float convertedDegree = totalDegrees / (100f + (float) value);
				int headPosition = (int) (convertedDegree * curValDifference);
				int tailPosition = headPosition - 270;

				if (tailPosition < 0 || tailPosition > totalDegrees - 360f) {
					tailPosition = 0;
				}

				if (tailPosition > 0 && tailPosition < totalDegrees - 360f) {
					headPosition = 270;
				}

				while (headPosition > 360) {
					headPosition -= 360;
				}

				bGraphics.fillArc(xOrigin, yOrigin, newRadius, newRadius, -tailPosition + 90, -headPosition);

				bGraphics.setColor(darkColor);

				xOrigin = xOrigin + 6;
				yOrigin = yOrigin + 6;
				newRadius = newRadius - 12;
				bGraphics.fillOval(xOrigin, yOrigin, newRadius, newRadius);
			}
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			BufferedImage bImg = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics bGraphics = bImg.createGraphics();
			((Graphics2D) bGraphics).setRenderingHints(rh);

			bGraphics.setColor(new Color(30, 30, 30));
			bGraphics.fillOval(this.getWidth() / 2 - (radius - 15), this.getHeight() / 2 - (radius - 15), radius * 2 - 30, radius * 2 - 30);

			drawHollowArc(bGraphics, 0, 0);
			drawHollowArc(bGraphics, 7, -5);
			drawHollowArc(bGraphics, 14, -10);
			drawHollowArc(bGraphics, 21, -15);
			drawHollowArc(bGraphics, 28, -20);

			bGraphics.setColor(darkColor);

			if (spinning) {
				currentVal += 0.25f;
			}
			if (currentVal >= 100f) {
				spinning = false;
				currentVal = 100f;
				buttonColor = new Color(255, 140, 0);
			}

			bGraphics.setColor(buttonColor);
			bGraphics.fillOval(this.getWidth() / 2 - radius / 2, this.getHeight() / 2 - radius / 2, radius, radius);
			bGraphics.setFont(new Font("Verdana", Font.BOLD, 16));
			bGraphics.setColor(Color.white);
			String text = new String((int) currentVal + "%");
			if (currentVal == 100) {
				text = "Ready!";
			}

			int stringLen = (int) bGraphics.getFontMetrics().getStringBounds(text, bGraphics).getWidth();
			int start = getWidth() / 2 - stringLen / 2;
			bGraphics.drawString(text, start, this.getHeight() / 2 + 5);

			g.drawImage(bImg, 0, 0, null);
		}
	}

	static class MyTextField extends JTextField {
		public MyTextField() {
			this(null);
		}

		public MyTextField(String text) {
			super(text);
			setForeground(Color.white);
			setFont(new Font("Verdana", Font.BOLD, 14));
			this.setBorder(emptyBorder);
			setDarker();
		}

		public void setText(String text) {
			super.setText(text.toUpperCase());
		}

		private void setDarker() {
			setBackground(backgroundColor);
		}

		public void setColor(Color c) {
			if (c != null) {
				setBackground(c);
			} else {
				setDarker();
			}
		}
	}

	static class MyFormattedTextField extends JFormattedTextField {
		public MyFormattedTextField() {
			setup();
		}

		public MyFormattedTextField(Format format) {
			super(format);
			setup();
		}

		public MyFormattedTextField(Object value) {
			super(value);
			setup();
		}

		private void setup() {
			setBackground(new Color(50, 50, 50));
			setForeground(Color.white);
			setFont(new Font("Verdana", Font.BOLD, 12));
			this.setBorder(emptyBorder);
		}
	}

	public static class MyTextArea extends JTextArea {
		public MyTextArea() {
			this(null);
		}

		public MyTextArea(String text) {
			super(text);
			setBackground(backgroundColor);
			setForeground(Color.white);
			setFont(new Font("Verdana", Font.BOLD, 12));
			this.setBorder(emptyBorder);
		}
	}

	public static class MyList extends JList {
		public MyList(MyButton[] arr) {
			super(arr);
			setBackground(backgroundColor);
			setForeground(Color.white);
			setFont(new Font("Verdana", Font.BOLD, 12));
			this.setBorder(emptyBorder);
		}
	}

	public static class BuySellButton extends MyPanel {
		private MyButton					transactionButton;
		private MyButtonExtended			quantityOne, quantityTwo, quantityFive, quantityTen, lastButton;
		private MyTextField					priceField;

		class MyButtonExtended extends MyButton {
			private MyButtonExtended thisButton;
			public MyButtonExtended(String s) {
				super(s, true);
				thisButton = this;
				this.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(lastButton != null){
							lastButton.setSelected(false);
						}
						lastButton = thisButton;
						setSelected(true);
					}
				});
			}
		}
		
		public void reset(){
			if(lastButton != null){
				lastButton.setSelected(false);
				lastButton = null;
			}
			priceField.setText("C N/A");
			repaint();
		}

		public BuySellButton(String text) {
			super(true);
			SpringLayout springLayout = new SpringLayout();
			setLayout(springLayout);
			add(transactionButton = new MyButton(text.toUpperCase(), true));
			add(quantityOne = new MyButtonExtended("1"));
			quantityOne.setSelected(true);
			lastButton = quantityOne;
			add(quantityTwo = new MyButtonExtended("2"));
			add(quantityFive = new MyButtonExtended("5"));
			add(quantityTen = new MyButtonExtended("10"));
			add(priceField = new MyTextField());
			reset();
		}

		public int getSelectedQuantity() {
			if(lastButton != null){
				return Integer.parseInt(lastButton.getText()); 
			}	
			return 1;
		}

		public void setText(String s) {
			priceField.setText(s);
		}

		public void addActionListener(ActionListener a) {
			transactionButton.addActionListener(a);
		}

		public void updateConstraints(int buttonWidth) {
			SpringLayout springLayout = (SpringLayout) getLayout();

			springLayout.putConstraint(SpringLayout.NORTH, transactionButton, 0, SpringLayout.NORTH, this);
			springLayout.putConstraint(SpringLayout.WEST, transactionButton, 0, SpringLayout.WEST, this);
			springLayout.putConstraint(SpringLayout.SOUTH, transactionButton, 0, SpringLayout.SOUTH, this);
			springLayout.putConstraint(SpringLayout.EAST, transactionButton, buttonWidth, SpringLayout.WEST, this);

			springLayout.putConstraint(SpringLayout.NORTH, quantityOne, 0, SpringLayout.NORTH, transactionButton);
			springLayout.putConstraint(SpringLayout.WEST, quantityOne, 10, SpringLayout.EAST, transactionButton);
			springLayout.putConstraint(SpringLayout.SOUTH, quantityOne, 0, SpringLayout.SOUTH, transactionButton);
			springLayout.putConstraint(SpringLayout.EAST, quantityOne, buttonWidth, SpringLayout.EAST, transactionButton);

			springLayout.putConstraint(SpringLayout.NORTH, quantityTwo, 0, SpringLayout.NORTH, quantityOne);
			springLayout.putConstraint(SpringLayout.WEST, quantityTwo, 10, SpringLayout.EAST, quantityOne);
			springLayout.putConstraint(SpringLayout.SOUTH, quantityTwo, 0, SpringLayout.SOUTH, quantityOne);
			springLayout.putConstraint(SpringLayout.EAST, quantityTwo, buttonWidth, SpringLayout.EAST, quantityOne);

			springLayout.putConstraint(SpringLayout.NORTH, quantityFive, 0, SpringLayout.NORTH, quantityTwo);
			springLayout.putConstraint(SpringLayout.WEST, quantityFive, 10, SpringLayout.EAST, quantityTwo);
			springLayout.putConstraint(SpringLayout.SOUTH, quantityFive, 0, SpringLayout.SOUTH, quantityTwo);
			springLayout.putConstraint(SpringLayout.EAST, quantityFive, buttonWidth, SpringLayout.EAST, quantityTwo);

			springLayout.putConstraint(SpringLayout.NORTH, quantityTen, 0, SpringLayout.NORTH, quantityFive);
			springLayout.putConstraint(SpringLayout.WEST, quantityTen, 10, SpringLayout.EAST, quantityFive);
			springLayout.putConstraint(SpringLayout.SOUTH, quantityTen, 0, SpringLayout.SOUTH, quantityFive);
			springLayout.putConstraint(SpringLayout.EAST, quantityTen, buttonWidth, SpringLayout.EAST, quantityFive);

			springLayout.putConstraint(SpringLayout.NORTH, priceField, 0, SpringLayout.NORTH, quantityTen);
			springLayout.putConstraint(SpringLayout.WEST, priceField, 10, SpringLayout.EAST, quantityTen);
			springLayout.putConstraint(SpringLayout.SOUTH, priceField, 0, SpringLayout.SOUTH, quantityTen);
			springLayout.putConstraint(SpringLayout.EAST, priceField, 0, SpringLayout.EAST, this);
			
			repaint();
		}
	}
}
