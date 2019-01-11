package com.scs.multiplayerarena.abilities;

import com.scs.multiplayerarena.entities.PlayersAvatar;

public abstract class AbstractAbility implements IAbility {
	
	protected PlayersAvatar player;

	public AbstractAbility(PlayersAvatar p) {
		player = p;
	}

}
