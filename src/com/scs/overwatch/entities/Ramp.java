package com.scs.overwatch.entities;

import ssmith.lang.NumberFunctions;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.BufferUtils;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.components.ICollideable;
import com.scs.overwatch.components.IProcessable;
import com.scs.overwatch.modules.GameModule;

public class Ramp extends PhysicalEntity implements IProcessable, ICollideable {

	public Ramp(Overwatch _game, GameModule _module, float x, float y, float z, float len, float ang) {
		super(_game, _module, "Ramp");

		String tex = null;
		if (Settings.NEON) {
			tex = "Textures/neon1.jpg";
		} else {
			tex = "Textures/skyscraper" + NumberFunctions.rnd(1, 4) + ".jpg";
		}

		float w = 1f;
		float heightLength = len;
		float thickness = 0.1f;
		float d = w;
		
		Box box1 = new Box(w/2, heightLength/2, thickness/2);
		Geometry geometry = new Geometry("ramp", box1);
		TextureKey key3 = new TextureKey(tex);
		key3.setGenerateMips(true);
		Texture tex3 = game.getAssetManager().loadTexture(key3);
		tex3.setWrap(WrapMode.Repeat);

		box1.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(new float[]{ // Ensure tex is tiled correctly
				0, heightLength, w, heightLength, w, 0, 0, 0, // back
				0, heightLength, d, heightLength, d, 0, 0, 0, // right
		        0, heightLength, w, heightLength, w, 0, 0, 0, // front
		        0, heightLength, d, heightLength, d, 0, 0, 0, // left
		        w, 0, w, d, 0, d, 0, 0, // top
		        w, 0, w, d, 0, d, 0, 0  // bottom
				}));

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
		
		geometry.setLocalTranslation(0, heightLength/2, 0);
		main_node.setLocalTranslation(x, y, z);
		main_node.rotate(ang, 0, 0);
	
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


	@Override
	public void collidedWith(ICollideable other) {
		// Do nothing

	}


}
