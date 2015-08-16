package economos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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
		
		public void setSelected(boolean selected){
			this.selected = selected;
			repaint();
		}

		public MyButton(String text, boolean enabled) {
			super(text);
			setup(enabled);			
		}
		
		public MyButton(String text, boolean enabled, Color hover, Color unpressed){
			super(text);
			setup(enabled);
			this.hoverColor = hover;
			this.unpressedColor = unpressed;
		}
		
		public void setup(boolean enabled){
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
			if (getModel().isPressed()|| selected) {
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
