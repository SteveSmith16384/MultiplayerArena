package com.scs.overwatch.entities;

import ssmith.util.RealtimeInterval;

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
import com.scs.overwatch.components.ICanShoot;
import com.scs.overwatch.components.ICollideable;
import com.scs.overwatch.components.IEntity;
import com.scs.overwatch.components.IProcessable;
import com.scs.overwatch.components.IShowOnHUD;
import com.scs.overwatch.modules.GameModule;

public class RoamingAI extends PhysicalEntity implements IProcessable, ICollideable, ICanShoot, IShowOnHUD {

	private Geometry geometry;
	private RigidBodyControl floor_phy;
	private Vector3f currDir = new Vector3f(0, 0, 1);
	private Vector3f shotDir = new Vector3f(0, 0, 0);
	protected RealtimeInterval targetCheck = new RealtimeInterval(1000);

	public RoamingAI(Overwatch _game, GameModule _module, float x, float z, float w, float h, float d) {
		super(_game, _module, "RoamingAI");

		Box box1 = new Box(w/2, h/2, d/2);
		geometry = new Geometry("Crate", box1);
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
		main_node.setLocalTranslation(x+(w/2), h/2, z+(d/2));

		floor_phy = new RigidBodyControl(1f);
		geometry.addControl(floor_phy);
		module.bulletAppState.getPhysicsSpace().add(floor_phy);

		this.geometry.setUserData(Settings.ENTITY, this);
		floor_phy.setUserObject(this);

		module.addEntity(this);

	}


	@Override
	public void process(float tpf) {
		this.floor_phy.applyCentralForce(currDir.mult(10));

		if (targetCheck.hitInterval()) {
			// todo
			
		}
	}


	@Override
	public void remove() {
		super.remove();
		this.module.bulletAppState.getPhysicsSpace().remove(this.floor_phy);

	}


	@Override
	public Vector3f getShootDir() {
		return shotDir;
	}


	@Override
	public void hasSuccessfullyHit(IEntity e) {
		Settings.p("AI has hit " + e.toString());
		
	}


	@Override
	public void collidedWith(ICollideable other) {
		// Change dir
		this.currDir.multLocal(-1);
		
	}


}
