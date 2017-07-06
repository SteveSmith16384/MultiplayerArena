package com.scs.overwatch.map;

public interface IMapInterface {

	int getWidth();
	
	int getDepth();
	
	int getCodeForSquare(int x, int z);
	
	//int getNumCollectables();
}
