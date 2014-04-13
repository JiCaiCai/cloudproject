package hadoop.similarPhoto;

import java.io.File;
import java.net.UnknownHostException;
import java.util.Random;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

import config.GeneralConfig;

public class FingerPrintGenerator {
	
	public static final String path = GeneralConfig.getSourceImagePath();
	public static void main(String[] args) {
		File file = new File(path);
		String subPaths[];
		subPaths = file.list();
		MongoDBUtil.dropCollection("fingerprint");
		MongoDBUtil.dropCollection("searchResult");
		MongoDBUtil.dropCollection("out");
		MongoDBUtil.dropCollection("statistic");
		for (String subPath : subPaths) {
			try {
				String imgPath = path+subPath;
				String fingerprint = SimilarImageSearch.produceFingerPrint(imgPath);
				boolean handsome = false;
				int i = new Random().nextInt(2);
				if (i==0) {
					handsome = false;
				} else {
					handsome = true;
				}
				System.out.println(subPath+" : "+fingerprint+" : "+handsome);
				if (!MongoDBUtil.isDuplicatedFingerprint(fingerprint)) {
					MongoDBUtil.insertFingerprintNHandsome(subPath, fingerprint, handsome);
				}
			} catch (RuntimeException e) {
				System.out.println(subPath +" Exception");
				e.printStackTrace();
			}
		}
	}
	
	public static void testMongoDB() {
		try {
			Mongo mongo = new Mongo();
			DB db = mongo.getDB("photo"); 
			DBCollection fingerPrints = db.getCollection("fingerPrint");
			fingerPrints.drop();
			BasicDBObject photoFp = new BasicDBObject("photoName", "MongoDB").append("fingerPrint", "12345");
			
			fingerPrints.insert(photoFp);
			DBCursor cur = fingerPrints.find();
			while (cur.hasNext()) {
				System.out.println(cur.next());
			}
			mongo.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		

	}
}
