package config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class GeneralConfig {
	private static Properties prop = new Properties();
	
	 static {
		 try {
			InputStream inputStream = new FileInputStream(new GeneralConfig().getClass().getResource("config.properties").getPath());
			prop.load(inputStream);
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	 }
	 
	 public static String getSourceImagePath() {
		 return prop.getProperty("sourceImagePath");
	 }
	 
	 public static String getDBFingerprint() {
		 return prop.getProperty("mongoDB.fingerprint");
	 }
	 
	 public static String getDBOutput() {
		 return prop.getProperty("mongoDB.output");
	 }
}
