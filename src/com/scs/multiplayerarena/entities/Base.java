package com.scs.multiplayerarena.entities;

import com.jme3.math.Vector3f;
import com.scs.multiplayerarena.MultiplayerArenaMain;
import com.scs.multiplayerarena.components.ICollideable;
import com.scs.multiplayerarena.modules.GameModule;

public class Base extends Floor {

	public Base(MultiplayerArenaMain _game, GameModule _module, float x, float y, float z, float w, float h, float d, String tex, Vector3f _texScroll) {
		super(_game, _module, x, y, z, w, h, d, tex, _texScroll);
	}

	
	@Override
	public void collidedWith(ICollideable other) {

	}



}
