package economos;

import java.awt.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.*;

public class EconomosMain {

	private JFrame frame;
	private JTextField typeTextField;
	private JTextField nameTextField;
	private JTextField possessTextField;
	private JTextField soldTextField;
	private JTextField averageProfitTextField;
	private JTextField averagePriceTextField;
	private JTextField demandTextField;
	private JTextField supplyTextField;
	private JTextField buyTextField;
	private JTextField sellTextField;
	private JTextField botTextField;
	private static User currentUser;
	private final JList<String> resourceList = new JList<String>(new String[]{"Empty"});
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EconomosMain window = new EconomosMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public EconomosMain() {
		load();
		initialize();
		GUIManager.updateComboBox(currentUser);
	}
	
	public static User getCurrentUser(){
		return currentUser;
	}

	private void load(){
		try {
			DataParser parser = new DataParser();
		} catch (IOException e) {
			System.out.println("Failed to load. Terminating");
			System.exit(0);
		}
		currentUser = new User("Sam", "Potatronics");
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{1184, 0};
		gridBagLayout.rowHeights = new int[]{661, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JTabbedPane tabPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabPane = new GridBagConstraints();
		gbc_tabPane.fill = GridBagConstraints.BOTH;
		gbc_tabPane.gridx = 0;
		gbc_tabPane.gridy = 0;
		frame.getContentPane().add(tabPane, gbc_tabPane);
		
		JPanel marketPanel = new JPanel();
		tabPane.addTab("Market", null, marketPanel, "Access the market to buy and sell resources");
		SpringLayout sl_marketPanel = new SpringLayout();
		marketPanel.setLayout(sl_marketPanel);
		
		JPanel listPanel = new JPanel();
		sl_marketPanel.putConstraint(SpringLayout.NORTH, listPanel, 0, SpringLayout.NORTH, marketPanel);
		sl_marketPanel.putConstraint(SpringLayout.WEST, listPanel, 0, SpringLayout.WEST, marketPanel);
		sl_marketPanel.putConstraint(SpringLayout.SOUTH, listPanel, 633, SpringLayout.NORTH, marketPanel);
		sl_marketPanel.putConstraint(SpringLayout.EAST, listPanel, 300, SpringLayout.WEST, marketPanel);
		marketPanel.add(listPanel);
		SpringLayout sl_listPanel = new SpringLayout();
		listPanel.setLayout(sl_listPanel);
		
		JScrollPane categoryScrollPane = new JScrollPane();
		sl_listPanel.putConstraint(SpringLayout.NORTH, categoryScrollPane, 0, SpringLayout.NORTH, listPanel);
		sl_listPanel.putConstraint(SpringLayout.WEST, categoryScrollPane, 0, SpringLayout.WEST, listPanel);
		sl_listPanel.putConstraint(SpringLayout.SOUTH, categoryScrollPane, 220, SpringLayout.NORTH, listPanel);
		sl_listPanel.putConstraint(SpringLayout.EAST, categoryScrollPane, 300, SpringLayout.WEST, listPanel);
		categoryScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		listPanel.add(categoryScrollPane);
		
		final JList<String> categoryList = new JList<String>(new String[]{"Empty"});;
		categoryList.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent arg0) {
				if(!arg0.getValueIsAdjusting()){
					GUIManager.updateJList(EconomosMain.getCurrentUser(), (String)categoryList.getSelectedValue(), resourceList);
				}
			}
		});
		categoryScrollPane.setViewportView(categoryList);
		
		JScrollPane resourceScrollPane = new JScrollPane();
		sl_listPanel.putConstraint(SpringLayout.NORTH, resourceScrollPane, 6, SpringLayout.SOUTH, categoryScrollPane);
		sl_listPanel.putConstraint(SpringLayout.WEST, resourceScrollPane, 0, SpringLayout.WEST, listPanel);
		sl_listPanel.putConstraint(SpringLayout.SOUTH, resourceScrollPane, 632, SpringLayout.NORTH, listPanel);
		sl_listPanel.putConstraint(SpringLayout.EAST, resourceScrollPane, 300, SpringLayout.WEST, listPanel);
		resourceScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		listPanel.add(resourceScrollPane);
		
		resourceList.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent arg0) {
				if(!arg0.getValueIsAdjusting() && categoryList.getSelectedValue() != "Empty"){
					GUIManager.setSelectedResource((String)categoryList.getSelectedValue(), resourceList.getSelectedValue());
				}
			}
		});
		resourceScrollPane.setViewportView(resourceList);
		
		JPanel bodyPanel = new JPanel();
		sl_marketPanel.putConstraint(SpringLayout.NORTH, bodyPanel, 0, SpringLayout.NORTH, marketPanel);
		sl_marketPanel.putConstraint(SpringLayout.WEST, bodyPanel, 6, SpringLayout.EAST, listPanel);
		sl_marketPanel.putConstraint(SpringLayout.SOUTH, bodyPanel, 0, SpringLayout.SOUTH, listPanel);
		sl_marketPanel.putConstraint(SpringLayout.EAST, bodyPanel, 0, SpringLayout.EAST, marketPanel);
		marketPanel.add(bodyPanel);
		SpringLayout sl_bodyPanel = new SpringLayout();
		bodyPanel.setLayout(sl_bodyPanel);
		
		JPanel resourceDetailPanel = new JPanel();
		sl_bodyPanel.putConstraint(SpringLayout.NORTH, resourceDetailPanel, 0, SpringLayout.NORTH, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.WEST, resourceDetailPanel, 0, SpringLayout.WEST, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.SOUTH, resourceDetailPanel, -299, SpringLayout.SOUTH, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.EAST, resourceDetailPanel, 672, SpringLayout.WEST, bodyPanel);
		bodyPanel.add(resourceDetailPanel);
		
		JPanel resourceGraphPanel = new JPanel();
		sl_bodyPanel.putConstraint(SpringLayout.NORTH, resourceGraphPanel, 6, SpringLayout.SOUTH, resourceDetailPanel);
		sl_bodyPanel.putConstraint(SpringLayout.WEST, resourceGraphPanel, 0, SpringLayout.WEST, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.SOUTH, resourceGraphPanel, -1, SpringLayout.SOUTH, bodyPanel);
		sl_bodyPanel.putConstraint(SpringLayout.EAST, resourceGraphPanel, 0, SpringLayout.EAST, bodyPanel);
		SpringLayout sl_resourceDetailPanel = new SpringLayout();
		resourceDetailPanel.setLayout(sl_resourceDetailPanel);
		
		JPanel topDetailPanel = new JPanel();
		sl_resourceDetailPanel.putConstraint(SpringLayout.NORTH, topDetailPanel, 0, SpringLayout.NORTH, resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.WEST, topDetailPanel, 0, SpringLayout.WEST, resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.SOUTH, topDetailPanel, 141, SpringLayout.NORTH, resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.EAST, topDetailPanel, 0, SpringLayout.EAST, resourceDetailPanel);
		resourceDetailPanel.add(topDetailPanel);
		SpringLayout sl_topDetailPanel = new SpringLayout();
		topDetailPanel.setLayout(sl_topDetailPanel);
		
		JPanel iconPanel = new JPanel();
		sl_topDetailPanel.putConstraint(SpringLayout.NORTH, iconPanel, 10, SpringLayout.NORTH, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.WEST, iconPanel, 10, SpringLayout.WEST, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.EAST, iconPanel, 141, SpringLayout.WEST, topDetailPanel);
		topDetailPanel.add(iconPanel);
		
		typeTextField = new JTextField();
		sl_topDetailPanel.putConstraint(SpringLayout.NORTH, typeTextField, 10, SpringLayout.NORTH, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.WEST, typeTextField, 6, SpringLayout.EAST, iconPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.SOUTH, typeTextField, -69, SpringLayout.SOUTH, topDetailPanel);
		topDetailPanel.add(typeTextField);
		typeTextField.setEditable(false);
		typeTextField.setColumns(10);
		
		nameTextField = new JTextField();
		sl_topDetailPanel.putConstraint(SpringLayout.NORTH, nameTextField, 6, SpringLayout.SOUTH, typeTextField);
		sl_topDetailPanel.putConstraint(SpringLayout.SOUTH, nameTextField, 0, SpringLayout.SOUTH, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.WEST, nameTextField, 6, SpringLayout.EAST, iconPanel);
		topDetailPanel.add(nameTextField);
		nameTextField.setEditable(false);
		nameTextField.setColumns(10);
		
		JTextArea descriptionTextArea = new JTextArea();
		sl_topDetailPanel.putConstraint(SpringLayout.EAST, descriptionTextArea, -10, SpringLayout.EAST, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.EAST, nameTextField, -6, SpringLayout.WEST, descriptionTextArea);
		sl_topDetailPanel.putConstraint(SpringLayout.EAST, typeTextField, -6, SpringLayout.WEST, descriptionTextArea);
		sl_topDetailPanel.putConstraint(SpringLayout.WEST, descriptionTextArea, 343, SpringLayout.WEST, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.SOUTH, iconPanel, 0, SpringLayout.SOUTH, descriptionTextArea);
		sl_topDetailPanel.putConstraint(SpringLayout.NORTH, descriptionTextArea, 10, SpringLayout.NORTH, topDetailPanel);
		sl_topDetailPanel.putConstraint(SpringLayout.SOUTH, descriptionTextArea, 0, SpringLayout.SOUTH, topDetailPanel);
		topDetailPanel.add(descriptionTextArea);
		descriptionTextArea.setEditable(false);
		
		JPanel bottomDetailPanel = new JPanel();
		sl_resourceDetailPanel.putConstraint(SpringLayout.NORTH, bottomDetailPanel, 6, SpringLayout.SOUTH, topDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.WEST, bottomDetailPanel, 0, SpringLayout.WEST, resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.SOUTH, bottomDetailPanel, 0, SpringLayout.SOUTH, resourceDetailPanel);
		sl_resourceDetailPanel.putConstraint(SpringLayout.EAST, bottomDetailPanel, 0, SpringLayout.EAST, topDetailPanel);
		resourceDetailPanel.add(bottomDetailPanel);
		SpringLayout sl_bottomDetailPanel = new SpringLayout();
		bottomDetailPanel.setLayout(sl_bottomDetailPanel);
		
		JPanel statsPanel = new JPanel();
		sl_bottomDetailPanel.putConstraint(SpringLayout.NORTH, statsPanel, 0, SpringLayout.NORTH, bottomDetailPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.WEST, statsPanel, 10, SpringLayout.WEST, bottomDetailPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.SOUTH, statsPanel, 187, SpringLayout.NORTH, bottomDetailPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.EAST, statsPanel, 210, SpringLayout.WEST, bottomDetailPanel);
		bottomDetailPanel.add(statsPanel);
		statsPanel.setLayout(new GridLayout(4, 1, 0, 10));
		
		possessTextField = new JTextField();
		possessTextField.setEditable(false);
		statsPanel.add(possessTextField);
		possessTextField.setColumns(10);
		
		soldTextField = new JTextField();
		soldTextField.setEditable(false);
		statsPanel.add(soldTextField);
		soldTextField.setColumns(10);
		
		averageProfitTextField = new JTextField();
		averageProfitTextField.setEditable(false);
		statsPanel.add(averageProfitTextField);
		averageProfitTextField.setColumns(10);
		
		averagePriceTextField = new JTextField();
		averagePriceTextField.setEditable(false);
		statsPanel.add(averagePriceTextField);
		averagePriceTextField.setColumns(10);
		
		JPanel purchasePanel = new JPanel();
		sl_bottomDetailPanel.putConstraint(SpringLayout.NORTH, purchasePanel, 0, SpringLayout.NORTH, statsPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.WEST, purchasePanel, 6, SpringLayout.EAST, statsPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.SOUTH, purchasePanel, 0, SpringLayout.SOUTH, statsPanel);
		bottomDetailPanel.add(purchasePanel);
		purchasePanel.setLayout(new GridLayout(4, 1, 0, 10));
		
		JButton sellButton = new JButton("Sell");
		purchasePanel.add(sellButton);
		
		JButton buyButton = new JButton("Buy");
		purchasePanel.add(buyButton);
		
		demandTextField = new JTextField();
		purchasePanel.add(demandTextField);
		demandTextField.setEditable(false);
		demandTextField.setColumns(10);
		
		supplyTextField = new JTextField();
		purchasePanel.add(supplyTextField);
		supplyTextField.setEditable(false);
		supplyTextField.setColumns(10);
		
		JPanel botPanel = new JPanel();
		sl_bottomDetailPanel.putConstraint(SpringLayout.EAST, purchasePanel, -6, SpringLayout.WEST, botPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.SOUTH, botPanel, 0, SpringLayout.SOUTH, statsPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.NORTH, botPanel, 0, SpringLayout.NORTH, statsPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.WEST, botPanel, 433, SpringLayout.WEST, bottomDetailPanel);
		sl_bottomDetailPanel.putConstraint(SpringLayout.EAST, botPanel, 0, SpringLayout.EAST, bottomDetailPanel);
		bottomDetailPanel.add(botPanel);
		botPanel.setLayout(new GridLayout(4, 1, 0, 0));
		
		botTextField = new JTextField();
		botTextField.setEnabled(false);
		botPanel.add(botTextField);
		botTextField.setColumns(10);
		
		JRadioButton activeRadioButton = new JRadioButton("Enable Bot (20C/s)");
		botPanel.add(activeRadioButton);
		
		JPanel buyPanel = new JPanel();
		botPanel.add(buyPanel);
		SpringLayout sl_buyPanel = new SpringLayout();
		buyPanel.setLayout(sl_buyPanel);
		
		JButton decreaseBuyButton = new JButton("-");
		sl_buyPanel.putConstraint(SpringLayout.NORTH, decreaseBuyButton, 25, SpringLayout.NORTH, buyPanel);
		sl_buyPanel.putConstraint(SpringLayout.SOUTH, decreaseBuyButton, 0, SpringLayout.SOUTH, buyPanel);
		sl_buyPanel.putConstraint(SpringLayout.EAST, decreaseBuyButton, -10, SpringLayout.EAST, buyPanel);
		buyPanel.add(decreaseBuyButton);
		
		buyTextField = new JTextField();
		sl_buyPanel.putConstraint(SpringLayout.WEST, decreaseBuyButton, 6, SpringLayout.EAST, buyTextField);
		sl_buyPanel.putConstraint(SpringLayout.NORTH, buyTextField, 10, SpringLayout.NORTH, buyPanel);
		sl_buyPanel.putConstraint(SpringLayout.WEST, buyTextField, 0, SpringLayout.WEST, buyPanel);
		sl_buyPanel.putConstraint(SpringLayout.EAST, buyTextField, -84, SpringLayout.EAST, buyPanel);
		sl_buyPanel.putConstraint(SpringLayout.SOUTH, buyTextField, -10, SpringLayout.SOUTH, buyPanel);
		buyTextField.setEnabled(false);
		buyPanel.add(buyTextField);
		buyTextField.setColumns(10);
		
		JPanel sellPanel = new JPanel();
		botPanel.add(sellPanel);
		SpringLayout sl_sellPanel = new SpringLayout();
		sellPanel.setLayout(sl_sellPanel);
		
		sellTextField = new JTextField();
		sl_sellPanel.putConstraint(SpringLayout.WEST, sellTextField, 0, SpringLayout.WEST, sellPanel);
		sellTextField.setEnabled(false);
		sl_sellPanel.putConstraint(SpringLayout.NORTH, sellTextField, 10, SpringLayout.NORTH, sellPanel);
		sl_sellPanel.putConstraint(SpringLayout.SOUTH, sellTextField, -10, SpringLayout.SOUTH, sellPanel);
		sl_sellPanel.putConstraint(SpringLayout.EAST, sellTextField, 155, SpringLayout.WEST, sellPanel);
		sellPanel.add(sellTextField);
		sellTextField.setColumns(10);
		
		JButton increaseSellButton = new JButton("+");
		sl_sellPanel.putConstraint(SpringLayout.NORTH, increaseSellButton, 0, SpringLayout.NORTH, sellPanel);
		sl_sellPanel.putConstraint(SpringLayout.WEST, increaseSellButton, 6, SpringLayout.EAST, sellTextField);
		sl_sellPanel.putConstraint(SpringLayout.EAST, increaseSellButton, -10, SpringLayout.EAST, sellPanel);
		sellPanel.add(increaseSellButton);
		bodyPanel.add(resourceGraphPanel);
		resourceGraphPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel buyGraphPanel = new JPanel();
		buyGraphPanel.add(new GraphPanel());
		buyGraphPanel.setBorder(null);
		resourceGraphPanel.add(buyGraphPanel);
		
		JButton increaseBuyButton = new JButton("+");
		sl_buyPanel.putConstraint(SpringLayout.NORTH, increaseBuyButton, 0, SpringLayout.NORTH, buyPanel);
		sl_buyPanel.putConstraint(SpringLayout.WEST, increaseBuyButton, 6, SpringLayout.EAST, buyTextField);
		sl_buyPanel.putConstraint(SpringLayout.EAST, increaseBuyButton, -10, SpringLayout.EAST, buyPanel);
		buyPanel.add(increaseBuyButton);
		
		JButton decreaseSellButton = new JButton("-");
		sl_sellPanel.putConstraint(SpringLayout.WEST, decreaseSellButton, 6, SpringLayout.EAST, sellTextField);
		sl_sellPanel.putConstraint(SpringLayout.SOUTH, decreaseSellButton, 0, SpringLayout.SOUTH, sellPanel);
		sl_sellPanel.putConstraint(SpringLayout.EAST, decreaseSellButton, -10, SpringLayout.EAST, sellPanel);
		sellPanel.add(decreaseSellButton);
		
		JPanel sellGraphPanel = new JPanel();
		sellGraphPanel.add(new GraphPanel());
		sellGraphPanel.setBorder(null);
		resourceGraphPanel.add(sellGraphPanel);
		
		JPanel companyPanel = new JPanel();
		tabPane.addTab("Company", null, companyPanel, "View information about your company and player statistics");
		
		JPanel constructionPanel = new JPanel();
		tabPane.addTab("Construction", null, constructionPanel, "Access Construction options from this tab");
	}

}
