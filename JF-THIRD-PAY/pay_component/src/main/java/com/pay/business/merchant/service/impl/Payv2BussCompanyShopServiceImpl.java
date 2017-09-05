package com.pay.business.merchant.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.core.teamwork.base.util.ReadPro;
import com.core.teamwork.base.util.ftp.FtpUploadClient;
import com.core.teamwork.base.util.insertid.ZipUtil;
import com.core.teamwork.base.util.page.PageHelper;
import com.core.teamwork.base.util.page.PageObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2BussCompanyShop;
import com.pay.business.merchant.mapper.Payv2BussCompanyMapper;
import com.pay.business.merchant.mapper.Payv2BussCompanyShopMapper;
import com.pay.business.merchant.service.Payv2BussCompanyShopService;
import com.pay.business.payv2.entity.Payv2BussAppSupportPayWay;
import com.pay.business.payv2.entity.Payv2PayShopQrcode;
import com.pay.business.payv2.entity.Payv2PlatformWay;
import com.pay.business.payv2.mapper.Payv2BussAppSupportPayWayMapper;
import com.pay.business.payv2.mapper.Payv2PayShopQrcodeMapper;
import com.pay.business.payv2.mapper.Payv2PlatformWayMapper;
import com.pay.business.payway.entity.Payv2PayWay;
import com.pay.business.payway.mapper.Payv2PayWayMapper;
import com.pay.business.util.GenerateUtil;
import com.pay.business.util.QRCodeUtilByZXing;
import com.pay.business.util.qrcode.ImageUtil;

/**
 * @author cyl
 * @version 
 */
@Service("payv2BussCompanyShopService")
public class Payv2BussCompanyShopServiceImpl extends BaseServiceImpl<Payv2BussCompanyShop, Payv2BussCompanyShopMapper> implements Payv2BussCompanyShopService {
	// 注入当前dao对象
    @Autowired
    private Payv2BussCompanyShopMapper payv2BussCompanyShopMapper;
    @Autowired
    private Payv2BussCompanyMapper payv2BussCompanyMapper;
    @Autowired
    private Payv2PlatformWayMapper payv2PlatformWayMapper;
    @Autowired
    private Payv2BussAppSupportPayWayMapper payv2BussAppSupportPayWayMapper;
    @Autowired
    private Payv2PayWayMapper payv2PayWayMapper;
    @Autowired
    private Payv2PayShopQrcodeMapper payv2PayShopQrcodeMapper;

    public Payv2BussCompanyShopServiceImpl() {
        setMapperClass(Payv2BussCompanyShopMapper.class, Payv2BussCompanyShop.class);
    }
    
 	public Payv2BussCompanyShop selectSingle(Payv2BussCompanyShop t) {
		return payv2BussCompanyShopMapper.selectSingle(t);
	}

	public List<Payv2BussCompanyShop> selectByObject(Payv2BussCompanyShop t) {
		return payv2BussCompanyShopMapper.selectByObject(t);
	}

	public PageObject<Payv2BussCompanyShop> payv2BussCompanyShopList(
			Map<String, Object> map) {
		int totalData = payv2BussCompanyShopMapper.getLinkCount(map);
		PageHelper pageHelper = new PageHelper(totalData, map);
		List<Payv2BussCompanyShop> list = payv2BussCompanyShopMapper.pageLinkQueryByObject(pageHelper.getMap());
		for(Payv2BussCompanyShop shop : list){
			if(shop.getCompanyId()!=null){
				Payv2BussCompany company = new Payv2BussCompany();
				company.setId(shop.getCompanyId());
				company = payv2BussCompanyMapper.selectSingle(company);
				if (company != null) {
					shop.setCompanyName(company.getCompanyName());
				}
			}
			// 得到支付方式个数
			Long appId = shop.getId();
			Payv2BussAppSupportPayWay payv2BussAppSupportPayWay = new Payv2BussAppSupportPayWay();
			payv2BussAppSupportPayWay.setAppId(appId);
			payv2BussAppSupportPayWay.setMerchantType(2);
			payv2BussAppSupportPayWay.setIsDelete(2);
			List<Payv2BussAppSupportPayWay> supportList = payv2BussAppSupportPayWayMapper
					.selectByObject(payv2BussAppSupportPayWay);
			shop.setAppSupportPayWayNumber(supportList.size());
			
			if (shop.getPayWayId() != null) {
				Payv2PayWay payv2PayWay = new Payv2PayWay();
				payv2PayWay.setId(shop.getPayWayId());
				payv2PayWay = payv2PayWayMapper.selectSingle(payv2PayWay);
				if (payv2PayWay != null) {
					shop.setWayName(payv2PayWay.getWayName());
				}
			}
		}
		
		PageObject<Payv2BussCompanyShop> pageObject = pageHelper.getPageObject();
		pageObject.setDataList(list);
		return pageObject;
	}
	/**
	 * 获取代理平台下面的线下店铺
	 */
	public List<Payv2BussCompanyShop> getLineShopList(Map<String, Object> map) {
		return payv2BussCompanyShopMapper.getLineShopList(map);
	}

