package com.scs.overwatch.map;

import java.awt.Point;

public interface IMapInterface {

	int getWidth();
	
	int getDepth();
	
	int getCodeForSquare(int x, int z);
	
	//int getNumCollectables();
	
	Point getPlayerStartPos(int id);
}
