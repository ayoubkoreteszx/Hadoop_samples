package com.bd.lab.striprelativefrequency;

import java.io.IOException;
import java.util.Map;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;

public class RFReducer extends Reducer<Text, MapWritable, Text, MapWritable> {

  public void reduce(Text key, Iterable<MapWritable> values, Context context)
      throws IOException, InterruptedException {
    MapWritable map = new MapWritable();
    values.iterator().forEachRemaining(map::putAll);
    double sum = 0;
    for (Map.Entry<Writable, Writable> entry : map.entrySet()) {
      sum += ((DoubleWritable) entry.getValue()).get();
    }
    for (Map.Entry<Writable, Writable> entry : map.entrySet()) {
      double avg = ((DoubleWritable) entry.getValue()).get() / sum;
      entry.setValue(new DoubleWritable(avg));
    }
    context.write(key, map);
  }
}