	/**
	 * 根据支付通道查询线下商户
	 * 根据平台ID获取关联的支付通道，根据支付通道获取线上的商户
	 */
	public PageObject<Payv2BussCompanyShop> selectByPayWayIds(Map<String, Object> map) {
		PageObject<Payv2BussCompanyShop> pageObject = null;
		Payv2PlatformWay payv2PlatformWay = new Payv2PlatformWay();
		payv2PlatformWay.setPlatformId(Long.valueOf(map.get("platformId").toString()));
		//根据平台ID获取关联的支付通道
		List<Payv2PlatformWay> pltfFormList = payv2PlatformWayMapper.selectByObject(payv2PlatformWay);
		List<Long> paywayIds = new ArrayList<Long>();
		for(Payv2PlatformWay way : pltfFormList){
			paywayIds.add(way.getPayWayId());
		}
		if(paywayIds.size() > 0){
			Map<String, Object> reqmap = new HashMap<String, Object>();
			reqmap.put("payWayIds", paywayIds);
			if(map.get("shopName") != null){
				reqmap.put("shopName", map.get("shopName").toString());
			}
			if(map.get("curPage") != null){
				reqmap.put("curPage", map.get("curPage").toString());
			}
			//根据支付通道获取线上的商户
			int totalData = payv2BussCompanyShopMapper.selectCountsByPayWayIds(reqmap);
			PageHelper pageHelper = new PageHelper(totalData, reqmap);
			List<Payv2BussCompanyShop> list = payv2BussCompanyShopMapper.selectByPayWayIds(pageHelper.getMap());
			for (Payv2BussCompanyShop shop : list) {
				Payv2BussCompany company = new Payv2BussCompany();
				company.setId(shop.getCompanyId());
				company = payv2BussCompanyMapper.selectSingle(company);
				if (company != null) {
					shop.setCompanyName(company.getCompanyName());
				}
			}
			pageObject = pageHelper.getPageObject();
			pageObject.setDataList(list);
		}else{
			pageObject = new PageObject(0, 1, 10);
			pageObject.setDataList(new ArrayList());
		}
		return pageObject;
	}

	public void updateFor(Map<String, Object> map) {
		// 需要终止旗下的商铺支付方式
		Payv2BussCompanyShop payv2BussCompanyShop = new Payv2BussCompanyShop();
		payv2BussCompanyShop.setId(Long.valueOf(map.get("id").toString()));
		payv2BussCompanyShop.setIsDelete(2);
		payv2BussCompanyShop = payv2BussCompanyShopMapper.selectSingle(payv2BussCompanyShop);
		if (payv2BussCompanyShop != null) {
			// 商铺支付方式列表
			Payv2BussAppSupportPayWay payv2BussAppSupportPayWay = new Payv2BussAppSupportPayWay();
			payv2BussAppSupportPayWay.setAppId(payv2BussCompanyShop.getId());
			payv2BussAppSupportPayWay.setMerchantType(2);
			payv2BussAppSupportPayWay.setIsDelete(2);
			List<Payv2BussAppSupportPayWay> list = payv2BussAppSupportPayWayMapper.selectByObject(payv2BussAppSupportPayWay);
			for (Payv2BussAppSupportPayWay payv2BussAppSupportPayWay2 : list) {
				payv2BussAppSupportPayWay2.setUpdateTime(new Date());
				payv2BussAppSupportPayWay2.setStatus(2);
				payv2BussAppSupportPayWayMapper.updateByEntity(payv2BussAppSupportPayWay2);
			}
		}
		payv2BussCompanyShopMapper.updateByEntity(M2O(map));
		
	}

	/**
	 * 批量二维码列表
	 */
	public PageObject<Payv2BussCompanyShop> qrcodeList(Map<String, Object> map) {
		int totalData = payv2BussCompanyShopMapper.qrcodeListCount(map);
		PageHelper pageHelper = new PageHelper(totalData, map);
		List<Payv2BussCompanyShop> list = payv2BussCompanyShopMapper.qrcodeList(pageHelper.getMap());
		PageObject<Payv2BussCompanyShop> pageObject = pageHelper.getPageObject();
		pageObject.setDataList(list);
		return pageObject;
	}

