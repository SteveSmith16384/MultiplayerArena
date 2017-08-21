package com.scs.overwatch.abilitiess.spells;

import com.scs.overwatch.abilities.IAbility;
import com.scs.overwatch.modules.GameModule;

public class Wall extends AbstractSpell implements IAbility {

	public Wall(GameModule module) {
		super(module, "Wall");
	}


	@Override
	public boolean activate(float interpol) {
		// TODO Auto-generated method stub
		return false;
	}


}
