package GUI;

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
	private static Color backgroundColor = new Color(17, 17, 17);
	public static Color darkColor = new Color(10, 10, 10);
	private static Border emptyBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
	public static Color goldenOrange = new Color(255, 140, 0);

	public static class MyButton extends JButton {
		private Color hoverColor = new Color(255, 180, 0);
		private Color pressedColor = new Color(255, 80, 0);
		private Color unpressedColor = new Color(255, 140, 0);
		private boolean selected = false, inList = false;

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
				if (inList) {
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

	public static class MyPanel extends JPanel {
		public MyPanel() {
			this(true);
		}

		public MyPanel(boolean darker) {
			super();
			if (darker) {
				setBackground(darkColor);
			} else {
				setBackground(backgroundColor);
			}
		}

		public void setBorder(Border border) {
		}
	}

	public static class MyTextField extends JTextField {
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

	public static class MyFormattedTextField extends JFormattedTextField {
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
		public MyList() {
			this(new MyButton[0]);
		}

		public MyList(MyButton[] arr) {
			super(arr);
			setBackground(backgroundColor);
			setForeground(Color.white);
			setFont(new Font("Verdana", Font.BOLD, 12));
			this.setBorder(emptyBorder);
		}
	}

	public static class BuySellButton extends MyPanel {
		private MyButton transactionButton;
		private MyButtonExtended quantityOne, quantityTwo, quantityFive, quantityTen, lastButton;
		private MyTextField priceField;

		class MyButtonExtended extends MyButton {
			private MyButtonExtended thisButton;

			public MyButtonExtended() {
				this("");
			}

			public MyButtonExtended(String s) {
				super(s, true);
				thisButton = this;
				this.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (lastButton != null) {
							lastButton.setSelected(false);
						}
						lastButton = thisButton;
						setSelected(true);
					}
				});
			}
		}

		public void reset() {
			if (lastButton != null) {
				lastButton.setSelected(false);
				lastButton = null;
			}
			priceField.setText("C N/A");
			repaint();
		}

		public BuySellButton() {
			this("");
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
			if (lastButton != null) {
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
			springLayout.putConstraint(SpringLayout.EAST, quantityOne, buttonWidth, SpringLayout.EAST,
					transactionButton);

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
