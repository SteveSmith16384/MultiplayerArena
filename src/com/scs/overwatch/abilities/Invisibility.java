package com.scs.overwatch.abilities;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial.CullHint;
import com.scs.overwatch.entities.PlayersAvatar;

public class Invisibility extends AbstractAbility {

	private static final Vector3f FORCE = new Vector3f(0, 1f, 0);
	private static final float MAX_FUEL = 10;
	
	private float fuel;
	private PlayersAvatar player;
	private boolean invisible;
	
	public Invisibility(PlayersAvatar _player) {
		super();
		
		player = _player;
	}

	
	@Override
	public void process(long interpol) {
		this.player.getMainNode().setCullHint(CullHint.Inherit); // Default
		invisible = false;
		fuel += interpol;
		fuel = Math.min(fuel, MAX_FUEL);
		
	}

	
	@Override
	public void activate(long interpol) {
		fuel -= interpol;
		fuel = Math.max(fuel, 0);
		if (fuel > 0) {
			this.player.getMainNode().setCullHint(CullHint.Always);
			invisible = true;
		}
		
	}

	@Override
	public String getHudText() {
		return invisible ? "INVISIBLE!" : "";
	}

}
