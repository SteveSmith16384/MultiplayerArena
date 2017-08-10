package com.scs.overwatch.modules;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import ssmith.util.TSArrayList;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.font.BitmapFont;
import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.RadialBlurFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.components.IAffectedByPhysics;
import com.scs.overwatch.components.ICollideable;
import com.scs.overwatch.components.IEntity;
import com.scs.overwatch.components.IProcessable;
import com.scs.overwatch.effects.SmallExplosion;
import com.scs.overwatch.entities.Collectable;
import com.scs.overwatch.entities.DodgeballBall;
import com.scs.overwatch.entities.PhysicalEntity;
import com.scs.overwatch.entities.PlayersAvatar;
import com.scs.overwatch.entities.RoamingAI;
import com.scs.overwatch.hud.HUD;
import com.scs.overwatch.input.IInputDevice;
import com.scs.overwatch.input.JoystickCamera2;
import com.scs.overwatch.input.JoystickCamera_ORIG;
import com.scs.overwatch.input.MouseAndKeyboardCamera;
import com.scs.overwatch.map.IPertinentMapData;
import com.scs.overwatch.map.SimpleCity;

public class GameModule implements IModule, PhysicsCollisionListener, ActionListener, PhysicsTickListener {

	private static final String QUIT = "Quit";
	private static final String TEST = "Test";

	protected Overwatch game;
	public BulletAppState bulletAppState;
	public TSArrayList<IEntity> entities = new TSArrayList<>();
	public TSArrayList<PlayersAvatar> avatars = new TSArrayList<>();
	public IPertinentMapData mapData;
	public List<PlayersAvatar> toWarp = new ArrayList<>();

	public GameModule(Overwatch _game) {
		super();

		game = _game;
	}


	@Override
	public void init() {
		game.getInputManager().addMapping(QUIT, new KeyTrigger(KeyInput.KEY_ESCAPE));
		game.getInputManager().addListener(this, QUIT);            

		game.getInputManager().addMapping(TEST, new KeyTrigger(KeyInput.KEY_T));
		game.getInputManager().addListener(this, TEST);            

		// Set up Physics
		bulletAppState = new BulletAppState();
		game.getStateManager().attach(bulletAppState);
		bulletAppState.getPhysicsSpace().addCollisionListener(this);
		bulletAppState.getPhysicsSpace().addTickListener(this);
		//bulletAppState.getPhysicsSpace().enableDebug(game.getAssetManager());

		game.getRenderManager().removeMainView(game.getViewPort()); // Since we create new ones for each player

		setUpLight();

		mapData = new SimpleCity(game, this);
		mapData.setup();
		//mapData = new OverworldMap(game, this);

		Joystick[] joysticks = game.getInputManager().getJoysticks();
		int numPlayers = 1+joysticks.length;

		// Auto-Create player 0 - keyboard and mouse
		{
			Camera newCam = this.createCamera(0, numPlayers);
			HUD hud = this.createHUD(newCam, 0);
			MouseAndKeyboardCamera keyboard = new MouseAndKeyboardCamera(newCam, game.getInputManager());
			this.addPlayersAvatar(0, newCam, keyboard, hud); // Keyboard player
		}

		// Create players for each joystick
		int nextid=1;
		if (joysticks == null || joysticks.length == 0) {
			//Settings.p("NO JOYSTICKS/GAMEPADS");
		} else {
			for (Joystick j : joysticks) {
				int id = nextid++;
				Camera newCam = this.createCamera(id, numPlayers);
				HUD hud = this.createHUD(newCam, id);
				JoystickCamera_ORIG joyCam = new JoystickCamera_ORIG(newCam, j, game.getInputManager());
				this.addPlayersAvatar(id, newCam, joyCam, hud);
			}
		}
		if (Settings.ALWAYS_SHOW_4_CAMS) {
			// Create extra cameras
			for (int id=nextid ; id<=3 ; id++) {
				Camera c = this.createCamera(id, numPlayers);
				this.createHUD(c, id);
				switch (id) {
				case 1:
					c.setLocation(new Vector3f(2f, PlayersAvatar.PLAYER_HEIGHT, 2f));
					break;
				case 2:
					c.setLocation(new Vector3f(mapData.getWidth()-3, PlayersAvatar.PLAYER_HEIGHT, 2f));
					break;
				case 3:
					c.setLocation(new Vector3f(2f, PlayersAvatar.PLAYER_HEIGHT, mapData.getDepth()-3));
					break;
				}
				c.lookAt(new Vector3f(mapData.getWidth()/2, PlayersAvatar.PLAYER_HEIGHT, mapData.getDepth()/2), Vector3f.UNIT_Y);
			}
		}

		//stateManager.getState(StatsAppState.class).toggleStats(); // Turn off stats

	}


