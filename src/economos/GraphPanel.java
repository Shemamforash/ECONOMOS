package economos;

import java.awt.*;
import java.awt.image.*;
import java.text.NumberFormat;
import java.util.*;
import javax.swing.*;

public class GraphPanel extends JPanel {
	private NumberFormat d = NumberFormat.getIntegerInstance();
	private ArrayList<MarketResource.MarketSnapshot> marketHistory;
	private Graphics bGraphics;
	private MarketResource marketResource;
	private int offset = 0;
	
	public GraphPanel(){
		setBackground(new Color(20, 20, 20));
		d.setMaximumFractionDigits(2);
	}
	
	public void drawGrid(){
		int width = this.getWidth() / 16;
		if(marketResource != null && marketResource.getMarketHistory().size() >= this.getWidth()){
			if(offset == width){
				offset = 0;
			} else {
				++offset;
			}
		}
		bGraphics.setColor(new Color(35, 35, 35));
		for(int i = offset; i < this.getWidth(); i += width){
			bGraphics.drawLine(i, 0, i, this.getHeight());
		} 
		for (int i = 0; i < this.getHeight(); i += width){
			bGraphics.drawLine(0, i, this.getWidth(), i);
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		((Graphics2D) g).setRenderingHints(rh);
		UserResource userR = EconomosGUI.getCurrentResource();

		if (userR != null) {
			marketResource = userR.getMarketResource();
			marketHistory = marketResource.getMarketHistory();
			BufferedImage bImg = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
			bGraphics = bImg.createGraphics();
			drawGrid();
			float pricePerPixel = (float) this.getHeight() / (marketResource.getMaxPrice() - marketResource.getMinPrice());
			((Graphics2D) bGraphics).setRenderingHints(
					new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

			plotLine(pricePerPixel, 1, marketResource.getMinPrice());
			
			pricePerPixel = (float)this.getHeight() / (marketResource.getMaxSupply());// - marketResource.getMinSupply());
			
			plotLine(pricePerPixel, 2, 0);
			
			pricePerPixel = (float)this.getHeight() / (marketResource.getMaxDemand());// - marketResource.getMinDemand());

			plotLine(pricePerPixel, 3, 0);
			
			bGraphics.setColor(Color.white);
			bGraphics.setFont(new Font("Verdana", Font.BOLD, 14));
			bGraphics.drawString("LOW: " + d.format(marketResource.getMinPrice()) + "C", 5, this.getHeight() - 10);
			bGraphics.drawString("HIGH: " + d.format(marketResource.getMaxPrice()) + "C", 5, 20);
			bGraphics.drawString("MID: " + d.format((marketResource.getMinPrice() + marketResource.getMaxPrice()) / 2) + "C", 5, this.getHeight() / 2);
			
			g.drawImage(bImg, 0, 0, null);
		} else {
			BufferedImage bImg = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
			bGraphics = bImg.createGraphics();
			drawGrid();
			g.drawImage(bImg, 0, 0, null);
		}
	}
	
	private void plotLine(float pricePerPixel, int color, float min){
		for (int i = marketHistory.size() - 1; i > 0; --i) {
			int curY, lastY;
			int curX = this.getWidth() - i;
			int lastX = this.getWidth() - (i - 1);
			
			if(color == 1){
				((Graphics2D) bGraphics).setStroke(new BasicStroke(2));
				curY = (int) (pricePerPixel * (marketHistory.get(i).getPrice() - min));
				lastY = (int) (pricePerPixel * (marketHistory.get(i - 1).getPrice() - min));
				bGraphics.setColor(new Color(255, 150 - i / 5, 0, 50));
				bGraphics.drawLine(curX, this.getHeight() - curY, curX, this.getHeight());
				bGraphics.setColor(new Color(255, 150 - i / 5, 0));
			} else if (color == 2) {
				((Graphics2D) bGraphics).setStroke(new BasicStroke(1));
				bGraphics.setColor(new Color(200 - i / 5, 200 - i / 5, 255));//, 255 - (672 - i) / 3));
				curY = (int) (pricePerPixel * (marketHistory.get(i).getSupply() - min));
				lastY = (int) (pricePerPixel * (marketHistory.get(i - 1).getSupply() - min));
			} else {			
				((Graphics2D) bGraphics).setStroke(new BasicStroke(1));
				bGraphics.setColor(new Color(100, 255, 255 - i / 5));//, 255 - (672 - i) / 3));
				curY = (int) (pricePerPixel * (marketHistory.get(i).getDemand() - min));
				lastY = (int) (pricePerPixel * (marketHistory.get(i - 1).getDemand() - min));
			}

			bGraphics.drawLine(lastX, this.getHeight() - lastY, curX, this.getHeight() - curY);
		}
	}
}
