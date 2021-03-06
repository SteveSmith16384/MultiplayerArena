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

public class SimplePillar extends PhysicalEntity {

	private static final float THICKNESS = .3f;
	private static final float HEIGHT = 4f;
	private static final String TEX = "Textures/OldRedBricks_T.jpg";

	public SimplePillar(MultiplayerArenaMain _game, GameModule _module, float x, float z) {
		super(_game, _module, "SimplePillar");

		Box vert = new Box(THICKNESS/2, HEIGHT/2, THICKNESS/2);
		vert.scaleTextureCoordinates(new Vector2f(THICKNESS, HEIGHT));
		Geometry  geometry = new Geometry("Fence", vert);
		TextureKey key3 = new TextureKey(TEX);
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
		
		main_node.setLocalTranslation(x+(THICKNESS/2), HEIGHT/2, z+(THICKNESS/2));

		rigidBodyControl = new RigidBodyControl(0);
		main_node.addControl(rigidBodyControl);

		module.bulletAppState.getPhysicsSpace().add(rigidBodyControl);
		
		geometry.setUserData(Settings.ENTITY, this);
		rigidBodyControl.setUserObject(this);

	}


	@Override
	public void process(float tpf) {
		// Do nothing
	}


}
