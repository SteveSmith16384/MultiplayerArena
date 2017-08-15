
package com.scs.overwatch.entities;

import java.util.List;

import ssmith.lang.NumberFunctions;
import ssmith.util.RealtimeInterval;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.collision.PhysicsRayTestResult;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.abilities.IAbility;
import com.scs.overwatch.components.IBullet;
import com.scs.overwatch.components.ICanShoot;
import com.scs.overwatch.components.ICollideable;
import com.scs.overwatch.components.IDamagable;
import com.scs.overwatch.components.IEntity;
import com.scs.overwatch.components.IMustRemainInArena;
import com.scs.overwatch.components.IProcessable;
import com.scs.overwatch.components.IShowOnHUD;
import com.scs.overwatch.components.ITargetByAI;
import com.scs.overwatch.modules.GameModule;
import com.scs.overwatch.weapons.LaserRifle;

public class RoamingAI extends PhysicalEntity implements IProcessable, ICanShoot, IShowOnHUD, IDamagable, ICollideable, IMustRemainInArena {

	private static final float SPEED = 7;

	//private Vector3f currDir = new Vector3f(0, 0, 1);
	private Vector3f currDir = new Vector3f(0, 1.3f, 1); // was 1.2f
	private Vector3f shotDir = new Vector3f(0, 0, 0);
	protected RealtimeInterval targetCheck = new RealtimeInterval(1000);
	private Vector3f lastPos;
	private IAbility weapon;

	public RoamingAI(Overwatch _game, GameModule _module, float x, float z) {
		super(_game, _module, "RoamingAI");

		float w = 1f;//0.5f;
		float h = 1f;//0.5f;
		float d = 1f;//0.5f;

		Box box1 = new Box(w/2, h/2, d/2);
		Geometry geometry = new Geometry("Crate", box1);
		//TextureKey key3 = new TextureKey("Textures/sun.jpg");//computerconsole2.jpg");
		TextureKey key3 = new TextureKey("Textures/computerconsole2.jpg");
		key3.setGenerateMips(true);
		Texture tex3 = game.getAssetManager().loadTexture(key3);
		tex3.setWrap(WrapMode.Repeat);

		Material floor_mat = null;
		if (Settings.LIGHTING) {
			floor_mat = new Material(game.getAssetManager(),"Common/MatDefs/Light/Lighting.j3md");
			floor_mat.setTexture("DiffuseMap", tex3);
		} else {
			floor_mat = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
			floor_mat.setTexture("ColorMap", tex3);
		}
		geometry.setMaterial(floor_mat);

		this.main_node.attachChild(geometry);
		//main_node.setLocalTranslation(x+(w/2), h/2, z+(d/2));
		main_node.setLocalTranslation(x+(w/2), 10, z+(d/2));

		floor_phy = new RigidBodyControl(1f);
		main_node.addControl(floor_phy);
		module.bulletAppState.getPhysicsSpace().add(floor_phy);

		geometry.setUserData(Settings.ENTITY, this);
		floor_phy.setUserObject(this);
		floor_phy.setCcdMotionThreshold(w);
		module.addEntity(this);

		weapon = new LaserRifle(_game, _module, this);

		Settings.p("Created new AI");
	}


	@Override
	public void process(float tpf) {
		weapon.process(tpf);
		this.floor_phy.applyCentralForce(currDir.mult(SPEED));

		if (targetCheck.hitInterval()) {
			// Check position
			if (lastPos == null) {
				lastPos = this.getMainNode().getWorldTranslation().clone();
			} else {
				float dist = this.getMainNode().getWorldTranslation().subtract(lastPos).length();
				//Settings.p("dist=" + dist);
				if (dist < 0.03f) {
					//this.currDir.multLocal(-1);
					setRandomDir(currDir);
					//Settings.p("ai New dir " + this.currDir);
				}
				lastPos.set(this.getMainNode().getWorldTranslation());
			}

			for(IEntity e : module.entities) {
				if (e instanceof ITargetByAI) {
					ITargetByAI enemy = (ITargetByAI)e;
					if (this.canSee(enemy)) {
						this.getMainNode().lookAt(enemy.getLocation(), Vector3f.UNIT_Y);
						Vector3f dir = enemy.getLocation().subtract(this.getLocation()).normalize();
						this.shotDir.set(dir);
						//Settings.p("AI shooting at " + enemy);
						this.weapon.activate(tpf);//.shoot();
					} else {
						//Settings.p("AI cannot see anyone to shoot at");
					}
				}
			}


		}
	}


	private boolean canSee(ITargetByAI enemy) {
		List<PhysicsRayTestResult> results = module.bulletAppState.getPhysicsSpace().rayTest(this.getLocation(), enemy.getLocation());
		return results.size() <= 2;
	}


	@Override
	public Vector3f getShootDir() {
		return shotDir;
	}


	@Override
	public void hasSuccessfullyHit(IEntity e) {
		//Settings.p("AI has shot " + e.toString());

	}


	private static void setRandomDir(Vector3f vec) {
		int i = NumberFunctions.rnd(1,  4);
		switch (i) {
		case 1: vec.set(1,  vec.y,  0);
		break;
		case 2: vec.set(-1,  vec.y,  0);
		break;
		case 3: vec.set(0,  vec.y,  1);
		break;
		case 4: vec.set(0,  vec.y,  -1);
		break;
		}
	}


	@Override
	public void damaged(float amt, String reason) {

	}


	@Override
	public void collidedWith(ICollideable other) {
		if (other instanceof IBullet) {
			IBullet bullet = (IBullet)other;
			if (bullet.getShooter() != this) {
				Settings.p("AI hit by bullet");
				CubeExplosionShard.Factory(game, module, this.getLocation(), 10);

				module.doExplosion(this.getLocation(), this);//, 5, 5);
				this.remove();
				bullet.getShooter().hasSuccessfullyHit(this);
				module.addAI(); // Add another
			}
		}		
	}


	@Override
	public void respawn() {
		module.addAI();
	}



}
