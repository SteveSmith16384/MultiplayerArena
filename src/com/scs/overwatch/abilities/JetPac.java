package com.scs.overwatch.abilities;

import com.jme3.math.Vector3f;
import com.scs.overwatch.entities.PlayersAvatar;

public class JetPac extends AbstractAbility {

	private static final Vector3f FORCE = new Vector3f(0, .3f, 0);
	private static final float MAX_FUEL = 10;

	private float fuel;
	private PlayersAvatar player;

	public JetPac(PlayersAvatar _player) {
		super();

		player = _player;
	}

	
	@Override
	public boolean process(float interpol) {
		//if (player.isOnGround()) {
			fuel += interpol;
			fuel = Math.min(fuel, MAX_FUEL);
		//}
		return fuel < MAX_FUEL;
	}


	@Override
	public boolean activate(float interpol) {
		fuel -= (interpol*4);
		fuel = Math.max(fuel, 0);
		if (fuel > 0) {
			//Settings.p("Jetpac-ing!");
			//player.playerControl.getPhysicsRigidBody().applyImpulse(FORCE, Vector3f.ZERO);
			player.walkDirection.addLocal(FORCE);//, Vector3f.ZERO);
			return true;
		}
		return false;
	}

	
	@Override
	public String getHudText() {
		return "JetPac Fuel:" + ((int)(fuel*10));
	}

}
