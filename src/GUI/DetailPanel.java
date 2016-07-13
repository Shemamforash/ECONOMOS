package GUI;

import javax.swing.SpringLayout;

import GUI.GUIElements;
import GUI.GuildPanel;
import economos.SelectedResourceCaller;
import economos.SelectedResourceListener;
import economos.UpdateCaller;
import economos.UpdateListener;

public abstract class DetailPanel extends GUIElements.MyPanel implements SelectedResourceListener, UpdateListener {
	protected SpringLayout springLayout;
	public boolean initialised;
	
	public DetailPanel(GuildPanel.PanelType panelType){
		super(true);
		springLayout = new SpringLayout();
		setLayout(springLayout);
		UpdateCaller.addListener(this);
		SelectedResourceCaller.addListener(this, panelType);
	}
	
	public boolean isInitialised() {
		return initialised;
	}
}