	private Camera createCamera(int id, int numPlayers) {
		Camera newCam = null;
		if (id == 0) {
			newCam = game.getCamera();
		} else {
			newCam = game.getCamera().clone();
		}

		if (Settings.ALWAYS_SHOW_4_CAMS || numPlayers > 2) {
			//newCam.resize(Overwatch.settings.getWidth()/2, Overwatch.settings.getHeight()/2, true);
			newCam.setFrustumPerspective(45f, (float) newCam.getWidth() / newCam.getHeight(), 0.01f, Settings.CAM_DIST);
			switch (id) { // left/right/bottom/top, from bottom-left!
			case 0: // TL
				newCam.setViewPort(0f, 0.5f, 0.5f, 1f);
				newCam.setName("Cam_TL");
				break;
			case 1: // TR
				newCam.setViewPort(0.5f, 1f, 0.5f, 1f);
				newCam.setName("Cam_TR");
				break;
			case 2: // BL
				newCam.setViewPort(0f, 0.5f, 0f, .5f);
				newCam.setName("Cam_BL");
				break;
			case 3: // BR
				newCam.setViewPort(0.5f, 1f, 0f, .5f);
				newCam.setName("Cam_BR");
				break;
			default:
				throw new RuntimeException("Unknown player id: " + id);
			}
		} else if (numPlayers == 2) {
			//newCam.resize(Overwatch.settings.getWidth(), Overwatch.settings.getHeight()/2, true);
			newCam.setFrustumPerspective(45f, (float) (newCam.getWidth()*2) / newCam.getHeight(), 0.01f, Settings.CAM_DIST);
			switch (id) { // left/right/bottom/top, from bottom-left!
			case 0: // TL
				//Settings.p("Creating camera top");
				newCam.setViewPort(0f, 1f, 0.5f, 1f);
				newCam.setName("Cam_Top");
				break;
			case 1: // TR
				//Settings.p("Creating camera bottom");
				newCam.setViewPort(0.0f, 1f, 0f, .5f);
				newCam.setName("Cam_bottom");
				break;
			default:
				throw new RuntimeException("Unknown player id: " + id);
			}
		} else if (numPlayers == 1) {
			//newCam.resize(Overwatch.settings.getWidth(), Overwatch.settings.getHeight(), true);
			newCam.setFrustumPerspective(45f, (float) newCam.getWidth() / newCam.getHeight(), 0.01f, Settings.CAM_DIST);
			//Settings.p("Creating full-screen camera");
			newCam.setViewPort(0f, 1f, 0f, 1f);
			newCam.setName("Cam_FullScreen");
		} else {
			throw new RuntimeException("Unknown number of players");

		}

		final ViewPort view2 = game.getRenderManager().createMainView("viewport_"+newCam.toString(), newCam);
		view2.setBackgroundColor(new ColorRGBA(0, 0, 0, 0f));
		view2.setClearFlags(true, true, true);
		view2.attachScene(game.getRootNode());

		FilterPostProcessor fpp = new FilterPostProcessor(game.getAssetManager());
		if (Settings.NEON) {
			BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Scene);
			bloom.setEnabled(true);
			bloom.setBloomIntensity(40f);//50f);
			bloom.setBlurScale(10f);
			fpp.addFilter(bloom);

			// test filter
			//RadialBlurFilter blur = new RadialBlurFilter(); // todo - remove?
			//fpp.addFilter(blur);
		}
		view2.addProcessor(fpp);

