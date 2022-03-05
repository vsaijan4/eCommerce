package com.ecommerce.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileUtil {
	private static final Properties prop = new Properties();

	public static StringBuffer getRootPath() {
		InputStream input = null;
		StringBuffer rootPath = new StringBuffer();
		try {
			input = new FileInputStream("config.properties");
			prop.load(input);
			rootPath.append(prop.getProperty("filePath"));
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return rootPath;
	}
	
	public static boolean removeDirectory(File dir) {
		File[] files = dir.listFiles();
		if (files != null && files.length > 0) {
			for (File eachFile : files) {
				removeDirectory(eachFile);
			}
		}
		return dir.delete();
	}
}
