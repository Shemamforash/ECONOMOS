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

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUIElements {
	private static Color backgroundColor = new Color(17, 17, 17);
	private static Color darkColor = new Color(10, 10, 10);
	private static Border emptyBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);

	static class MyButton extends JButton {
		private Color hoverColor = new Color(255, 180, 0);
		private Color pressedColor = new Color(255, 80, 0);
		private Color unpressedColor = new Color(255, 140, 0);
		private boolean selected = false;

		public MyButton() {
			this(null, false);
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
			repaint();
		}

		public MyButton(String text, boolean enabled) {
			super(text);
			setup(enabled);
		}

		public MyButton(String text, boolean enabled, Color hover, Color unpressed) {
			super(text);
			setup(enabled);
			this.hoverColor = hover;
			this.unpressedColor = unpressed;
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
			super.paintComponent(g);
			if (getModel().isPressed() || selected) {
				g.setColor(pressedColor);
			} else if (getModel().isRollover()) {
				g.setColor(hoverColor);
			} else {
				g.setColor(unpressedColor);
			}
			g.fillRect(0, 0, getWidth(), getHeight());
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

	static class PercentageSpinner extends JPanel {
		private int radius = 50;
		private float currentVal = 100;
		private boolean spinning = false;
		private Color buttonColor = new Color(255, 140, 0);
		private PercentageSpinner thisSpinner;

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
				
				//speed = distance / time (distance = 360f) (time = 100 - value)
				int noRevolutions = 3;
				float totalDegrees = noRevolutions * 360f;
				float convertedDegree = totalDegrees / (100f + (float)value);
				int headPosition = (int)(convertedDegree * curValDifference);
				int tailPosition = headPosition - 270;
				
				if(tailPosition < 0 || tailPosition > totalDegrees - 360f){
					tailPosition = 0;
				}
								
				if(tailPosition > 0 && tailPosition < totalDegrees - 360f){
					headPosition = 270;
				}
				
				while(headPosition > 360){
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
			bGraphics.fillOval(this.getWidth() / 2 - (radius - 15), this.getHeight() / 2 - (radius - 15),
					radius * 2 - 30, radius * 2 - 30);

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
			String text = new String((int)currentVal + "%");
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
			setBackground(backgroundColor);
			setForeground(Color.white);
			setFont(new Font("Verdana", Font.BOLD, 12));
			this.setBorder(emptyBorder);
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
}
