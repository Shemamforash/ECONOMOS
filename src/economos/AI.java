package economos;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class AI extends User{
	private int aggressiveness, accuracy;
	private String focusType;
	
	public AI(String name, String company){
		super(name, company);
		aggressiveness = new Random().nextInt(100) + 1;
		accuracy = new Random().nextInt(100 - aggressiveness) + 1;
		Timer t = new Timer();
		t.schedule(new AITimer(this), 0, 100);
	}
	
	public void tick(){
		
	}
	
	class AITimer extends TimerTask{
		AI ai;
		
		public AITimer(AI ai){
			this.ai = ai;
		}
		
		public void run(){
			ai.tick();
		}
	}
}
