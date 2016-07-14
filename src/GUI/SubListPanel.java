package GUI;

import MarketSimulator.Debug;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Sam on 13/07/2016.
 */
public class SubListPanel <T extends JComponent> extends GUIElements.MyPanel {
    private int height;
    private ArrayList<MainPanel> mainPanels = new ArrayList<MainPanel>();
    private JScrollPane scrollPane;
    private GUIElements.MyPanel panelList = new GUIElements.MyPanel(false);
	private boolean defaultVisible;

    public SubListPanel(int height, boolean defaultVisible){
        super(true);
        this.height = height;
	    this.defaultVisible = defaultVisible;
        setup();
    }

    public void clear(){
        mainPanels.clear();
    }

    public void update() {
        panelList.removeAll();
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.ipady = height / 30;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        for (MainPanel m : mainPanels) {
            panelList.add(m.getHeader(), gc);
            ++gc.gridy;
            for(T t : m.getSubPanels()){
                panelList.add(t, gc);
                ++gc.gridy;
            }
        }
	    GUIElements.MyPanel p = new GUIElements.MyPanel();
	    p.setPreferredSize(new Dimension(this.getWidth(), height - gc.gridy * mainPanels.size()));
	    panelList.add(p, gc);
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
        panelList.validate();
        panelList.repaint();
        scrollPane.getViewport().revalidate();
    }

    public void setup(){
        setLayout(new BorderLayout());
        scrollPane = new JScrollPane(panelList);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.setBorder(null);
        panelList.setLayout(new GridBagLayout());
        add(scrollPane, BorderLayout.CENTER);
    }

    public MainPanel addMainPanel(JComponent panel){
        MainPanel p = new MainPanel(panel);
        mainPanels.add(p);
        return p;
    }

	public MainPanel addMainPanelAsButton (String title){
		MainPanel p = new MainPanel(title);
		mainPanels.add(p);
		return p;
	}

    public void addSubPanel(MainPanel p, T subPanel) {
        p.add(subPanel);
    }

    public class MainPanel {
        private ArrayList<T> subPanels = new ArrayList<T>();
        private JComponent panel;
        private boolean showContents;
        private T header;

        public MainPanel(JComponent panel){
            this.panel = panel;
            header = (T) panel;
        }

	    public MainPanel(String title) {
		    GUIElements.MyButton b = new GUIElements.MyButton(title, true);
		    showContents = defaultVisible;
		    b.addActionListener(e -> {
			    showContents = !showContents;
			    subPanels.forEach(p -> p.setVisible(showContents));
			    scrollPane.revalidate();
		    });
		    header = (T)b;
	    }

        public T getHeader(){
            return header;
        }

        public ArrayList<T> getSubPanels() {
            return subPanels;
        }

        public void add(T panel){
            subPanels.add(panel);
            panel.setVisible(defaultVisible);
        }
    }
}
