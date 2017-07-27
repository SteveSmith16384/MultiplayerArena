package com.scs.overwatch.components;


public interface IBullet extends ICollideable {

	float getDamageCaused();
	
	ICanShoot getShooter();
	
	void remove();
}
