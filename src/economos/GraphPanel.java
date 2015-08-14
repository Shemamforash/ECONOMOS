package economos;

import java.awt.*;
import java.awt.image.*;
import java.text.NumberFormat;
import java.util.*;
import javax.swing.*;

public class GraphPanel extends JPanel {
	private NumberFormat d = NumberFormat.getIntegerInstance();

	public GraphPanel(){
		d.setMaximumFractionDigits(2);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		((Graphics2D) g).setRenderingHints(rh);

		UserResource userR = EconomosGUI.getSelectedResource();

		if (userR != null) {
			MarketResource marketR = userR.getMarketResource();
			ArrayList<Float> previousPrices = marketR.getPrices();
			BufferedImage bImg = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics bGraphics = bImg.createGraphics();
			bGraphics.setColor(new Color(35, 35, 35));
			bGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());
			float pricePerPixel = (float) this.getHeight() / (marketR.getMaxPrice() - marketR.getMinPrice());
			((Graphics2D) bGraphics).setStroke(new BasicStroke(2));
			((Graphics2D) bGraphics).setRenderingHints(
					new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

			for (int i = previousPrices.size() - 1; i > 0; --i) {
				bGraphics.setColor(new Color(255, 150 - i / 5, 0));

				int curX = this.getWidth() - i;
				int lastX = this.getWidth() - (i - 1);

				int curY = (int) (pricePerPixel * (previousPrices.get(i) - marketR.getMinPrice()));
				int lastY = (int) (pricePerPixel * (previousPrices.get(i - 1) - marketR.getMinPrice()));

				bGraphics.drawLine(lastX, this.getHeight() - lastY, curX, this.getHeight() - curY);
			}

			bGraphics.setColor(Color.white);
			bGraphics.setFont(new Font("Verdana", Font.BOLD, 14));
			bGraphics.drawString("LOW: " + d.format(marketR.getMinPrice()) + "C", 5, this.getHeight() - 10);
			bGraphics.drawString("HIGH: " + d.format(marketR.getMaxPrice()) + "C", 5, 20);
			bGraphics.drawString("MID: " + d.format((marketR.getMinPrice() + marketR.getMaxPrice()) / 2) + "C", 5, this.getHeight() / 2);

			g.drawImage(bImg, 0, 0, null);
		}
	}
}
