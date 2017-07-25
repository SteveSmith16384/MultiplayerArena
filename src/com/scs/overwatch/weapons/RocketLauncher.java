package com.scs.overwatch.weapons;

import com.scs.overwatch.Overwatch;
import com.scs.overwatch.abilities.IAbility;
import com.scs.overwatch.components.ICanShoot;
import com.scs.overwatch.modules.GameModule;

public class RocketLauncher extends AbstractGun implements IAbility {

	public RocketLauncher(Overwatch _game, GameModule _module, ICanShoot shooter) {
		super(_game, _module, 1200, shooter);
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
