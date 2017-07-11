package com.scs.overwatch.entities;

import ssmith.util.RealtimeInterval;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.font.BitmapFont;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.Camera.FrustumIntersect;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.components.ICanShoot;
import com.scs.overwatch.hud.HUD;
import com.scs.overwatch.input.IInputDevice;

public class PlayersAvatar extends PhysicalEntity implements ICanShoot {

	private HUD hud;

	//private SpotLight spotlight;
	private Vector3f walkDirection = new Vector3f();
	private IInputDevice input;

	//Temporary vectors used on each frame.
	private Camera cam;
	private Vector3f camDir = new Vector3f();
	private Vector3f camLeft = new Vector3f();

	//private Geometry playerGeometry;
	public BetterCharacterControl playerControl;

	public final int id;
	private RealtimeInterval shotInterval = new RealtimeInterval(200);
	private float timeSinceLastMove = 0;
	
	public int score = 0;

	public PlayersAvatar(Overwatch _game, int _id, Camera _cam, IInputDevice _input) {
		super(_game, "Player");

		id = _id;
		cam = _cam;
		input = _input;

		/** Create a box to use as our player model */
		/*Box box1 = new Box(Settings.PLAYER_RAD, Settings.PLAYER_HEIGHT, Settings.PLAYER_RAD);
		playerGeometry = new Geometry("Player", box1);
		Material mat = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
		mat.setColor("Color", ColorRGBA.Blue);
		playerGeometry.setMaterial(mat);    
		playerGeometry.setLocalTranslation(new Vector3f(0,2,0));
		//playerGeometry.setCullHint(CullHint.Always);
		this.getMainNode().attachChild(playerGeometry);*/
		Crate crate = new Crate(game, 0, 0, 0);
		this.getMainNode().attachChild(crate.getMainNode());
		
		// create character control parameters (Radius,Height,Weight)
		playerControl = new BetterCharacterControl(Settings.PLAYER_RAD, Settings.PLAYER_HEIGHT, 1f);
		// set basic physical properties:
		playerControl.setJumpForce(new Vector3f(0, 5f, 0)); 
		playerControl.setGravity(new Vector3f(0, 1f, 0));
		//playerControl.warp(new Vector3f(0, 6, 0)); // So we drop
		this.getMainNode().addControl(playerControl);

		game.bulletAppState.getPhysicsSpace().add(playerControl);

		this.getMainNode().setUserData(Settings.ENTITY, this);

		BitmapFont guiFont_small = game.getAssetManager().loadFont("Interface/Fonts/Console.fnt");
		hud = new HUD(game, game.getAssetManager(), 0, 0, cam.getWidth(), cam.getHeight(), guiFont_small);
		game.getGuiNode().attachChild(hud);
		//this.entities.add(hud);

		/*this.spotlight = new SpotLight();
		spotlight.setColor(ColorRGBA.White.mult(3f));
		spotlight.setSpotRange(10f);
		spotlight.setSpotInnerAngle(FastMath.QUARTER_PI / 8);
		spotlight.setSpotOuterAngle(FastMath.QUARTER_PI / 2);
		game.getRootNode().addLight(spotlight);*/
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

		if (input.isJumpPressed() || timeSinceLastMove > 10) {
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
		cam.setLocation(new Vector3f(vec.x, vec.y + Settings.PLAYER_HEIGHT, vec.z));

		/*if (spotlight != null) {
			this.spotlight.setPosition(cam.getLocation());
			this.spotlight.setDirection(cam.getDirection());
		}*/

	}


	public void shoot() {
		if (shotInterval.hitInterval()) {
			/*Sphere sphere = new Sphere(32, 32, 0.4f, true, false); // todo - create bullet entity
			sphere.setTextureMode(TextureMode.Projected);
			Geometry ball_geo = new Geometry("cannon ball", sphere);

			TextureKey key3 = new TextureKey( "Textures/OldRedBricks_T.jpg");
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

			game.getRootNode().attachChild(ball_geo);

			ball_geo.setLocalTranslation(cam.getLocation());

			RigidBodyControl ball_phy = new RigidBodyControl(1f);

			ball_geo.addControl(ball_phy);
			game.bulletAppState.getPhysicsSpace().add(ball_phy);

			ball_phy.setLinearVelocity(cam.getDirection().mult(25));*/
			
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
		
	}

}
