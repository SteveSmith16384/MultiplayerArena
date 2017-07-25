package com.scs.overwatch.weapons;

import com.scs.overwatch.Overwatch;
import com.scs.overwatch.abilities.IAbility;
import com.scs.overwatch.components.ICanShoot;
import com.scs.overwatch.entities.KillerCrateBullet;
import com.scs.overwatch.modules.GameModule;

public class KillerCrateGun extends AbstractGun implements IAbility {

	public KillerCrateGun(Overwatch _game, GameModule _module, ICanShoot shooter) {
		super(_game, _module, "KrateGun", 1000, shooter);
	}
	

	@Override
	public boolean activate(float interpol) {
		if (shotInterval.hitInterval()) {
			KillerCrateBullet b = new KillerCrateBullet(game, module, shooter);
			return true;
		}
		return false;
	}


}
