package com.pay.company.controller.upload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.core.teamwork.base.util.ReadPro;
import com.core.teamwork.base.util.UUIDGenerator;
import com.core.teamwork.base.util.ftp.FtpUploadClient;
import com.core.teamwork.base.util.plist.Icon;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.util.ParameterEunm;
import com.pay.company.util.FileConstants;

/**
 * @ClassName: UploadController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author buyuer
 * @date 2015年12月11日 下午3:59:12
 * 
 */
@RequestMapping("/upload/*")
@Controller
public class UploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);
    
    @ResponseBody
    @RequestMapping(value = "/addFile")
    public Map<String, Object> addFile(@RequestParam(value = "fileinfo", required = false) MultipartFile fileinfo, @RequestParam Map<String, Object> map, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (fileinfo != null) {
            try {
            	synchronized (fileinfo) {
            		String path = request.getSession().getServletContext().getRealPath("/");  
            		String fileName = System.currentTimeMillis()+".png";  
            		File targetFile = new File(path, fileName);  
            		if(!targetFile.exists()){  
            			targetFile.mkdirs();  
            		}  
            		//保存  
            		fileinfo.transferTo(targetFile); 
            		String original = fileinfo.getOriginalFilename();
            		if(StringUtils.isEmpty(original) || "nofilename".equals(original) || original.indexOf(".") == -1){
            			original = "AAA.png";
            		}
            		int index = original.lastIndexOf(".");
            		String indexStr = original.substring(index);
            		String url = FtpUploadClient.getInstance().uploadFile(UUID.randomUUID().toString()+indexStr,targetFile);
            		resultMap.put("url",url);
            		//resultMap.put("fileSize", fileinfo.getSize()/1024);
            		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
            		if(null!=targetFile && targetFile.exists()){
            			targetFile.delete();
            		}
				}

    	
            } catch (Exception e) {
                LOGGER.error("上传失败 …… ", e);
                resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null, null);
            }
        } else {
            resultMap = ReMessage.resultBack(ParameterEunm.ERROR_PARAMS_CODE,null, null);
        }
        return resultMap;
    }

    /**
     * 单文件上传
     * 
     * @param file
     * @param response
     * @throws Exception
     */
    private String common(MultipartFile file,HttpServletRequest request) throws Exception {
        String path = request.getSession().getServletContext().getRealPath("/");  
        LOGGER.info(" 正在上传文件中.... ");
        String fileName = file.getOriginalFilename();  
        File targetFile = new File(path, fileName);  
        if(!targetFile.exists()){  
            targetFile.mkdirs();  
        }  
        //保存  
        file.transferTo(targetFile); 
        String original = file.getOriginalFilename();
        int index = original.lastIndexOf(".");
        String indexStr =  original.substring(index);
        String url = FtpUploadClient.getInstance().uploadFile(UUID.randomUUID().toString()+indexStr,targetFile);
        if(!StringUtils.isEmpty(url)){
        	LOGGER.info("上传成功  文件路径为 ："+url);
        }else{
        	LOGGER.info("上传失败 ！ 文件路径为空！ ");
        }
        return url;
    }
    
    @ResponseBody
    @RequestMapping(value = "/addFiles")
    public Map<String, Object> addFiles(@RequestParam(value = "file", required = false) MultipartFile fileinfo, @RequestParam Map<String, Object> map, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
    	if (fileinfo != null) {
    		try {
    			String name = fileinfo.getOriginalFilename();
    			String ext = name.substring(name.indexOf("."));  
    			UUIDGenerator uuidGenerator = new UUIDGenerator();
            	String fileName = uuidGenerator.generate().toString() + ext;
            	String fileDir = FileConstants.getInstance().getFileDir(FileConstants.NFS_SHARE_TYPE_20001);
            	String fileTo = FileConstants.getNfsShareRoot() + fileDir;
            	
            	 // 执行上传到NFS目录操作
                File sdkFile = getFile(fileinfo.getInputStream(), fileTo, fileName);
                
    			Long fileSize = fileinfo.getSize();
    			// 执行上传操作
    			Map<String, Object> fileMap = new HashMap<String, Object>();
    			 // 执行上传到Ftp操作
                FtpUploadClient ftp = new FtpUploadClient();
                String ftpDir = File.separator + fileDir;
                ftpDir = ftpDir.replace("\\", "/");
                Boolean flag = ftp.upGetReady(ftpDir, sdkFile.getPath());
                
                if(flag){
	                String url = File.separator + fileDir + fileName;
	            	url = url.replace("\\", "/");
	    			fileMap.put("url",ReadPro.getValue("ftp.visit.path")+ReadPro.getValue("ftp.upload.path")+url);
	    			fileMap.put("fileSize", fileSize);
	    			fileMap.put("fileName", name);
	    			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, fileMap);
                }else{
                	resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, null);
                }
                if(null!=sdkFile && sdkFile.exists()){
                	sdkFile.delete();
        		}
    		} catch (Exception e) {
    			LOGGER.error("上传失败 …… ", e);
    			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null, null);
    		}
    		
    	} else {
    		resultMap = ReMessage.resultBack(ParameterEunm.ERROR_PARAMS_CODE,null, null);
    	}
        return resultMap;
    }

    /**
     * 根据byte数组，生成文件
     */
    private static File getFile(InputStream in, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
        	if(!filePath.endsWith(File.separator)){
        		filePath = filePath + File.separator;
        	}
            File dir = new File(filePath);
            if (!dir.exists()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            byte[] buffer = new byte[1024];  
            int len = -1;  
            while ((len = in.read(buffer)) != -1) {  
            	bos.write(buffer, 0, len);  
            }  
            in.close();  
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	if(in != null){
        		try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }
    
    @RequestMapping(value = "/uploadMultipleImg.do")  
    public void uploadMultipleImg(@RequestParam(value = "multipleImg", required = false) MultipartFile [] files, HttpServletRequest request,HttpServletResponse response /*, ModelMap model*/) throws Exception {  
        common(files,response,request);
    }
    
    @RequestMapping(value = "/uploadImgs.do")  
    public void uploadImgs(@RequestParam(value = "Imgs", required = false) MultipartFile [] files, HttpServletRequest request,HttpServletResponse response /*, ModelMap model*/) throws Exception {  
        common(files,response,request);
    }
    
    /**
     * 多文件上传
     * @param files
     * @param response
     * @throws Exception
     */
    private void common(MultipartFile[] files,HttpServletResponse response,HttpServletRequest request) throws Exception{
        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw = null;
        try {
            Map<String, Object> resultMap=new HashMap<String, Object>();
            StringBuilder sb = new StringBuilder();
            if(null!=files && files.length>0){
                for(MultipartFile file:files){
                    String path = request.getSession().getServletContext().getRealPath("/");  
                    String fileName = file.getOriginalFilename();  
                    File targetFile = new File(path, fileName);  
                    if(!targetFile.exists()){  
                        targetFile.mkdirs();  
                    }  
                    //保存
                    file.transferTo(targetFile);  
                    String original = file.getOriginalFilename();
                    int index = original.lastIndexOf(".");
                    String indexStr =  original.substring(index);
                    String url = FtpUploadClient.getInstance().uploadFile(UUID.randomUUID().toString()+indexStr,targetFile);
                    sb.append(url+",");
					// 删除临时生成的文件
					if (null != targetFile && targetFile.exists()) {
						targetFile.delete();
					}
                }
            }
            
            String url = sb.toString();
            if(StringUtils.isNoneBlank(url) && url.lastIndexOf(",") > 0){
            	//去掉最后一个逗号
            	resultMap.put("url", url.substring(0, url.length()-1));
            }else{
            	resultMap.put("url", url);
            }
            resultMap.put("status","success");
            String jsonText = JSON.toJSONString(resultMap, true);
            pw = response.getWriter();
            pw.write(jsonText);
            pw.flush();
        } finally {
            if(null!=pw){
                pw.close();
            }
        }
       
    }
    
    
    /**
     * 根据byte数组，生成文件
     */
    public static File getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }
    
    
    /**
   	 * 单文件上传(上传完成后不需要删除临时文件)
   	 * 
   	 * @param file
   	 * @param response
   	 * @throws Exception
   	 */
   	private String common2(File f) throws Exception {
   		FtpUploadClient ftp = new FtpUploadClient();
   		
   		String ftpDir = System.currentTimeMillis()+File.separator;
           String url = ReadPro.getValue("ftp.visit.path")
   				+ ReadPro.getValue("ftp.upload.path") + ftpDir +  f.getName();
           return url;
   	}
    
    @ResponseBody
    @RequestMapping(value = "/uploadApkIpa.do")
    public Map<String, Object> uploadApkIpa(@RequestParam(value = "file", required = false) MultipartFile fileinfo, @RequestParam Map<String, Object> map, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (fileinfo != null) {
            try {
         	   String fileName = fileinfo.getOriginalFilename();
         	   Map<String,Object> obj = new HashMap<>();
         	   obj.put("fileName", fileName);
         	   Long fileSize = fileinfo.getSize();
                byte[] arrays = fileinfo.getBytes();
                int index = fileName.lastIndexOf(".");
                String indexStr =  fileName.substring(index);
                File f = getFile(arrays, request.getSession().getServletContext().getRealPath("/"), UUID.randomUUID().toString()+indexStr);
               
                InputStream inputStream = fileinfo.getInputStream();
                byte[] bytes = IOUtils.toByteArray(inputStream);
                // 校验md5
                String md5 = DigestUtils.md5Hex(bytes);
                //执行上传操作
                String url = common2(f);
                if (StringUtils.isNoneBlank(url)) {
             	   obj.put("url", url);//文件地址
             	   obj.put("md5", md5);
             	   obj.put("fileSize", fileSize.intValue());//文件大小
             	   
                    //进行ipa和apk文件解析，提取app信息(获取包名和版本信息等)
                    Map<String, String> appInfoMap = Icon.getParams(f.getAbsolutePath());
                    //检查APP及版本
                    if (appInfoMap!=null&&appInfoMap.size() > 0&&appInfoMap.get("appType")!=null&&!String.valueOf(appInfoMap.get("appType")).equals("")) {
                 	    obj.put("packageName", appInfoMap.get("package"));	//包名
                 	    obj.put("version", appInfoMap.get("versionName"));	//主版本
                 	    obj.put("versionCode", appInfoMap.get("versionCode"));	//子版本
                 	    obj.put("appName", appInfoMap.get("appName"));
                    }
                    if (null != f && f.exists()) {
                        f.delete();
                    }
                    resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, obj);
                } else {
                    resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "服务器异常");
                }    
            } catch (Exception e) {
                LOGGER.error("上传失败 …… ", e);
                resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
            }
            
        } else {
            resultMap = ReMessage.resultBack(ParameterEunm.ERROR_PARAMS_CODE, "文件丢失了");
        }
        return resultMap;
    }
    
}
