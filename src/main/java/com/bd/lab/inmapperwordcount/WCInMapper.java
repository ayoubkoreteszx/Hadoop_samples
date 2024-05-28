package com.bd.lab.inmapperwordcount;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WCInMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

  private Map<String, Integer> counts;

  @Override
  protected void setup(Context context) throws IOException, InterruptedException {
    counts = new HashMap<>();
  }

  @Override
  protected void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
    StringTokenizer tokenizer = new StringTokenizer(value.toString());
    while (tokenizer.hasMoreTokens()) {
      String word = tokenizer.nextToken();
      counts.put(word, counts.getOrDefault(word, 0) + 1);
    }
  }

  @Override
  protected void cleanup(Context context) throws IOException, InterruptedException {
    for (Map.Entry<String, Integer> entry : counts.entrySet()) {
      context.write(new Text(entry.getKey()), new IntWritable(entry.getValue()));
    }
  }
}
