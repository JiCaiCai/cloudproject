package hadoop.similarPhoto;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.bson.BSONObject;

import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.util.MongoConfigUtil;

public class HandsomeSearch {
	private static String sourcePhoto = null;
	private static String sourceFingerprint = null;
	
	public static class PhotoMapper extends Mapper<Object, BSONObject, Text, DoubleWritable> {  
		  
		  
	       private final Text photoText = new Text();  
	       private final static DoubleWritable similarity = new DoubleWritable( 0.0 );  
	  
	  
	       public void map( Object key , BSONObject value , Context context ) throws IOException, InterruptedException{  
	  
	           String photo = value.get( "photo" ).toString();
	           String fingerprint = value.get( "fingerprint" ).toString();
	           
	           if (!value.containsField("handsome")) {
	        	   return;
	           }
	           
	           System.out.println( "key: " + key );  
	           System.out.println( "value: " + value ); 
	           String handsome = value.get("handsome").toString();
	           
	           int hammingDistance = SimilarImageSearch.hammingDistance(sourceFingerprint, fingerprint);
	           photoText.set(photo + "^&^" + sourcePhoto + "$" + handsome);
	           similarity.set((16.0 - hammingDistance) / 16.0);
	           context.write(photoText, similarity);
	       }  
	   }  
	  
	  
	   public static class SimilarityReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {  
	  
	  
	       private final DoubleWritable result = new DoubleWritable();  
	  
	  
	       public void reduce( Text key , Iterable<DoubleWritable> values , Context context ) throws IOException, InterruptedException{  
	  
	           double sum = 0;  
	           for ( final DoubleWritable val : values ) {  
	               sum += val.get();  
	           }  
	           result.set( sum );
	           System.out.println("key: "+key);
	           context.write( key, result );  
	       }  
	   } 
	
	public static void main(String[] args) throws Exception {
		sourcePhoto = "/home/hduser/workspace/images/source.jpg";
		sourceFingerprint = SimilarImageSearch.produceFingerPrint(sourcePhoto);
		
		final Configuration conf = new Configuration();
		MongoConfigUtil.setInputURI(conf, "mongodb://localhost/photo.fingerprint");
		MongoConfigUtil.setOutputURI(conf, "mongodb://localhost/photo.handsomeOut");
		System.out.println("Conf: " + conf);

		final Job job = new Job(conf, "similar photo");

		job.setJarByClass(MdbSimilarPhoto.class);

		// Mapper,Reduce and Combiner type definition
		 job.setMapperClass( PhotoMapper.class );
		
		
		 job.setCombinerClass( SimilarityReducer.class );
		 job.setReducerClass( SimilarityReducer.class );

		// output key/value type definition
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DoubleWritable.class);

		// InputFormat and OutputFormat type definition
		job.setInputFormatClass(MongoInputFormat.class);
		job.setOutputFormatClass(MongoOutputFormat.class);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
