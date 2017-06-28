package com.xt.sms.mt;

import java.io.PrintStream;

public class DeByteCode
{
  private int len;
  private int size;
  public int offset;
  private byte[] bytes;

  protected int getLen()
  {
    return this.len;
  }

  protected int getSize() {
    return this.size;
  }

  protected DeByteCode(int i) {
    this.bytes = new byte[i];
    this.size = i;
    this.len = 0;
    this.offset = 0;
  }

  protected DeByteCode(byte[] a) {
    this.bytes = a;
    this.size = a.length;
    this.len = a.length;
    this.offset = 0;
  }

  public byte[] getBytes(int i) throws Exception {
    if (this.offset + i > this.size) {
      throw new Exception("underrun.int16.");
    }
    byte[] b = new byte[i];
    for (int j = 0; j < i; j++) {
      b[j] = this.bytes[(this.offset + j)];
    }
    this.offset += i;
    return b;
  }

  public short int16() throws Exception
  {
    if (this.offset + 1 > this.size) {
      throw new Exception("underrun.int16.");
    }
    short word0 = (short)((this.bytes[this.offset] & 0xFF) << 8 | this.bytes[(this.offset + 1)] & 0xFF);
    this.offset += 2;
    return word0;
  }

  public byte int8() throws Exception
  {
    if (this.offset > this.size) {
      throw new Exception("underrun.int8.");
    }
    byte i = this.bytes[this.offset];
    this.offset += 1;
    return i;
  }

  public int int32() throws Exception
  {
    if (this.offset + 3 > this.size) {
      throw new Exception("underrun.int32.");
    }
    int i = (this.bytes[this.offset] & 0xFF) << 24 | (this.bytes[(this.offset + 1)] & 0xFF) << 16 | (this.bytes[(this.offset + 2)] & 0xFF) << 8 | this.bytes[(this.offset + 3)] & 0xFF;
    this.offset += 4;
    return i;
  }

  public String asciiz(int i)
  {
    String str = null;
    byte[] b = new byte[i];

    for (int j = 0; j < b.length; j++) {
      b[j] = this.bytes[(this.offset + j)];
    }
    this.offset += i;
    if (i == 0)
      str = null;
    else {
      str = new String(b);
    }
    return str;
  }

  public void printBytes() {
    int j = 0;
    for (int i = 0; i < this.len; i++) {
      if (i % 10 == 0) {
        System.out.println('\n');
        j++;
        System.out.print("Line Number " + j + "  :");
      }
      System.out.print(' ');
      System.out.print(' ');
      System.out.print('[');
      System.out.print(Long.toString(this.bytes[i] & 0xFF, 16));
      System.out.print('|');
      System.out.print((char)this.bytes[i]);
      System.out.print(']');
    }
    System.out.println("\n-------------------------------------------------------------------");
  }
}