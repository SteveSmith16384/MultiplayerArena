package com.scs.overwatch.map;

import com.jme3.scene.Node;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.entities.Fence;
import com.scs.overwatch.entities.MedievalStatue;
import com.scs.overwatch.entities.PhysicalEntity;
import com.scs.overwatch.entities.SimplePillar;
import com.scs.overwatch.entities.Skull;
import com.scs.overwatch.entities.Skull2;
import com.scs.overwatch.entities.StoneCoffin;
import com.scs.overwatch.entities.Tree;
import com.scs.overwatch.shapes.CreateShapes;

public class MapLoader {

	private Overwatch main;
	private Node rootNode;
	
	public MapLoader(Overwatch _main) {
		main = _main;
		this.rootNode = main.getRootNode();
		
		//loadMap();

	}


	public IMapInterface loadMap() {
		IMapInterface map;
		try {
			map = new CSVMap("bin/maps/map1.csv");//"./maps/map1.csv");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				map = new CSVMap("./map1.csv");
			} catch (Exception e2) {
				e2.printStackTrace();
				try {
					map = new CSVMap("/map1.csv");
				} catch (Exception e3) {
					e3.printStackTrace();
					throw new RuntimeException("Unable to load map");
				}
			}
		}

		// Floor first
		for (int z=0 ; z<map.getDepth() ; z+= Settings.FLOOR_SECTION_SIZE) {
			for (int x=0 ; x<map.getWidth() ; x+= Settings.FLOOR_SECTION_SIZE) {
				//p("Creating floor at " + x + "," + z);
				CreateShapes.CreateFloorTL(main.getAssetManager(), main.bulletAppState, this.rootNode, x, 0f, z, Settings.FLOOR_SECTION_SIZE, 0.1f, Settings.FLOOR_SECTION_SIZE, "Textures/DirtWithWeeds_S.jpg");
			}			
		}

		// Now add scenery
		for (int z=0 ; z<map.getDepth() ; z++) {
			for (int x=0 ; x<map.getWidth() ; x++) {
				int code = map.getCodeForSquare(x, z);
				switch (code) {
				case Settings.MAP_NOTHING:
					break;

				case 2: //Settings.MAP_PLAYER:
					//players[0].playerControl.warp(new Vector3f(x, 2f, z));
					// Do nothing
					break;

				case Settings.MAP_MONSTER_GHOST:
					break;

				case Settings.MAP_MONSTER_STATUE:
					break;

				case Settings.MAP_MONSTER_MOVING_STATUE:
					break;

				case Settings.MAP_TREE:
					PhysicalEntity tree = new Tree(main, x, z);
					this.rootNode.attachChild(tree.getMainNode());
					break;

				case Settings.MAP_FENCE_LR:
					PhysicalEntity fence1 = new Fence(main, x, z, 0);
					this.rootNode.attachChild(fence1.getMainNode());
					break;

				case Settings.MAP_FENCE_FB:
					PhysicalEntity fence2 = new Fence(main, x, z, 90);
					this.rootNode.attachChild(fence2.getMainNode());
					break;

				case Settings.MAP_MEDIEVAL_STATUE:
					PhysicalEntity ms = new MedievalStatue(main, x, z);
					this.rootNode.attachChild(ms.getMainNode());
					break;

				case Settings.MAP_SIMPLE_PILLAR:
					PhysicalEntity lg = new SimplePillar(main, x, z);
					this.rootNode.attachChild(lg.getMainNode());
					break;

				case Settings.MAP_STONE_COFFIN:
					PhysicalEntity gs = new StoneCoffin(main, x, z);
					this.rootNode.attachChild(gs.getMainNode());
					break;

				case Settings.MAP_SIMPLE_CROSS:
					//PhysicalEntity cross = new SimpleCross(main, x, z);
					//this.rootNode.attachChild(cross.getMainNode());
					break;

				case Settings.MAP_SKULL:
					PhysicalEntity skull = new Skull(main, x, z);
					this.rootNode.attachChild(skull.getMainNode());
					break;

				case Settings.MAP_SKULL2:
					PhysicalEntity skull2 = new Skull2(main, x, z);
					this.rootNode.attachChild(skull2.getMainNode());
					break;

				case Settings.MAP_CHARGING_GHOST:
					//AbstractEntity ch = new ChargingHarmlessMonster(this, x, z);
					//this.rootNode.attachChild(ch.getMainNode());
					//this.objects.add(ch);
					break;

				default:
					Settings.p("Ignoring map code " + code);
					//throw new RuntimeException("Unknown type:" + code);
				}
			}
		}
		
		return map;

	}


}
