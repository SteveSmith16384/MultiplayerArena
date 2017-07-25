package com.scs.overwatch.weapons;

import ssmith.util.RealtimeInterval;

import com.scs.overwatch.Overwatch;
import com.scs.overwatch.abilities.IAbility;
import com.scs.overwatch.components.ICanShoot;
import com.scs.overwatch.modules.GameModule;

public abstract class AbstractGun implements IAbility {

	protected Overwatch game;
	protected GameModule module;
	protected ICanShoot shooter;
	protected RealtimeInterval shotInterval;

	public AbstractGun(Overwatch _game, GameModule _module, long shotIntervalMS, ICanShoot _shooter) {
		game = _game;
		module = _module;
		shooter = _shooter;
		shotInterval = new RealtimeInterval(shotIntervalMS);
	}


	@Override
	public boolean process(float interpol) {
		// Do nothing
		return false;
	}


	@Override
	public String getHudText() {
		// TODO Auto-generated method stub
		return "todo";
	}

}
