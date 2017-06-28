package com.system.excel;
import java.io.BufferedOutputStream;  
import java.io.File;  
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.zip.ZipEntry;  
import java.util.zip.ZipOutputStream;

import com.system.util.ConfigManager;

import jxl.Workbook;  
import jxl.format.Alignment;  
import jxl.format.Border;  
import jxl.format.BorderLineStyle;  
import jxl.format.Colour;  
import jxl.format.UnderlineStyle;  
import jxl.format.VerticalAlignment;  
import jxl.write.Label;  
import jxl.write.WritableCellFormat;  
import jxl.write.WritableFont;  
import jxl.write.WritableSheet;  
import jxl.write.WritableWorkbook;  
import jxl.write.WriteException;  
import jxl.write.biff.RowsExceededException;  
  
/** 
 * zip压缩文件实例 
 * @author Administrator 
 * 
 */  
public class ZipUtil {  
  
    /** 
     * @param args 
     * @throws IOException  
     * @throws WriteException  
     * @throws RowsExceededException  
     */  

      
    /** 
     * 创建文件夹; 
     * @param path 
     * @return 
     */  
    public static String createFile(String path){  
        File file = new File(path);  
        //判断文件是否存在;  
        if(!file.exists()){  
            //创建文件;  
            boolean bol = file.mkdirs();  
            if(bol){  
                System.out.println(path+" 路径创建成功!");  
            }else{  
                System.out.println(path+" 路径创建失败!");  
            }  
        }else{  
            System.out.println(path+" 文件已经存在!");  
        }  
        return path;  
    }  
      
    /** 
     * 生成.zip文件; 
     * @param path 
     * @throws IOException  
     */  
    public static void craeteZipPath(String path,String zipName){  
    	 ZipOutputStream zipOutputStream = null;  
         File file = new File(path+zipName+".zip");  
         try {
		 zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
         File[] files = new File(path).listFiles();  
         FileInputStream fileInputStream = null;  
         byte[] buf = new byte[1024];  
         int len = 0;  
         if(files!=null && files.length > 0){  
             for(File excelFile:files){  
                 String fileName = excelFile.getName();  
                 fileInputStream = new FileInputStream(excelFile);  
                 //放入压缩zip包中;  
               
				zipOutputStream.putNextEntry(new ZipEntry(path + "/"+fileName));
					
                   
                 //读取文件;  
                 while((len=fileInputStream.read(buf)) >0){  
                     zipOutputStream.write(buf, 0, len);  
                 }  
                 //关闭;  
                 zipOutputStream.closeEntry();  
                 if(fileInputStream != null){  
                     fileInputStream.close();  
                 }  
             }  
         }  
           
         if(zipOutputStream !=null){  
             zipOutputStream.close();  
         }  
         } catch (FileNotFoundException e) {
        	 // TODO Auto-generated catch block
        	 System.err.println("文件未找到");
        	 e.printStackTrace();
         }  catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
    }  
      
    /** 
     * 删除目录下所有的文件; 
     * @param path 
     */  
    public static boolean deleteExcelPath(File file){  
        String[] files = null;  
        if(file != null){  
            files = file.list();  
        }  
          
        if(file.isDirectory()){  
            for(int i =0;i<files.length;i++){  
                boolean bol = deleteExcelPath(new File(file,files[i]));  
                if(bol){  
                    System.out.println("删除成功!");  
                }else{  
                    System.out.println("删除失败!");  
                }  
            }  
        }  
        return file.delete();  
    }  
}  