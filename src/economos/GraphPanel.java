package economos;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;

public class GraphPanel extends JPanel{
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    ((Graphics2D) g).setRenderingHints(rh);
		
		UserResource userR = EconomosGUI.getSelectedResource();
		
		if(userR != null){
			MarketResource marketR = userR.getMarketResource();
			ArrayList<Float> previousPrices = marketR.getPrices();
			BufferedImage bImg = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics bGraphics = bImg.createGraphics();
			bGraphics.setColor(Color.GRAY);
			bGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());
			for(int i = previousPrices.size() - 1; i > 0; --i){
				bGraphics.setColor(Color.RED);
				
				int curX = i;
				int lastX = i - 1;
												
				float pricePerPixel = (float)this.getHeight() / (marketR.getMaxPrice() - marketR.getMinPrice());
				int curY = (int)(pricePerPixel * (float)previousPrices.get(i));
				int lastY = (int)(this.getHeight() / (marketR.getMaxPrice() - marketR.getMinPrice()) * previousPrices.get(i - 1));
								
				bGraphics.drawLine(lastX, this.getHeight() - lastY, curX, this.getHeight() - curY);
			}
			
			g.drawImage(bImg, 0, 0, null);
		}
	}
}
