package com.dataset.Merging.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

	public Properties loadProperties(String propertyFilePath) {
		Properties prop = new Properties();
		try(InputStream ins = new FileInputStream(propertyFilePath)){
			prop.load(ins);
			
		} catch (Exception exe) {
			System.out.println("error while loading spark configuration property file");
			exe.printStackTrace();
		}
		return prop;
	}
}
