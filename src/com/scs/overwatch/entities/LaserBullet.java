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
import com.scs.overwatch.effects.Explosion;
import com.scs.overwatch.models.BeamLaserModel;
import com.scs.overwatch.modules.GameModule;

public class LaserBullet extends PhysicalEntity implements IBullet {

	public ICanShoot shooter;
	private float timeLeft = 3;

	public LaserBullet(Overwatch _game, GameModule _module, ICanShoot _shooter) {
		super(_game, _module, "LaserBullet");

		this.shooter = _shooter;

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
		ball_geo.setUserData(Settings.ENTITY, this);
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
			//Settings.p("Laser collided with " + other);
			//this.getLocation();
			Explosion expl = new Explosion(module, game.getRootNode(), game.getAssetManager(), game.getRenderManager(), .05f);
			expl.setLocalTranslation(this.getLocation());
			module.addEntity(expl);
			
			CubeExplosionShard.Factory(game, module, this.getLocation(), 3);

			module.audioSmallExplode.play();
			
			this.remove(); // Don't bounce
		}
	}


	@Override
	public float getDamageCaused() {
		return 1;
	}



}
