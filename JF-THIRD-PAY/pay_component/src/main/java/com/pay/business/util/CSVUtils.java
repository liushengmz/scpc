package com.pay.business.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;


public class CSVUtils<T> {
	
    /** 
     * 生成为CVS文件  
     * @param exportData 
     *              源数据List 
     * @param map 
     *              csv文件的列表头map 
     * @param outPutPath 
     *              文件路径 
     * @param fileName 
     *              文件名称 
     * @return 
     */  
    @SuppressWarnings("rawtypes")  
    public static File createCSVFile(List exportData, LinkedHashMap map, String outPutPath,  
                                     String fileName, String alipayTime, 
                         	        int tradeTotol, double tradePayMoney, int refundTotol, double refundPayMoney) {  
        File csvFile = null;  
        BufferedWriter csvFileOutputStream = null;  
        try {  
            File file = new File(outPutPath);  
            if (!file.exists()) {  
                file.mkdir();  
            }  
            //定义文件名格式并创建  
            csvFile = File.createTempFile(fileName, ".csv", new File(outPutPath));  
            // UTF-8使正确读取分隔符","    
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(  
                csvFile), "GBK"), 1024);  
            csvFileOutputStream.write("#全民金服支付集业务明细查询\r\n"); 
            csvFileOutputStream.write("#起始日期：["+alipayTime+" 00:00:00]   终止日期：["+alipayTime+" 23:59:59]"+"\r\n");
            csvFileOutputStream.write("#-----------------------------------------业务明细列表----------------------------------------\r\n");
            // 写入文件头部    
            for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator.hasNext();) {  
                java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();  
                csvFileOutputStream.write((String) propertyEntry.getValue() != null ? new String(  
                    ((String) propertyEntry.getValue()).getBytes("GBK"), "GBK") : "");  
                if (propertyIterator.hasNext()) {  
                    csvFileOutputStream.write(",");  
                }  
/*                System.out.println(new String(((String) propertyEntry.getValue()).getBytes("GBK"),  
                    "GBK")); */ 
            }  
            csvFileOutputStream.write("\r\n");  
            // 写入文件内容    
            for (Iterator iterator = exportData.iterator(); iterator.hasNext();) {  
                Object row = (Object) iterator.next();  
                for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator  
                    .hasNext();) {  
                    java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator  
                        .next();  
                    csvFileOutputStream.write((String) BeanUtils.getProperty(row,  
                        ((String) propertyEntry.getKey()) != null? (String) propertyEntry.getKey()  
                            : ""));  
                    if (propertyIterator.hasNext()) {  
                        csvFileOutputStream.write(",");  
                    }  
                }  
                if (iterator.hasNext()) {  
                    csvFileOutputStream.write("\r\n");  
                }  
            }
            csvFileOutputStream.write("\r\n");  
            csvFileOutputStream.write("#-----------------------------------------业务明细列表结束------------------------------------\r\n");
            csvFileOutputStream.write("#交易合计："+tradeTotol+"笔，总金额"+tradePayMoney+"元\r\n");
            csvFileOutputStream.write("##退款合计："+refundTotol+"笔，退款共"+refundPayMoney+"元\r\n");
            csvFileOutputStream.flush();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                csvFileOutputStream.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return csvFile;  
    }  
    
    /**
     * 通用导出
     * @param head 列名
     * @param dataList 
     * @param outPutPath
     * @param filename
     * @return
     */
    public File commonCSV(List<Object> head, List<T> dataList, String outPutPath, String filename){
    	long start = new Date().getTime();
    	File csvFile = null;
        BufferedWriter csvWtriter = null;
        try {
            csvFile = new File(outPutPath + File.separator + filename);
            File parent = csvFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            csvFile.createNewFile();

            // GB2312使正确读取分隔符","
            csvWtriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                    csvFile), "GB2312"), 1024);
            // 写入文件头部
            writeRow(head, csvWtriter);

            // 写入文件内容
            Iterator<T> it = dataList.iterator();
            while (it.hasNext()) {
            	List<Object> row = new ArrayList<Object>();
            	T t = (T) it.next();
                // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
                Field[] fields = t.getClass().getDeclaredFields();
                for (short i = 0; i < fields.length; i++) {
                	Field field = fields[i];
                    String fieldName = field.getName();
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    try {
                    	Class tCls = t.getClass();
                        Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
                        Object value = getMethod.invoke(t, new Object[] {});
                        // 判断值的类型后进行强制类型转换
                        String textValue = null;
                        if (value instanceof Date) {
                            Date date = (Date) value;
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            textValue = sdf.format(date);
                        }else if(value instanceof Double){
                        	textValue = value.toString();
                        }else if (null != value) {
                            textValue = value.toString()+"\t";
                        }else {
                        	textValue = "";
                        }
                        row.add(textValue);
					} catch (Exception e) {
						// TODO: handle exception
					}
                }
                writeRow(row, csvWtriter);
            }
            csvWtriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvWtriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long end = new Date().getTime();
        System.out.println("消耗时间：" + (end - start));
        return csvFile;
    }
    
    /**
     * 写一行数据方法
     * @param row
     * @param csvWriter
     * @throws IOException
     */
    public void writeRow(List<Object> row, BufferedWriter csvWriter) throws IOException {
        // 写入文件头部
        for (Object data : row) {
            StringBuffer sb = new StringBuffer();
            String rowStr = sb.append("\"").append(data).append("\",").toString();
            csvWriter.write(rowStr);
        }
        csvWriter.newLine();
    }
    
    /** 
     * 测试数据 
     * @param args 
     */  
    @SuppressWarnings({ "rawtypes", "unchecked" })  
    public static void main(String[] args) {  
    	
    	/*Object[] headers = { "支付集订单号", "商户订单号", "来源商户", "来源应用", "支付渠道", "订单金额(元)", "实际支付金额(元)", "优惠金额(元)", "退款金额(元)", "订单状态", "创建时间", "订单支付时间", "订单回调时间" };
    	List<Object> headList = Arrays.asList(headers);
    	String path = "C:/Users/Administrator/Desktop/";
    	String fileName = "test";
    	List<Object> dataset = new ArrayList<Object>();
    	for (int i = 0; i < 70000; i++) {
    		Payv2PayOrderBean bte = new Payv2PayOrderBean();
			// 支付集订单号
			bte.setOrderNum("456423" + i);
			// 商户订单号
			bte.setMerchantOrderNum("8956465" + i);
			// 来源商户
			bte.setCompanyName("中文测试" + i);
			// 来源应用
			bte.setAppName("中文测试" + i);
			// 支付渠道
			bte.setWayName("中文测试" + i);
			// 订单金额(元)
			bte.setPayMoney(0.01);
			// 实际支付金额(元)
			bte.setPayDiscountMoney(0.02);
			// 优惠金额(元)
			bte.setDiscountMoney(100.00);
			// 退款金额(元)
			bte.setRefundMoney(50.00);
			// 订单状态
			bte.setPayStatusName("支付成功");
			// 创建时间
			bte.setCreateTime(new Date());
			// 支付时间
			bte.setPayTime(new Date());
			// 回调时间
			bte.setCallbackTime(new Date());
			dataset.add(bte);
		}
    	
    	CSVUtils<Object> test = new CSVUtils<Object>(); 
    	
    	
    	File file = test.commonCSV(headList, dataset, path, fileName);*/
    	
        /*List exportData = new ArrayList<Map>();  
        Map row1 = new LinkedHashMap<String, String>();  
        row1.put("1", "11");  
        row1.put("2", "12");  
        row1.put("3", "13");  
        row1.put("4", "14");  
        exportData.add(row1);  
        row1 = new LinkedHashMap<String, String>();  
        row1.put("1", "21");  
        row1.put("2", "22");  
        row1.put("3", "23");  
        row1.put("4", "24");  
        exportData.add(row1);  
        LinkedHashMap map = new LinkedHashMap();  
        map.put("1", "第一列");  
        map.put("2", "第二列");  
        map.put("3", "第三列");  
        map.put("4", "第四列");  
  
        String path = "c:/export/";  
        String fileName = "文件导出";  
        File file = CSVUtils.createCSVFile(exportData, map, "C:/Users/Administrator/Desktop/", fileName, "x" ,0 , 0.0 ,0 , 0.0); */
//        String fileName2 = file.getName();  
//        System.out.println("文件名称：" + fileName2);  
    	//File file = new File("c:/export/文件导出1476037662567984675.csv");
    	
//		FtpUploadClient c =  new FtpUploadClient("192.168.1.14", 210, "jinfuftp", "jinfuftp", "/yizhan/aizichan/");
//    	String url = c.uploadFile("2017"+".csv",file);
//    	System.out.println(url);
    	
//    	ftp.upload.address=192.168.1.14
//    			ftp.upload.port=210
//    			ftp.upload.name=jinfuftp
//    			ftp.upload.pwd=jinfuftp
//    			ftp.upload.path=/yizhan/aizichan/
//    			ftp.visit.path=http://192.168.1.14
    } 
    
}
