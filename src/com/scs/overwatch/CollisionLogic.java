package com.scs.overwatch;

import com.scs.overwatch.entities.PhysicalEntity;
import com.scs.overwatch.entities.Collectable;
import com.scs.overwatch.entities.PlayersAvatar;

public class CollisionLogic {

	public static void collision(Overwatch game, PhysicalEntity a, PhysicalEntity b) {
		if (a instanceof PlayersAvatar && b instanceof Collectable) {
			Player_Collectable(game, (PlayersAvatar)a, (Collectable)b);
		}
		if (a instanceof Collectable && b instanceof PlayersAvatar) {
			Player_Collectable(game, (PlayersAvatar)b, (Collectable)a);
		}
	}
	
	
	private static void Player_Collectable(Overwatch game, PlayersAvatar player, Collectable col) {
	}
	

}
