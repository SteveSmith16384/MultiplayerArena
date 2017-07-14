package com.scs.overwatch;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

public class Sky {

	public Geometry geom;
	
	public Sky(AssetManager assetManager) {
		super();
		
		Material mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");  // create a simple material

		Texture t = assetManager.loadTexture("Textures/sky3.jpg");
		t.setWrap(WrapMode.Repeat);
		mat.setTexture("DiffuseMap", t);

		Quad quad = new Quad(160, 160); // todo - use map size
		quad.scaleTextureCoordinates(new Vector2f(5, 5));
		geom = new Geometry("Billboard", quad);
		geom.setMaterial(mat);

	}

	
}
