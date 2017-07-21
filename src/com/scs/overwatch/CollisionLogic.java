package com.scs.overwatch;

import com.scs.overwatch.entities.KillerCrateBullet;
import com.scs.overwatch.entities.Collectable;
import com.scs.overwatch.entities.PhysicalEntity;
import com.scs.overwatch.entities.PlayersAvatar;
import com.scs.overwatch.modules.GameModule;

public class CollisionLogic {

	public static void collision(GameModule game, PhysicalEntity a, PhysicalEntity b) {
		//Settings.p(a + " has collided with " + b);

		if (a instanceof PlayersAvatar && b instanceof KillerCrateBullet) {
			Player_Bullet(game, (PlayersAvatar)a, (KillerCrateBullet)b);
		}
		if (a instanceof KillerCrateBullet && b instanceof PlayersAvatar) {
			Player_Bullet(game, (PlayersAvatar)b, (KillerCrateBullet)a);
		}

		if (a instanceof PlayersAvatar && b instanceof Collectable) {
			Player_Collectable(game, (PlayersAvatar)a, (Collectable)b);
		}
		if (a instanceof Collectable && b instanceof PlayersAvatar) {
			Player_Collectable(game, (PlayersAvatar)b, (Collectable)a);
		}
	}


	private static void Player_Collectable(GameModule game, PlayersAvatar player, Collectable col) {
	}


	private static void Player_Bullet(GameModule game, PlayersAvatar playerHit, KillerCrateBullet col) {
		if (col.shooter != playerHit) {
			playerHit.hitByBullet();
			col.shooter.hasSuccessfullyHit(playerHit);
		}
	}


}
