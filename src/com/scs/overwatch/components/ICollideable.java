package com.scs.overwatch.components;

import com.jme3.math.Vector3f;

public interface ICollideable {

	void collidedWith(ICollideable other);
	
	boolean blocksPlatforms();
	
}
