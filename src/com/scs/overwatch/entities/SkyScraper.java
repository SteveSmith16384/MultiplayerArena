package com.scs.overwatch.entities;

import ssmith.lang.NumberFunctions;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
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

public class SkyScraper extends PhysicalEntity implements IProcessable, ICollideable {

	public SkyScraper(Overwatch _game, GameModule _module, float leftX, float backZ, float w, float h, float d) {
		super(_game, _module, "SkyScraper");

		String tex = null;
		if (Settings.NEON) {
			tex = "Textures/neon1.jpg";
		} else {
			tex = "Textures/skyscraper" + NumberFunctions.rnd(1, 4) + ".jpg";
		}

		Box box1 = new Box(w/2, h/2, d/2);

		box1.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(new float[]{ // Ensure texture is tiled correctly
				0, h, w, h, w, 0, 0, 0, // back
				0, h, d, h, d, 0, 0, 0, // right
		        0, h, w, h, w, 0, 0, 0, // front
		        0, h, d, h, d, 0, 0, 0, // left
		        w, 0, w, d, 0, d, 0, 0, // top
		        w, 0, w, d, 0, d, 0, 0  // bottom
				}));

		box1.scaleTextureCoordinates(new Vector2f(.1f, .1f)); // scs
		
		Geometry geometry = new Geometry("SkyScraper", box1);
		TextureKey key3 = new TextureKey(tex);
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
		//floor_mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		//geometry.setQueueBucket(Bucket.Transparent);

		this.main_node.attachChild(geometry);
		main_node.setLocalTranslation(leftX+(w/2), h/2, backZ+(d/2));

		rigidBodyControl = new RigidBodyControl(0);
		main_node.addControl(rigidBodyControl);
		module.bulletAppState.getPhysicsSpace().add(rigidBodyControl);

		geometry.setUserData(Settings.ENTITY, this);
		rigidBodyControl.setUserObject(this);

	}


	@Override
	public void process(float tpf) {
	}


	@Override
	public void collidedWith(ICollideable other) {
		// Do nothing

	}


}
