package com.scs.multiplayerarena.weapons;

import com.scs.multiplayerarena.MultiplayerArenaMain;
import com.scs.multiplayerarena.abilities.IAbility;
import com.scs.multiplayerarena.components.ICanShoot;
import com.scs.multiplayerarena.entities.KillerCrateBullet;
import com.scs.multiplayerarena.modules.GameModule;

public class KillerCrateGun extends AbstractGun implements IAbility {

	public KillerCrateGun(MultiplayerArenaMain _game, GameModule _module, ICanShoot shooter) {
		super(_game, _module, "KrateGun", 1000, shooter);
	}
	

	@Override
	public boolean activate(float interpol) {
		if (shotInterval.hitInterval()) {
			new KillerCrateBullet(game, module, shooter);
			return true;
		}
		return false;
	}


}
