package com.pay.business.util;

import java.io.BufferedInputStream;    
import java.io.File;    
import java.io.FileInputStream;    
import java.io.FileOutputStream;    
import java.util.zip.CRC32;    
import java.util.zip.CheckedOutputStream;    
  
import org.apache.log4j.Logger;  
import org.apache.tools.zip.ZipEntry;    
import org.apache.tools.zip.ZipOutputStream;    
  
/** 
 * @ClassName: ZipCompressor 
 * @CreateTime Apr 28, 2013 1:12:16 PM 
 * @author : Mayi 
 * @Description: 压缩文件的通用工具类-采用org.apache.tools.zip.ZipOutputStream实现，较复杂。 
 * 
 */  
public class ZipCompressor {  
    private Logger logger = Logger.getLogger(ZipCompressor.class);  
    static final int BUFFER = 8192;    
    private static File zipFile ;    
      
    public static void getZipFile(String file){
    	zipFile = new File(file);
    }
    
    /** 
     * 执行压缩操作 
     * @param srcPathName 被压缩的文件/文件夹 
     */  
    public static void compressExe(String srcPathName) {    
        File file = new File(srcPathName);    
        if (!file.exists()){  
            throw new RuntimeException(srcPathName + "不存在！");    
        }  
        try {    
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);    
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,new CRC32());    
            ZipOutputStream out = new ZipOutputStream(cos);    
            String basedir = "";    
            compressByType(file, out, basedir);    
            out.close();    
        } catch (Exception e) {   
            e.printStackTrace();  
            throw new RuntimeException(e);    
        }    
    }    
    
    /** 
     * 判断是目录还是文件，根据类型（文件/文件夹）执行不同的压缩方法 
     * @param file  
     * @param out 
     * @param basedir 
     */  
    public static void compressByType(File file, ZipOutputStream out, String basedir) {    
        /* 判断是目录还是文件 */    
        if (file.isDirectory()) {    
            compressDirectory(file, out, basedir);    
        } else {    
            compressFile(file, out, basedir);    
        }    
    }    
    
    /** 
     * 压缩一个目录 
     * @param dir 
     * @param out 
     * @param basedir 
     */  
    public static void compressDirectory(File dir, ZipOutputStream out, String basedir) {    
        if (!dir.exists()){  
             return;    
        }  
             
        File[] files = dir.listFiles();    
        for (int i = 0; i < files.length; i++) {    
            /* 递归 */    
            compressByType(files[i], out, basedir + dir.getName() + "/");    
        }    
    }    
    
    /** 
     * 压缩一个文件 
     * @param file 
     * @param out 
     * @param basedir 
     */  
    public static void compressFile(File file, ZipOutputStream out, String basedir) {    
        if (!file.exists()) {    
            return;    
        }    
        try {    
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));    
            ZipEntry entry = new ZipEntry(basedir + file.getName());    
            out.putNextEntry(entry);    
            int count;    
            byte data[] = new byte[BUFFER];    
            while ((count = bis.read(data, 0, BUFFER)) != -1) {    
                out.write(data, 0, count);    
            }    
            bis.close();    
        } catch (Exception e) {    
            throw new RuntimeException(e);    
        }    
    }   
    
    public static void main(String[] args) {
    	getZipFile("C://img111.zip");  
        compressExe("C://img");
          
       /* ZipCompressorByAnt zca = new ZipCompressorByAnt("E:\\szhzipant.zip");  
        zca.compress("E:\\test");  */
	}
}  
