package com.dairymoose.biomech;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ConfigPrefetch {

	public ConfigPrefetch() {
		
	}
	
	public String parseConfigForKey(String modName, String fileSuffix, String key, String defaultReturn) {
		if (modName == null || fileSuffix == null)
			return defaultReturn;
			
		if (!fileSuffix.endsWith(".toml")) {
			fileSuffix += ".toml";
		}
		String CONFIG_PREFIX = "config/";
		String finalPath = CONFIG_PREFIX + modName + "-" + fileSuffix;
		File f = new File(finalPath);
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			for (;;) {
				String line = br.readLine();
				if (line == null)
					break;
				String trimmed = line.trim();
				if (trimmed == null || trimmed.length() == 0)
					continue;
				
				List<String> path;
				
				if (key == null)
					continue;
				
				if (trimmed.startsWith(key)) {
					String[] keyValue = trimmed.split("=");
					if (keyValue.length == 2) {
						String value = keyValue[1];
						String trimmedValue = value.trim();
						return trimmedValue;
					}
				}
			}
			fr.close();
		} catch (IOException e) {
			BioMech.LOGGER.trace("File not found", e);
		}
		
		return defaultReturn;
	}
	
}
