package MerchantResources;

import java.util.ArrayList;

public class MerchantResource {
	private MarketResource marketResource;
	private int bought = 0, sold = 0, quantity = 0;
	private float averageSell = 0f, averageBuy = 0f, averageProfit = 0f;
	private ArrayList<ResourcePacket> resourcePackets = new ArrayList<ResourcePacket>();

	public MerchantResource(MarketResource mr) {
		marketResource = mr;
	}

	public void addPacket(ResourcePacket p) {
		for (int i = 1; i < resourcePackets.size(); ++i) {
			int j = i;
			while (j > 0 && resourcePackets.get(j - 1).compareTo(resourcePackets.get(j)) > 0) {
				ResourcePacket temp = resourcePackets.get(j);
				resourcePackets.set(j, resourcePackets.get(j - 1));
				resourcePackets.set(j - 1, temp);
				--j;
			}
		}
	}

	/*
	 * When user clicks sell button this is called with the quantity to sell. It
	 * finds the largest packet size equal to or less than the amount to sell
	 * and sells it. If the number sold does not equal the amount the user wants
	 * to sell it repeats the process. If no packet is found smaller or equal to
	 * the amount remaining to sell it will break down the largest packet into
	 * packets of size 1 and repeat. If there are no packets remaining it simply
	 * breaks and sells no more packets.
	 */
	public float sell(int amount) {
		sold += amount;
		return breakDownPackets(amount, getMarketResource().getSellPrice(1));
	}

	public void buy(int quantity, float price) {
		getMarketResource().updateDesiredThisTick(quantity);
		resourcePackets.add(new ResourcePacket(price, quantity));
		averageBuy = ((averageBuy * bought) + (price * quantity)) / (bought + quantity);
		bought += quantity;
		averageProfit = getPredictedProfit(quantity, price);
		this.quantity += quantity;											//TODO THIS IS THE ERRO :O
	}

	public float getPredictedProfit(int amount, float price) {
		return averageProfit - price / (float) amount;
	}

	public MarketResource getMarketResource() {
		return marketResource;
	}

	public float getAverageProfit() {
		return averageProfit;
	}

	public int getSold() {
		return sold;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getName() {
		return marketResource.getName();
	}
	
	public float breakDownPackets(int amount, float value){
		float moneyGained = 0;
		int remaining = amount;
		while (remaining > 0) {
			for (int i = 0; i < resourcePackets.size(); ++i) {
				ResourcePacket p = resourcePackets.get(i);
				if (p.getQuantity() <= remaining) {
					remaining -= p.getQuantity();
					moneyGained = value * p.getQuantity();
					averageSell = ((averageSell * sold) + (value * p.getQuantity())) / (sold - remaining);
					resourcePackets.remove(i);
				}
			}
			if (resourcePackets.size() > 0) {
				ResourcePacket p = resourcePackets.get(0);
				for (int i = 0; i < p.getQuantity(); ++i) {
					resourcePackets.add(new ResourcePacket(p.getPurchaseCost(), 1));
				}
				resourcePackets.remove(0);
			}
		}
		averageProfit = getPredictedProfit(amount, moneyGained);
		this.quantity -= amount;
		return moneyGained;
	}

	public void consumeResource(int amount) {
		breakDownPackets(amount, 0);
	}
}
