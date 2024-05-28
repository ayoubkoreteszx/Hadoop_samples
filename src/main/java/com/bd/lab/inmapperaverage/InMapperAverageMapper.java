package com.bd.lab.inmapperaverage;

import com.bd.lab.util.KeyPair;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class InMapperAverageMapper extends Mapper<LongWritable, Text, Text, KeyPair> {

  private final Text ipAddressText = new Text();
  private Map<String, KeyPair> maps;

  private static Double getFileSize(String[] s) {
    double size;
    try {
      size = Double.parseDouble(s[9].trim());
    } catch (NumberFormatException ex) {
      return null;
    }
    return size;
  }

  @Override
  protected void setup(Context context) throws IOException, InterruptedException {
    maps = new HashMap<>();
  }

  @Override
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
    Double size = getFileSize(terms);
    if (size == null) {
      return;
    }
    updateValue(ipAddress, size);
  }

  private void updateValue(String ipAddress, Double size) {
    if (maps.containsKey(ipAddress)) {
      KeyPair value = maps.get(ipAddress);
      value.setKey(value.getKey() + size);
      value.setValue(value.getValue() + 1);
      maps.put(ipAddress, value);
    } else {
      maps.put(ipAddress, new KeyPair(size, 1));
    }
  }

  @Override
  protected void cleanup(Context context) throws IOException, InterruptedException {
    for (Map.Entry<String, KeyPair> entry : maps.entrySet()) {
      ipAddressText.set(entry.getKey());
      context.write(ipAddressText, entry.getValue());
    }
  }

  private boolean isIpAddress(String value) {
    String regex = "^((25[0-5]|(2[0-4]|1\\d|[1-9])?\\d)\\.){3}(25[0-5]|(2[0-4]|1\\d|[1-9])?\\d)$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(value);
    return matcher.matches();
  }
}
