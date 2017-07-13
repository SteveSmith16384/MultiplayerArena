package com.scs.overwatch.map;

import com.jme3.scene.Node;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.entities.Fence;
import com.scs.overwatch.entities.MedievalStatue;
import com.scs.overwatch.entities.PhysicalEntity;
import com.scs.overwatch.entities.SimplePillar;
import com.scs.overwatch.entities.Skull;
import com.scs.overwatch.entities.StoneCoffin;
import com.scs.overwatch.entities.Tree;
import com.scs.overwatch.shapes.CreateShapes;

public class MapLoader {

	private Overwatch game;
	private Node rootNode;
	
	public MapLoader(Overwatch _game) {
		game = _game;
		this.rootNode = game.getRootNode();
		
	}


	public IMapInterface loadMap() {
		IMapInterface map = new BoxMap(game); //EmptyMap(game);//
		/*try {
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
		}*/

		// Floor first
		for (int z=0 ; z<map.getDepth() ; z+= Settings.FLOOR_SECTION_SIZE) {
			for (int x=0 ; x<map.getWidth() ; x+= Settings.FLOOR_SECTION_SIZE) {
				//p("Creating floor at " + x + "," + z);
				CreateShapes.CreateFloorTL(game.getAssetManager(), game.bulletAppState, this.rootNode, x, 0f, z, Settings.FLOOR_SECTION_SIZE, 0.1f, Settings.FLOOR_SECTION_SIZE, "Textures/sandstone.png");
			}			
		}

		// Now add scenery
		for (int z=0 ; z<map.getDepth() ; z++) {
			for (int x=0 ; x<map.getWidth() ; x++) {
				int code = map.getCodeForSquare(x, z);
				switch (code) {
				case Settings.MAP_NOTHING:
					break;

				case Settings.MAP_TREE:
					PhysicalEntity tree = new Tree(game, x, z);
					this.rootNode.attachChild(tree.getMainNode());
					break;

				case Settings.MAP_FENCE_LR:
					PhysicalEntity fence1 = new Fence(game, x, z, 0);
					this.rootNode.attachChild(fence1.getMainNode());
					break;

				case Settings.MAP_FENCE_FB:
					PhysicalEntity fence2 = new Fence(game, x, z, 90);
					this.rootNode.attachChild(fence2.getMainNode());
					break;

				case Settings.MAP_MEDIEVAL_STATUE:
					PhysicalEntity ms = new MedievalStatue(game, x, z);
					this.rootNode.attachChild(ms.getMainNode());
					break;

				case Settings.MAP_SIMPLE_PILLAR:
					PhysicalEntity lg = new SimplePillar(game, x, z);
					this.rootNode.attachChild(lg.getMainNode());
					break;

				case Settings.MAP_STONE_COFFIN:
					PhysicalEntity gs = new StoneCoffin(game, x, z);
					this.rootNode.attachChild(gs.getMainNode());
					break;

				case Settings.MAP_SKULL:
					PhysicalEntity skull = new Skull(game, x, z);
					this.rootNode.attachChild(skull.getMainNode());
					break;

				default:
					Settings.p("Ignoring map code " + code);
					//throw new RuntimeException("Unknown type:" + code);
				}
			}
		}
		
		map.addMisc();
		
		return map;

	}


}
