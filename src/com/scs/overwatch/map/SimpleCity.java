package com.scs.overwatch.map;

import java.awt.Point;

import ssmith.lang.NumberFunctions;

import com.jme3.math.Vector3f;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.Settings.GameMode;
import com.scs.overwatch.entities.AbstractPlatform;
import com.scs.overwatch.entities.Base;
import com.scs.overwatch.entities.Crate;
import com.scs.overwatch.entities.Floor;
import com.scs.overwatch.entities.Lift;
import com.scs.overwatch.entities.PlayersClone;
import com.scs.overwatch.entities.SkyScraper;
import com.scs.overwatch.modules.GameModule;

public class SimpleCity implements IPertinentMapData {

	private static final int SKYSCRAPER_WIDTH = 7;

	private static final float WALL_THICKNESS = 3;
	public static final float FLOOR_THICKNESS = 3f;
	private static final float PATH_THICKNESS = .1f;

	private Overwatch game;
	private GameModule module;
	private int numSectors;

	public SimpleCity(Overwatch _game, GameModule _module) {
		game = _game;
		module = _module;

		numSectors = Settings.NUM_SECTORS;
	}


	public void setup() {
		for (int y=0 ; y<numSectors ; y++) {
			for (int x=0 ; x<numSectors ; x++) {
				boolean createBase = Settings.GAME_MODE == GameMode.KingOfTheHill && x == 1 && y == 1;
				createSector(createBase, x*(SKYSCRAPER_WIDTH+6), y*(SKYSCRAPER_WIDTH+6));
			}			
		}

		// Add outer walls
		for (int j=0 ; j<numSectors ; j++) {
			// Back
			float height = NumberFunctions.rndFloat(10, 20);
			SkyScraper skyscraperBack = new SkyScraper(game, module, j*(SKYSCRAPER_WIDTH+6), -WALL_THICKNESS, SKYSCRAPER_WIDTH+6, height, WALL_THICKNESS);
			game.getRootNode().attachChild(skyscraperBack.getMainNode());

			// Left
			height = NumberFunctions.rndFloat(10, 20);
			SkyScraper skyscraperLeft = new SkyScraper(game, module, -WALL_THICKNESS, j*(SKYSCRAPER_WIDTH+6), WALL_THICKNESS, height, SKYSCRAPER_WIDTH+6);
			game.getRootNode().attachChild(skyscraperLeft.getMainNode());
			
			// Front
			height = NumberFunctions.rndFloat(10, 20);
			SkyScraper skyscraperFront = new SkyScraper(game, module, j*(SKYSCRAPER_WIDTH+6), (numSectors*(SKYSCRAPER_WIDTH+6)), SKYSCRAPER_WIDTH+6, height, WALL_THICKNESS);
			game.getRootNode().attachChild(skyscraperFront.getMainNode());

			// Right
			height = NumberFunctions.rndFloat(10, 20);
			SkyScraper skyscraperRight = new SkyScraper(game, module, numSectors*(SKYSCRAPER_WIDTH+6), j*(SKYSCRAPER_WIDTH+6), WALL_THICKNESS, height, SKYSCRAPER_WIDTH+6);
			game.getRootNode().attachChild(skyscraperRight.getMainNode());
		}

		// Add moving platforms - front-back
		/*for (int i=0 ; i<SECTORS*2 ; i++) {
			float x = NumberFunctions.rndFloat(1, (SECTORS-1)*(SKYSCRAPER_WIDTH+6));
			float y = 0.5f;//NumberFunctions.rndFloat(2, 10);
			Vector3f dir = new Vector3f(0, 0, 1f);
			MovingPlatform mp = new MovingPlatform(game, module, x, y, 2, dir);
			game.getRootNode().attachChild(mp.getMainNode());
		}


		// Add moving platforms - left-right
		for (int i=0 ; i<SECTORS*2 ; i++) {
			float z = NumberFunctions.rndFloat(1, (SECTORS-1)*(SKYSCRAPER_WIDTH+6));
			float y = .5f;//NumberFunctions.rndFloat(2, 10);
			Vector3f dir = new Vector3f(1f, 0f, 0f);
			MovingPlatform mp = new MovingPlatform(game, module, 0, y, z, dir);
			game.getRootNode().attachChild(mp.getMainNode());
		}*/


		// Floating walkway
		if (Settings.GAME_MODE != GameMode.Dodgeball) {
			addFloatingWalkways();
		}

		// Drop new collectable
		for (int i=0 ; i<Settings.NUM_COLLECTABLES_PER_SECTOR * numSectors ; i++) {
			module.createCollectable();
		}

		// Add AI roamers
		for (int i=0 ; i<Settings.NUM_AI ; i++) {
			module.addAI();
		}

		if (Settings.GAME_MODE != GameMode.CloneWars) {
			// Sprinkle lots of boxes
			for (int i=0 ; i<numSectors*6 ; i++) {
				int x = NumberFunctions.rnd(4, getWidth()-5);
				int z = NumberFunctions.rnd(4, getDepth()-5);
				float w = NumberFunctions.rndFloat(.2f, 2f);
				float d = NumberFunctions.rndFloat(w, w+0.3f);
				Crate crate = new Crate(game, module, x, 20f, z, w, w, d, NumberFunctions.rnd(0, 359));
				game.getRootNode().attachChild(crate.getMainNode());
			}
		} else {
			// Sprinkle lots of identicals
			for (int i=0 ; i<numSectors*8 ; i++) {
				int x = NumberFunctions.rnd(4, getWidth()-5);
				int z = NumberFunctions.rnd(4, getDepth()-5);
				PlayersClone box = new PlayersClone(game, module, x, 20f, z, NumberFunctions.rnd(0, 359));
				game.getRootNode().attachChild(box.getMainNode());
			}
		}

		if (Settings.GAME_MODE == GameMode.Dodgeball) {
			for (int i=0 ; i<game.getInputManager().getJoysticks().length+1 ; i++) { // one for each player
				// Add the ball
				module.createDodgeballBall();
			}
		}
	}


