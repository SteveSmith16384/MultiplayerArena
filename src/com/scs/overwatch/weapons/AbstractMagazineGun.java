package com.scs.overwatch.weapons;

import com.scs.overwatch.Overwatch;
import com.scs.overwatch.abilities.IAbility;
import com.scs.overwatch.components.ICanShoot;
import com.scs.overwatch.modules.GameModule;

public abstract class AbstractMagazineGun implements IAbility {

	protected Overwatch game;
	protected GameModule module;
	protected ICanShoot shooter;
	protected String name;

	protected float timeUntilShoot = 0;
	protected int magazineSize;
	protected int bulletsLeftInMag;
	protected float shotInterval, reloadInterval; 

	public AbstractMagazineGun(Overwatch _game, GameModule _module, String _name, ICanShoot _shooter, float shotInt, float reloadInt, int magSize) {
		super();

		game = _game;
		module = _module;
		name = _name;
		shooter = _shooter;
		this.shotInterval = shotInt;
		this.reloadInterval = reloadInt;
		this.magazineSize = magSize;

		this.bulletsLeftInMag = this.magazineSize;
	}


	public abstract void launchBullet(Overwatch _game, GameModule _module, ICanShoot _shooter);


	@Override
	public final boolean activate(float interpol) {
		if (this.timeUntilShoot <= 0 && bulletsLeftInMag > 0) {
			this.launchBullet(game, module, shooter);
			timeUntilShoot = this.shotInterval;
			bulletsLeftInMag--;
			return true;
		}
		return false;
	}


	@Override
	public boolean process(float interpol) {
		if (this.bulletsLeftInMag <= 0) {
			// Reload
			this.bulletsLeftInMag = this.magazineSize;
			this.timeUntilShoot += this.reloadInterval;
		}
		timeUntilShoot -= interpol;
		return false;
	}


	@Override
	public String getHudText() {
		if (this.bulletsLeftInMag == this.magazineSize && this.timeUntilShoot > 0) {
			return name + " RELOADING";
		} else {
			return name + " (" + this.bulletsLeftInMag + "/" + this.magazineSize  +")";
		}
	}

}
