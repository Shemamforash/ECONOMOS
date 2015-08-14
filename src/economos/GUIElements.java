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
	private static Color backgroundColor = new Color(35, 35, 35);

	static class MyButton extends JButton {
		private Color hoverColor = new Color(255, 180, 0);
		private Color pressedColor = new Color(255, 80, 0);
		private Color unpressedColor = new Color(255, 140, 0);

		public MyButton() {
			this(null);
		}

		public MyButton(String text) {
			super(text);
			super.setContentAreaFilled(false);
			setForeground(Color.white);
			setFont(new Font("Verdana", Font.BOLD, 12));
		}

		public void setBorder(Border border) {
		}

		protected void paintComponent(Graphics g) {
			if (getModel().isPressed()) {
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
		public MyPanel() {
			setBackground(backgroundColor);
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
		}

		public void setBorder(Border border) {
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
		}

		public void setBorder(Border border) {
		}
	}

	public static class MyTabbedPane extends JTabbedPane {
		final MyTabbedPane tabbedPane;

		public MyTabbedPane(int tabPlacement) {
			super(tabPlacement);
			tabbedPane = this;
			setBackground(backgroundColor);
			setForeground(Color.white);
			setFont(new Font("Verdana", Font.BOLD, 12));

			this.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					int noTabs = tabbedPane.getTabCount();
					for (int i = 0; i < noTabs; ++i) {
						if (i == tabbedPane.getSelectedIndex()) {
							tabbedPane.setBackgroundAt(i, new Color(255, 140, 0));
						} else {
							tabbedPane.setBackgroundAt(i, new Color(50, 50, 50));
						}
						tabbedPane.setForegroundAt(i, Color.white);
					}
				}
			});
		}


		public void setBorder(Border border) {
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
		}

		public void setBorder(Border border) {
		}
	}
}
