package com.scs.overwatch.map;

import java.awt.Point;
import java.util.Random;

public class RandomMap implements IMapInterface {
	
	private static final int WIDTH = 12;
	private static final int DEPTH = 12;
	
	private static final int PLAYER_X = WIDTH/2;
	private static final int PLAYER_Z = DEPTH/2;
	
	private static final Random rnd = new Random();

	@Override
	public int getWidth() {
		return WIDTH;
	}
	

	@Override
	public int getDepth() {
		return DEPTH;
	}

	
	@Override
	public int getCodeForSquare(int x, int z) {
		int num = rnd.nextInt(25)-23;
		if (num < 0) {
			num = 0;
		}
		return num;
	}


	@Override
	public Point getPlayerStartPos(int id) {
		return new Point(PLAYER_X, PLAYER_Z);
	}


	@Override
	public void addMisc() {
	}


}
