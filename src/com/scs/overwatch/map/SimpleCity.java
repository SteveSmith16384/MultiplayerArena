package com.scs.overwatch.map;

import java.awt.Point;

import ssmith.lang.NumberFunctions;

import com.scs.overwatch.Overwatch;
import com.scs.overwatch.entities.SkyScraper;
import com.scs.overwatch.modules.GameModule;
import com.scs.overwatch.shapes.CreateShapes;

public class SimpleCity implements IPertinentMapData {

	private static final int SECTORS = 3;
	private Overwatch game;
	private GameModule module;

	public SimpleCity(Overwatch _game, GameModule _module) {
		game = _game;
		module = _module;

		//createSector(0, 0);
		
		for (int y=0 ; y<SECTORS ; y++) {
			for (int x=0 ; x<SECTORS ; x++) {
				createSector(x*16, y*16);
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
		CreateFloor(x+2, y+2, 12, 0.2f, 1, "Textures/sandstone.png"); // top
		CreateFloor(x+13, y+3, 1, 0.2f, 11, "Textures/sandstone.png"); // right
		CreateFloor(x+3, y+13, 11, 0.2f, 1, "Textures/sandstone.png"); // bottom
		CreateFloor(x+2, y+3, 1, 0.2f, 11, "Textures/sandstone.png"); // Left

		// Road
		CreateFloor(x, y, 16, 0.1f, 2, "Textures/road2.png"); // top
		CreateFloor(x+14, y+2, 2, 0.1f, 13, "Textures/road2.png"); // right
		CreateFloor(x+2, y+14, 13, 0.1f, 2, "Textures/road2.png"); // bottom
		CreateFloor(x, y+2, 2, 0.1f, 14, "Textures/road2.png"); // Left

		// Add skyscraper
		if (NumberFunctions.rnd(1, 5) == 1) {
			CreateFloor(x+3, y+3, 10, 0.1f, 10, "Textures/grass.jpg");
		} else {
			SkyScraper skyscraper = new SkyScraper(game, module, x+3, y+3, 10, 20, 10, "Textures/skyscraper1.jpg");
			game.getRootNode().attachChild(skyscraper.getMainNode());
		}

	}


	private void CreateFloor(float x, float z, float w, float h, float d, String tex) {
		CreateShapes.CreateFloorTL(game.getAssetManager(), module.bulletAppState, game.getRootNode(), x, 0f, z, w, h, d, tex);//, "Textures/road2.png");
	}

	@Override
	public int getWidth() {
		return SECTORS*16;
	}


	@Override
	public int getDepth() {
		return SECTORS*16;
	}


	@Override
	public Point getPlayerStartPos(int id) {
		int x = NumberFunctions.rnd(4, getWidth()-5);
		int z = NumberFunctions.rnd(4, getDepth()-5);
		return new Point(15, 15); // todo
	}


}
