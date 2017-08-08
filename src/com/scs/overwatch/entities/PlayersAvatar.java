package com.scs.overwatch.entities;

import java.awt.Point;

import ssmith.lang.NumberFunctions;

import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.Camera.FrustumIntersect;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.scs.overwatch.MyBetterCharacterControl;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.abilities.IAbility;
import com.scs.overwatch.abilities.Invisibility;
import com.scs.overwatch.abilities.JetPac;
import com.scs.overwatch.abilities.RunFast;
import com.scs.overwatch.components.IAffectedByPhysics;
import com.scs.overwatch.components.IBullet;
import com.scs.overwatch.components.ICanShoot;
import com.scs.overwatch.components.ICollideable;
import com.scs.overwatch.components.IDamagable;
import com.scs.overwatch.components.IEntity;
import com.scs.overwatch.components.IProcessable;
import com.scs.overwatch.components.IShowOnHUD;
import com.scs.overwatch.components.ITargetByAI;
import com.scs.overwatch.hud.AbstractHUDImage;
import com.scs.overwatch.hud.HUD;
import com.scs.overwatch.input.IInputDevice;
import com.scs.overwatch.modules.GameModule;
import com.scs.overwatch.weapons.GrenadeLauncher;
import com.scs.overwatch.weapons.LaserRifle;

public class PlayersAvatar extends PhysicalEntity implements IProcessable, ICollideable, ICanShoot, IShowOnHUD, ITargetByAI, IAffectedByPhysics, IDamagable {

	private static final long RESTART_DUR = 3000;

	// Player dimensions
	public static final float PLAYER_HEIGHT = 0.7f;
	public static final float PLAYER_RAD = 0.2f;
	private static final float WEIGHT = 3f;

	public final Vector3f walkDirection = new Vector3f();
	public float moveSpeed = Settings.DEFAULT_MOVE_SPEED;
	private IInputDevice input;

	//Temporary vectors used on each frame.
	private Camera cam;
	private final Vector3f camDir = new Vector3f();
	private final Vector3f camLeft = new Vector3f();

	public HUD hud;
	public MyBetterCharacterControl playerControl;
	public final int id;
	private IAbility abilityGun, abilityOther;
	public Geometry playerGeometry;
	private int score = 20;
	private float health = 100;

	private boolean restarting = false;
	private long restartAt, invulnerableUntil;
	private Geometry gun;

