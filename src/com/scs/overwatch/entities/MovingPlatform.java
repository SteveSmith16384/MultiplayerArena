package com.scs.overwatch.entities;

import com.jme3.math.Vector3f;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.components.ICollideable;
import com.scs.overwatch.modules.GameModule;

public class MovingPlatform extends AbstractPlatform implements ICollideable {

	private static final float SPEED = 1f;

	private Vector3f offset;

	public MovingPlatform(Overwatch _game, GameModule _module, float x, float y, float z, Vector3f dir) {
		super(_game, _module, x, y, z, 1, 1, 0);

		offset = dir;
	}


	@Override
	public void process(float tpf) {
		Vector3f pos = this.floor_phy.getPhysicsLocation();
		pos = pos.add(this.offset.mult(SPEED*tpf));
		this.floor_phy.setPhysicsLocation(pos);
		this.main_node.setLocalTranslation(pos);

	}


	@Override
	public void collidedWith(ICollideable other) {
		if (other.getClass().equals(this.getClass())) {
			Settings.p("MovingPlatform hit " + other + " and going up");
			this.main_node.getLocalTranslation().y += 0.1f; // move out the way
		} else {
			Settings.p("MovingPlatform hit " + other);
			this.offset.multLocal(-1);
		}
	}

}
