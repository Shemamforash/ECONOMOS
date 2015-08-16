package economos;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.event.*;

public class EconomosGUI {
	public  JFrame frame;
	
	private static Player currentPlayer;
	public GUIElements.MyPanel infoPanel;
	public static PlayerResource currentResource = null;
	public static int timeStep = 17;
	private MainPanel merchantsPanel, craftersPanel, overviewPanel, currentPanel;
	private final GUIElements.MyPanel gamePanel = new GUIElements.MyPanel(true);
	private GUIElements.MyTextField headlineTextField;

	public void postNewHeadline(String txt) {
		headlineTextField.setText(txt);
	}
	
	public Player getCurrentPlayer(){
		return currentPlayer;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EconomosGUI window = new EconomosGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	class AIExecutor extends TimerTask {
		private int aiCount, currentAI = 0;
		private long starttime;

		public AIExecutor(int aiCount) {
			this.aiCount = aiCount;
		}

		public void run() {
			if (currentAI == aiCount) {
				this.cancel();
			} else {
				AI ai = new AI("Potato", "Potato Industries");
				++currentAI;
			}
		}
	}

	public EconomosGUI() {
		load();
		initialize();
		int aiCount = 100;
		Timer t = new Timer();
		t.schedule(new AIExecutor(aiCount), 0, 1000 / aiCount);

		AI ai = new AI("Sam's AI", "Sam's AI");

		t.schedule(new UpdateGUI(), 0, 17);
	}

	private void load() {
		try {
			DataParser parser = new DataParser();
		} catch (IOException e) {
			System.out.println("Failed to load. Terminating");
			System.exit(0);
		}
		currentPlayer = new Player("Sam", "Potatronics");
	}

	class UpdateGUI extends TimerTask {
		public void run() {
			if (currentPanel.getSelectedGuild() != null && currentPanel.getSelectedResource() != null) {
				setSelectedResource(currentPanel.getSelectedGuild(), currentPanel.getSelectedResource());
			}
		}
	}

	public void setSelectedResource(String type, String name) {
		ResourceMap<PlayerResource> m = currentPlayer.getPlayerResourceMap();
		currentResource = m.getResource(type, name);
		currentPanel.updateResource(currentResource);
	}

	public static UserResource getCurrentResource() {
		return currentResource;
	}	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBackground(new Color(40, 40, 40));
		frame.setBounds(100, 100, 1000, 730);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		springLayout.putConstraint(SpringLayout.NORTH, gamePanel, 0, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, gamePanel, 0, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, gamePanel, -30, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, gamePanel, 0, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().setLayout(springLayout);

		infoPanel = new GUIElements.MyPanel(true);
		springLayout.putConstraint(SpringLayout.NORTH, infoPanel, -30, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, infoPanel, 0, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, infoPanel, 0, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, infoPanel, 0, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(infoPanel);
		frame.getContentPane().add(gamePanel);
		gamePanel.setLayout(new CardLayout(0, 0));

		merchantsPanel = new MerchantsPanel(this);
		currentPanel = merchantsPanel;
		springLayout.putConstraint(SpringLayout.NORTH, merchantsPanel, 5, SpringLayout.NORTH, gamePanel);
		springLayout.putConstraint(SpringLayout.WEST, merchantsPanel, 10, SpringLayout.WEST, gamePanel);
		gamePanel.add(merchantsPanel, "Merchants");
		springLayout.putConstraint(SpringLayout.SOUTH, merchantsPanel, 701, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, merchantsPanel, 994, SpringLayout.WEST, frame.getContentPane());
		
		SpringLayout sl_infoPanel = new SpringLayout();
		infoPanel.setLayout(sl_infoPanel);

		headlineTextField = new GUIElements.MyTextField();
		sl_infoPanel.putConstraint(SpringLayout.NORTH, headlineTextField, 6, SpringLayout.NORTH, infoPanel);
		sl_infoPanel.putConstraint(SpringLayout.WEST, headlineTextField, 6, SpringLayout.WEST, infoPanel);
		sl_infoPanel.putConstraint(SpringLayout.SOUTH, headlineTextField, -6, SpringLayout.SOUTH, infoPanel);
		sl_infoPanel.putConstraint(SpringLayout.EAST, headlineTextField, -248, SpringLayout.EAST, infoPanel);
		infoPanel.add(headlineTextField);
		headlineTextField.setColumns(10);

		GUIElements.MyButton merchantsPanelButton = new GUIElements.MyButton("Merchants", true);
		merchantsPanelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				CardLayout cardLayout = (CardLayout)gamePanel.getLayout();
				cardLayout.show(gamePanel, "Merchants");
			}
		});
		sl_infoPanel.putConstraint(SpringLayout.NORTH, merchantsPanelButton, 0, SpringLayout.NORTH, headlineTextField);
		sl_infoPanel.putConstraint(SpringLayout.WEST, merchantsPanelButton, 6, SpringLayout.EAST, headlineTextField);
		sl_infoPanel.putConstraint(SpringLayout.SOUTH, merchantsPanelButton, 0, SpringLayout.SOUTH, headlineTextField);
		sl_infoPanel.putConstraint(SpringLayout.EAST, merchantsPanelButton, 80, SpringLayout.EAST, headlineTextField);
		infoPanel.add(merchantsPanelButton);

