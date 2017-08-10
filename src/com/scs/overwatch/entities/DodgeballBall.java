package com.scs.overwatch.entities;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.texture.Texture;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.components.IBullet;
import com.scs.overwatch.components.ICanShoot;
import com.scs.overwatch.components.ICollideable;
import com.scs.overwatch.modules.GameModule;

public class DodgeballBall extends PhysicalEntity implements IBullet {

	public ICanShoot shooter;
	public boolean live = true;
	private Geometry ball_geo;
	
	public DodgeballBall(Overwatch _game, GameModule _module, ICanShoot _shooter) {
		super(_game, _module, "DodgeballBall");

		this.shooter = _shooter;

		Sphere sphere = new Sphere(16, 16, 0.2f, true, false);
		sphere.setTextureMode(TextureMode.Projected);
		/** Create a cannon ball geometry and attach to scene graph. */
		ball_geo = new Geometry("cannon ball", sphere);

		TextureKey key3 = new TextureKey( "Textures/cells3.png");
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
		if (shooter != null) {
			ball_geo.setLocalTranslation(shooter.getLocation().add(shooter.getShootDir().multLocal(PlayersAvatar.PLAYER_RAD*2)));
		}
		floor_phy = new RigidBodyControl(.3f);
		ball_geo.addControl(floor_phy);
		module.bulletAppState.getPhysicsSpace().add(floor_phy);
		/** Accelerate the physical ball to shoot it. */
		if (shooter != null) {
			floor_phy.setLinearVelocity(shooter.getShootDir().mult(25));
		}
		this.getMainNode().setUserData(Settings.ENTITY, this);
		floor_phy.setUserObject(this);
		module.addEntity(this);

		floor_phy.setRestitution(.8f); // Bouncy

	}


	@Override
	public void process(float tpf) {
		// Check if fallen off edge
		if (this.getMainNode().getWorldTranslation().y < -1f) {
			Settings.p("Dodgeball has fallen off the edge");
			this.remove();
			module.createDodgeballBall();
		}


	}


	@Override
	public ICanShoot getShooter() {
		return shooter;
	}


	@Override
	public void collidedWith(ICollideable other) {
		//Settings.p("Coll with " + other);
		if (other instanceof PlayersAvatar) {
			PlayersAvatar av = (PlayersAvatar) other;
			if (live) {
				if (other != this.shooter) {
					av.hitByBullet(1);
					if (getShooter() != null) {
						getShooter().hasSuccessfullyHit(av);
					}
				}
			} else if (av.getHasBall() == false) {
				av.setHasBall(true);
				this.remove();
			}
		} else {
			this.live = false;
			
			// Set tex to dark
			TextureKey key3 = new TextureKey( "Textures/mud.png");
			Texture tex3 = game.getAssetManager().loadTexture(key3);
			this.ball_geo.getMaterial().setTexture("DiffuseMap", tex3);

		}
	}


	@Override
	public float getDamageCaused() {
		return 0;
	}


	@Override
	public boolean blocksPlatforms() {
		return false;
	}


}
