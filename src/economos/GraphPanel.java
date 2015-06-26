package economos;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;

public class GraphPanel extends JPanel{
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		UserResource userR = GUIManager.getSelectedResource();
		
		if(userR != null){
			MarketResource marketR = userR.getMarketResource();
			ArrayList<Integer> temp = marketR.getPrices();
			BufferedImage bImg = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics bGraphics = bImg.createGraphics();
			bGraphics.setColor(Color.WHITE);
			bGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());
			for(int i = 1; i < temp.size(); ++i){
				bGraphics.setColor(Color.RED);
				int curX = (temp.size() - i - 1) * 10;
				int lastX = (temp.size() - i) * 10;
				
				int curY = (int)(((float)this.getHeight() / (float)marketR.getPriceDiff()) * ((float)temp.get(i) - (float)marketR.getMinPrice()));
				int lastY = (int)(((float)this.getHeight() / (float)marketR.getPriceDiff()) * ((float)temp.get(i - 1) - (float)marketR.getMinPrice()));
								
				bGraphics.drawLine(this.getWidth() - lastX, lastY, this.getWidth() - curX, curY);
			}
			
			g.drawImage(bImg, 0, 0, null);
		}
	}
}
