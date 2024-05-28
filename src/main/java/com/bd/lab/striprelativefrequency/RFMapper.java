package com.bd.lab.striprelativefrequency;

import java.io.IOException;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class RFMapper extends Mapper<LongWritable, Text, Text, MapWritable> {

  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
    String[] terms = value.toString().split(" ");
    for (int i = 0; i < terms.length - 1; i++) {
      MapWritable map = new MapWritable();
      String term = terms[i];
      int j = i + 1;
      while (j < terms.length && terms[j] != term) {
        map.put(new Text(terms[j]),
            new DoubleWritable(
                ((DoubleWritable) map.getOrDefault(terms[j], new DoubleWritable(0))).get() + 1));
        j++;
      }
      context.write(new Text(term), map);
    }
  }
}
