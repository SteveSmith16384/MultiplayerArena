package com.scs.overwatch.entities;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapFont;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.Camera.FrustumIntersect;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.hud.HUD;
import com.scs.overwatch.input.IInputDevice;

public class PlayersAvatar extends PhysicalEntity {

	private HUD hud;

	//private SpotLight spotlight;
	private Vector3f walkDirection = new Vector3f();
	public boolean left = false, right = false, up = false, down = false;
	private IInputDevice input; // todo - use this
	
	//Temporary vectors used on each frame.
	private Camera cam;
	private Vector3f camDir = new Vector3f();
	private Vector3f camLeft = new Vector3f();

	private Geometry playerGeometry;
	public BetterCharacterControl playerControl;

	public final int id;

	public PlayersAvatar(Overwatch _game, int _id, Camera _cam, IInputDevice _input) {
		super(_game, "Player");

		id = _id;
		cam = _cam;
		input = _input;

		/** Create a box to use as our player model */
		Box box1 = new Box(Settings.PLAYER_RAD, Settings.PLAYER_HEIGHT, Settings.PLAYER_RAD);
		playerGeometry = new Geometry("Player", box1);
		Material mat = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
		mat.setColor("Color", ColorRGBA.Blue);
		playerGeometry.setMaterial(mat);    
		playerGeometry.setLocalTranslation(new Vector3f(0,2,0));
		//playerGeometry.setCullHint(CullHint.Always);
		this.getMainNode().attachChild(playerGeometry);

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
		/*
		 * The direction of character is determined by the camera angle
		 * the Y direction is set to zero to keep our character from
		 * lifting of terrain. For free flying games simply add speed 
		 * to Y axis
		 */
		camDir.set(cam.getDirection()).multLocal(Settings.moveSpeed, 0.0f, Settings.moveSpeed);
		camLeft.set(cam.getLeft()).multLocal(Settings.strafeSpeed);
		walkDirection.set(0, 0, 0);
		if (left) {
			walkDirection.addLocal(camLeft);
		}
		if (right) {
			walkDirection.addLocal(camLeft.negate());
		}
		if (up) {
			walkDirection.addLocal(camDir);
		}
		if (down) {
			walkDirection.addLocal(camDir.negate());
		}
		playerControl.setWalkDirection(walkDirection);

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
		Sphere sphere = new Sphere(32, 32, 0.4f, true, false); // todo - create bullet entity
		sphere.setTextureMode(TextureMode.Projected);
		/** Create a cannon ball geometry and attach to scene graph. */
		Geometry ball_geo = new Geometry("cannon ball", sphere);
		//ball_geo.setMaterial(stone_mat);
		game.getRootNode().attachChild(ball_geo);
		/** Position the cannon ball  */
		ball_geo.setLocalTranslation(cam.getLocation());
		/** Make the ball physical with a mass > 0.0f */
		RigidBodyControl ball_phy = new RigidBodyControl(1f);
		/** Add physical ball to physics space. */
		ball_geo.addControl(ball_phy);
		game.bulletAppState.getPhysicsSpace().add(ball_phy);
		/** Accelerate the physical ball to shoot it. */
		ball_phy.setLinearVelocity(cam.getDirection().mult(25));
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

}