	private void createSector(boolean createBase, float x, float y) {
		/* 123456789012
		 * XRRRRRRRRRRR
		 * RRRRRRRRRRRR
		 * XRSSSSSSSSXR
		 * RRSxxxxxxSRR
		 * RRSxxxxxxSRR
		 * RRSxxxxxxSRR
		 * RRSxxxxxxSRR
		 * RRSxxxxxxSRR
		 * RRSxxxxxxSRR
		 * RRSSSSSSSSRR
		 * RRXRRRRRRRRR
		 * RRRRRRRRRRRR
		 * 
		 */

		// Road
		String roadtex = null;
		if (Settings.NEON) {
			roadtex = "Textures/tron2.jpg";
		} else {
			roadtex = "Textures/road2.png";
		}
		CreateFloor(x, -FLOOR_THICKNESS, y, SKYSCRAPER_WIDTH+6, FLOOR_THICKNESS, 2, roadtex, null); // top x
		CreateFloor(x+SKYSCRAPER_WIDTH+4, -FLOOR_THICKNESS, y+2, 2, FLOOR_THICKNESS, SKYSCRAPER_WIDTH+4, roadtex, null); // right x
		CreateFloor(x+2, -FLOOR_THICKNESS, y+SKYSCRAPER_WIDTH+4, SKYSCRAPER_WIDTH+2, FLOOR_THICKNESS, 2, roadtex, null); // bottom x
		CreateFloor(x, -FLOOR_THICKNESS, y+2, 2, FLOOR_THICKNESS, SKYSCRAPER_WIDTH+4, roadtex, null); // Left

		// Sidewalk
		String sidewalktex = null;
		if (Settings.NEON) {
			sidewalktex = "Textures/tron3.jpg";//bluecross.png";//tron1.jpg";
		} else {
			sidewalktex = "Textures/floor015.png";
		}
		CreateFloor(x+2, -FLOOR_THICKNESS+PATH_THICKNESS, y+2, SKYSCRAPER_WIDTH+2, FLOOR_THICKNESS, 1, sidewalktex, null); // top x
		CreateFloor(x+SKYSCRAPER_WIDTH+3, -FLOOR_THICKNESS+PATH_THICKNESS, y+3, 1, FLOOR_THICKNESS, SKYSCRAPER_WIDTH+1, sidewalktex, null); // right x
		CreateFloor(x+2, -FLOOR_THICKNESS+PATH_THICKNESS, y+SKYSCRAPER_WIDTH+3, SKYSCRAPER_WIDTH+1, FLOOR_THICKNESS, 1, sidewalktex, null); // bottom x
		CreateFloor(x+2, -FLOOR_THICKNESS+PATH_THICKNESS, y+3, 1, FLOOR_THICKNESS, SKYSCRAPER_WIDTH, sidewalktex, null); // Left x

		if (createBase) {//x == 1 && y == 1 && Settings.HAVE_BASE) {
			Base base = new Base(game, module, x+3, 0f, y+3, SKYSCRAPER_WIDTH, 0.1f, SKYSCRAPER_WIDTH, "Textures/sun.jpg", null);
			game.getRootNode().attachChild(base.getMainNode());
		} else {
			int i = NumberFunctions.rnd(1, 4);
			if (i == 1) {
				// Grass area
				String grasstex = null;
				if (Settings.NEON) {
					grasstex = "Textures/tron4.jpg";
				} else {
					grasstex = "Textures/grass.png";
				}
				CreateFloor(x+3, 0f, y+3, SKYSCRAPER_WIDTH, 0.1f, SKYSCRAPER_WIDTH, grasstex, null);
			} else if (i == 2) {
				pyramid(x+2, y+2, sidewalktex);
			} else {
				// Add skyscraper
				float height = NumberFunctions.rndFloat(4, 10);
				SkyScraper skyscraper = new SkyScraper(game, module, x+3, y+3, SKYSCRAPER_WIDTH, height, SKYSCRAPER_WIDTH);
				game.getRootNode().attachChild(skyscraper.getMainNode());

				// Add lift
				Lift lift1 = new Lift(game, module, x+4, y+2, 0.1f+AbstractPlatform.HEIGHT, height);
				game.getRootNode().attachChild(lift1.getMainNode());

				Lift lift2 = new Lift(game, module, x+5, y+3+SKYSCRAPER_WIDTH, 0.1f+AbstractPlatform.HEIGHT, height);
				game.getRootNode().attachChild(lift2.getMainNode());
			}
		}
	}


