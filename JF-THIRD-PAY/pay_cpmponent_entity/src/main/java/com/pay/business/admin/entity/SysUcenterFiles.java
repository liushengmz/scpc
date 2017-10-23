package com.pay.business.admin.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.sys_ucenter_files        
--------------------------------------------------------
id                   Integer(10)        NOTNULL             //文件自增长ID
file_md5             String(255)        NOTNULL             //文件的MD5值
file_url             String(255)        NOTNULL             //文件地址
file_type            String(50)                             //文件后缀
file_size            Long(19)                               //文件大小
create_time          Date(19)                               //创建时间
crc32                String(32)                             //sdk的crc32
*/
public class SysUcenterFiles implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 391380536031781981L;
	private	Integer id;
	private	String fileMd5;
	private	String fileUrl;
	private	String fileType;
	private	Long fileSize;
	private	Date createTime;
	private String crc32;

	/**
	* id  Integer(10)  NOTNULL  //文件自增长ID    
	*/
	public Integer getId(){
		return id;
	}
	
	/**
	* id  Integer(10)  NOTNULL  //文件自增长ID    
	*/
	public void setId(Integer id){
		this.id = id;
	}
	
	/**
	* file_md5  String(255)  NOTNULL  //文件的MD5值    
	*/
	public String getFileMd5(){
		return fileMd5;
	}
	
	/**
	* file_md5  String(255)  NOTNULL  //文件的MD5值    
	*/
	public void setFileMd5(String fileMd5){
		this.fileMd5 = fileMd5;
	}
	
	/**
	* file_url  String(255)  NOTNULL  //文件地址    
	*/
	public String getFileUrl(){
		return fileUrl;
	}
	
	/**
	* file_url  String(255)  NOTNULL  //文件地址    
	*/
	public void setFileUrl(String fileUrl){
		this.fileUrl = fileUrl;
	}
	
	/**
	* file_type  String(50)  //文件后缀    
	*/
	public String getFileType(){
		return fileType;
	}
	
	/**
	* file_type  String(50)  //文件后缀    
	*/
	public void setFileType(String fileType){
		this.fileType = fileType;
	}
	
	/**
	* file_size  Long(19)  //文件大小    
	*/
	public Long getFileSize(){
		return fileSize;
	}
	
	/**
	* file_size  Long(19)  //文件大小    
	*/
	public void setFileSize(Long fileSize){
		this.fileSize = fileSize;
	}
	
	/**
	* create_time  Date(19)  //创建时间    
	*/
	public Date getCreateTime(){
		return createTime;
	}
	
	/**
	* create_time  Date(19)  //创建时间    
	*/
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	
	/**
	 * crc32(32)  //sdk的crc32
	 */
	public String getCrc32() {
		return crc32;
	}

	public void setCrc32(String crc32) {
		this.crc32 = crc32;
	}
	
	
}