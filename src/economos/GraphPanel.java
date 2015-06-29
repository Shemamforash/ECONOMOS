package economos;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.util.Timer;

import javax.swing.*;

import economos.MarketResource.UpdateResource;

public class GraphPanel extends JPanel{
	
	public GraphPanel(){
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
			ArrayList<Float> temp = marketR.getPrices();
			BufferedImage bImg = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics bGraphics = bImg.createGraphics();
			bGraphics.setColor(Color.GRAY);
			bGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());
			for(int i = 1; i < temp.size(); ++i){
				bGraphics.setColor(Color.RED);
				int curX = (temp.size() - (temp.size() - 1 - i));
				int lastX = (temp.size() - (temp.size() - i));
				
				float priceDiff = marketR.getPriceDiff();
				float minPrice = marketR.getMinPrice();
												
				int curY = (int)(((float)this.getHeight() / marketR.getMaxPrice()) * ((float)temp.get(i) - marketR.getPricePerUnit()));
				int lastY = (int)(((float)this.getHeight() / marketR.getMaxPrice()) * ((float)temp.get(i - 1) - marketR.getPricePerUnit()));
								
				bGraphics.drawLine(this.getWidth() - lastX, lastY, this.getWidth() - curX, curY);
			}
			
			g.drawImage(bImg, 0, 0, null);
		}
	}
}
