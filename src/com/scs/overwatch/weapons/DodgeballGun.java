package com.scs.overwatch.weapons;

import com.scs.overwatch.Overwatch;
import com.scs.overwatch.abilities.IAbility;
import com.scs.overwatch.components.ICanShoot;
import com.scs.overwatch.entities.DodgeballBall;
import com.scs.overwatch.entities.PlayersAvatar;
import com.scs.overwatch.modules.GameModule;

public class DodgeballGun extends AbstractGun implements IAbility {

	public DodgeballGun(Overwatch _game, GameModule _module, ICanShoot shooter) {
		super(_game, _module, "DodgeballGun", 1000, shooter);
	}


	@Override
	public boolean activate(float interpol) {
		if (shotInterval.hitInterval()) {
			PlayersAvatar av = (PlayersAvatar) shooter;
			if (av.getHasBall()) {
				new DodgeballBall(game, module, shooter);
				av.setHasBall(false);
				return true;
			}
		}
		return false;
	}

}
