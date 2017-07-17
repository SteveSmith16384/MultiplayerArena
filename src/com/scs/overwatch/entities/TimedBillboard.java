package com.scs.overwatch.entities;

import com.jme3.renderer.Camera;
import com.scs.overwatch.Overwatch;

public class TimedBillboard extends AbstractBillboard {

	private float timeLeft;
	
	public TimedBillboard(Overwatch _game, String tex, Camera _cam, float dur) {
		super(_game, tex, 2, 1, _cam);
		
		this.timeLeft = dur;
	}
	
	
	@Override
	public void process(float tpf) {
		super.process(tpf);
		
		this.timeLeft -= tpf;
		if (this.timeLeft <= 0) {
			node.removeFromParent();
			game.removeEntity(this);
		}
	}

}
