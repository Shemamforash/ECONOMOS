package GUI.Crafter;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import CraftingResources.CraftingResource;
import GUI.GUIElements;
import GUI.GuildPanel.GuildPanel;
import MerchantResources.Resource;
import economos.SelectedResourceCaller;
import economos.SelectedResourceListener;

public class MinigameController extends JPanel implements SelectedResourceListener {
	private int						width, height;
	private String					currentGuild;
	private GUIElements.MyButton	craftingButton;
	private ButtonMasher			masherGame;
	private Forger					forgerGame;
	private CraftingResource currentResource;

	public int width() {
		return width;
	}

	public int height() {
		return height;
	}

	public MinigameController(){
		this(0, 0);
		System.out.println("We should go back!");
	}
	
	public MinigameController(int width, int height) {
		this.width = width;
		this.height = height;
		this.setLayout(new CardLayout(0, 0));
		craftingButton = new GUIElements.MyButton("Start Crafting", true);
		craftingButton.addActionListener(arg0 -> {
			if(currentResource != null && currentResource.canCraft()){
				currentResource.craft();
			}
            // Check player can craft before switch
//				switchPanel("Smith");
//				switchPanel("Embroider");

        });
		this.add(craftingButton, "Button");
		masherGame = new ButtonMasher(12, this);
		this.add(masherGame, "Masher");
		forgerGame = new Forger(this);
		this.add(forgerGame, "Forger");
		this.add(new SuccessMessage(true), "Success");
		SelectedResourceCaller.addListener(this, GuildPanel.PanelType.CRAFTING);
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

	@Override
	public void selectedResourceChanged(Resource m) {
		currentResource = (CraftingResource)m;
	}

	@Override
	public void selectedGuildChanged(String g) {

	}

	private class SuccessMessage extends GUIElements.MyPanel {
		public SuccessMessage(boolean darker) {
			super(darker);
		}

		public SuccessMessage() {
			this(true);
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
