package economos;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public abstract class MinigamePanel extends JPanel {
	private MinigameController minigameController;
	
	public MinigamePanel(MinigameController minigameController) {
		this.minigameController = minigameController;
		this.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
				requestFocus();
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {
			}

			public void mouseReleased(MouseEvent arg0) {
			}
		});
		this.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent arg0) {
				receiveKey(arg0.getKeyChar());
			}

			public void keyReleased(KeyEvent arg0) {
			}

			public void keyTyped(KeyEvent arg0) {
			}

		});
	}
	
	public MinigameController minigameController(){
		return minigameController;
	}
	
	public abstract void receiveKey(char c);
}
