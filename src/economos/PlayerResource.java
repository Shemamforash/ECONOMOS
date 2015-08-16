package economos;

public class PlayerResource extends UserResource{
	private int botBuyQuantity = 1, botSellQuantity = 1;
	private boolean botActive = false;
	private float botBuy = 1, botSell = 1;
	
	public PlayerResource(String name, String description, String type, String rarity) {
		super(name, description, type, rarity);
	}
	
	public void activeBot(boolean botActive){
		this.botActive = botActive;
	}
	
	public void setBotBuy(float botBuy, int botQuantity){
		this.botBuy = botBuy;
		this.botBuyQuantity = botQuantity;
	}
		
	public void setBotSell(float botSell, int botSellQuantity){
		this.botSell = botSell;
		this.botSellQuantity = botSellQuantity;
	}
	
	public int getBotBuyQuantity(){
		return botBuyQuantity;
	}
	
	public int getBotSellQuantity(){
		return botSellQuantity;
	}
	
	public float getBotSellPrice(){
		return botSell;
	}
	
	public float getBotBuyPrice(){
		return botBuy;
	}
	
	public boolean isBotActive(){
		return botActive;
	}
}