		GUIElements.MyButton craftersPanelButton = new GUIElements.MyButton("Crafters", true);
		craftersPanelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				CardLayout cardLayout = (CardLayout)gamePanel.getLayout();
				cardLayout.show(gamePanel, "Crafters");
			}
		});
		
		sl_infoPanel.putConstraint(SpringLayout.NORTH, craftersPanelButton, 0, SpringLayout.NORTH,
				merchantsPanelButton);
		sl_infoPanel.putConstraint(SpringLayout.WEST, craftersPanelButton, 6, SpringLayout.EAST, merchantsPanelButton);
		sl_infoPanel.putConstraint(SpringLayout.SOUTH, craftersPanelButton, 0, SpringLayout.SOUTH,
				merchantsPanelButton);
		sl_infoPanel.putConstraint(SpringLayout.EAST, craftersPanelButton, 80, SpringLayout.EAST, merchantsPanelButton);
		infoPanel.add(craftersPanelButton);

		GUIElements.MyButton overviewPanelButton = new GUIElements.MyButton("Overview", true);
		overviewPanelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				CardLayout cardLayout = (CardLayout)gamePanel.getLayout();
				cardLayout.show(gamePanel, "Overview");
			}
		});
		sl_infoPanel.putConstraint(SpringLayout.NORTH, overviewPanelButton, 0, SpringLayout.NORTH, craftersPanelButton);
		sl_infoPanel.putConstraint(SpringLayout.WEST, overviewPanelButton, 6, SpringLayout.EAST, craftersPanelButton);
		sl_infoPanel.putConstraint(SpringLayout.SOUTH, overviewPanelButton, 0, SpringLayout.SOUTH, craftersPanelButton);
		sl_infoPanel.putConstraint(SpringLayout.EAST, overviewPanelButton, 80, SpringLayout.EAST, craftersPanelButton);
		infoPanel.add(overviewPanelButton);

		CraftingPanel craftersPanel = new CraftingPanel(this);
		springLayout.putConstraint(SpringLayout.NORTH, craftersPanel, 5, SpringLayout.NORTH, gamePanel);
		springLayout.putConstraint(SpringLayout.WEST, craftersPanel, 15, SpringLayout.WEST, gamePanel);
		gamePanel.add(craftersPanel, "Crafters");
		springLayout.putConstraint(SpringLayout.SOUTH, craftersPanel, 701, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, craftersPanel, 994, SpringLayout.WEST, frame.getContentPane());
		SpringLayout sl_craftersPanel = new SpringLayout();
		craftersPanel.setLayout(sl_craftersPanel);
		craftersPanel.setEnabled(false);

		overviewPanel = new OverviewPanel(this);
		springLayout.putConstraint(SpringLayout.NORTH, overviewPanel, 5, SpringLayout.NORTH, gamePanel);
		springLayout.putConstraint(SpringLayout.WEST, overviewPanel, 20, SpringLayout.WEST, gamePanel);
		gamePanel.add(overviewPanel, "Overview");
		springLayout.putConstraint(SpringLayout.SOUTH, overviewPanel, 701, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, overviewPanel, 994, SpringLayout.WEST, frame.getContentPane());
		SpringLayout sl_overviewPanel = new SpringLayout();
		overviewPanel.setLayout(sl_overviewPanel);
		overviewPanel.setEnabled(false);
	}
}
