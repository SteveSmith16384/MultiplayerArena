package com.scs.overwatch.abilities;

import com.scs.overwatch.entities.PlayersAvatar;

public abstract class AbstractAbility implements IAbility {
	
	protected PlayersAvatar player;

	public AbstractAbility(PlayersAvatar p) {
		player = p;
	}

}
