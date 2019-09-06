package edu.gatech.cse6242;

import java.util.StringTokenizer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.IOException;

public class Q1 {

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Q1");

    /* TODO: Needs to be implemented */
    
    job.setJarByClass(Q1.class);
    job.setMapperClass(Q1Mapper.class);
    job.setReducerClass(Q1Reducer.class);
    job.setCombinerClass(Q1Reducer.class);
    job.setOutputKeyClass(IntWritable.class);
    job.setOutputValueClass(IntWritable.class);

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
  
  
  public static class Q1Mapper extends Mapper<Object, Text, IntWritable, IntWritable>{	
	  
	  public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		  String[] result = (value.toString()).split("\\t");
		  if (result != null && result.length == 3) {
			  //check if target is non negative and value is positive
			  if(Integer.parseInt(result[1]) >= 0 && Integer.parseInt(result[2]) > 0) {
			  	context.write(new IntWritable(Integer.parseInt(result[1])), new IntWritable(Integer.parseInt(result[2])));
			  }
		  }
	  }
	
  }
  
  public static class Q1Reducer extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable>{	
	  
	  private IntWritable result = new IntWritable();
	  public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		  int min = Integer.MAX_VALUE;
		  for (IntWritable val : values) {
			  if (min > val.get())
				  min = val.get();
		  }
		  result.set(min);
		  context.write(key, result);
	  }
	  
  }
  
  
}
