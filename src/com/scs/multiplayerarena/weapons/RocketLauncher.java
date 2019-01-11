package com.scs.multiplayerarena.weapons;

import com.scs.multiplayerarena.MultiplayerArenaMain;
import com.scs.multiplayerarena.abilities.IAbility;
import com.scs.multiplayerarena.components.ICanShoot;
import com.scs.multiplayerarena.entities.Rocket;
import com.scs.multiplayerarena.modules.GameModule;

public class RocketLauncher extends AbstractGun implements IAbility {

	public RocketLauncher(MultiplayerArenaMain _game, GameModule _module, ICanShoot shooter) {
		super(_game, _module, "Rocket Launcher", 1200, shooter);
	}
	

	@Override
	public boolean activate(float interpol) {
		if (shotInterval.hitInterval()) {
			Rocket b = new Rocket(game, module, shooter);
			module.addEntity(b);
			return true;
		}
		return false;
	}


}