		return newCam;
	}


	private HUD createHUD_ORIG(Camera c, int id) {
		BitmapFont guiFont_small = game.getAssetManager().loadFont("Interface/Fonts/Console.fnt");

		// cam.getWidth() = 640x480, cam.getViewPortLeft() = 0.5f
		float x = c.getWidth() * c.getViewPortLeft();
		//float y = (_cam.getHeight() * _cam.getViewPortTop())-(_cam.getHeight()/2);
		float y = (c.getHeight() * c.getViewPortTop())-(c.getHeight()/2);
		Settings.p("Created HUD for " + id + ": " + x + "," +y);
		float w = c.getWidth() * (c.getViewPortRight()-c.getViewPortLeft());
		float h = c.getHeight() * (c.getViewPortTop()-c.getViewPortBottom());
		HUD hud = new HUD(game, this, x, y, w, h, guiFont_small, id, c);
		game.getGuiNode().attachChild(hud);
		return hud;

	}


	private HUD createHUD(Camera c, int id) {
		BitmapFont guiFont_small = game.getAssetManager().loadFont("Interface/Fonts/Console.fnt");
		// HUD coords are full screen co-ords!
		// cam.getWidth() = 640x480, cam.getViewPortLeft() = 0.5f
		float xBL = c.getWidth() * c.getViewPortLeft();
		//float y = (c.getHeight() * c.getViewPortTop())-(c.getHeight()/2);
		float yBL = c.getHeight() * c.getViewPortBottom();
		
		Settings.p("Created HUD for " + id + ": " + xBL + "," +yBL);

		float w = c.getWidth() * (c.getViewPortRight()-c.getViewPortLeft());
		float h = c.getHeight() * (c.getViewPortTop()-c.getViewPortBottom());
		HUD hud = new HUD(game, this, xBL, yBL, w, h, guiFont_small, id, c);
		game.getGuiNode().attachChild(hud);
		return hud;

	}


	private void addPlayersAvatar(int id, Camera cam, IInputDevice input, HUD hud) {
		PlayersAvatar player = new PlayersAvatar(game, this, id, cam, input, hud);
		game.getRootNode().attachChild(player.getMainNode());
		this.entities.add(player);

		player.moveToStartPostion();

		// Look towards centre
		player.getMainNode().lookAt(new Vector3f(mapData.getWidth()/2, PlayersAvatar.PLAYER_HEIGHT, mapData.getDepth()/2), Vector3f.UNIT_Y);

	}


	private void setUpLight() {
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(3));
		game.getRootNode().addLight(al);
	}


	@Override
	public void update(float tpf) {
		if (tpf > 1) {
			Settings.p("TPF is " + tpf);
			tpf = 1;
		}
		this.entities.refresh();
		this.avatars.refresh();

		for(IEntity e : entities) {
			if (e instanceof IProcessable) {
				IProcessable ip = (IProcessable)e;
				ip.process(tpf);
			}
		}

	}


	@Override
	public void collision(PhysicsCollisionEvent event) {
		//String s = event.getObjectA().getUserObject().toString() + " collided with " + event.getObjectB().getUserObject().toString();
		//System.out.println(s);
		/*if (s.equals("Entity:Player collided with cannon ball (Geometry)")) {
			int f = 3;
		}*/

		//String s = event.getObjectA().getUserObject().toString() + " collided with " + event.getObjectB().getUserObject().toString();
		//System.out.println(s);
		/*if (s.equals("Entity:Player collided with cannon ball (Geometry)")) {
			int f = 3;
		}*/

		PhysicalEntity a=null, b=null;
		Object oa = event.getObjectA().getUserObject(); 
		if (oa instanceof Spatial) {
			Spatial ga = (Spatial)event.getObjectA().getUserObject(); 
			a = ga.getUserData(Settings.ENTITY);
		} else if (oa instanceof PhysicalEntity) {
			a = (PhysicalEntity)oa;
		}

		Object ob = event.getObjectB().getUserObject(); 
		if (ob instanceof Spatial) {
			Spatial gb = (Spatial)event.getObjectB().getUserObject(); 
			b = gb.getUserData(Settings.ENTITY);
		} else if (oa instanceof PhysicalEntity) {
			b = (PhysicalEntity)ob;
		}

		if (a != null && b != null) {
			//CollisionLogic.collision(this, a, b);
			if (a instanceof ICollideable && b instanceof ICollideable) {
				//Settings.p(a + " has collided with " + b);
				ICollideable ica = (ICollideable)a;
				ICollideable icb = (ICollideable)b;
				ica.collidedWith(icb);
				icb.collidedWith(ica);
			}
		} else {
			if (a == null) {
				Settings.p(oa + " has no entity data!");
			}
			if (b == null) {
				Settings.p(ob + " has no entity data!");
			}
		}
	}


	public void addEntity(IEntity e) {
		this.entities.add(e);

		if (e instanceof PlayersAvatar) {
			PlayersAvatar a = (PlayersAvatar)e;
			this.avatars.add(a);
		}
	}


	public void removeEntity(IEntity e) {
		this.entities.remove(e);

		if (e instanceof PlayersAvatar) {
			PlayersAvatar a = (PlayersAvatar)e;
			this.avatars.remove(a);
		}
	}


	public BulletAppState getBulletAppState() {
		return bulletAppState;
	}


	@Override
	public void onAction(String name, boolean value, float tpf) {
		if (!value) {
			return;
		}

		if (name.equals(TEST)) {
			for(IEntity e : entities) {
				if (e instanceof PlayersAvatar) {
					PlayersAvatar ip = (PlayersAvatar)e;

					ip.damaged(999);
					
					/*Vector3f pos = ip.getLocation().clone();
					pos.x-=2;
					pos.y = 0;
					pos.z-=2;
					doExplosion(pos);//, 5, 10);*/
					break;
				}
			}

			/*Vector3f tmp = new Vector3f();
			this.getBulletAppState().getPhysicsSpace().getGravity(tmp);
			this.getBulletAppState().getPhysicsSpace().setGravity(tmp.mult(-1));*/
		} else if (name.equals(QUIT)) {
			game.setNextModule(new StartModule(game));
		}

	}


	public void doExplosion(Vector3f pos, IEntity ignore) {//, float range, float power) {
		Settings.p("Showing explosion");
		float range = 5;
		float power = 20f;

		for(IEntity e : entities) {
			if (e != ignore) { // Stop infinite loop
				if (e instanceof IAffectedByPhysics) {
					IAffectedByPhysics pe = (IAffectedByPhysics)e;
					float dist = pe.getLocation().subtract(pos).length();
					if (dist <= range) {
						//Settings.p("Applying explosion force to " + e);
						Vector3f force = pe.getLocation().subtract(pos).normalizeLocal().multLocal(power);
						pe.applyForce(force);
						/*if (e instanceof IDamagable) {
						IDamagable id = (IDamagable)e;
						id.damaged(1 * (range-dist)); // todo
					}*/
					}
				}
			}
		}

		// show explosion effect
		SmallExplosion expl = new SmallExplosion(this, game.getRootNode(), game.getAssetManager(), game.getRenderManager());
		expl.setLocalTranslation(pos);
		this.addEntity(expl);
	}


	@Override
	public void destroy() {
		game.getInputManager().clearMappings();
		game.getInputManager().clearRawInputListeners();
	}


	public void addAI() {
		Point p = mapData.getRandomCollectablePos();
		RoamingAI ai = new RoamingAI(game, this, p.x, p.y);
		game.getRootNode().attachChild(ai.getMainNode());

	}


	@Override
	public void physicsTick(PhysicsSpace arg0, float arg1) {

	}


	@Override
	public void prePhysicsTick(PhysicsSpace arg0, float arg1) {
		while (this.toWarp.size() > 0) {
			PlayersAvatar a = this.toWarp.remove(0);
			a.playerControl.warp(a.warpPos);
		}
	}


	public void createDodgeballBall() {
		Point p = mapData.getRandomCollectablePos();
		DodgeballBall c = new DodgeballBall(game, this, null);
		c.getMainNode().setLocalTranslation(p.x,  10f,  p.y);
		c.floor_phy.setPhysicsLocation(new Vector3f(p.x,  10f,  p.y));
		Overwatch.instance.getRootNode().attachChild(c.getMainNode());

	}


	public void createCollectable() {
		Point p = mapData.getRandomCollectablePos();
		Collectable c = new Collectable(game, this, p.x, p.y);
		Overwatch.instance.getRootNode().attachChild(c.getMainNode());

	}

}
