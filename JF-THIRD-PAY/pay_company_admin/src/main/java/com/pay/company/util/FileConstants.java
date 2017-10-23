/**  
 * @Title: FileConstants.java
 * @Package com.ijm.gc.constant
 * @Description: TODO
 * @author pengzhihao
 * @date 2016-5-14
 */
package com.pay.company.util;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.core.teamwork.base.util.ReadPro;
import com.core.teamwork.base.util.date.DateUtil;


/**
 * 
 * @author qiuguojie
 *
 */
public class FileConstants {

	private static String signOpenssl = ReadPro.getValue("sign_openssl");
	 
	//根目录
	private static String nfsShareRoot = ReadPro.getValue("nfs_share_root");
    
	//总目录
    private static String nfsShareDir = ReadPro.getValue("nfs_share_dir");
    
    //金服目录
    private static String nfsShareJinfu = ReadPro.getValue("nfs_share_jinfu");
    
    //金服的图片文件目录
    private static String nfsShareJinfuFile = ReadPro.getValue("nfs_share_jinfu_file");
    
    private static FileConstants instance;
    
    private Map<String, String> nfsShareDirMap;
    
    
    /**
     * 金服目录类型
     */
    public static final String NFS_SHARE_TYPE_2000 = "2000";
    
    /**
     * 金服的图片文件目录类型
     */
    public static final String NFS_SHARE_TYPE_20001 = "20001";
    
    //私有构造
    private FileConstants(){

    }
    
    public static FileConstants getInstance(){
    	if(instance == null){
    		instance = new FileConstants();
    		//初始化
    		instance.init();
    	}
    	return instance;
    }
    
    public void init(){
    	nfsShareDirMap= new HashMap<String, String>();
		nfsShareDirMap.put("2000", getNfsShareJinfu());       //金服目录类型
		nfsShareDirMap.put("20001", getNfsShareJinfuFile());   //金服的图片文件目录类型
    }
    
    /**
     * @Description: 时间目录 （yyyMMdd）
     * @param @return   
     * @return String  
     * @throws
     * @author pengzhihao
     * @date 2016-7-21上午11:04:40
     */
    public static String getDateDir(){
    	return DateUtil.DateToString(new Date(), "yyyyMMdd") + File.separator;
    }
    
    /**
     * @Description: NFS共享根目录
     * @param @return   
     * @return String  
     * @throws
     * @author pengzhihao
     * @date 2016-7-4下午3:23:41
     */
    public static String getNfsShareRoot(){
    	return nfsShareRoot;
    }
    
    /**
     * @Description: NFS共享根目录(不带后缀)
     * @param @return   
     * @return String  
     * @throws
     * @author pengzhihao
     * @date 2016-7-21下午2:29:59
     */
    public static String getNfsShareRootNot(){
    	String nfsShareRootNot = nfsShareRoot.substring(0,nfsShareRoot.length() - 1);
    	return nfsShareRootNot;
    }
    
    /**
     * @Description: NFS共享文件存放总目录
     * @param @return   
     * @return String  
     * @throws
     * @author pengzhihao
     * @date 2016-7-4下午3:24:01
     */
    public static String getNfsShareDir(){
    	return nfsShareDir;
    }
    
    /**
     * @Description: NFS共享文件存放总目录  + Date时间目录 
     * @param @return   
     * @return String  
     * @throws
     * @author pengzhihao
     * @date 2016-7-4下午3:28:07
     */
    public static String getNfsShare(){
    	return getNfsShareDir() + getDateDir();
    }
    
    
    /**
     * @Description: NFS共享文件存放金服目录  + Date时间目录 
     * @param @return   
     * @return String  
     * @throws
     * @author pengzhihao
     * @date 2016-7-21上午11:06:15
     */
    public static String getNfsShareJinfu(){
    	return getNfsShareDir() + nfsShareJinfu + getDateDir();
    }
    
    /**
     * @Description: NFS共享金乌图片文件存放目录  + Date时间目录
     * @param @return   
     * @return String  
     * @throws
     * @author pengzhihao
     * @date 2016-7-21上午11:10:30
     */
    public static String getNfsShareJinfuFile(){
    	return getNfsShareDir() + nfsShareJinfu + nfsShareJinfuFile + getDateDir();
    }
    
    /**
     * @Description: 获取生成签名的openssl
     * @param @return   
     * @return String  
     * @throws
     * @author pengzhihao
     * @date 2016-7-15上午9:53:53
     */
    public static String getSignOpenssl(){
    	return signOpenssl;
    }
    
    /**
     * @Description: 返回文件目录
     * @param @param type
     * @param @return   
     * @return String  
     * @throws
     * @author pengzhihao
     * @date 2016-7-21上午11:18:04
     */
    public String getFileDir(String type){
    	if(StringUtils.isBlank(type)){
    		return getNfsShare();
    	}
    	String fileDir = getNfsShareDirMap().get(type);
    	if(StringUtils.isBlank(fileDir)){
    		fileDir = getNfsShare();
    	}
    	return fileDir;
    }
    
	public Map<String, String> getNfsShareDirMap() {
		return nfsShareDirMap;
	}
    
}
