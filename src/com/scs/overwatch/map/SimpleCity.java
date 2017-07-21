package com.scs.overwatch.map;

import java.awt.Point;

import ssmith.lang.NumberFunctions;

import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.entities.SkyScraper;
import com.scs.overwatch.modules.GameModule;
import com.scs.overwatch.shapes.CreateShapes;

public class SimpleCity implements IPertinentMapData {

	private Overwatch game;
	private GameModule module;

	public SimpleCity(Overwatch _game, GameModule _module) {
		game = _game;
		module = _module;
		
		// Sidewalk
		//CreateShapes.CreateFloorTL(game.getAssetManager(), module.bulletAppState, game.getRootNode(), 0, 0f, 0, 12, 0.1f, 1f, "Textures/road2.png");
		CreateFloor(0, 0, 12, 1); // top
		CreateFloor(11, 1, 1, 11); // right
		CreateFloor(1, 11, 11, 1); // bottom
		CreateFloor(0, 1, 1, 11); // Left
	
		// Add skyscraper
		SkyScraper skyscraper = new SkyScraper(game, module, 1, 1, 10, 20, 10, "Textures/skyscraper1.jpg");
		game.getRootNode().attachChild(skyscraper.getMainNode());
		
		
	}

	
	private void CreateFloor(float x, float z, float w, float d) {
		CreateShapes.CreateFloorTL(game.getAssetManager(), module.bulletAppState, game.getRootNode(), x, 0f, z, w, 0.1f, d, "Textures/road2.png");
	}

	@Override
	public int getWidth() {
		return 20;//todo data[0].length;
	}


	@Override
	public int getDepth() {
		return 20;//todo data.length;
	}


	@Override
	public Point getPlayerStartPos(int id) {
		int x = NumberFunctions.rnd(4, getWidth()-5);
		int z = NumberFunctions.rnd(4, getDepth()-5);
		return new Point(11, 11);//, z);
	}


}
