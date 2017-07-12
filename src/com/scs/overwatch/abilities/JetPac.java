package com.scs.overwatch.abilities;

import com.jme3.math.Vector3f;
import com.scs.overwatch.entities.PlayersAvatar;

public class JetPac extends AbstractAbility {

	private static final Vector3f FORCE = new Vector3f(0, 1f, 0);
	private static final float MAX_FUEL = 10;
	
	private float fuel;
	private PlayersAvatar player;
	
	public JetPac(PlayersAvatar _player) {
		super();
		
		player = _player;
	}

	@Override
	public void process(long interpol) {
		fuel += interpol;
		fuel = Math.min(fuel, MAX_FUEL);
		
	}

	
	@Override
	public void activate(long interpol) {
		fuel -= interpol;
		fuel = Math.max(fuel, 0);
		player.playerControl.getPhysicsRigidBody().applyImpulse(FORCE, Vector3f.ZERO);
		
	}

	@Override
	public String getHudText() {
		return "Fuel:" + ((int)fuel);
	}

}
