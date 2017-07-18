package com.scs.overwatch.hud;

import com.jme3.scene.Node;
import com.jme3.ui.Picture;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.components.IEntity;
import com.scs.overwatch.components.IProcessable;

public class AbstractHUDImage extends Picture implements IEntity, IProcessable {

	private Overwatch game;
	private float timeLeft;

	public AbstractHUDImage(Overwatch _game, Node guiNode, String tex, float w, float h, float dur) {
		super("AbstractHUDImage");
		
		game = _game;
		this.timeLeft = dur;
		
		setImage(game.getAssetManager(), tex, true);
		setWidth(w);
		setHeight(h);
		this.setPosition(w/2, h/2);
		
		guiNode.attachChild(this);
		game.addEntity(this);
		
	}

	
	@Override
	public void process(float tpf) {
		this.timeLeft -= tpf;
		if (this.timeLeft <= 0) {
			this.removeFromParent();
			game.removeEntity(this);
		}
	}

}
