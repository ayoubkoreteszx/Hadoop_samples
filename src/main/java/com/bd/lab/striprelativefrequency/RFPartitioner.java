package com.bd.lab.striprelativefrequency;

import org.apache.hadoop.mapreduce.Partitioner;

public class RFPartitioner extends Partitioner {

  @Override
  public int getPartition(Object o, Object o2, int i) {
    return o.hashCode() % i;
  }
}
