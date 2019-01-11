package com.scs.multiplayerarena.weapons;

import com.scs.multiplayerarena.MultiplayerArenaMain;
import com.scs.multiplayerarena.abilities.IAbility;
import com.scs.multiplayerarena.components.ICanShoot;
import com.scs.multiplayerarena.entities.LaserBullet;
import com.scs.multiplayerarena.modules.GameModule;

public class LaserRifle extends AbstractMagazineGun implements IAbility {

	public LaserRifle(MultiplayerArenaMain _game, GameModule _module, ICanShoot shooter) {
		super(_game, _module, "Laser Rifle", shooter, .2f, 2, 10);
	}

	
	@Override
	public void launchBullet(MultiplayerArenaMain game, GameModule module, ICanShoot _shooter) {
		new LaserBullet(game, module, shooter);
		
	}
	

	/*@Override
	public boolean activate(float interpol) {
		if (shotInterval.hitInterval()) {
			new LaserBullet(game, module, shooter);
			return true;
		}
		return false;
	}*/

}
