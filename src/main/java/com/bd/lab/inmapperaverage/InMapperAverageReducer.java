package com.bd.lab.inmapperaverage;

import com.bd.lab.util.KeyPair;
import java.io.IOException;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class InMapperAverageReducer extends Reducer<Text, KeyPair, Text, DoubleWritable> {

  private final DoubleWritable result = new DoubleWritable();

  @Override
  public void reduce(Text key, Iterable<KeyPair> values, Context context)
      throws IOException, InterruptedException {
    double sum = 0, count = 0;
    for (KeyPair val : values) {
      sum += val.getKey();
      count += val.getValue();
    }
    result.set(sum / count);
    context.write(key, result);
  }
}