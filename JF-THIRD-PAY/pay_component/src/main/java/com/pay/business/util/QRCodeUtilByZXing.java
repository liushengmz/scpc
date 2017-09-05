package com.pay.business.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

/**
 * java生成二维条码
 * 
 * @author Administrator
 * 
 */
public class QRCodeUtilByZXing {

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    
    public static void writeToFile(BitMatrix matrix, String format, File file)
            throws IOException {
      //1.1去白边
        int[] rec = matrix.getEnclosingRectangle();  
        int resWidth = rec[2] + 1;  
        int resHeight = rec[3] + 1;  
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);  
        resMatrix.clear();  
        for (int i = 0; i < resWidth; i++) {  
            for (int j = 0; j < resHeight; j++) {  
                if (matrix.get(i + rec[0], j + rec[1])) { 
                     resMatrix.set(i, j); 
                } 
            }  
        }  
        //2
        int width = resMatrix.getWidth();
        int height = resMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_BYTE_INDEXED);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, resMatrix.get(x, y) == true ? 
                        BLACK : WHITE);
            }
        }
        
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }


    
    public static void writeToStream(BitMatrix matrix, String format,
            OutputStream stream) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format "  + format);
        }
    }

    /**
     * 
     * 主函数 
     */
    public static void main(String[] args) {
        try {
            String content = "http://tapi.aizichan.cn/api/?userId=" + 123;
            String path = "d://";
            
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            
            BitMatrix bitMatrix = multiFormatWriter.encode(content,
                    BarcodeFormat.QR_CODE, 980, 980, hints);
            File file1 = new File(path, "ccc1.jpg");
            System.out.println(file1.getPath());
            QRCodeUtilByZXing zx = new QRCodeUtilByZXing();
            zx.writeToFile(bitMatrix, "jpg", file1);
//            QRCodeUtilByZXing.parseQR_CODEImage(new File("D://cut__1487672975193_thumb_mdistinguishdfdfe0a70337b13e43993dbd6b667197.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    }
    
    /** 
     * 二维码的解析 
     * 
     * @param file 
     * @throws IOException 
     */  
    public static String parseQR_CODEImage(File file) throws IOException  
    {  
        try  
        {  
            MultiFormatReader formatReader = new MultiFormatReader();  
   
            // File file = new File(filePath);  
            if (!file.exists())  
            {  
                return "";  
            }  
   
            BufferedImage image = ImageIO.read(file);  
   
            LuminanceSource source = new BufferedImageLuminanceSource(image);  
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));  
   
            Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();  
            
            hints.put(DecodeHintType.CHARACTER_SET, "GBK");  
   
            Result result = formatReader.decode(binaryBitmap, hints);  
   
            return result.toString()!=null?result.toString():result.getText();
        }  
        catch (NotFoundException e)  
        {  
            e.printStackTrace();  
            return "";  
        }  
    } 
    
}
