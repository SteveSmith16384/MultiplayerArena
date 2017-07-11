package com.scs.overwatch.map;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.scs.overwatch.Settings;

public class CSVMap implements IMapInterface {

	private ArrayList<String> al = new ArrayList<>();

	public CSVMap(String filename) throws IOException {
		Settings.p("Trying to load " + filename + "...");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));
			//br = new BufferedReader(new FileReader("bin/maps/map1.csv"));
		} catch (FileNotFoundException ex) {
			br =  new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(filename)));
		}
		try {
			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				al.add(line.trim());
			}
		} finally {
			br.close();
		}
	}


	@Override
	public int getWidth() {
		return al.get(0).split("\t").length;
	}


	@Override
	public int getDepth() {
		return al.size();
	}


	@Override
	public int getCodeForSquare(int x, int z) {
		String line = al.get(z);
		String parts[] = line.split("\t");
		return Integer.parseInt(parts[x]);
	}


	@Override
	public Point getPlayerStartPos(int id) {
		return new Point(2, 2); // todo
	}


	@Override
	public void addMisc() {
		// TODO Auto-generated method stub
		
	}


}
