package com.pay.manger.interceptor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class dddd
{
  private static String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.";
  private static String[] strings = { 
    "BCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.A", 
    "CDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.AB", 
    "DEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABC", 
    "EFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCD", 
    "FGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDE", 
    "GHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEF", 
    "HIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFG", 
    "IJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGH", 
    "JKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHI", 
    "KLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJ", 
    "LMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJK", 
    "MNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKL", 
    "NOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLM", 
    "OPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMN", 
    "PQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNO", 
    "QRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOP", 
    "RSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQ", 
    "STUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQR", 
    "TUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRS", 
    "UVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRST", 
    "VWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTU", 
    "WXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUV", 
    "XYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVW", 
    "YZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWX", 
    "Zabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXY", 
    "abcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZ", 
    "bcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZa", 
    "cdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZab", 
    "defghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabc", 
    "efghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcd", 
    "fghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcde", 
    "ghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdef", 
    "hijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefg", 
    "ijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefgh", 
    "jklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghi", 
    "klmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghij", 
    "lmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk", 
    "mnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijkl", 
    "nopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklm", 
    "opqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmn", 
    "pqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmno", 
    "qrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnop", 
    "rstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopq", 
    "stuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqr", 
    "tuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrs", 
    "uvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrst", 
    "vwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstu", 
    "wxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuv", 
    "xyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvw", 
    "yz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwx", 
    "z1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxy", 
    "1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", 
    "234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1", 
    "34567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz12", 
    "4567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123", 
    "567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234", 
    "67890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz12345", 
    "7890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456", 
    "890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567", 
    "90_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz12345678", 
    "0_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789", 
    "_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890", 
    "$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_", 
    "@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$", 
    ".ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@" };

  String st2 = "BCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.A";
  String st3 = "CDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.AB";
  String st4 = "DEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABC";
  String st5 = "EFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCD";
  String st6 = "FGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDE";
  String st7 = "GHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEF";
  String st8 = "HIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFG";
  String st9 = "IJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGH";
  String s10 = "JKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHI";
  String s11 = "KLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJ";
  String s12 = "LMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJK";
  String s13 = "MNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKL";
  String s14 = "NOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLM";
  String s15 = "OPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMN";
  String s16 = "PQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNO";
  String s17 = "QRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOP";
  String s18 = "RSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQ";
  String s19 = "STUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQR";
  String s20 = "TUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRS";
  String s21 = "UVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRST";
  String s22 = "VWXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTU";
  String s23 = "WXYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUV";
  String s24 = "XYZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVW";
  String s25 = "YZabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWX";
  String s26 = "Zabcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXY";
  String s27 = "abcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  String s28 = "bcdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZa";
  String s29 = "cdefghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZab";
  String s30 = "defghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabc";
  String s31 = "efghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcd";
  String s32 = "fghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcde";
  String s33 = "ghijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdef";
  String s34 = "hijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefg";
  String s35 = "ijklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefgh";
  String s36 = "jklmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghi";
  String s37 = "klmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghij";
  String s38 = "lmnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk";
  String s39 = "mnopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijkl";
  String s40 = "nopqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklm";
  String s41 = "opqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmn";
  String s42 = "pqrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmno";
  String s43 = "qrstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnop";
  String s44 = "rstuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopq";
  String s45 = "stuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqr";
  String s46 = "tuvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrs";
  String s47 = "uvwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrst";
  String s48 = "vwxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstu";
  String s49 = "wxyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuv";
  String s50 = "xyz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvw";
  String s51 = "yz1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwx";
  String s52 = "z1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxy";
  String s53 = "1234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  String s54 = "234567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1";
  String s55 = "34567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzq2";
  String s56 = "4567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzq23";
  String s57 = "567890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzq234";
  String s58 = "67890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzq2345";
  String s59 = "7890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzq23456";
  String s60 = "890_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzq234567";
  String s61 = "90_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzq2345678";
  String s62 = "0_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzq23456789";
  String s63 = "_$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzq234567890";
  String s64 = "$@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzq234567890_";
  String s65 = "@.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzq234567890_$";

  public static void main(String[] args)
  {
    new dddd(); System.out.println(decrypt("qr9qoMp2zGHI", 12));
  }

  public static String encrypt_1(String plainText)
  {
    try
    {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(plainText.getBytes());
      byte[] b = md.digest();

      StringBuffer buf = new StringBuffer("");
      for (int offset = 0; offset < b.length; offset++) {
        int i = b[offset];
        if (i < 0)
          i += 256;
        if (i < 16)
          buf.append("0");
        buf.append(Integer.toHexString(i));
      }
      return buf.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String encrypt_2(String oldPwd, int salt)
  {
    try
    {
      String analysis1 = encrypt_1(oldPwd);
      String newStr = analysis1 + salt;
      return encrypt_1(newStr);
    }
    catch (Exception localException) {
    }
    return "";
  }

  public static String encrypt_2_1(String add1Pwd, int salt)
  {
    String newStr = add1Pwd + salt;
    String analysis2 = encrypt_1(newStr);
    return analysis2;
  }

  public static String encrypt_3(String dec, int salt)
  {
    long b = System.currentTimeMillis();
    if ((dec.length() < 1) || (salt < 0))
      return "";
    dec = encrypt_2(dec, salt);
    String result = "";
    for (int i = 0; i < dec.length(); i++) {
      char cha = dec.charAt(i);
      int index = str.indexOf(cha);
      result = result + strings[salt].charAt(index);
    }
    System.out.println("加密耗时:" + (System.currentTimeMillis() - b));
    return result;
  }

  public static int getSalt()
  {
    Random random = new Random();
    return random.nextInt(strings.length);
  }

  public static String decrypt(String encryptStr, int salt)
  {
    String result = "";
    for (int i = 0; i < encryptStr.length(); i++) {
      char cha = encryptStr.charAt(i);
      int index = strings[salt].indexOf(cha);
      if (index >= 0)
        result = result + str.charAt(index);
      else
        result = result + cha;
    }
    return result;
  }

  public static String encrypt(String dec, int salt)
  {
    String result = "";
    for (int i = 0; i < dec.length(); i++) {
      char cha = dec.charAt(i);
      int index = str.indexOf(cha);
      result = result + strings[salt].charAt(index);
    }

    return result;
  }
}
