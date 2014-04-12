package hadoop.similarPhoto;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import com.facehandsome.bean.Photo;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import config.GeneralConfig;

public final class MongoDBUtil {  
	  
    private static final String HOST = "127.0.0.1";  
  
    private static final String dbName = "photo";  
  
    private static Mongo mongo;  
  
    private static DB db;  
    
    private static DBCollection fingerprints;
    
    static {  
        try {  
            mongo = new Mongo(GeneralConfig.getHOST());  
            db = mongo.getDB(dbName);  
            fingerprints = db.getCollection("fingerprint");
            // db.authenticate(username, passwd)  
        } catch (UnknownHostException e) {  
            e.printStackTrace();  
        } catch (MongoException e) {  
            e.printStackTrace();  
        }  
    }  
  
    private MongoDBUtil() {  
    }  
      
    public static boolean collectionExists(String collectionName) {  
        return db.collectionExists(collectionName);  
    }  
      
    public static void dropCollection(String collectionName) {
    	getCollection(collectionName).drop();
	}
    
    public static void insertFingerprint(String photo, String fingerprint) {
		BasicDBObject photoFp = new BasicDBObject("photo", photo).append("fingerprint", fingerprint);
		fingerprints.insert(photoFp);
	}
    
    public static void insertFingerprintNHandsome(String photo, String fingerprint, boolean handsome) {
    	BasicDBObject photoFp = new BasicDBObject("photo", photo).append("fingerprint", fingerprint).append("handsome", handsome).append("isRandom", true);
    	fingerprints.insert(photoFp);
    }
    
    public static void insertSearchResult(ArrayList<Photo> result) {
    	DBCollection searchResult = db.getCollection("searchResult");
    	for (Photo photo : result) {
    		BasicDBObject res = new BasicDBObject("photo", photo.getPath()).append("date", GeneralConfig.dateFormat.format(new Date()));
    		searchResult.insert(res);
    	}
    }
    
    public static ArrayList<Photo> findTop3SimilarPhoto(String sourcePath, String collectionName) {
    	Pattern pattern = Pattern.compile("^.*" + sourcePath+ ".*$", Pattern.CASE_INSENSITIVE); 
    	DBCursor dbCursor = getCollection(collectionName).find(new BasicDBObject("_id",pattern)).limit(3).sort(new BasicDBObject("value",-1));
    	ArrayList<Photo> result = new ArrayList<Photo>();
    	while (dbCursor.hasNext()) {
    		Photo photo = new Photo();
    		String[] resSet = dbCursor.next().get("_id").toString().split("\\^\\&\\^");
    		photo.setPath(resSet[1]);
    		photo.setHandsome(resSet[2]);
    		result.add(photo);
    	}
    	return result;
    }
    
    /**
     * get the unrated photos
     * @return
     */
    public static String[] getUnratedPhotoPaths() {
    	String[] res = new String[fingerprints.find().count()];
    	int i = 0;
   
    	BasicDBObject query = new BasicDBObject();
    	query.put("isRandom", true);
   
    	DBCursor dbCursor = fingerprints.find(query);
   
    	while (dbCursor.hasNext()) {
    		res[i] = dbCursor.next().get("photo").toString();
    		i += 1;
    	}
   
    	return res;
    }
    
    /**
     * update the rate of a photo
     * @param imageName
     * @param isHandsome
     * @return
     */
    public static boolean updatePhotoRate(String imageName, boolean isHandsome) {
    	boolean res = false;
   
    	BasicDBObject query = new BasicDBObject();
    	query.put("photo", imageName);
    	DBObject one = fingerprints.find(query).next();
   
    	one.put("handsome", isHandsome);
    	one.put("isRandom", false);
    	fingerprints.update(query, one);
   
    	return res;
    }
    
    public static DBCollection getCollection(String collectionName) {  
        return db.getCollection(collectionName);  
    }
    
    public static void main(String[] args) {
//    	dropCollection("out");
//		findTop3SimilarPhoto("/home/hduser/workspace/images/source.jpg", "out");
    	Photo photo  = new Photo();
    	photo.setPath("abc");
    	ArrayList<Photo> photos = new ArrayList<>();
    	photos.add(photo);
    	insertSearchResult(photos);
	}
}
