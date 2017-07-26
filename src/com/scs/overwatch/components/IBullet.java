package com.scs.overwatch.components;


public interface IBullet extends ICollideable {

	//todo -r e-add float getDamageCaused();
	
	ICanShoot getShooter();
	
	void remove();
}
