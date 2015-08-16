package economos;

public abstract class MainPanel extends GUIElements.MyPanel{
	protected EconomosGUI main;
	protected GuildPanel listPanel;
	
	public MainPanel(EconomosGUI main){
		super(true);
		this.main = main;
	}
	
	public abstract void updateResource(PlayerResource r);
	public abstract String getSelectedGuild();
	public abstract String getSelectedResource();
	
	public Player getCurrentPlayer(){
		return main.getCurrentPlayer();
	}
}
