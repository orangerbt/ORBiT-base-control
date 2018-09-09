package org.carton.common.net;

import java.util.TimerTask;
/**
 * CounterTask extends TimerTask class that allow a execute in certain times
 * @version 1.0
 * @author Mike
 *
 */
public abstract class CounterTask extends TimerTask {
	private int count;
	private int time;
	public CounterTask(int time){
		this.time=time;
	}
	@Override
	public final void run() {
		// TODO Auto-generated method stub
		action();
		count++;
		if(count==time)
			cancel();
	}
	abstract void action();
}