	/**
	 * @throws IOException 
	 * 
	 */
	public Map<String, Object> uploadZipCode(Map<String, Object> map) throws Exception {
		Map<String,Object> resultMap = new HashMap<>();
		String fileName = map.get("fileName").toString();
		//zip包文件解压的文件夹名称
		String outputPathFileName = fileName.substring(
				0, fileName.lastIndexOf(File.separator));
		
		String str = fileName.substring(
				0, fileName.lastIndexOf("."));
		// 3.解压
		ZipUtil.unzip(fileName, outputPathFileName, true);
		
		File alipayFile = new File(str);
		File[] array = alipayFile.listFiles();
		for (File file : array) {
			String filePath = file.getPath();
			//压缩图片生成缩略图
			ImageUtil.thumbnailImage(filePath, 496, 703);
			
			String paths= filePath.substring(
					filePath.lastIndexOf(File.separator)+1);
			//截取图片二维码部分
			ImageUtil.cutImage(str+File.separator+"thumb_"+paths,str+File.separator, 148, 260, 202, 202);
			
			//解析支付宝二维码
			String alipayCode = QRCodeUtilByZXing.parseQR_CODEImage(new File(str+File.separator+"cut_thumb_"+paths));
			String shopKey = GenerateUtil.getRandomString(64);
			//生成二维码
			String content = ReadPro.getValue("qr_code") +"?shopKey="+shopKey ;
			String path = map.get("path").toString();//request.getSession().getServletContext().getRealPath("/");
			String name = System.currentTimeMillis() + "";
			MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
			Hashtable hints = new Hashtable();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			File file1 = new File(path, name + ".jpg");
			
			BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 1240, 1240, hints);
			
			QRCodeUtilByZXing zx = new QRCodeUtilByZXing();
			zx.writeToFile(bitMatrix, "jpg", file1);
			
			String filename = System.currentTimeMillis() + "";
			File file2 = new File(path, filename+".jpg");
			//二维码套在背景图中
			ImageUtil.writeBackImg(path+"qrcode_rgb.jpg", file1.getPath(),file2.getPath());
			
			
			FtpUploadClient ftp = new FtpUploadClient();
			String ftpDir = System.currentTimeMillis() + File.separator;
			// upGetReady 第一个参数是路径上传路径 第二个参数是文件路径
			Boolean flag = ftp.upGetReady(ftpDir, file2.getPath());
			String url = ReadPro.getValue("ftp.visit.path") + ReadPro.getValue("ftp.upload.path") + ftpDir + file2.getName();
			
			if (null != file1 && file1.exists()) {
				file1.delete();
			}
			if (null != file2 && file2.exists()) {
				file2.delete();
			}
			Payv2BussCompanyShop payv2BussCompanyShop = new Payv2BussCompanyShop();
			payv2BussCompanyShop.setShopTwoCodeUrl(url);
			payv2BussCompanyShop.setShopStatus(2);
			payv2BussCompanyShop.setShopKey(shopKey);
			payv2BussCompanyShop.setCreateTime(new Date());
			payv2BussCompanyShopMapper.insertByEntity(payv2BussCompanyShop);
			
			Payv2PayShopQrcode pavy2PayShopQrcode = new Payv2PayShopQrcode();
			pavy2PayShopQrcode.setAlipayQrcode(alipayCode);
			pavy2PayShopQrcode.setCreateTime(new Date());
			pavy2PayShopQrcode.setShopId(payv2BussCompanyShop.getId());
			payv2PayShopQrcodeMapper.insertByEntity(pavy2PayShopQrcode);
		}
		deleteFile(alipayFile);
		return resultMap;
	}
	
	public static void main(String[] args) {
		System.out.println("输入你的名字");
		Scanner c=new Scanner(System.in);
		String name=c.next();
		System.out.println("输入你的年龄");
		String age=c.next();
		System.out.println("my name is"+name+" my age is"+age);
	}
	/** 
     * 通过递归调用删除一个文件夹及下面的所有文件 
     * @param file 
     */  
    public void deleteFile(File file){  
    	
        if(file.isFile()){//表示该文件不是文件夹  
            file.delete();  
        }else{  
            //首先得到当前的路径  
            String[] childFilePaths = file.list(); 
            if(childFilePaths!=null){
            	for(String childFilePath : childFilePaths){  
                    File childFile=new File(file.getAbsolutePath()+File.separator+childFilePath);  
                    deleteFile(childFile);  
                } 
            }
            file.delete();  
        }  
    }

	public List<Payv2BussCompanyShop> allQrcodeList(Map<String, Object> map) {
		return payv2BussCompanyShopMapper.allQrcodeList(map);
	}

	public List<Map<String, Object>> getshopIdNameList(Map<String, Object> map) {
		return payv2BussCompanyShopMapper.getshopIdNameList(M2O(map));
	} 
}
