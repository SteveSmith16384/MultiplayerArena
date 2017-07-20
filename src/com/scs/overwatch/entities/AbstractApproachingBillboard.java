package com.scs.overwatch.entities;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.components.IEntity;
import com.scs.overwatch.components.IProcessable;
import com.scs.overwatch.modules.GameModule;

public class AbstractApproachingBillboard implements IEntity, IProcessable {

	private float dist = 10f;
	protected Overwatch game;
	protected GameModule module;
	private Camera cam;
	protected Node node;

	public AbstractApproachingBillboard(Overwatch _game, GameModule _module, String tex, float w, float h, Camera _cam) {
		super();

		game = _game;
		module = _module;
		
		cam = _cam;

		Material mat = new Material(game.getAssetManager(),"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
		mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

		Texture t = game.getAssetManager().loadTexture(tex);
		t.setWrap(WrapMode.Repeat);
		mat.setTexture("DiffuseMap", t);

		Quad quad = new Quad(w, h);
		Geometry geom = new Geometry("Billboard", quad);
		geom.setMaterial(mat);
		geom.setQueueBucket(Bucket.Transparent);

		node = new Node("SkyNode");
		node.attachChild(geom);
		geom.setLocalTranslation(-w/2, -h/2, 0); // todo - remove?

		game.getRootNode().attachChild(node);

	}


	@Override
	public void process(float tpf) {
		// Stay in front of player
		Vector3f pos = cam.getLocation().add(cam.getDirection().mult(dist));
		node.setLocalTranslation(pos);

		this.node.lookAt(cam.getLocation(), Vector3f.UNIT_Y);

		this.dist -= tpf*5;
		if (dist <= 0)		 {
			node.removeFromParent();
			module.removeEntity(this);
		}

	}

}
