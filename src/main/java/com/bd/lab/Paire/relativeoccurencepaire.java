package com.bd.lab.Paire;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;
import java.util.Collection;
import java.io.DataInput;
import java.io.DataOutput;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class relativeoccurencepaire {
   


  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, FloatWritable>{

    private final static FloatWritable one = new FloatWritable(1);
    private Text word = new Text();
    
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
                   
                 
                      String record = value.toString();
                      StringTokenizer lineTokenizer = new StringTokenizer(record, "\n\r");
                      String line;
                     
                      
      while (lineTokenizer.hasMoreTokens()) {
      line = lineTokenizer.nextToken();       
      String[] itr =line.toString().split("\\s+");
      for (int i=0;i<itr.length-1;i++) {
        for(int j=1;j<itr.length;j++){
           if(itr[i].equals(itr[j])) break;
            word.set(itr[i]+","+itr[j]);
            context.write(word, one);
            word.set(itr[i]+",0");
            context.write(word, one);
        }
  }
}
  }
}

 public  static class IntSumReducer
       extends Reducer<Text,FloatWritable,Text,FloatWritable> {
    private FloatWritable result = new FloatWritable();
    private float occurWildCard=1;

    public void reduce(Text key, Iterable<FloatWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      float sum = 0;
      if(key.toString().contains(",0")){
        occurWildCard=(float)StreamSupport.stream(values.spliterator(), false).count();
        return;
      }
      for (FloatWritable val : values) {
            sum+=val.get();
      }
      if(!key.toString().contains(",0"))
       {
      result.set(sum/occurWildCard);
      context.write(key, result);
    }
      
    }
  }
public   static void delete(File f) throws IOException {
    if (f.exists()){
      if (f.isDirectory()) {
          for (File c : f.listFiles())
          delete(c);
      }
      if (!f.delete()){
          throw new FileNotFoundException("Failed to delete file: " + f);
      }
    }
  }
  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "word count");

    job.setJarByClass(relativeoccurencepaire.class);
    job.setMapperClass(TokenizerMapper.class);

    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(FloatWritable.class);
   
    FileInputFormat.addInputPath(job, new Path(args[0]));
    delete(new File(args[1]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }


}
