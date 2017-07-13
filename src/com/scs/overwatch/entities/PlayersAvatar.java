package com.scs.overwatch.entities;

import java.awt.Point;

import com.jme3.font.BitmapFont;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.Camera.FrustumIntersect;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.scs.overwatch.MyBetterCharacterControl;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.abilities.IAbility;
import com.scs.overwatch.abilities.JetPac;
import com.scs.overwatch.components.ICanShoot;
import com.scs.overwatch.hud.HUD;
import com.scs.overwatch.input.IInputDevice;

import ssmith.util.RealtimeInterval;

public class PlayersAvatar extends PhysicalEntity implements ICanShoot {

	// Player dimensions
	public static final float PLAYER_HEIGHT = 1.5f;//1.5f;
	public static final float PLAYER_RAD = .2f;//.35f;
	
	private Vector3f walkDirection = new Vector3f();
	private IInputDevice input;

	//Temporary vectors used on each frame.
	private Camera cam;
	private Vector3f camDir = new Vector3f();
	private Vector3f camLeft = new Vector3f();

	private HUD hud;
	public MyBetterCharacterControl playerControl;
	public final int id;
	private RealtimeInterval shotInterval = new RealtimeInterval(200);
	private float timeSinceLastMove = 0;
	private IAbility ability;
	
	public int score = 0;

	public PlayersAvatar(Overwatch _game, int _id, Camera _cam, IInputDevice _input) {
		super(_game, "Player");

		id = _id;
		cam = _cam;
		input = _input;

		/** Create a box to use as our player model */
		Box box1 = new Box(PLAYER_RAD, PLAYER_HEIGHT/2, PLAYER_RAD);
		Geometry playerGeometry = new Geometry("Player", box1);
		Material mat = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
		mat.setColor("Color", ColorRGBA.Blue);
		playerGeometry.setMaterial(mat);    
		playerGeometry.setLocalTranslation(new Vector3f(0,PLAYER_HEIGHT/2,0));
		this.getMainNode().attachChild(playerGeometry);
		
/*		Crate crate = new Crate(game, 0, 0, PLAYER_RAD*2, PLAYER_HEIGHT, PLAYER_RAD*2, 0);
		crate.getMainNode().setLocalTranslation(new Vector3f(0, PLAYER_HEIGHT, 0));
		this.getMainNode().attachChild(crate.getMainNode());
	*/	
		// create character control parameters (Radius,Height,Weight)
		playerControl = new MyBetterCharacterControl(PLAYER_RAD, PLAYER_HEIGHT, 1f);
		playerControl.setJumpForce(new Vector3f(0, 2f, 0)); 
		playerControl.setGravity(new Vector3f(0, 1f, 0));
		this.getMainNode().addControl(playerControl);

		game.bulletAppState.getPhysicsSpace().add(playerControl);
		
		this.getMainNode().setUserData(Settings.ENTITY, this);

		BitmapFont guiFont_small = game.getAssetManager().loadFont("Interface/Fonts/Console.fnt");
		// todo - cam.getWidth() = 640x480
		// cam.getViewPortLeft() = 0,5f
		hud = new HUD(game, game.getAssetManager(), cam.getViewPortLeft(), cam.getViewPortTop(), cam.getWidth(), cam.getHeight(), guiFont_small);
		game.getGuiNode().attachChild(hud);
		
		this.ability = new JetPac(this); // todo - make random
	}


	public void hitByBullet() {
		this.hud.showDamageBox();
		this.moveToStartPostion();
	}
	
	
	public void moveToStartPostion() {
		Point p = game.map.getPlayerStartPos(id);
		playerControl.warp(new Vector3f(p.x, 10f, p.y));

	}
	
	
	@Override
	public void process(float tpf) {
		timeSinceLastMove += tpf;
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
		playerControl.setWalkDirection(walkDirection);

		if (input.isJumpPressed()|| timeSinceLastMove > 10) {
			Settings.p("timeSinceLastMove=" + timeSinceLastMove);
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

	}


	public void shoot() {
		if (shotInterval.hitInterval()) {
			Bullet b = new Bullet(game, this);
			game.addEntity(b);
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


	@Override
	public void hasSuccessfullyHit(Entity e) {
		this.score++;
		this.jump();
	}

}
