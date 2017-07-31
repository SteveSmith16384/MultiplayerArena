package com.scs.overwatch.map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import ssmith.lang.NumberFunctions;

import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.entities.Collectable;
import com.scs.overwatch.entities.Floor;
import com.scs.overwatch.modules.GameModule;

public class OverworldMap implements IPertinentMapData {

	private static final int WIDTH = 20;

	private Overwatch game;
	private GameModule module;
	private List<Point> startPositions = new ArrayList<>();

	public OverworldMap(Overwatch _game, GameModule _module) {
		game = _game;
		module = _module;

		// Floating walkway
		addFloatingWalkway();

		// Drop new collectable
		{
			Point p = getRandomCollectablePos();
			Collectable c = new Collectable(Overwatch.instance, module, p.x, p.y);
			Overwatch.instance.getRootNode().attachChild(c.getMainNode());
		}

	}


	private void CreateFloor(float x, float y, float z, float w, float h, float d, String tex) {
		//CreateShapes.CreateFloorTL(game.getAssetManager(), module.bulletAppState, game.getRootNode(), x, 0f, z, w, h, d, tex);//, "Textures/road2.png");
		Floor floor = new Floor(game, module, x, y, z, w, h, d, tex);
		game.getRootNode().attachChild(floor.getMainNode());
	}


	@Override
	public int getWidth() {
		return WIDTH;
	}


	@Override
	public int getDepth() {
		return WIDTH;
	}


	@Override
	public Point getPlayerStartPos(int id) {
		int i = NumberFunctions.rnd(0, this.startPositions.size());
		return this.startPositions.get(i);
	}


	@Override
	public Point getRandomCollectablePos() {
		return getPlayerStartPos(0);
	}


	private void addFloatingWalkway() {
		// Left-right
		for (int i=0 ; i < WIDTH ; i++) {
			float x = 0f;
			float y = NumberFunctions.rnd(4,  10);
			float z = NumberFunctions.rnd(0, WIDTH-1);
			float w = WIDTH;
			float h = 1f;
			float d = 1f;
			CreateFloor(x, y, z, w, h, d, Settings.getRoadwayTex());// "Textures/floor0041.png");

			startPositions.add(new Point((int)x, (int)z));
		}

		// front-back
		for (int i=0 ; i < WIDTH ; i++) {
			float x = NumberFunctions.rnd(0, WIDTH-1);
			float y = NumberFunctions.rnd(4,  10);
			float z = 0f;
			float w = 1f;//
			float h = 1f;
			float d = WIDTH;
			CreateFloor(x, y, z, w, h, d, Settings.getRoadwayTex());//, "Textures/floor0041.png");

			startPositions.add(new Point((int)x, (int)z));
		}

	}

}