package com.scs.overwatch.abilitiess.spells;

import com.scs.overwatch.abilities.IAbility;
import com.scs.overwatch.entities.PlayersAvatar;
import com.scs.overwatch.modules.GameModule;

public class Wall extends AbstractSpell implements IAbility {

	public Wall(GameModule module, PlayersAvatar _avatar) {
		super(module, _avatar, "Wall");
	}


	@Override
	public boolean activate(float interpol) {
		player.getPointOnFloor(30f);
		return false;
	}


}
