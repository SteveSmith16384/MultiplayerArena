package com.scs.overwatch.entities;

import java.io.IOException;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;

public class Entity implements Savable {
	
	private static int nextId = 0;
	
	public final int id;

	public Entity() {
		id = nextId++;
	}


	@Override
	public void write(JmeExporter ex) throws IOException {
		
	}


	@Override
	public void read(JmeImporter im) throws IOException {
		
	}

}
