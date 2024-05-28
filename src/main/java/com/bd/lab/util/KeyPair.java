package com.bd.lab.util;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.hadoop.io.WritableComparable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KeyPair implements WritableComparable<KeyPair> {

  double key;
  int value;

  @Override
  public void write(DataOutput dataOutput) throws IOException {
    dataOutput.writeDouble(this.key);
    dataOutput.writeInt(this.value);
  }

  @Override
  public void readFields(DataInput dataInput) throws IOException {
    this.key = dataInput.readDouble();
    this.value = dataInput.readInt();
  }

  @Override
  public int compareTo(KeyPair other) {
    if (this.key == other.key) {
      return this.value == other.value ? 0 : -1;
    }
    return -1;
  }

  @Override
  public String toString() {
    return "KeyPair{" +
        "key='" + key + '\'' +
        ", value=" + value +
        '}';
  }
}
