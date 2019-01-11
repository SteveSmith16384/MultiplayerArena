package com.scs.multiplayerarena.entities;

import ssmith.lang.NumberFunctions;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.scs.multiplayerarena.MultiplayerArenaMain;
import com.scs.multiplayerarena.Settings;
import com.scs.multiplayerarena.modules.GameModule;

public class CubeExplosionShard extends PhysicalEntity {//implements IAffectedByPhysics, ICollideable {//,  { // IProcessable,  // Need ICollideable so lasers don't bounce off it

	public static void Factory(MultiplayerArenaMain _game, GameModule _module, Vector3f pos, int num) {
		for (int i=0 ; i<num ; i++) {
			CubeExplosionShard s = new CubeExplosionShard(_game, _module, pos.x, pos.y, pos.z);
			_game.getRootNode().attachChild(s.getMainNode());

		}
	}
	
	
	private float timeLeft = 8f; 

	private CubeExplosionShard(MultiplayerArenaMain _game, GameModule _module, float x, float y, float z) {
		super(_game, _module, "CubeExplosionShard");

		float s = .1f;
		Box box1 = new Box(s, s, s);
		//box1.scaleTextureCoordinates(new Vector2f(WIDTH, HEIGHT));
		Geometry geometry = new Geometry("Crate", box1);
		//int i = NumberFunctions.rnd(1, 10);
		TextureKey key3 = new TextureKey("Textures/sun.jpg");
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
		int rotDegreesX = NumberFunctions.rnd(0,365);
		float radsX = (float)Math.toRadians(rotDegreesX);
		int rotDegreesY = NumberFunctions.rnd(0,365);
		float radsY = (float)Math.toRadians(rotDegreesY);
		main_node.rotate(radsX, radsY, 0);
		main_node.setLocalTranslation(x, y, z);

		rigidBodyControl = new RigidBodyControl(.2f);
		main_node.addControl(rigidBodyControl);
		module.getBulletAppState().getPhysicsSpace().add(rigidBodyControl);

		geometry.setUserData(Settings.ENTITY, this);
		main_node.setUserData(Settings.ENTITY, this);
		rigidBodyControl.setUserObject(this);
		
		module.addEntity(this);
		
		Vector3f force = new Vector3f(NumberFunctions.rndFloat(-1, 1), NumberFunctions.rndFloat(1, 2), NumberFunctions.rndFloat(-1, 1));
		//Vector3f force = new Vector3f(0, 1.4f, 0);
		this.rigidBodyControl.applyImpulse(force, Vector3f.ZERO);
		
		this.rigidBodyControl.setRestitution(.9f);

	}


	@Override
	public void process(float tpf) {
		//Settings.p("Pos: " + this.getLocation());
		timeLeft -= tpf;
		if (timeLeft <= 0) {
			this.remove();
		}
	}


}
