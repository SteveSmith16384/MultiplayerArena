package com.scs.overwatch.components;

import com.jme3.math.Vector3f;
import com.scs.overwatch.entities.Entity;

public interface ICanShoot {

	Vector3f getLocation();

	Vector3f getDir();
	
	void hasSuccessfullyHit(IEntity e);
	
}
