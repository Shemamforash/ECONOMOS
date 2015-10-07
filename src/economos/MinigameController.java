package economos;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class MinigameController extends JPanel {
	private int						width, height;
	private String					currentGuild;
	private GUIElements.MyButton	craftingButton;
	private ButtonMasher			masherGame;
	private Forger					forgerGame;

	public int width() {
		return width;
	}

	public int height() {
		return height;
	}

	public MinigameController(int width, int height) {
		this.width = width;
		this.height = height;
		this.setLayout(new CardLayout(0, 0));
		craftingButton = new GUIElements.MyButton("Start Crafting", true);
		craftingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Check player can craft before switch
				switchPanel("Smith");
//				switchPanel("Embroider");
			}
		});
		this.add(craftingButton, "Button");
		masherGame = new ButtonMasher(12, this);
		this.add(masherGame, "Masher");
		forgerGame = new Forger(this);
		this.add(forgerGame, "Forger");
		this.add(new SuccessMessage(true), "Success");
	}

	public void setCurrentGuild(String currentGuild) {
		this.currentGuild = currentGuild;
	}

	private void switchPanel(String str) {
		CardLayout cardLayout = (CardLayout) this.getLayout();
		switch (str) {
			case "Embroider":
				cardLayout.show(this, "Masher");
				masherGame.requestFocus();
				break;
			case "Smith":
				cardLayout.show(this, "Forger");
				forgerGame.requestFocus();
				break;
			default:
				cardLayout.show(this, "Button");
				break;
		}
	}

	public void showSuccessMessage() {
		CardLayout cardLayout = (CardLayout) this.getLayout();
		cardLayout.show(this, "Success");
	}

	private class SuccessMessage extends GUIElements.MyPanel {
		public SuccessMessage(boolean darker) {
			super(darker);
		}

		private Color	originalColor	= Color.white;

		public void reset() {
			originalColor = Color.white;
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			BufferedImage bImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics bGraphics = bImg.createGraphics();
			bGraphics.setColor(new Color(10, 10, 10));
			bGraphics.fillRect(0, 0, width, height);
			bGraphics.setColor(originalColor);
			originalColor = new Color(255, 255, 255, originalColor.getAlpha() - 5);

			bGraphics.setFont(new Font("Verdana", Font.BOLD, 40));
			String text = new String("Success!");
			int stringLen = (int) bGraphics.getFontMetrics().getStringBounds(text, bGraphics).getWidth();
			int start = width / 2 - stringLen / 2;
			bGraphics.drawString(text, start, this.getHeight() / 2 - 25);

			if (originalColor.getAlpha() == 0) {
				switchPanel("");
			}

			g.drawImage(bImg, 0, 0, null);
		}
	}
}
