package com.scs.overwatch.abilitiess.spells;

import java.util.List;

import com.jme3.bullet.collision.PhysicsRayTestResult;
import com.jme3.math.Vector3f;
import com.scs.overwatch.modules.GameModule;

public abstract class AbstractSpell {

	protected GameModule module;
	protected String name;
	
	public AbstractSpell(GameModule _module, String _name) {
		name = _name;
	}


	public boolean process(float interpol) {
		return false;
	}

	
	public String getHudText() {
		return name;
	}

	
	public Vector3f getPointOnFloor() {
		//todo List<PhysicsRayTestResult> results = module.bulletAppState.getPhysicsSpace().rayTest(this.getLocation(), enemy.getLocation());

		return null;
	}
}
