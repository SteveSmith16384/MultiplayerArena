package com.scs.overwatch.entities;

import java.awt.Point;

import ssmith.util.RealtimeInterval;

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
import com.scs.overwatch.abilities.JetPac;
import com.scs.overwatch.components.ICanShoot;
import com.scs.overwatch.components.IEntity;
import com.scs.overwatch.hud.HUD;
import com.scs.overwatch.input.IInputDevice;

public class PlayersAvatar extends PhysicalEntity implements ICanShoot {

	// Player dimensions
	public static final float PLAYER_HEIGHT = 0.5f;//1.5f;
	public static final float PLAYER_RAD = 0.25f; //.2f; //.5f;//.35f; // if you increase this, player bounces!?
	private static final float WEIGHT = 3f;
	
	public Vector3f walkDirection = new Vector3f();
	private IInputDevice input;

	//Temporary vectors used on each frame.
	private Camera cam;
	private Vector3f camDir = new Vector3f();
	private Vector3f camLeft = new Vector3f();

	public HUD hud;
	public MyBetterCharacterControl playerControl;
	public final int id;
	private RealtimeInterval shotInterval = new RealtimeInterval(1000);
	private float timeSinceLastMove = 0;
	private IAbility ability;
	public Geometry playerGeometry;
	public int score = 20;

	public PlayersAvatar(Overwatch _game, int _id, Camera _cam, IInputDevice _input, HUD _hud) {
		super(_game, "Player");

		id = _id;
		cam = _cam;
		input = _input;
		hud = _hud;

		/*		Crate crate = new Crate(game, 0, 0, PLAYER_RAD*2, PLAYER_HEIGHT, PLAYER_RAD*2, 0);
		crate.getMainNode().setLocalTranslation(new Vector3f(0, PLAYER_HEIGHT, 0));
		this.getMainNode().attachChild(crate.getMainNode());
		 */	

		Box box1 = new Box(PLAYER_RAD, PLAYER_HEIGHT/2, PLAYER_RAD);
		playerGeometry = new Geometry("Player", box1);
		TextureKey key3 = new TextureKey("Textures/boxes and crates/1.jpg");
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

		// create character control parameters (Radius,Height,Weight)
		playerControl = new MyBetterCharacterControl(PLAYER_RAD, PLAYER_HEIGHT, WEIGHT);
		playerControl.setJumpForce(new Vector3f(0, 6f, 0)); 
		//playerControl.setGravity(new Vector3f(0, 1f, 0));
		this.getMainNode().addControl(playerControl);

		game.bulletAppState.getPhysicsSpace().add(playerControl);

		this.getMainNode().setUserData(Settings.ENTITY, this);
		playerControl.getPhysicsRigidBody().setUserObject(this);

		/*BitmapFont guiFont_small = game.getAssetManager().loadFont("Interface/Fonts/Console.fnt");
		// cam.getWidth() = 640x480, cam.getViewPortLeft() = 0.5f
		float x = cam.getWidth() * cam.getViewPortLeft();
		float y = cam.getHeight() * cam.getViewPortTop();
		int w = cam.getWidth();
		int h = cam.getHeight();
		hud = new HUD(game, game.getAssetManager(), x, y, w, h, guiFont_small);
		game.getGuiNode().attachChild(hud);*/

		this.ability = new JetPac(this); //Invisibility(this);//  todo - make random
	}


	public void moveToStartPostion() {
		Point p = game.map.getPlayerStartPos(id);
		playerControl.warp(new Vector3f(p.x, 10f, p.y));

	}


	@Override
	public void process(float tpf) {
		timeSinceLastMove += tpf;
		if (ability.process(tpf)) {
			this.hud.setAbilityText(this.ability.getHudText());
		}
		hud.process(tpf);

		/*
		 * The direction of character is determined by the camera angle
		 * the Y direction is set to zero to keep our character from
		 * lifting of terrain. For free flying games simply add speed 
		 * to Y axis
		 */
		camDir.set(cam.getDirection()).multLocal(Settings.moveSpeed, 0.0f, Settings.moveSpeed);
		camLeft.set(cam.getLeft()).multLocal(Settings.strafeSpeed);
		walkDirection.set(0, 0, 0);
		if (input.isStrafeLeftPressed()) {
			walkDirection.addLocal(camLeft);
			timeSinceLastMove = 0;
		}
		if (input.isStrafeRightPressed()) {
			walkDirection.addLocal(camLeft.negate());
			timeSinceLastMove = 0;
		}
		if (input.isFwdPressed()) {
			walkDirection.addLocal(camDir);
			timeSinceLastMove = 0;
		}
		if (input.isBackPressed()) {
			walkDirection.addLocal(camDir.negate());
			timeSinceLastMove = 0;
		}
		if (input.isAbility1Pressed()) { // Must be before we set the walkDirection, as this method may affect it
			Settings.p("Using " + this.ability.toString());
			this.ability.activate(tpf);
		}

		playerControl.setWalkDirection(walkDirection);

		if (input.isJumpPressed() || timeSinceLastMove > 10) {
			//Settings.p("timeSinceLastMove=" + timeSinceLastMove);
			timeSinceLastMove = 0;
			this.jump();
		}

		if (input.isShootPressed()) {
			timeSinceLastMove = 0;
			shoot();
		}

		/*
		 * By default the location of the box is on the bottom of the terrain
		 * we make a slight offset to adjust for head height.
		 */
		Vector3f vec = getMainNode().getWorldTranslation();
		cam.setLocation(new Vector3f(vec.x, vec.y + (PLAYER_HEIGHT/2), vec.z));

		// Rotate us to point in the direction of the camera
		Vector3f lookAtPoint = cam.getLocation().add(cam.getDirection().mult(10));
		lookAtPoint.y = PLAYER_HEIGHT;
		this.playerGeometry.lookAt(lookAtPoint, Vector3f.UNIT_Y);

		this.input.resetFlags();

		// Have we fallen off the edge
		if (this.getMainNode().getWorldTranslation().y < -5f) {
			this.moveToStartPostion();
		}
	}


	public void shoot() {
		if (shotInterval.hitInterval()) {
			Bullet b = new Bullet(game, this);
			game.addEntity(b);
			this.score--;
			if (this.score <= 0) {
				game.playerOut(this);
			}
		}
	}


	public FrustumIntersect getInsideOutside(PhysicalEntity entity) {
		FrustumIntersect insideoutside = cam.contains(entity.getMainNode().getWorldBound());
		return insideoutside;
	}


	@Override
	public void remove() {
		super.remove();
		this.game.bulletAppState.getPhysicsSpace().remove(this.playerControl);

	}


	@Override
	public Vector3f getLocation() {
		return this.cam.getLocation();
	}


	@Override
	public Vector3f getDir() {
		return this.cam.getDirection();
	}


	public void jump() {
		this.playerControl.jump();
	}


	public void hitByBullet() {
		this.hud.showDamageBox();
		this.moveToStartPostion();
	}



	@Override
	public void hasSuccessfullyHit(IEntity e) {
		this.score += 5;
		this.hud.setScore(this.score);
		this.jump();

		TimedBillboard bb = new TimedBillboard(game, "Textures/text/hit.png", this.cam, 4);
		game.addEntity(bb);
	}

}
