package com.scs.multiplayerarena.effects;

import ssmith.util.RealtimeInterval;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.scs.multiplayerarena.components.IEntity;
import com.scs.multiplayerarena.components.IProcessable;
import com.scs.multiplayerarena.modules.GameModule;

public class Explosion extends Node implements IEntity, IProcessable {

	private static final boolean POINT_SPRITE = true;
	private static final Type EMITTER_TYPE = POINT_SPRITE ? Type.Point : Type.Triangle;
	private static final int COUNT_FACTOR = 1;
	private static final float COUNT_FACTOR_F = 1f;

	private ParticleEmitter flame, flash;
	private Node explosionEffect = new Node("explosionFX");
	private GameModule module;
	private RealtimeInterval expire = new RealtimeInterval(1000, false);

	public Explosion(GameModule _module, Node mainNode, AssetManager assetManager, RenderManager renderManager, float size) {
		super("SmallExplosionModel");

		module = _module;

		this.createFlame(assetManager);
		//re- add this.createFlash(assetManager);

		explosionEffect.scale(size);//.2f);
		explosionEffect.updateModelBound();

		this.attachChild(explosionEffect);
		//this.setModelBound(new BoundingSphere());
		//this.updateGeometricState();
		//this.updateModelBound();
		renderManager.preloadScene(this);

		this.setQueueBucket(Bucket.Transparent);

		mainNode.attachChild(this);
	}


	@Override
	public void process(float tpf) {
		flame.emitAllParticles();
		if (flash != null) {
			flash.emitAllParticles();
		}
		if (expire.hitInterval()) {
			this.stop();
			this.removeFromParent();
			this.module.removeEntity(this);
		}
	}


	public void stop() {
		flame.killAllParticles();
		if (flash != null) {
			flash.killAllParticles();
		}
	}


	private void createFlame(AssetManager assetManager){
		flame = new ParticleEmitter("Flame", EMITTER_TYPE, 32 * COUNT_FACTOR);
		flame.setSelectRandomImage(true);
		flame.setStartColor(new ColorRGBA(1f, 0.4f, 0.05f, (float) (1f / COUNT_FACTOR_F)));
		flame.setEndColor(new ColorRGBA(.4f, .22f, .12f, 0f));
		flame.setStartSize(1.3f);
		flame.setEndSize(2f);
		flame.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
		flame.setParticlesPerSec(0);
		flame.setGravity(0, -5, 0);
		flame.setLowLife(.4f);
		flame.setHighLife(.5f);
		flame.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 7, 0));
		flame.getParticleInfluencer().setVelocityVariation(1f);
		flame.setImagesX(2);
		flame.setImagesY(2);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat.setTexture("Texture", assetManager.loadTexture("Textures/flame.png"));
		//mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		mat.setBoolean("PointSprite", POINT_SPRITE);
		flame.setMaterial(mat);
		explosionEffect.attachChild(flame);
	}


	private void createFlash(AssetManager assetManager){
		flash = new ParticleEmitter("Flash", EMITTER_TYPE, 24 * COUNT_FACTOR);
		flash.setSelectRandomImage(true);
		flash.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, (float) (1f / COUNT_FACTOR_F)));
		flash.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, 0f));
		flash.setStartSize(.1f);
		flash.setEndSize(3.0f);
		flash.setShape(new EmitterSphereShape(Vector3f.ZERO, .05f));
		flash.setParticlesPerSec(0);
		flash.setGravity(0, 0, 0);
		flash.setLowLife(.2f);
		flash.setHighLife(.2f);
		flash.setInitialVelocity(new Vector3f(0, 5f, 0));
		flash.setVelocityVariation(1);
		flash.setImagesX(2);
		flash.setImagesY(2);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat.setTexture("Texture", assetManager.loadTexture("Textures/flash.png"));
		//mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		mat.setBoolean("PointSprite", POINT_SPRITE);
		flash.setMaterial(mat);
		explosionEffect.attachChild(flash);
	}


}
