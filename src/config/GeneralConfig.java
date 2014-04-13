package config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class GeneralConfig {
	private static Properties prop = new Properties();
	
	private static final String dateFormatStr = "yyyy-MM-dd HH-mm-ss";
	
	public static final DateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
	
	public static final Map<String, String> colorMap = new HashMap<String, String>();
	
	 static {
		 try {
			InputStream inputStream = new FileInputStream(new GeneralConfig().getClass().getResource("config.properties").getPath());
			prop.load(inputStream);
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	 }
	 
	 static {
		 //true is orange and false is blue
		 colorMap.put("true", "#F38630");
		 colorMap.put("false", "#69D2E7");
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
	 
	 public static String getDBSearchResult() {
		 return prop.getProperty("mongoDB.searchResult");
	 }
	 
	 public static String getDBStatistic() {
		 return prop.getProperty("mongoDB.statistic");
	 }
	 
	 public static String getDBName() {
		 return prop.getProperty("dbName");
	 }
	 
	 public static String getCollectionSearchResult() {
		 return prop.getProperty("collection.searchResult");
	 }
	 
	 public static String getCollectionFingerPrint() {
		 return prop.getProperty("collection.fingerprint");
	 }
}