	private void pyramid(float sx, float sz, String tex) {
		for (int i=0 ; i<4 ; i++) {
			float size = 8-(i*2);
			Floor floor = new Floor(game, module, sx+i, i, sz+i, size, 1, size, tex, null);
			game.getRootNode().attachChild(floor.getMainNode());
		}

	}


	private Floor CreateFloor(float x, float y, float z, float w, float h, float d, String tex, Vector3f scroll) {
		Floor floor = new Floor(game, module, x, y, z, w, h, d, tex, scroll);
		game.getRootNode().attachChild(floor.getMainNode());
		return floor;
	}


	@Override
	public int getWidth() {
		return numSectors*(SKYSCRAPER_WIDTH+6);
	}


	@Override
	public int getDepth() {
		return numSectors*(SKYSCRAPER_WIDTH+6);
	}


	@Override
	public Point getPlayerStartPos(int id) {
		int sx = NumberFunctions.rnd(0, numSectors-1);
		int sz = NumberFunctions.rnd(0, numSectors-1);
		int x = sx*(SKYSCRAPER_WIDTH+6);
		int z = sz*(SKYSCRAPER_WIDTH+6); 
		return new Point(x+1, z+1);
	}


	@Override
	public Point getRandomCollectablePos() {
		/*int x = NumberFunctions.rnd(0, SECTORS-1);
		int z = NumberFunctions.rnd(0, SECTORS-1);
		return new Point(x*(SKYSCRAPER_WIDTH+6), z*(SKYSCRAPER_WIDTH+6));*/
		return this.getPlayerStartPos(0);
	}


	private void addFloatingWalkways() {
		Vector3f scrolllr = new Vector3f(1, 0, 0);
		Vector3f scrollfb = new Vector3f(0, 0, 1);

		// Left-right
		for (int i=0 ; i < numSectors ; i++) {
			float x = 0f;
			float y = NumberFunctions.rnd(4,  10);
			float z = NumberFunctions.rnd(0, numSectors*(SKYSCRAPER_WIDTH+6));
			float w = numSectors*(SKYSCRAPER_WIDTH+6);
			float h = 0.1f;
			float d = 1f;
			CreateFloor(x, y, z, w, h, d, Settings.getRoadwayTex(), scrolllr);// "Textures/floor0041.png");
		}

		// front-back
		for (int i=0 ; i < numSectors ; i++) {
			float x = NumberFunctions.rnd(0, numSectors*(SKYSCRAPER_WIDTH+6));
			float y = NumberFunctions.rnd(4,  10);
			float z = 0f;
			float w = 1f;//
			float h = 0.1f;
			float d = numSectors*(SKYSCRAPER_WIDTH+6);
			CreateFloor(x, y, z, w, h, d, Settings.getRoadwayTex(), scrollfb);//, "Textures/floor0041.png");
		}

	}


	@Override
	public float getRespawnHeight() {
		return 20f;
	}

}