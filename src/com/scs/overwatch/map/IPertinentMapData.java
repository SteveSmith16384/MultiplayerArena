package com.scs.overwatch.map;

import java.awt.Point;

public interface IPertinentMapData {
	
	void setup();

	int getWidth();
	
	int getDepth();
	
	Point getPlayerStartPos(int id);
	
	Point getRandomCollectablePos();
	
}
