package com.scs.overwatch.entities;

import ssmith.lang.NumberFunctions;

import com.jme3.math.Vector3f;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.components.ICollideable;
import com.scs.overwatch.modules.GameModule;

public class MovingPlatform extends AbstractPlatform implements ICollideable {

	//private static final float SPEED = 1f;

	private Vector3f offset;
	private float speed = NumberFunctions.rndFloat(.5f,  1.5f);

	public MovingPlatform(Overwatch _game, GameModule _module, float x, float y, float z, Vector3f dir) {
		super(_game, _module, x, y, z, 1, 1, 0);

		offset = dir;
	}


	@Override
	public void process(float tpf) {
		move(tpf);
	}


	private void move(float tpf) {
		Vector3f pos = this.main_node.getWorldTranslation();//.floor_phy.getPhysicsLocation();
		pos = pos.add(this.offset.mult(speed*tpf));
		//this.floor_phy.setPhysicsLocation(pos);
		this.main_node.setLocalTranslation(pos);

	}


	@Override
	public void collidedWith(ICollideable other) {
		/*if (other.getClass().equals(this.getClass())) {
			//Settings.p("MovingPlatform hit " + other + " and going up");
			this.main_node.getLocalTranslation().y += 0.1f; // move out the way
		} else {// todo - moveback*/
		//Settings.p("MovingPlatform " + this.toString() + " hit " + other);
		if (other.blocksPlatforms()) {
			this.offset.multLocal(-1);
			move(0.1f);
		}
		//this.move(1);
		//}
	}


	@Override
	public boolean blocksPlatforms() {
		return true;
	}


}
