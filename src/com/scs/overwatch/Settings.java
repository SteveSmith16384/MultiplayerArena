package com.scs.overwatch;

import ssmith.lang.NumberFunctions;

public class Settings {

	public static final boolean NEON = true;
	public static final String VERSION = "0.01";
	public static final boolean DEBUG_TARGETTER = false;
	public static final boolean SHOW_LOGO = false;
	public static final boolean ALWAYS_SHOW_4_CAMS = false;
	public static final boolean RECORD_VID = false;
	
	public static final int NUM_AI = 2;

	// Our movement speed
	public static final float DEFAULT_MOVE_SPEED = 3f;
	public static final float DEFAULT_STRAFE_SPEED = 3f;

	public static final float CAM_DIST = 50f;
	public static final int FLOOR_SECTION_SIZE = 12;
	public static final boolean LIGHTING = true;
	public static final String NAME = "Overkill";
	
	// User Data
	public static final String ENTITY = "Entity";
	
	// Map codes
	public static final int MAP_NOTHING = 0;
	public static final int MAP_TREE = 1;
	//public static final int MAP_PLAYER = 2;
	public static final int MAP_FENCE_LR_HIGH = 4;
	public static final int MAP_FENCE_FB_HIGH = 5;
	public static final int MAP_SIMPLE_PILLAR = 7;
	public static final int MAP_FENCE_LR_NORMAL = 8;
	public static final int MAP_FENCE_FB_NORMAL = 9;

	
	public static void p(String s) {
		System.out.println(System.currentTimeMillis() + ": " + s);
	}

	
	public static String getCrateTex() {
		if (NEON) {
			return "Textures/10125-v4.jpg";//glowingbox.png";
		} else {
			int i = NumberFunctions.rnd(1, 10);
			return "Textures/boxes and crates/" + i + ".png";
		}
	}
	
	
	public static String getRoadwayTex() {
		if (NEON) {
			return "Textures/tron1.jpg";
		} else {
			int i = NumberFunctions.rnd(1, 10);
			return "Textures/floor0041.png";
		}
	}

}
