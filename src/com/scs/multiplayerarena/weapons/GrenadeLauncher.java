package com.scs.multiplayerarena.weapons;

import com.scs.multiplayerarena.MultiplayerArenaMain;
import com.scs.multiplayerarena.abilities.IAbility;
import com.scs.multiplayerarena.components.ICanShoot;
import com.scs.multiplayerarena.entities.Grenade;
import com.scs.multiplayerarena.modules.GameModule;

public class GrenadeLauncher extends AbstractGun implements IAbility {

	public GrenadeLauncher(MultiplayerArenaMain _game, GameModule _module, ICanShoot shooter) {
		super(_game, _module, "GrenadeLauncher", 1500, shooter);
	}
	

	@Override
	public boolean activate(float interpol) {
		if (shotInterval.hitInterval()) {
			new Grenade(game, module, shooter);
			return true;
		}
		return false;
	}


}
