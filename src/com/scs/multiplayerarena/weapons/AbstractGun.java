package com.scs.multiplayerarena.weapons;

import com.scs.multiplayerarena.MultiplayerArenaMain;
import com.scs.multiplayerarena.abilities.IAbility;
import com.scs.multiplayerarena.components.ICanShoot;
import com.scs.multiplayerarena.modules.GameModule;

import ssmith.util.RealtimeInterval;

public abstract class AbstractGun implements IAbility {

	protected MultiplayerArenaMain game;
	protected GameModule module;
	protected ICanShoot shooter;
	protected String name;
	protected RealtimeInterval shotInterval;

	public AbstractGun(MultiplayerArenaMain _game, GameModule _module, String _name, long shotIntervalMS, ICanShoot _shooter) {
		game = _game;
		module = _module;
		name = _name;
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
		return name;
	}

}
