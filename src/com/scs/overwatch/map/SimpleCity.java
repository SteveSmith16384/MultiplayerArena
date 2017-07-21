package com.scs.overwatch.map;

import java.awt.Point;

import ssmith.lang.NumberFunctions;

import com.scs.overwatch.Overwatch;
import com.scs.overwatch.entities.SkyScraper;
import com.scs.overwatch.modules.GameModule;
import com.scs.overwatch.shapes.CreateShapes;

public class SimpleCity implements IPertinentMapData {

	private static final int SKYSCRAPER_WIDTH = 8;
	private static final int SECTORS = 3;
	
	private Overwatch game;
	private GameModule module;

	public SimpleCity(Overwatch _game, GameModule _module) {
		game = _game;
		module = _module;

		//createSector(0, 0);
		
		for (int y=0 ; y<SECTORS ; y++) {
			for (int x=0 ; x<SECTORS ; x++) {
				createSector(x*SKYSCRAPER_WIDTH+6, y*SKYSCRAPER_WIDTH+6);
			}			
		}
		
		// Sidewalk
		/*CreateFloor(0, 0, 12, 1); // top
		CreateFloor(11, 1, 1, 11); // right
		CreateFloor(1, 11, 11, 1); // bottom
		CreateFloor(0, 1, 1, 11); // Left

		// Add skyscraper
		SkyScraper skyscraper = new SkyScraper(game, module, 1, 1, 10, 20, 10, "Textures/skyscraper1.jpg");
		game.getRootNode().attachChild(skyscraper.getMainNode());*/

	}


	private void createSector(float x, float y) {
		// Sidewalk
		CreateFloor(x+2, y+2, SKYSCRAPER_WIDTH+2, 0.2f, 1, "Textures/sandstone.png"); // top
		CreateFloor(x+SKYSCRAPER_WIDTH+3, y+3, 1, 0.2f, SKYSCRAPER_WIDTH+1, "Textures/sandstone.png"); // right
		CreateFloor(x+3, y+SKYSCRAPER_WIDTH+3, SKYSCRAPER_WIDTH+1, 0.2f, 1, "Textures/sandstone.png"); // bottom
		CreateFloor(x+2, y+3, 1, 0.2f, SKYSCRAPER_WIDTH+1, "Textures/sandstone.png"); // Left

		// Road
		CreateFloor(x, y, SKYSCRAPER_WIDTH+6, 0.1f, 2, "Textures/road2.png"); // top
		CreateFloor(x+SKYSCRAPER_WIDTH+4, y+2, 2, 0.1f, SKYSCRAPER_WIDTH+3, "Textures/road2.png"); // right
		CreateFloor(x+2, y+SKYSCRAPER_WIDTH+4, SKYSCRAPER_WIDTH+3, 0.1f, 2, "Textures/road2.png"); // bottom
		CreateFloor(x, y+2, 2, 0.1f, SKYSCRAPER_WIDTH+4, "Textures/road2.png"); // Left

		// Add skyscraper
		if (NumberFunctions.rnd(1, 5) == 1) {
			CreateFloor(x+3, y+3, SKYSCRAPER_WIDTH, 0.1f, SKYSCRAPER_WIDTH, "Textures/grass.jpg");
		} else {
			float height = NumberFunctions.rndFloat(10, 20);
			SkyScraper skyscraper = new SkyScraper(game, module, x+3, y+3, SKYSCRAPER_WIDTH, height, SKYSCRAPER_WIDTH, "Textures/skyscraper1.jpg");
			game.getRootNode().attachChild(skyscraper.getMainNode());
		}

	}


	private void CreateFloor(float x, float z, float w, float h, float d, String tex) {
		CreateShapes.CreateFloorTL(game.getAssetManager(), module.bulletAppState, game.getRootNode(), x, 0f, z, w, h, d, tex);//, "Textures/road2.png");
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
		int x = NumberFunctions.rnd(0, SECTORS-1);
		int z = NumberFunctions.rnd(0, SECTORS-1);
		return new Point(x*(SKYSCRAPER_WIDTH+6), z*(SKYSCRAPER_WIDTH+6));
	}


}
