package com.scs.overwatch.entities;

import java.util.LinkedList;

import ssmith.lang.NumberFunctions;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.components.IProcessable;

public class Crate extends PhysicalEntity implements IProcessable {

	private static final Vector3f JUMP = new Vector3f(0, -2, 0);

	private Geometry geometry;
	private RigidBodyControl floor_phy;

	public Crate(Overwatch _game, float x, float z, float w, float h, float d, float rotDegrees) {
		super(_game, "Crate");

		Box box1 = new Box(w/2, h/2, d/2);
		//box1.scaleTextureCoordinates(new Vector2f(WIDTH, HEIGHT));
		geometry = new Geometry("Crate", box1);
		//TextureKey key3 = new TextureKey("Textures/crate.png");
		TextureKey key3 = new TextureKey("Textures/boxes and crates/1.jpg");
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
		float rads = (float)Math.toRadians(rotDegrees);
		main_node.rotate(0, rads, 0);
		main_node.setLocalTranslation(x+(w/2), h/2, z+0.5f);

		floor_phy = new RigidBodyControl(1f);
		geometry.addControl(floor_phy);
		game.bulletAppState.getPhysicsSpace().add(floor_phy);

		this.geometry.setUserData(Settings.ENTITY, this);
	}


	@Override
	public void process(float tpf) {
		if (NumberFunctions.rnd(0, 10) == 0) { // todo - 50
			floor_phy.applyCentralForce(JUMP);
		}
	}


	@Override
	public void remove() {
		super.remove();
		this.game.bulletAppState.getPhysicsSpace().remove(this.floor_phy);

	}


}
