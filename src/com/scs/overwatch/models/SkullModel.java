package com.scs.overwatch.models;

import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class SkullModel extends Node {
	
	public SkullModel(AssetManager assetManager) {
		super("SkullModel");
		
		Spatial s = assetManager.loadModel("Models/skull/obj/skull_monster.obj");
		s.scale(0.1f);
		s.rotate(0, 90 * FastMath.DEG_TO_RAD, 0);
	
		this.attachChild(s);
	}

}
