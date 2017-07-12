package com.scs.overwatch;

public class Settings {
	
	public static final String VERSION = "0.02";

	public static final boolean RECORD_VID = false;
	public static final boolean SHOW_DEBUG = false;
	public static final boolean DEBUG_LIGHT = true;
	public static final boolean SHOW_LOGO = false;
	
	// Our movement speed
	public static final float moveSpeed = 3f;
	public static final float strafeSpeed = 3f;

	public static final float CAM_DIST = 30f;
	public static final int FLOOR_SECTION_SIZE=12;
	public static final boolean LIGHTING = true;
	public static final String NAME = "Crates";
	
	// User Data
	public static final String ENTITY = "Entity";
	
	// Map codes
	public static final int MAP_NOTHING = 0;
	public static final int MAP_TREE = 1;
	//public static final int MAP_PLAYER = 2;
	public static final int MAP_FENCE_LR = 4;
	public static final int MAP_FENCE_FB = 5;
	public static final int MAP_MEDIEVAL_STATUE = 6;
	public static final int MAP_SIMPLE_PILLAR = 7;
	public static final int MAP_SKULL = 10;
	public static final int MAP_STONE_COFFIN = 13;
	
	public static void p(String s) {
		System.out.println(System.currentTimeMillis() + ": " + s);
	}


}
