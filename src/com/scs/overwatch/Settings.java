package com.scs.overwatch;

import ssmith.lang.NumberFunctions;

public class Settings {

	public static final boolean NEON = true;
	public static final String VERSION = "0.01";
	public static final boolean SHOW_LOGO = false;
	public static final boolean ALWAYS_SHOW_4_CAMS = false;
	public static final boolean RECORD_VID = false;
	public static final boolean USE_MODEL_FOR_PLAYERS = true;

	// DEBUG
	public static final boolean DEBUG_DEATH = true;
	public static final boolean DEBUG_GAMEPAD_TURNING = false;
	public static final boolean DEBUG_HUD = false;
	public static final boolean DEBUG_TARGETTER = false;

	// Game settings
	public enum GameMode {Skirmish, KingOfTheHill, Dodgeball, Bladerunner, CloneWars }
	
	public static GameMode GAME_MODE;
	public static boolean PVP = true;
	public static int NUM_AI = 0;
	public static int NUM_COLLECTABLES_PER_SECTOR = 0;
	public static int NUM_SECTORS = 3;
	
	// Our movement speed
	public static final float DEFAULT_MOVE_SPEED = 3f;
	public static final float DEFAULT_STRAFE_SPEED = 3f;
	public static final float MAX_TURN_SPEED = 1f;

	public static final float CAM_DIST = 50f;
	public static final int FLOOR_SECTION_SIZE = 12;
	public static final boolean LIGHTING = true;
	public static final String NAME = "Multiplayer Arena";
	public static final int CLONE_ID = 2;
	
	// User Data
	public static final String ENTITY = "Entity";
	
	// Map codes
	public static final int MAP_NOTHING = 0;
	public static final int MAP_TREE = 1;
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
			return "Textures/10125-v4.jpg"; //glowingbox.png";
		} else {
			int i = NumberFunctions.rnd(1, 10);
			return "Textures/boxes and crates/" + i + ".png";
		}
	}
	
	
	public static String getRoadwayTex() {
		if (NEON) {
			return "Textures/tron_blue.jpg";
		} else {
			int i = NumberFunctions.rnd(1, 10);
			return "Textures/floor0041.png";
		}
	}

}
