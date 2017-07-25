package com.scs.overwatch.components;

import com.jme3.math.Vector3f;

public interface IAffectedByPhysics {

	Vector3f getLocation();
	
	void applyForce(Vector3f dir); 
}
