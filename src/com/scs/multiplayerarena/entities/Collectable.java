package com.scs.multiplayerarena.entities;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.scs.multiplayerarena.MultiplayerArenaMain;
import com.scs.multiplayerarena.Settings;
import com.scs.multiplayerarena.components.IAffectedByPhysics;
import com.scs.multiplayerarena.components.ICollideable;
import com.scs.multiplayerarena.components.IShowOnHUD;
import com.scs.multiplayerarena.modules.GameModule;

public class Collectable extends PhysicalEntity implements ICollideable, IShowOnHUD, IAffectedByPhysics {

	private static final float SIZE = .1f;
	
	public boolean collected = false;

	public Collectable(MultiplayerArenaMain _game, GameModule _module, float x, float y, float z) {
		super(_game, _module, "Collectable");

		Box box1 = new Box(SIZE, SIZE, SIZE);
		Geometry geometry = new Geometry("Collectable", box1);
		TextureKey key3 = new TextureKey("Textures/greensun.jpg");
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
		main_node.setLocalTranslation(x, y, z); // Drop from sky

		rigidBodyControl = new RigidBodyControl(0.1f);
		main_node.addControl(rigidBodyControl);

		module.bulletAppState.getPhysicsSpace().add(rigidBodyControl);
		
		geometry.setUserData(Settings.ENTITY, this);
		rigidBodyControl.setUserObject(this);

		rigidBodyControl.setRestitution(.5f);

		module.addEntity(this); // need this to target it
		
		Settings.p("Created collectable");
		
	}


	@Override
	public void process(float tpf) {
		// Do nothing
	}


	@Override
	public void collidedWith(ICollideable other) {
		
	}


}
