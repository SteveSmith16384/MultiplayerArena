package com.scs.overwatch.abilitiess.spells;

import ssmith.lang.NumberFunctions;

import com.jme3.math.Vector3f;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.abilities.IAbility;
import com.scs.overwatch.entities.PlayersAvatar;
import com.scs.overwatch.entities.Wall;
import com.scs.overwatch.modules.GameModule;

public class WallSpell extends AbstractSpell implements IAbility {

	public WallSpell(GameModule module, PlayersAvatar _avatar) {
		super(module, _avatar, "Wall");
	}


	@Override
	public boolean activate(float interpol) {
		Vector3f pos = player.getPointOnFloor(30f);
		if (pos != null) {
			Wall wall = new Wall(Overwatch.instance, module, pos.x, pos.y, pos.z, 0); // todo - rotation
			Overwatch.instance.getRootNode().attachChild(wall.getMainNode());
		}
		return false;
	}


}
