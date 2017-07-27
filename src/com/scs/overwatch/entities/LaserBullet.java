package com.scs.overwatch.entities;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.components.IBullet;
import com.scs.overwatch.components.ICanShoot;
import com.scs.overwatch.components.ICollideable;
import com.scs.overwatch.models.BeamLaserModel;
import com.scs.overwatch.modules.GameModule;

public class LaserBullet extends PhysicalEntity implements IBullet {

	public ICanShoot shooter;
	//private RigidBodyControl ball_phy;
	private float timeLeft = 3;

	public LaserBullet(Overwatch _game, GameModule _module, ICanShoot _shooter) {
		super(_game, _module, "LaserBullet");

		this.shooter = _shooter;

		/*Sphere sphere = new Sphere(4, 4, 0.1f, true, false); // todo - use long cylinder
		sphere.setTextureMode(TextureMode.Projected);
		Geometry ball_geo = new Geometry("cannon ball", sphere);

		TextureKey key3 = new TextureKey( "Textures/sun.jpg");
		Texture tex3 = game.getAssetManager().loadTexture(key3);
		Material floor_mat = null;
		if (Settings.LIGHTING) {
			floor_mat = new Material(game.getAssetManager(),"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
			floor_mat.setTexture("DiffuseMap", tex3);
		} else {
			floor_mat = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
			floor_mat.setTexture("ColorMap", tex3);
		}
		ball_geo.setMaterial(floor_mat);*/

		Vector3f origin = shooter.getLocation().clone();

		Node ball_geo = BeamLaserModel.Factory(game.getAssetManager(), origin, origin.add(shooter.getShootDir().multLocal(1)), ColorRGBA.Pink);
		
		this.main_node.attachChild(ball_geo);
		game.getRootNode().attachChild(this.main_node);
		/** Position the cannon ball  */
		ball_geo.setLocalTranslation(shooter.getLocation().add(shooter.getShootDir().multLocal(PlayersAvatar.PLAYER_RAD*3)));
		ball_geo.getLocalTranslation().y -= 0.1f; // Drop bullets slightly
		/** Make the ball physical with a mass > 0.0f */
		floor_phy = new RigidBodyControl(.1f);
		/** Add physical ball to physics space. */
		ball_geo.addControl(floor_phy);
		module.bulletAppState.getPhysicsSpace().add(floor_phy);
		/** Accelerate the physical ball to shoot it. */
		floor_phy.setLinearVelocity(shooter.getShootDir().mult(40));
		floor_phy.setGravity(Vector3f.ZERO);

		this.getMainNode().setUserData(Settings.ENTITY, this);
		floor_phy.setUserObject(this);
		module.addEntity(this);

	}


	@Override
	public void process(float tpf) {
		this.timeLeft -= tpf;
		if (this.timeLeft < 0) {
			//Settings.p("Bullet removed");
			this.remove();
		}

	}


	@Override
	public ICanShoot getShooter() {
		return shooter;
	}


	@Override
	public void collidedWith(ICollideable other) {
		if (other != this.shooter) {
			this.remove(); // Don't bounce
		}
	}


	@Override
	public float getDamageCaused() {
		return 1;
	}


	@Override
	public boolean blocksPlatforms() {
		return false;
	}


}
