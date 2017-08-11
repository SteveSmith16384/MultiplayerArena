package com.scs.overwatch.entities;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
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

public class Rocket extends PhysicalEntity implements IBullet {

	public ICanShoot shooter;

	public Rocket(Overwatch _game, GameModule _module, ICanShoot _shooter) {
		super(_game, _module, "Rocket");

		this.shooter = _shooter;

		Sphere sphere = new Sphere(8, 8, 0.2f, true, false);
		sphere.setTextureMode(TextureMode.Projected);
		/** Create a cannon ball geometry and attach to scene graph. */
		Geometry ball_geo = new Geometry("cannon ball", sphere);

		TextureKey key3 = new TextureKey( "Textures/mud.png");
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
		ball_geo.setLocalTranslation(shooter.getLocation().add(shooter.getShootDir().multLocal(PlayersAvatar.PLAYER_RAD*2)));
		floor_phy = new RigidBodyControl(1f);
		ball_geo.addControl(floor_phy);
		module.bulletAppState.getPhysicsSpace().add(floor_phy);
		floor_phy.setLinearVelocity(shooter.getShootDir().mult(25));
		floor_phy.setGravity(Vector3f.ZERO);

		this.getMainNode().setUserData(Settings.ENTITY, this);
		floor_phy.setUserObject(this);
		module.addEntity(this);

	}


	@Override
	public void process(float tpf) {
	}


	@Override
	public ICanShoot getShooter() {
		return shooter;
	}


	@Override
	public void collidedWith(ICollideable other) {
		if (other != this.shooter) {
			module.doExplosion(this.getLocation(), this);//, 3, 10);
			this.remove();
		}
	}


	@Override
	public float getDamageCaused() {
		return 0;
	}



}
