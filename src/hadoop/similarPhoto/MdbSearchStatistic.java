package hadoop.similarPhoto;


import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.bson.BSONObject;

import com.facehandsome.bean.PieGraph;
import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.util.MongoConfigUtil;

import config.GeneralConfig;

public class MdbSearchStatistic {
	
	public static class SearchResultMapper extends Mapper<Object, BSONObject, Text, IntWritable> {  
		  
		  
	       private final Text photoText = new Text();  
	       private final static IntWritable one = new IntWritable(1);
	  
	       public void map( Object key , BSONObject value , Context context ) throws IOException, InterruptedException{  
	           System.out.println( "key: " + key );  
	           System.out.println( "value: " + value );  
	  
	           String photo = value.get( "photo" ).toString();
	           photoText.set(photo);
	           context.write(photoText, one);
	       }  
	   }  
	  
	  
	   public static class SearchResultReducer extends Reducer<Text, IntWritable, Text, IntWritable> {  
	  
	       public void reduce( Text key , Iterable<IntWritable> values , Context context ) throws IOException, InterruptedException{  
	    	   int sum = 0; 
	    	   
	           for ( final IntWritable val : values ) {  
	               sum += val.get();  
	           }  
	           System.out.println(key +" : "+ sum);
	           context.write( key, new IntWritable(sum));  
	       }  
	   }
	   
	public static void searchResultStatistic() {
		try {
			if (!MongoDBUtil.collectionExists(GeneralConfig.getCollectionSearchResult())) {
				return;
			}
			
			final Configuration conf = new Configuration();
			MongoConfigUtil.setInputURI(conf, GeneralConfig.getDBSearchResult());
			MongoConfigUtil.setOutputURI(conf, GeneralConfig.getDBStatistic());
			System.out.println("Conf: " + conf);

			final Job job = new Job(conf, "search_statistic");

			job.setJarByClass(MdbSimilarPhoto.class);

			// Mapper,Reduce and Combiner type definition
			job.setMapperClass(SearchResultMapper.class);

			job.setCombinerClass(SearchResultReducer.class);
			job.setReducerClass(SearchResultReducer.class);

			// output key/value type definition
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);

			// InputFormat and OutputFormat type definition
			job.setInputFormatClass(MongoInputFormat.class);
			job.setOutputFormatClass(MongoOutputFormat.class);

			if (job.waitForCompletion(true)) {
				System.out.println("search result map reduce complete");
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	   public static void main(String[] args) throws Exception {
			final Configuration conf = new Configuration();
			MongoConfigUtil.setInputURI(conf, GeneralConfig.getDBSearchResult());
			MongoConfigUtil.setOutputURI(conf, GeneralConfig.getDBStatistic());
			System.out.println("Conf: " + conf);

			final Job job = new Job(conf, "search_statistic");

			job.setJarByClass(MdbSimilarPhoto.class);

			// Mapper,Reduce and Combiner type definition
			 job.setMapperClass( SearchResultMapper.class );
			
			
			 job.setCombinerClass( SearchResultReducer.class );
			 job.setReducerClass( SearchResultReducer.class );

			// output key/value type definition
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);

			// InputFormat and OutputFormat type definition
			job.setInputFormatClass(MongoInputFormat.class);
			job.setOutputFormatClass(MongoOutputFormat.class);

			System.exit(job.waitForCompletion(true) ? 0 : 1);
		}
}