	public PlayersAvatar(Overwatch _game, GameModule _module, int _id, Camera _cam, IInputDevice _input, HUD _hud) {
		super(_game, _module, "Player");

		id = _id;
		cam = _cam;
		input = _input;
		hud = _hud;

		{
			// Add player's box
			Box box1 = new Box(PLAYER_RAD, PLAYER_HEIGHT/2, PLAYER_RAD);
			//Cylinder box1 = new Cylinder(1, 8, PLAYER_RAD, PLAYER_HEIGHT, true);
			playerGeometry = new Geometry("Player", box1);
			TextureKey key3 = new TextureKey("Textures/computerconsole2.jpg");
			key3.setGenerateMips(true);
			Texture tex3 = game.getAssetManager().loadTexture(key3);
			Material floor_mat = null;
			if (Settings.LIGHTING) {
				floor_mat = new Material(game.getAssetManager(),"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
				floor_mat.setTexture("DiffuseMap", tex3);
			} else {
				floor_mat = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
				floor_mat.setTexture("ColorMap", tex3);
			}
			playerGeometry.setMaterial(floor_mat);
			//playerGeometry.setLocalTranslation(new Vector3f(0, PLAYER_HEIGHT/2, 0)); // Need this to ensure the crate is on the floor
			playerGeometry.setLocalTranslation(new Vector3f(0, (PLAYER_HEIGHT/2)-.075f, 0)); // Need this to ensure the crate is on the floor
			this.getMainNode().attachChild(playerGeometry);
			//this.getMainNode().setLocalTranslation(new Vector3f(0,PLAYER_HEIGHT,0)); // Need this to ensure the crate is on the floor
		}

		// Add gun
		{
			Box box1 = new Box(.1f, .1f, .3f);
			gun = new Geometry("Gun", box1);
			TextureKey key3 = new TextureKey("Textures/computerconsole2.jpg");
			key3.setGenerateMips(true);
			Texture tex3 = game.getAssetManager().loadTexture(key3);
			Material floor_mat = null;
			if (Settings.LIGHTING) {
				floor_mat = new Material(game.getAssetManager(),"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
				floor_mat.setTexture("DiffuseMap", tex3);
			} else {
				floor_mat = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
				floor_mat.setTexture("ColorMap", tex3);
			}
			gun.setMaterial(floor_mat);
			gun.setLocalTranslation(0, 0, .3f);
			this.getMainNode().attachChild(gun);
		}

		// create character control parameters (Radius,Height,Weight)
		playerControl = new MyBetterCharacterControl(PLAYER_RAD, PLAYER_HEIGHT, WEIGHT);
		playerControl.setJumpForce(new Vector3f(0, 6f, 0)); 
		this.getMainNode().addControl(playerControl);

		module.bulletAppState.getPhysicsSpace().add(playerControl);

		this.getMainNode().setUserData(Settings.ENTITY, this);
		playerControl.getPhysicsRigidBody().setUserObject(this);

		//abilityGun = new RocketLauncher(_game, _module, this);
		abilityGun = new LaserRifle(_game, _module, this); 
		this.abilityOther = new JetPac(this);// getRandomAbility(this);

		this.hud.setAbilityGunText(this.abilityGun.getHudText());
		this.hud.setAbilityOtherText(this.abilityOther.getHudText());
	}


	private static IAbility getRandomAbility(PlayersAvatar _player) {
		int i = NumberFunctions.rnd(1, 3);
		switch (i) {
		case 1:
			return new JetPac(_player);
		case 2:
			return new Invisibility(_player);
		case 3:
			return new RunFast(_player);
		default:
			throw new RuntimeException("Unknown ability: " + i);
		}

	}


	public void moveToStartPostion() {
		Settings.p("Restarting player");
		Point p = module.mapData.getPlayerStartPos(id);
		playerControl.warp(new Vector3f(p.x, 20f, p.y));
		this.getMainNode().setLocalTranslation(new Vector3f(p.x, 20f, p.y));
		this.getMainNode().updateGeometricState();
		Settings.p("Player starting at:" + this.getMainNode().getWorldTranslation());
	}


	@Override
	public void process(float tpf) {
		if (this.restarting) {
			if (this.restartAt < System.currentTimeMillis()) {
				this.moveToStartPostion();
				restarting = false;
			}
		}

		if (!this.restarting) {
			// Have we fallen off the edge
			if (this.getMainNode().getWorldTranslation().y < -5f) {
				//this.moveToStartPostion();
				died();
				return;
			}

			abilityGun.process(tpf);
			abilityOther.process(tpf);

			hud.process(tpf);

			if (input.isAbilityOtherPressed()) { // Must be before we set the walkDirection & moveSpeed, as this method may affect it
				//Settings.p("Using " + this.ability.toString());
				this.abilityOther.activate(tpf);
			}

			/*
			 * The direction of character is determined by the camera angle
			 * the Y direction is set to zero to keep our character from
			 * lifting of terrain. For free flying games simply add speed 
			 * to Y axis
			 */
			camDir.set(cam.getDirection()).multLocal(moveSpeed, 0.0f, moveSpeed);
			camLeft.set(cam.getLeft()).multLocal(moveSpeed);
			if (input.getFwdValue() > 0) {		
				//Settings.p("fwd=" + input.getFwdValue());
				walkDirection.addLocal(camDir.mult(input.getFwdValue()));
			}
			if (input.getBackValue() > 0) {
				walkDirection.addLocal(camDir.negate().mult(input.getBackValue()));
			}
			if (input.getStrafeLeftValue() > 0) {		
				walkDirection.addLocal(camLeft.mult(input.getStrafeLeftValue()));
			}
			if (input.getStrafeRightValue() > 0) {		
				walkDirection.addLocal(camLeft.negate().mult(input.getStrafeRightValue()));
			}

			/*if (walkDirection.length() != 0) {
				Settings.p("walkDirection=" + walkDirection);
			}*/
			playerControl.setWalkDirection(walkDirection);

			if (input.isJumpPressed()){
				this.jump();
			}

			if (input.isShootPressed()) {
				shoot();
			}

			// These must be after we might use them, so the hud is correct 
			this.hud.setAbilityGunText(this.abilityGun.getHudText());
			this.hud.setAbilityOtherText(this.abilityOther.getHudText());

		}

		// Position camera at node
		Vector3f vec = getMainNode().getWorldTranslation();
		cam.setLocation(new Vector3f(vec.x, vec.y + (PLAYER_HEIGHT/2), vec.z));

		// Rotate us to point in the direction of the camera
		Vector3f lookAtPoint = cam.getLocation().add(cam.getDirection().mult(10));
		gun.lookAt(lookAtPoint.clone(), Vector3f.UNIT_Y);
		lookAtPoint.y = cam.getLocation().y; // Look horizontal
		this.playerGeometry.lookAt(lookAtPoint, Vector3f.UNIT_Y);
		//this.getMainNode().lookAt(lookAtPoint.clone(), Vector3f.UNIT_Y);  This won't rotate the model since it's locked to the physics controller

		this.input.resetFlags();

		walkDirection.set(0, 0, 0);
	}


	public boolean isOnGround() {
		return playerControl.isOnGround();
	}


	public void shoot() {
		if (this.abilityGun.activate(0)) {
			this.score--;
			this.hud.setScore(this.score);
		}
	}


	public FrustumIntersect getInsideOutside(PhysicalEntity entity) {
		FrustumIntersect insideoutside = cam.contains(entity.getMainNode().getWorldBound());
		return insideoutside;
	}


	@Override
	public void remove() {
		super.remove();
		this.module.bulletAppState.getPhysicsSpace().remove(this.playerControl);

	}


	@Override
	public Vector3f getLocation() {
		return this.cam.getLocation();
	}


	@Override
	public Vector3f getShootDir() {
		return this.cam.getDirection();
	}


	public void jump() {
		this.playerControl.jump();
	}


	public void hitByBullet(float dam) {
		if (System.currentTimeMillis() > this.invulnerableUntil) {
			if (dam > 0) {
				module.doExplosion(this.main_node.getWorldTranslation());//, 5, 20);
				this.health -= dam;
				this.hud.setHealth(this.health);
				this.hud.showDamageBox();

				died();
			}
		} else {
			Settings.p("Player hit but is currently invulnrable");
		}
	}


	private void died() {
		this.restarting = true;
		this.restartAt = System.currentTimeMillis() + RESTART_DUR;
		invulnerableUntil = System.currentTimeMillis() + (RESTART_DUR*2);

		// Move us below the map
		Vector3f pos = this.getMainNode().getWorldTranslation().clone();//.floor_phy.getPhysicsLocation().clone();
		pos.y = -3;
		playerControl.warp(pos);
	}


	@Override
	public void hasSuccessfullyHit(IEntity e) {
		this.incScore(20);
		//new AbstractHUDImage(game, module, this.hud, "Textures/text/hit.png", this.hud.hud_width, this.hud.hud_height, 2);
	}


	public void incScore(int amt) {
		this.score += amt;
		this.hud.setScore(this.score);

		if (this.score >= 100) {
			new AbstractHUDImage(game, module, this.hud, "Textures/text/winner.png", this.hud.hud_width, this.hud.hud_height, 10);
		}
	}


	@Override
	public void collidedWith(ICollideable other) {
		if (other instanceof IBullet) {
			IBullet bullet = (IBullet)other;
			if (bullet.getShooter() != this) {
				this.hitByBullet(bullet.getDamageCaused());
				bullet.getShooter().hasSuccessfullyHit(this);
			}
		} else if (other instanceof Collectable) {
			Collectable col = (Collectable)other;
			col.remove();
			this.incScore(20);
			this.hud.showCollectBox();

			// Drop new collectable
			Point p = module.mapData.getRandomCollectablePos();
			Collectable c = new Collectable(Overwatch.instance, module, p.x, p.y);
			Overwatch.instance.getRootNode().attachChild(c.getMainNode());
		}
	}


	@Override
	public void applyForce(Vector3f force) {
		//playerControl.getPhysicsRigidBody().applyImpulse(force, Vector3f.ZERO);//.applyCentralForce(dir);
		//playerControl.getPhysicsRigidBody().applyCentralForce(force);
		//Settings.p("Applying force to player:" + force);
		//this.addWalkDirection.addLocal(force);
	}


	public Camera getCamera() {
		return this.cam;
	}


	@Override
	public void damaged(float amt) {
		// TODO Auto-generated method stub
	}


	@Override
	public boolean blocksPlatforms() {
		return false;
	}

}
