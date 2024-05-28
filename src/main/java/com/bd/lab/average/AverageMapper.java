package com.bd.lab.average;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class AverageMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {

  private final Text ipAddressText = new Text();

  private static Double getFileSize(String[] s) {
    double size;
    try {
      size = Double.parseDouble(s[9].trim());
    } catch (NumberFormatException ex) {
      return null;
    }
    return size;
  }

  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
    String[] terms = value.toString().split(" ");
    if (terms.length < 9) {
      return;
    }
    String ipAddress = terms[0].trim();
    if (!isIpAddress(ipAddress)) {
      return;
    }
    ipAddressText.set(ipAddress);
    Double size = getFileSize(terms);
    if (size == null) {
      return;
    }
    context.write(ipAddressText, new DoubleWritable(size));
  }

  private boolean isIpAddress(String value) {
    String regex = "^((25[0-5]|(2[0-4]|1\\d|[1-9])?\\d)\\.){3}(25[0-5]|(2[0-4]|1\\d|[1-9])?\\d)$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(value);
    return matcher.matches();
  }
}
