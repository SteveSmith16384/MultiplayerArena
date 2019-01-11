package com.scs.multiplayerarena.entities;

import com.jme3.audio.AudioNode;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.multiplayerarena.MultiplayerArenaMain;
import com.scs.multiplayerarena.Settings;
import com.scs.multiplayerarena.components.IBullet;
import com.scs.multiplayerarena.components.ICanShoot;
import com.scs.multiplayerarena.components.ICollideable;
import com.scs.multiplayerarena.effects.Explosion;
import com.scs.multiplayerarena.models.BeamLaserModel;
import com.scs.multiplayerarena.modules.GameModule;

public class LaserBullet extends PhysicalEntity implements IBullet {

	public ICanShoot shooter;
	private float timeLeft = 3;

	public LaserBullet(MultiplayerArenaMain _game, GameModule _module, ICanShoot _shooter) {
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
		rigidBodyControl = new RigidBodyControl(.1f);
		/** Add physical ball to physics space. */
		ball_geo.addControl(rigidBodyControl);
		module.bulletAppState.getPhysicsSpace().add(rigidBodyControl);
		/** Accelerate the physical ball to shoot it. */
		rigidBodyControl.setLinearVelocity(shooter.getShootDir().mult(40));
		rigidBodyControl.setGravity(Vector3f.ZERO);

		this.getMainNode().setUserData(Settings.ENTITY, this);
		ball_geo.setUserData(Settings.ENTITY, this);
		rigidBodyControl.setUserObject(this);
		module.addEntity(this);

		AudioNode audio_gun = new AudioNode(game.getAssetManager(), "Sound/laser3.wav", false);
		audio_gun.setPositional(false);
		audio_gun.setLooping(false);
		audio_gun.setVolume(2);
		this.getMainNode().attachChild(audio_gun);
		audio_gun.play();

	}


	@Override
	public void process(float tpf) {
		this.timeLeft -= tpf;
		if (this.timeLeft < 0) {
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

			if (Settings.SHOW_FLASH_EXPLOSIONS) {
				Explosion expl = new Explosion(module, game.getRootNode(), game.getAssetManager(), game.getRenderManager(), .05f);
				expl.setLocalTranslation(this.getLocation());
				module.addEntity(expl);
			}

			CubeExplosionShard.Factory(game, module, this.getLocation(), 3);

			module.audioSmallExplode.play();

			this.remove(); // Don't bounce
		}
	}


	@Override
	public float getDamageCaused() {
		return 10;
	}



}
