package com.scs.multiplayerarena.map;

import java.awt.Point;

import com.jme3.math.Vector3f;
import com.scs.multiplayerarena.MultiplayerArenaMain;
import com.scs.multiplayerarena.Settings;
import com.scs.multiplayerarena.entities.Floor;
import com.scs.multiplayerarena.modules.GameModule;

public class FlatWorld implements IPertinentMapData {

	private static final int WIDTH = 7;
	public static final float FLOOR_THICKNESS = 3f;

	private MultiplayerArenaMain game;
	private GameModule module;

	public FlatWorld(MultiplayerArenaMain _game, GameModule _module) {
		game = _game;
		module = _module;

	}


	public void setup() {
		String roadtex = null;
		if (Settings.NEON) {
			roadtex = "Textures/tron_purple.jpg";
		} else {
			roadtex = "Textures/road2.png";
		}
		CreateFloor(0, -FLOOR_THICKNESS, 0, WIDTH, FLOOR_THICKNESS, WIDTH, roadtex, null);

	}

	
	private Floor CreateFloor(float x, float y, float z, float w, float h, float d, String tex, Vector3f scroll) {
		Floor floor = new Floor(game, module, x, y, z, w, h, d, tex, scroll);
		game.getRootNode().attachChild(floor.getMainNode());
		return floor;
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
		return new Point(3, 3);
	}


	@Override
	public Point getRandomCollectablePos() {
		return this.getPlayerStartPos(0);
	}


	@Override
	public float getRespawnHeight() {
		return 5f;
	}

}