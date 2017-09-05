package com.pay.business.util.qrcode;

import java.io.ByteArrayOutputStream;  
import java.io.File;  
import java.io.FileOutputStream;  
import java.io.InputStream;  
import java.net.HttpURLConnection;  
import java.net.URL;  

import com.core.teamwork.base.util.insertid.ZipUtil;
/** 
 * @说明 从网络获取图片到本地 
 * @author 
 * @version 1.0 
 * @since 
 */  
public class GetImage {  
    /** 
     * 测试 
     * @param args 
     */  
    public static void main(String[] args) {  
       /* String url = "http://192.168.1.14/yizhan/aizichan/1487762336673/1487762336666.jpg";  
        byte[] btImg = getImageFromNetByUrl(url);  
        if(null != btImg && btImg.length > 0){  
            System.out.println("读取到：" + btImg.length + " 字节");  
            String fileName = "百度.jpg";  
            writeImageToDisk(btImg, fileName,"C:\\img\\");  
        }else{  
            System.out.println("没有从该连接获得内容");  
        }  */
    	try {
			ZipUtil.zip("C:\\img\\", "C:\\", "img");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }  
    /** 
     * 将图片写入到磁盘 
     * @param img 图片数据流 
     * @param fileName 文件保存时的名称 
     */  
    public static void writeImageToDisk(byte[] img, String fileName,String path){  
        try {  
        	File dir = new File(path);
            if (!dir.exists()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            File file = new File(path + fileName);  
            FileOutputStream fops = new FileOutputStream(file);  
            fops.write(img);  
            fops.flush();  
            fops.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    /** 
     * 根据地址获得数据的字节流 
     * @param strUrl 网络连接地址 
     * @return 
     */  
    public static byte[] getImageFromNetByUrl(String strUrl){  
        try {  
            URL url = new URL(strUrl);  
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
            conn.setRequestMethod("GET");  
            conn.setConnectTimeout(5 * 1000);  
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据  
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据  
            inStream.close();
            return btImg;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
    /** 
     * 从输入流中获取数据 
     * @param inStream 输入流 
     * @return 
     * @throws Exception 
     */  
    public static byte[] readInputStream(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        while( (len=inStream.read(buffer)) != -1 ){  
            outStream.write(buffer, 0, len);  
        }  
        byte[] btImg = outStream.toByteArray();//得到图片的二进制数据  
        inStream.close();  
        outStream.close();
        return btImg;  
    }  
}  