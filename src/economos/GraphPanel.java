package economos;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.util.Timer;

import javax.swing.*;

import economos.MarketResource.UpdateResource;

public class GraphPanel extends JPanel{
	public boolean isSellGraph;	
	
	public GraphPanel(boolean isSellGraph){
		this.isSellGraph = isSellGraph;
		Timer t = new Timer();
		t.schedule(new UpdateGUI(this), 0, 1000);
	}
	
	class UpdateGUI extends TimerTask{
		GraphPanel p;
		
		public UpdateGUI(GraphPanel p){
			this.p = p;
		}
		
		public void run(){
			p.repaint();
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		UserResource userR = EconomosMain.getSelectedResource();
		
		if(userR != null){
			MarketResource marketR = userR.getMarketResource();
			ArrayList<Integer> temp;
			if(isSellGraph){
				temp = marketR.getSellPrices();
			} else {
				temp = marketR.getBuyPrices();
			}
			BufferedImage bImg = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics bGraphics = bImg.createGraphics();
			bGraphics.setColor(Color.GRAY);
			bGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());
			for(int i = 1; i < temp.size(); ++i){
				bGraphics.setColor(Color.RED);
				int curX = (temp.size() - i - 1);
				int lastX = (temp.size() - i);
				
				float priceDiff, minPrice;
				
				if(isSellGraph){
					priceDiff = marketR.getSellDiff();
					minPrice = (float)marketR.getMinSell();
				} else {
					priceDiff = marketR.getBuyDiff();
					minPrice = (float)marketR.getMinBuy();
				}
								
				int curY = (int)(((float)this.getHeight() / priceDiff) * ((float)temp.get(i) - minPrice));
				int lastY = (int)(((float)this.getHeight() / priceDiff) * ((float)temp.get(i - 1) - minPrice));
								
				bGraphics.drawLine(this.getWidth() - lastX, lastY, this.getWidth() - curX, curY);
			}
			
			g.drawImage(bImg, 0, 0, null);
		}
	}
}
