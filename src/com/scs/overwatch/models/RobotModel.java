package com.scs.overwatch.models;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

public class RobotModel extends Node {
	
	public RobotModel(AssetManager assetManager) {
		super("RobotModel");
		
		Spatial model = assetManager.loadModel("Models/AbstractRTSModels/Player.obj");

		Material mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
		Texture t = assetManager.loadTexture("Textures/sun.jpg");
		mat.setTexture("DiffuseMap", t);
	    this.setMaterial(mat);

	    this.attachChild(model);
	}

}
