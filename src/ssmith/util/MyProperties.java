package ssmith.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.scs.overwatch.Settings;

public class MyProperties {

	private String filename;
	private Properties properties;

	public MyProperties(String _name) {
		filename = _name;
	}


	public void loadProperties() throws IOException {
		String filepath = filename;
		File propsFile = new File(filepath);
		if (propsFile.canRead()) {
			properties.load(new FileInputStream(new File(filepath)));
		} else {
			properties = new Properties();
		}
	}


	public void saveProperties() {
		String filepath = filename;
		File propsFile = new File(filepath);
		try {
			properties.store(new FileOutputStream(propsFile), Settings.NAME + " settings file");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public int getPropertyAsInt(String name, int def) {
		try {
			int value = Integer.parseInt(properties.getProperty(name));
			return value;
		} catch (Exception ex) {
			ex.printStackTrace();
			properties.put(name, ""+def);
			return def;
		}
	}


	public boolean getPropertyAsBoolean(String name, boolean def) {
		try {
			if (properties.containsKey(name)) {
				boolean value = Boolean.parseBoolean(properties.getProperty(name));
				properties.put(name, ""+value);
				return value;
			} else {
				properties.put(name, ""+def);
				return getPropertyAsBoolean(name, def);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			properties.put(name, ""+def);
			return def;
		}
	}


	public float getPropertyAsFloat(String name, float def) {
		try {
			float value = Float.parseFloat(properties.getProperty(name));
			return value;
		} catch (Exception ex) {
			ex.printStackTrace();
			properties.put(name, ""+def);
			return def;
		}
	}


}
