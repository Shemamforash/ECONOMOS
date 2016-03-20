package MerchantResources;

import java.util.Comparator;

import economos.User;

public class ResourcePacket implements Comparable<ResourcePacket>{
	private float purchaseCost;
	private int quantity;
	
	public ResourcePacket(float purchaseCost, int quantity){
		this.purchaseCost = purchaseCost;
		this.quantity = quantity;
	}
	
	public float getPurchaseCost(){
		return purchaseCost;
	}
	
	public int getQuantity(){
		return quantity;
	}
	
	public float getPacketValue(){
		return purchaseCost * quantity;
	}

	@Override
	public int compareTo(ResourcePacket other) {
		if(quantity > other.getQuantity()){
			return -1;
		} else if (quantity < other.getQuantity()){
			return 1;
		} else if (getPacketValue() > other.getPacketValue()){
			return -1;
		} else if (getPacketValue() < other.getPacketValue()){
			return 1;
		}
		return 0;
	}
}
