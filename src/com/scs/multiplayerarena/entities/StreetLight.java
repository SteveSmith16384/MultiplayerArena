package com.scs.multiplayerarena.entities;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.scs.multiplayerarena.MultiplayerArenaMain;
import com.scs.multiplayerarena.Settings;
import com.scs.multiplayerarena.modules.GameModule;

public class StreetLight extends PhysicalEntity {

	public StreetLight(MultiplayerArenaMain _game, GameModule _module, float x, float z) {
		super(_game, _module, "StreetLight");
		
		float diam = 0.15f;
		float h = 5f;

		Box box1 = new Box(diam/2, h/2, diam/2);
		box1.scaleTextureCoordinates(new Vector2f(1, h));
		Geometry geometry = new Geometry("Crate", box1);
		TextureKey key3 = new TextureKey("Textures/floor0041.png");
		key3.setGenerateMips(true);
		Texture tex3 = game.getAssetManager().loadTexture(key3);
		tex3.setWrap(WrapMode.Repeat);
		Material floor_mat = null;
		if (Settings.LIGHTING) {
			floor_mat = new Material(game.getAssetManager(),"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
			floor_mat.setTexture("DiffuseMap", tex3);
		} else {
			floor_mat = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
			floor_mat.setTexture("ColorMap", tex3);
		}
		geometry.setMaterial(floor_mat);
		
		this.main_node.attachChild(geometry);
		main_node.setLocalTranslation(x+(diam/2), h/2, z+0.5f);

		rigidBodyControl = new RigidBodyControl(3f);
		main_node.addControl(rigidBodyControl);
		module.bulletAppState.getPhysicsSpace().add(rigidBodyControl);
		
		geometry.setUserData(Settings.ENTITY, this);
		rigidBodyControl.setUserObject(this);
		
		game.getRootNode().attachChild(this.getMainNode());
		//module.addEntity(this);
	}


	@Override
	public void process(float tpf) {
		// Do nothing
	}


}
