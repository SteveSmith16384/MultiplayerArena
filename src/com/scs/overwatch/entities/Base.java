package com.scs.overwatch.entities;

import com.jme3.math.Vector3f;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.components.ICollideable;
import com.scs.overwatch.modules.GameModule;

public class Base extends Floor {

	public Base(Overwatch _game, GameModule _module, float x, float y, float z, float w, float h, float d, String tex, Vector3f _texScroll) {
		super(_game, _module, x, y, z, w, h, d, tex, _texScroll);
	}

	
	@Override
	public void collidedWith(ICollideable other) {

	}



}
