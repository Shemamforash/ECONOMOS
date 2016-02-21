package Resources;

import economos.User;

public class ResourcePacket {
	private int purchaseValue, quantity;
	private User owner;
	
	public ResourcePacket(User owner, int purchaseValue, int quantity){
		this.owner = owner;
		this.purchaseValue = purchaseValue;
		this.quantity = quantity;
	}
	
	public int getPurchaseValue(){
		return purchaseValue;
	}
	
	public int getQuantity(){
		return quantity;
	}
	
	public int getPacketValue(){
		return purchaseValue * quantity;
	}
	
	public User getOwner(){
		return owner;
	}
}
