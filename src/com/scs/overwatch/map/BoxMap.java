package com.scs.overwatch.map;

import java.awt.Point;

import ssmith.lang.NumberFunctions;

import com.scs.overwatch.Overwatch;
import com.scs.overwatch.entities.Crate;

public class BoxMap implements IMapInterface {

	int[][] data = new int[][]{
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},  
			{0, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 0},  
			{0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0},  
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},  
			{0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0},  
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},  
			{0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0},  
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 5, 0, 6, 0, 0},  
			{0, 5, 0, 0, 0, 0, 5, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0},  
			{1, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 1},  
			{0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0},  
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 0},  
			{0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0},  
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},  
			{0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 5, 0},  
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},  
			{0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0},  
			{0, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 0},  
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},  
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0}
	};

	private Overwatch game;

	public BoxMap(Overwatch _game) {
		game = _game;
	}


	@Override
	public int getWidth() {
		return data[0].length;
	}


	@Override
	public int getDepth() {
		return data.length;
	}


	@Override
	public int getCodeForSquare(int x,  int z) {
		return data[z][x];
	}


	@Override
	public Point getPlayerStartPos(int id) {
		int x = NumberFunctions.rnd(2, getWidth()-3);
		int z = NumberFunctions.rnd(2, getDepth()-3);
		return new Point(5,5); // todo - readd x, z);
	}


	@Override
	public void addMisc() {
		// Sprinkle lots of boxes
		for (int i=0 ; i<25 ; i++) {
			int x = NumberFunctions.rnd(2, getWidth()-3);
			int z = NumberFunctions.rnd(2, getDepth()-3);
			float w = NumberFunctions.rndFloat(.5f, 1.5f);
			float d = NumberFunctions.rndFloat(w, w+0.3f);
			//Crate crate = new Crate(game, x, z, w, w, d, NumberFunctions.rnd(0, 359));
			//game.getRootNode().attachChild(crate.getMainNode());
		}
	}


}
