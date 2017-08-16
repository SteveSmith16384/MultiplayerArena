package com.scs.overwatch.abilities;

import com.scs.overwatch.entities.PlayersAvatar;

public class BoostFwd extends AbstractAbility {

	private static final float POWER = 10f;//.3f;
	private static final float MAX_FUEL = 10;

	private float fuel = 100;
	private PlayersAvatar player;

	public BoostFwd(PlayersAvatar _player) {
		super();

		player = _player;
	}


	@Override
	public boolean process(float interpol) {
		fuel += interpol*3;
		fuel = Math.min(fuel, MAX_FUEL);
		return fuel < MAX_FUEL;
	}


	@Override
	public boolean activate(float interpol) {
		fuel -= (interpol*10);
		fuel = Math.max(fuel, 0);
		if (fuel > 0) {
			//Settings.p("Jetpac-ing!");
			//player.walkDirection.addLocal(FORCE);//, Vector3f.ZERO);

			player.camDir.set(player.cam.getDirection()).multLocal(POWER, 0.0f, POWER);
			player.walkDirection.addLocal(player.camDir);
			return true;
		}
		return false;
	}


	@Override
	public String getHudText() {
		return "Boost Fuel:" + ((int)(fuel*10));
	}

}
