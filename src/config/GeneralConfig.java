package config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class GeneralConfig {
	private static Properties prop = new Properties();
	
	private static final String dateFormatStr = "yyyy-MM-dd HH-mm-ss";
	
	public static final DateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
	
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
	 
	 public static String getHOST() {
		 return prop.getProperty("HOST");
	 }
}
