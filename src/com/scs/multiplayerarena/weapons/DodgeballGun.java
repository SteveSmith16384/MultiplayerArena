package com.scs.multiplayerarena.weapons;

import com.scs.multiplayerarena.MultiplayerArenaMain;
import com.scs.multiplayerarena.abilities.IAbility;
import com.scs.multiplayerarena.components.ICanShoot;
import com.scs.multiplayerarena.entities.DodgeballBall;
import com.scs.multiplayerarena.entities.PlayersAvatar;
import com.scs.multiplayerarena.modules.GameModule;

public class DodgeballGun extends AbstractGun implements IAbility {

	public DodgeballGun(MultiplayerArenaMain _game, GameModule _module, ICanShoot shooter) {
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
