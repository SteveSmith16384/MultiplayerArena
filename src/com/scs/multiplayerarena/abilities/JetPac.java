package com.scs.multiplayerarena.abilities;

import com.jme3.math.Vector3f;
import com.scs.multiplayerarena.entities.PlayersAvatar;

public class JetPac extends AbstractAbility {

	//private static final Vector3f FORCE = new Vector3f(0, .25f, 0);
	private static final float POWER = .7f;//.3f;
	private static final float MAX_FUEL = 10;

	private float fuel = 100;
	private final Vector3f camUp = new Vector3f();

	public JetPac(PlayersAvatar _player) {
		super(_player);
	}


	@Override
	public boolean process(float interpol) {
		fuel += interpol*4;
		fuel = Math.min(fuel, MAX_FUEL);
		return fuel < MAX_FUEL;
	}


	@Override
	public boolean activate(float interpol) {
		fuel -= (interpol*20);
		fuel = Math.max(fuel, 0);
		if (fuel > 0) {
			//Settings.p("Jetpac-ing!");
			//player.walkDirection.addLocal(FORCE);//, Vector3f.ZERO);
			camUp.set(player.cam.getUp()).multLocal(POWER);
			player.walkDirection.addLocal(camUp);
			return true;
		}
		return false;
	}


	@Override
	public String getHudText() {
		return "JetPac Fuel:" + ((int)(fuel*10));
	}

}
