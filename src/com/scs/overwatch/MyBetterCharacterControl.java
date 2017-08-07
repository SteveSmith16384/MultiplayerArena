package com.scs.overwatch;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.objects.PhysicsRigidBody;

// https://github.com/jMonkeyEngine/jmonkeyengine/blob/master/jme3-bullet/src/common/java/com/jme3/bullet/control/BetterCharacterControl.java
public class MyBetterCharacterControl extends BetterCharacterControl {

	//private float mass;
	
	public MyBetterCharacterControl(float a, float b, float c) {
		super(a, b, c);
	
		//mass = c;
	}
	
	
	public PhysicsRigidBody getPhysicsRigidBody() {
		return super.rigidBody;
	}

}
