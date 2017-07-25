package com.scs.overwatch.weapons;

import com.scs.overwatch.Overwatch;
import com.scs.overwatch.abilities.IAbility;
import com.scs.overwatch.components.ICanShoot;
import com.scs.overwatch.entities.LaserBullet;
import com.scs.overwatch.modules.GameModule;

public class LaserRifle extends AbstractGun implements IAbility {

	public LaserRifle(Overwatch _game, GameModule _module, ICanShoot shooter) {
		super(_game, _module, 300, shooter);
	}
	

	@Override
	public boolean activate(float interpol) {
		if (shotInterval.hitInterval()) {
			LaserBullet b = new LaserBullet(game, module, shooter);
			return true;
		}
		return false;
	}

}
