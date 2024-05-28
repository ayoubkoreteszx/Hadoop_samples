package com.bd.lab.striprelativefrequency;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MapContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class RFRunner {

  public static void main(String[] args)
      throws IOException, InterruptedException, ClassNotFoundException {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Relative Frequency Pairs");
    job.setJarByClass(RFRunner.class);

    job.setMapperClass(RFMapper.class);
    job.setCombinerClass(RFReducer.class);
    job.setReducerClass(RFReducer.class);
    job.setPartitionerClass(RFPartitioner.class);

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(MapWritable.class);
    job.setNumReduceTasks(3);

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
