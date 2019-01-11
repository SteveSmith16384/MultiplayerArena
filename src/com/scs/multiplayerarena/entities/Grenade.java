package com.scs.multiplayerarena.entities;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.texture.Texture;
import com.scs.multiplayerarena.MultiplayerArenaMain;
import com.scs.multiplayerarena.Settings;
import com.scs.multiplayerarena.components.IBullet;
import com.scs.multiplayerarena.components.ICanShoot;
import com.scs.multiplayerarena.components.ICollideable;
import com.scs.multiplayerarena.modules.GameModule;

public class Grenade extends PhysicalEntity implements IBullet {

	public ICanShoot shooter;
	private float timeLeft = 2f;
	
	public Grenade(MultiplayerArenaMain _game, GameModule _module, ICanShoot _shooter) {
		super(_game, _module, "Grenade");

		this.shooter = _shooter;
		
		Sphere sphere = new Sphere(8, 8, 0.1f, true, false);
		sphere.setTextureMode(TextureMode.Projected);
		/** Create a cannon ball geometry and attach to scene graph. */
		Geometry ball_geo = new Geometry("cannon ball", sphere);

		TextureKey key3 = new TextureKey( "Textures/grenade.png");
		Texture tex3 = game.getAssetManager().loadTexture(key3);
		Material floor_mat = null;
		if (Settings.LIGHTING) {
			floor_mat = new Material(game.getAssetManager(),"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
			floor_mat.setTexture("DiffuseMap", tex3);
		} else {
			floor_mat = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
			floor_mat.setTexture("ColorMap", tex3);
		}
		ball_geo.setMaterial(floor_mat);

		this.main_node.attachChild(ball_geo);
		game.getRootNode().attachChild(this.main_node);
		/** Position the cannon ball  */
		ball_geo.setLocalTranslation(shooter.getLocation().add(shooter.getShootDir().multLocal(PlayersAvatar.PLAYER_RAD*2)));
		/** Make the ball physical with a mass > 0.0f */
		rigidBodyControl = new RigidBodyControl(.2f);
		/** Add physical ball to physics space. */
		ball_geo.addControl(rigidBodyControl);
		module.bulletAppState.getPhysicsSpace().add(rigidBodyControl);
		/** Accelerate the physical ball to shoot it. */
		rigidBodyControl.setLinearVelocity(shooter.getShootDir().mult(15));
		
		this.getMainNode().setUserData(Settings.ENTITY, this);
		rigidBodyControl.setUserObject(this);
		module.addEntity(this);

	}

	
	@Override
	public void process(float tpf) {
		this.timeLeft -= tpf;
		if (this.timeLeft < 0) {
			module.doExplosion(this.getLocation(), this);//, 3, 10);
			this.remove();
		}
		
	}


	@Override
	public ICanShoot getShooter() {
		return shooter;
	}


	@Override
	public void collidedWith(ICollideable other) {
		
	}


	@Override
	public float getDamageCaused() {
		return 0;
	}



}
