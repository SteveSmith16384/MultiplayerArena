package com.scs.overwatch.abilitiess.spells;

import com.scs.overwatch.entities.PlayersAvatar;
import com.scs.overwatch.modules.GameModule;

public abstract class AbstractSpell {

	protected GameModule module;
	protected String name;
	protected PlayersAvatar player;
	
	public AbstractSpell(GameModule _module, PlayersAvatar _player, String _name) {
		name = _name;
		player = _player;
	}


	public boolean process(float interpol) {
		return false;
	}

	
	public String getHudText() {
		return name;
	}

	
/*	public Vector3f getPointOnFloor() {
		List<PhysicsRayTestResult> results = module.bulletAppState.getPhysicsSpace().rayTest(this.getLocation(), enemy.getLocation());

		return null;
	}*/
}
