package com.scs.overwatch.map;

import java.awt.Point;

import ssmith.lang.NumberFunctions;

import com.scs.overwatch.Overwatch;
import com.scs.overwatch.entities.Crate;
import com.scs.overwatch.entities.Plank;

public class ConfigMap implements IMapInterface {

	private Overwatch game;

	public ConfigMap(Overwatch _game) {
		game = _game;
	}


	@Override
	public int getWidth() {
		return 0;//todo data[0].length;
	}


	@Override
	public int getDepth() {
		return 0;//todo 
	}


	@Override
	public int getCodeForSquare(int x,  int z) {
		return 0;//todo 
	}


	@Override
	public Point getPlayerStartPos(int id) {
		int x = NumberFunctions.rnd(4, getWidth()-5);
		int z = NumberFunctions.rnd(4, getDepth()-5);
		return new Point(x, z);
	}


	@Override
	public void addMisc() {
		int numCrates = 35;
		try {
			numCrates = Integer.parseInt(game.properties.getProperty("numCrates"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		// Sprinkle lots of boxes
		for (int i=0 ; i<numCrates ; i++) {
			int x = NumberFunctions.rnd(4, getWidth()-5);
			int z = NumberFunctions.rnd(4, getDepth()-5);
			float w = NumberFunctions.rndFloat(.2f, 1f);
			float d = NumberFunctions.rndFloat(w, w+0.3f);
			Crate crate = new Crate(game, x, z, w, w, d, NumberFunctions.rnd(0, 359), game.crateTexKey);
			game.getRootNode().attachChild(crate.getMainNode());
		}

		// Sprinkle lots of planks
		int numPlanks = 10;
		try {
			numPlanks = Integer.parseInt(game.properties.getProperty("numPlanks"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		for (int i=0 ; i<numPlanks ; i++) {
			int x = NumberFunctions.rnd(4, getWidth()-5);
			int z = NumberFunctions.rnd(4, getDepth()-5);
			float w = NumberFunctions.rndFloat(.2f, .4f);
			float d = NumberFunctions.rndFloat(3f, 5f);
			Plank plank = new Plank(game, x, z, w, d, w, NumberFunctions.rnd(0, 359));
			game.getRootNode().attachChild(plank.getMainNode());
		}

	}


}
