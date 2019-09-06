package edu.gatech.cse6242;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.IOException;

public class Q4 {

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    /* set first job to calculate degree of each node.
     * Mapper maps the in and out dgrees of each node
     * Reducer calculates the difference
     */
    Job job1 = Job.getInstance(conf, "Q4-Degrees");

    /* TODO: Needs to be implemented */
    job1.setJarByClass(Q4.class);
    job1.setMapperClass(Q4Mapper.class);
    job1.setReducerClass(Q4Reducer.class);
    job1.setCombinerClass(Q4Reducer.class);
    job1.setOutputKeyClass(IntWritable.class);
    job1.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job1, new Path(args[0]));
    FileOutputFormat.setOutputPath(job1, new Path("temp"));
    
    
    
    /* set second job to map the difference to  node ids.
     * Reducer sums the number for same key values
     */
    job1.waitForCompletion(true);
    Job job2 = Job.getInstance(conf, "Q4-NodeCount");
    job2.setJarByClass(Q4.class);
    job2.setMapperClass(Q4NodeCountMapper.class);
    job2.setReducerClass(Q4Reducer.class);
    job2.setCombinerClass(Q4Reducer.class);
    job2.setOutputKeyClass(IntWritable.class);
    job2.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job2, new Path("temp"));
    FileOutputFormat.setOutputPath(job2, new Path(args[1]));
   

    System.exit(job2.waitForCompletion(true) ? 0 : 1);
  }
  
 public static class Q4Mapper extends Mapper<Object, Text, IntWritable, IntWritable>{	
	 private final static IntWritable in = new IntWritable(-1);
	 private final static IntWritable out = new IntWritable(1);
	 
	 public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		  String[] result = (value.toString()).split("\\t");
		  if (result != null && result.length == 2) {
			  //check if node id is non negative and value is positive
			  if(Integer.parseInt(result[0]) >= 0 && Integer.parseInt(result[1]) > 0) {
			  	context.write(new IntWritable(Integer.parseInt(result[0])), out);
			  	context.write(new IntWritable(Integer.parseInt(result[1])), in);
			  }
		  }
	  }
 }
 
 public static class Q4Reducer extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable>{	
	  public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		  int sum = 0;
		  for (IntWritable val : values) {
			 sum += val.get();
		  }
		  context.write(key, new IntWritable(sum));
	  }
	  
 }
 
 public static class Q4NodeCountMapper extends Mapper<Object, Text, IntWritable, IntWritable>{	
	 private IntWritable count = new IntWritable(1);
	 public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		  String[] result = (value.toString()).split("\\t");
		  if (result != null && result.length == 2) {
			  context.write(new IntWritable(Integer.parseInt(result[1])), count);
		}
	  }
 }
	 

}
