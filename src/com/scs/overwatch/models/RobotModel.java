package com.scs.overwatch.models;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class RobotModel extends Node {
	
	public RobotModel(AssetManager assetManager) {
		super("RobotModel");
		
		Spatial s = assetManager.loadModel("Models/ROBOT_V1.blend");
		s.scale(.1f);
		this.attachChild(s);
	}

}
