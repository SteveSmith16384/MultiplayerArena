package com.scs.overwatch.entities;

import com.jme3.math.Vector3f;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.components.ICollideable;
import com.scs.overwatch.modules.GameModule;

public class Lift extends AbstractPlatform {

	private static final float SPEED = 0.5f;

	private boolean goingUp = true;
	private float bottom, top;

	public Lift(Overwatch _game, GameModule _module, float x, float z, float minH, float maxH) {
		super(_game, _module, x, 1, z, 1, 1, 0);

		bottom = minH;
		top = maxH;
	}


	@Override
	public void process(float tpf) {
		Vector3f pos = this.getMainNode().getWorldTranslation();//.floor_phy.getPhysicsLocation();
		if (goingUp) {
			pos.y += SPEED * tpf;
			if (pos.y >= top) {
				pos.y = top;
				goingUp = false;
			}
		} else {
			pos.y -= SPEED * tpf;
			if (pos.y <= bottom) {
				pos.y = bottom;
				goingUp = true;
			}
		}
		this.getMainNode().setLocalTranslation(pos);
		//this.floor_phy.setPhysicsLocation(pos);

	}


	@Override
	public void collidedWith(ICollideable other) {
				
	}

}
