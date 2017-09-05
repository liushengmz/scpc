package com.pay.business.util.qrcode;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageUtil {

    private static String DEFAULT_THUMB_PREVFIX = "thumb_";
    private static String DEFAULT_CUT_PREVFIX = "cut_";
    private static Boolean DEFAULT_FORCE = false;
    
    /**
     * <p>Title: cutImage</p>
     * <p>Description:  根据原图与裁切size截取局部图片</p>
     * @param srcImg    源图片
     * @param output    图片输出流
     * @param rect        需要截取部分的坐标和大小
     */
    public static void cutImage(File srcImg, OutputStream output, java.awt.Rectangle rect){
        if(srcImg.exists()){
            FileInputStream fis = null;
            ImageInputStream iis = null;
            try {
                fis = new FileInputStream(srcImg);
                // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
                String types = Arrays.toString(ImageIO.getReaderFormatNames()).replace("]", ",");
                String suffix = null;
                // 获取图片后缀
                if(srcImg.getName().indexOf(".") > -1) {
                    suffix = srcImg.getName().substring(srcImg.getName().lastIndexOf(".") + 1);
                }// 类型和图片后缀全部小写，然后判断后缀是否合法
                if(suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase()+",") < 0){
                    return ;
                }
                // 将FileInputStream 转换为ImageInputStream
                iis = ImageIO.createImageInputStream(fis);
                // 根据图片类型获取该种类型的ImageReader
                ImageReader reader = ImageIO.getImageReadersBySuffix(suffix).next();
                reader.setInput(iis,true);
                ImageReadParam param = reader.getDefaultReadParam();
                param.setSourceRegion(rect);
                BufferedImage bi = reader.read(0, param);
                ImageIO.write(bi, suffix, output);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(fis != null) fis.close();
                    if(iis != null) iis.close();
                    if(output!=null) output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
        }
    }
    
    public static void cutImage(File srcImg, OutputStream output, int x, int y, int width, int height){
        cutImage(srcImg, output, new java.awt.Rectangle(x, y, width, height));
    }
    
    public static void cutImage(File srcImg, String destImgPath, java.awt.Rectangle rect){
        File destImg = new File(destImgPath);
        if(destImg.exists()){
            String p = destImg.getPath();
            try {
                if(!destImg.isDirectory()) p = destImg.getParent();
                if(!p.endsWith(File.separator)) p = p + File.separator;
                cutImage(srcImg, new java.io.FileOutputStream(p + DEFAULT_CUT_PREVFIX  + srcImg.getName()), rect);
            } catch (FileNotFoundException e) {
            }
        }else{
        	
        };
    }
    
    public static void cutImage(File srcImg, String destImg, int x, int y, int width, int height){
        cutImage(srcImg, destImg, new java.awt.Rectangle(x, y, width, height));
    }
    
    public static void cutImage(String srcImg, String destImg, int x, int y, int width, int height){
        cutImage(new File(srcImg), destImg, new java.awt.Rectangle(x, y, width, height));
    }
    /**
     * <p>Title: thumbnailImage</p>
     * <p>Description: 根据图片路径生成缩略图 </p>
     * @param imagePath    原图片路径
     * @param w            缩略图宽
     * @param h            缩略图高
     * @param prevfix    生成缩略图的前缀
     * @param force        是否强制按照宽高生成缩略图(如果为false，则生成最佳比例缩略图)
     */
    public static void thumbnailImage(File srcImg, OutputStream output, int w, int h, String prevfix, boolean force){
        if(srcImg.exists()){
            try {
                // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
                String types = Arrays.toString(ImageIO.getReaderFormatNames()).replace("]", ",");
                String suffix = null;
                // 获取图片后缀
                if(srcImg.getName().indexOf(".") > -1) {
                    suffix = srcImg.getName().substring(srcImg.getName().lastIndexOf(".") + 1);
                }// 类型和图片后缀全部小写，然后判断后缀是否合法
                if(suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase()+",") < 0){
                    return ;
                }
                Image img = ImageIO.read(srcImg);
                // 根据原图与要求的缩略图比例，找到最合适的缩略图比例
                if(!force){
                    int width = img.getWidth(null);
                    int height = img.getHeight(null);
                    if((width*1.0)/w < (height*1.0)/h){
                        if(width > w){
                            h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w/(width*1.0)));
                        }
                    } else {
                        if(height > h){
                            w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h/(height*1.0)));
                        }
                    }
                }
                BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Graphics g = bi.getGraphics();
                g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
                g.dispose();
                // 将图片保存在原目录并加上前缀
                ImageIO.write(bi, suffix, output);
                output.close();
            } catch (IOException e) {
            	e.printStackTrace();
            }
        }else{
        }
    }
    public static void thumbnailImage(File srcImg, int w, int h, String prevfix, boolean force){
        String p = srcImg.getAbsolutePath();
        try {
            if(!srcImg.isDirectory()) p = srcImg.getParent();
            if(!p.endsWith(File.separator)) p = p + File.separator;
            thumbnailImage(srcImg, new java.io.FileOutputStream(p + prevfix +srcImg.getName()), w, h, prevfix, force);
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        }
    }
    
    public static void thumbnailImage(String imagePath, int w, int h, String prevfix, boolean force){
        File srcImg = new File(imagePath);
        thumbnailImage(srcImg, w, h, prevfix, force);
    }
    
    public static void thumbnailImage(String imagePath, int w, int h, boolean force){
        thumbnailImage(imagePath, w, h, DEFAULT_THUMB_PREVFIX, DEFAULT_FORCE);
    }
    
    public static void thumbnailImage(String imagePath, int w, int h){
        thumbnailImage(imagePath, w, h, DEFAULT_FORCE);
    }
    
    /** 
     * 通过递归调用删除一个文件夹及下面的所有文件 
     * @param file 
     */  
    public static void deleteFile(File file){  
    	
        if(file.isFile()){//表示该文件不是文件夹  
            file.delete();  
        }else{  
            //首先得到当前的路径  
            String[] childFilePaths = file.list();  
            for(String childFilePath : childFilePaths){  
                File childFile=new File(file.getAbsolutePath()+"\\"+childFilePath);  
                deleteFile(childFile);  
            }  
            file.delete();  
        }  
    }  
    
    /**
     * 二维码套在背景图上		8/10：二维码可覆盖背景图中间的80%
     * @param backPath	背景图路劲
     * @param codePath	二维码路劲
     * @param lastPath	整合的图片保存路劲
     * @throws IOException
     */
    public static void writeBackImg(String backPath,String codePath,String savePath) throws IOException{
		BufferedImage image= ImageIO.read(new File(codePath));//二维码
    	BufferedImage bg= ImageIO.read(new File(backPath));//获取背景图片
    	Graphics2D g=bg.createGraphics();
    	int width=image.getWidth(null) > bg.getWidth() * 8/10? (bg.getWidth() * 8/10) : image.getWidth(null);
    	int height=image.getHeight(null) > bg.getHeight() *8 /10? (bg.getHeight() * 8/10) : image.getWidth(null);
    	g.drawImage(image,(bg.getWidth()- width)/2,(bg.getHeight()-height)/2,width,height,null);
    	g.dispose();
    	bg.flush();
    	image.flush();
    	ImageIO.write(bg,"jpg", new File(savePath));
    }
    
    /**
     * cmyk转rgb （颜色会失真）
     * @param filename
     * @return
     * @throws IOException
     */
    public static String readImage(String filename) throws IOException {          
        File file = new File(filename);  
         ImageInputStream input = ImageIO.createImageInputStream(file);  
         Iterator readers = ImageIO.getImageReaders(input);  
         if(readers == null || !readers.hasNext()) {  
             throw new RuntimeException("1 No ImageReaders found");  
         }  
         ImageReader reader = (ImageReader) readers.next();  
         reader.setInput(input);  
         String format = reader.getFormatName() ;  
         BufferedImage image;   
          
         if ( "JPEG".equalsIgnoreCase(format) ||"JPG".equalsIgnoreCase(format) )   {              
             try {    
                 // 尝试读取图片 (包括颜色的转换).     
             image = reader.read(0); //RGB  
             } catch (IIOException e) {    
                 // 读取Raster (没有颜色的转换).     
             Raster raster = reader.readRaster(0, null);//CMYK    
                 image = createJPEG4(raster);  
             }   
            image.getGraphics().drawImage(image, 0, 0, null);  
            String dstfilename = filename.substring(0,filename.lastIndexOf("."))+"_rgb"+filename.substring(filename.lastIndexOf("."));  
            String newfilename = filename;  
            File newFile = new File(dstfilename);  
            FileOutputStream out = new FileOutputStream(newFile);  
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);  
            encoder.encode(image);  
            out.flush();  
            out.close();   
            return dstfilename;  
        }  
         return null;  
     }  
  
     private static BufferedImage createJPEG4(Raster raster) {  
         int w = raster.getWidth();  
         int h = raster.getHeight();  
         byte[] rgb = new byte[w * h * 3];  
       //彩色空间转换          
         float[] Y = raster.getSamples(0, 0, w, h, 0, (float[]) null);  
         float[] Cb = raster.getSamples(0, 0, w, h, 1, (float[]) null);  
         float[] Cr = raster.getSamples(0, 0, w, h, 2, (float[]) null);  
         float[] K = raster.getSamples(0, 0, w, h, 3, (float[]) null);  
         for (int i = 0, imax = Y.length, base = 0; i < imax; i++, base += 3) {  
             float k = 220 - K[i], y = 255 - Y[i], cb = 255 - Cb[i],  
                     cr = 255 - Cr[i];  
  
             double val = y + 1.402 * (cr - 128) - k;  
             val = (val - 128) * .65f + 128;  
             rgb[base] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff  
                     : (byte) (val + 0.5);  
  
             val = y - 0.34414 * (cb - 128) - 0.71414 * (cr - 128) - k;  
             val = (val - 128) * .65f + 128;  
             rgb[base + 1] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff  
                     : (byte) (val + 0.5);  
  
             val = y + 1.772 * (cb - 128) - k;  
             val = (val - 128) * .65f + 128;  
             rgb[base + 2] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff  
                     : (byte) (val + 0.5);  
         }  
         raster = Raster.createInterleavedRaster(new DataBufferByte(rgb, rgb.length), w, h, w * 3, 3, new int[]{0, 1, 2}, null);  
         ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);  
         ColorModel cm = new ComponentColorModel(cs, false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);  
         return new BufferedImage(cm, (WritableRaster) raster, true, null);  
     }  
       
     public static String TestImg(String src) {  
        File imgsrc = new File(src);  
        try {  
            ImageIO.read(imgsrc);  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
              
            String msg = e.getMessage();  
            System.out.println("msg:"+msg);  
            if (msg.indexOf("Unsupported Image Type") == 0) {  
                try {  
                    return readImage(src);  
                } catch (IOException e1) {  
                    // TODO Auto-generated catch block  
                    e1.printStackTrace();  
                }  
            } else {  
                e.printStackTrace();  
                return null;  
            }  
        }  
        return src;  
     } 
    
    public static void main(String[] args) throws Exception {
    	//System.out.println(readImage("d:/二维码.jpg"));
    	//zip("d:/2017-02-21 10_36_21.zip");
        //ImageUtil.thumbnailImage(s, 290, 490);
        writeBackImg("d:/qrcode_ rgb.jpg", "d:/ccc1.jpg","d://ggggg.jpg");
        //ImageUtil.cutImage("d:/thumb_mdistinguishdfdfe0a70337b13e43993dbd6b667197.jpg","d:/", 148, 260, 202, 202);
    }

}