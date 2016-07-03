package MerchantResources;

public class ResourcePacket implements Comparable<ResourcePacket>{
	private float purchaseCost;
	private int quantity;
	
	public ResourcePacket(float purchaseCost, int quantity){
		this.purchaseCost = purchaseCost;
		this.quantity = quantity;
	}
	
	public float purchaseCost(){
		return purchaseCost;
	}
	
	public int quantity(){
		return quantity;
	}
	
	public float value(){
		return purchaseCost * quantity;
	}

	@Override
	public int compareTo(ResourcePacket other) {
		if(quantity > other.quantity()){
			return -1;
		} else if (quantity < other.quantity()){
			return 1;
		} else if (value() > other.value()){
			return -1;
		} else if (value() < other.value()){
			return 1;
		}
		return 0;
	}
}
