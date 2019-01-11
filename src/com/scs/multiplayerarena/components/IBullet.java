package com.scs.multiplayerarena.components;


public interface IBullet extends ICollideable {

	float getDamageCaused();
	
	ICanShoot getShooter();
	
	void remove();
}
