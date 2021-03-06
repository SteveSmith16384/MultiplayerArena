package com.scs.multiplayerarena.entities;

import java.io.IOException;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.scs.multiplayerarena.MultiplayerArenaMain;
import com.scs.multiplayerarena.components.IEntity;
import com.scs.multiplayerarena.modules.GameModule;

public class Entity implements IEntity, Savable {
	
	private static int nextId = 0;
	
	public final int id;
	protected MultiplayerArenaMain game;
	protected GameModule module;
	public String name;

	public Entity(MultiplayerArenaMain _game, GameModule _module, String _name) {
		id = nextId++;
		game = _game;
		module = _module;
		name = _name;
	}


	@Override
	public String toString() {
		return "E_" + name + "_" + id;
	}


	public void remove() {
		module.removeEntity(this);
	}
	
	
	@Override
	public void write(JmeExporter ex) throws IOException {
		
	}


	@Override
	public void read(JmeImporter im) throws IOException {
		
	}

}
