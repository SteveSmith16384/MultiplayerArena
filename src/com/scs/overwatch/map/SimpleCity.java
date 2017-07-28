package com.scs.overwatch.map;

import java.awt.Point;
import java.util.List;

import ssmith.lang.NumberFunctions;

import com.jme3.bullet.collision.PhysicsRayTestResult;
import com.jme3.math.Vector3f;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.entities.AbstractPlatform;
import com.scs.overwatch.entities.Collectable;
import com.scs.overwatch.entities.Crate;
import com.scs.overwatch.entities.Floor;
import com.scs.overwatch.entities.Lift;
import com.scs.overwatch.entities.MovingPlatform;
import com.scs.overwatch.entities.RoamingAI;
import com.scs.overwatch.entities.SkyScraper;
import com.scs.overwatch.entities.StreetLight;
import com.scs.overwatch.modules.GameModule;

public class SimpleCity implements IPertinentMapData {

	private static final int SKYSCRAPER_WIDTH = 7;
	private static final int SECTORS = 3;

	private Overwatch game;
	private GameModule module;

	public SimpleCity(Overwatch _game, GameModule _module) {
		game = _game;
		module = _module;

		for (int y=0 ; y<SECTORS ; y++) {
			for (int x=0 ; x<SECTORS ; x++) {
				createSector(x*(SKYSCRAPER_WIDTH+6), y*(SKYSCRAPER_WIDTH+6));
			}			
		}

		// Add outer walls
		for (int x=0 ; x<SECTORS ; x++) {
			float height = NumberFunctions.rndFloat(10, 20);
			SkyScraper skyscraperBack = new SkyScraper(game, module, x*(SKYSCRAPER_WIDTH+6), -1, SKYSCRAPER_WIDTH+6, height, 1);
			game.getRootNode().attachChild(skyscraperBack.getMainNode());

			height = NumberFunctions.rndFloat(10, 20);
			SkyScraper skyscraperLeft = new SkyScraper(game, module, -1, x*(SKYSCRAPER_WIDTH+6), 1, height, SKYSCRAPER_WIDTH+6);
			game.getRootNode().attachChild(skyscraperLeft.getMainNode());

			height = NumberFunctions.rndFloat(10, 20);
			SkyScraper skyscraperFront = new SkyScraper(game, module, x*(SKYSCRAPER_WIDTH+6), SECTORS*(SKYSCRAPER_WIDTH+6), SKYSCRAPER_WIDTH+6, height, 1);
			game.getRootNode().attachChild(skyscraperFront.getMainNode());

			height = NumberFunctions.rndFloat(10, 20);
			SkyScraper skyscraperRight = new SkyScraper(game, module, SECTORS*(SKYSCRAPER_WIDTH+6), x*(SKYSCRAPER_WIDTH+6), 1, height, SKYSCRAPER_WIDTH+6);
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
		addFloatingWalkway();

		// Drop new collectable
		{
			Point p = getRandomCollectablePos();
			Collectable c = new Collectable(Overwatch.instance, module, p.x, p.y);
			Overwatch.instance.getRootNode().attachChild(c.getMainNode());
		}

		// Add AI roamers
		if (Settings.HAVE_AI) {
		for (int i=0 ; i<SECTORS ; i++) {
			Point p = getRandomCollectablePos();
			RoamingAI ai = new RoamingAI(game, module, p.x, p.y);
			game.getRootNode().attachChild(ai.getMainNode());
		}
		}

		// Sprinkle lots of boxes
		for (int i=0 ; i<SECTORS*5 ; i++) {
			int x = NumberFunctions.rnd(4, getWidth()-5);
			int z = NumberFunctions.rnd(4, getDepth()-5);
			float w = NumberFunctions.rndFloat(.2f, 1f);
			float d = NumberFunctions.rndFloat(w, w+0.3f);
			Crate crate = new Crate(game, module, x, 2f, z, w, w, d, NumberFunctions.rnd(0, 359));
			game.getRootNode().attachChild(crate.getMainNode());
		}

	}


	private void createSector(float x, float y) {
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
		CreateFloor(x, 0f, y, SKYSCRAPER_WIDTH+6, 0.1f, 2, "Textures/road2.png"); // top x
		CreateFloor(x+SKYSCRAPER_WIDTH+4, 0f, y+2, 2, 0.1f, SKYSCRAPER_WIDTH+4, "Textures/road2.png"); // right x
		CreateFloor(x+2, 0f, y+SKYSCRAPER_WIDTH+4, SKYSCRAPER_WIDTH+4, 0.1f, 2, "Textures/road2.png"); // bottom x
		CreateFloor(x, 0f, y+2, 2, 0.1f, SKYSCRAPER_WIDTH+4, "Textures/road2.png"); // Left

		// Sidewalk
		CreateFloor(x+2, 0f, y+2, SKYSCRAPER_WIDTH+2, 0.2f, 1, "Textures/floor015.png"); // top x
		new StreetLight(game, module, x+2.5f, y+2.5f);
		CreateFloor(x+SKYSCRAPER_WIDTH+3, 0f, y+3, 1, 0.2f, SKYSCRAPER_WIDTH+1, "Textures/floor015.png"); // right x
		CreateFloor(x+2, 0f, y+SKYSCRAPER_WIDTH+3, SKYSCRAPER_WIDTH+1, 0.2f, 1, "Textures/floor015.png"); // bottom x
		CreateFloor(x+2, 0f, y+3, 1, 0.2f, SKYSCRAPER_WIDTH, "Textures/floor015.png"); // Left x

		if (NumberFunctions.rnd(1, 5) == 1) {
			// Grass area
			CreateFloor(x+3, 0f, y+3, SKYSCRAPER_WIDTH, 0.1f, SKYSCRAPER_WIDTH, "Textures/grass.jpg");
			
			// 
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


	private void CreateFloor(float x, float y, float z, float w, float h, float d, String tex) {
		//CreateShapes.CreateFloorTL(game.getAssetManager(), module.bulletAppState, game.getRootNode(), x, 0f, z, w, h, d, tex);//, "Textures/road2.png");
		Floor floor = new Floor(game, module, x, y, z, w, h, d, tex);
		game.getRootNode().attachChild(floor.getMainNode());
	}


	@Override
	public int getWidth() {
		return SECTORS*(SKYSCRAPER_WIDTH+6);
	}


	@Override
	public int getDepth() {
		return SECTORS*(SKYSCRAPER_WIDTH+6);
	}


	@Override
	public Point getPlayerStartPos(int id) {
		int sx = NumberFunctions.rnd(0, SECTORS-1);
		int sz = NumberFunctions.rnd(0, SECTORS-1);
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


	private void addFloatingWalkway() {
		// Left-right
		for (int i=0 ; i < SECTORS ; i++) {
			float x = 0f;
			float y = NumberFunctions.rnd(4,  10);
			float z = NumberFunctions.rnd(0, SECTORS*(SKYSCRAPER_WIDTH+6));
			float w = SECTORS*(SKYSCRAPER_WIDTH+6);
			float h = 0.1f;
			float d = 1f;
			CreateFloor(x, y, z, w, h, d, Settings.getRoadwayTex());// "Textures/floor0041.png");
		}

		// front-back
		for (int i=0 ; i < SECTORS ; i++) {
			float x = NumberFunctions.rnd(0, SECTORS*(SKYSCRAPER_WIDTH+6));
			float y = NumberFunctions.rnd(4,  10);
			float z = 0f;
			float w = 1f;//
			float h = 0.1f;
			float d = SECTORS*(SKYSCRAPER_WIDTH+6);
			CreateFloor(x, y, z, w, h, d, Settings.getRoadwayTex());//, "Textures/floor0041.png");
		}

	}

}