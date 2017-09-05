package com.pay.company.controller.offline;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.core.teamwork.base.util.IdUtils;
import com.core.teamwork.base.util.ObjectUtil;
import com.core.teamwork.base.util.ReadPro;
import com.core.teamwork.base.util.ftp.FtpUploadClient;
import com.core.teamwork.base.util.page.PageHelper;
import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2BussCompanyShop;
import com.pay.business.merchant.mapper.Payv2BussCompanyMapper;
import com.pay.business.merchant.service.Payv2BussCompanyShopService;
import com.pay.business.payv2.entity.Payv2ShopCode;
import com.pay.business.payv2.service.Payv2ShopCodeService;
import com.pay.business.util.ParameterEunm;
import com.pay.business.util.QRCodeUtilByZXing;
import com.pay.business.util.qrcode.GetImage;
import com.pay.company.controller.admin.BaseManagerController;

@Controller
@RequestMapping("/offline/bussCompanyCheques/*")
public class Payv2BussCompanyChequesController extends BaseManagerController<Payv2BussCompany, Payv2BussCompanyMapper>{

	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private Payv2ShopCodeService payv2ShopCodeService;
	
	@Autowired
	private Payv2BussCompanyShopService payv2BussCompanyShopService;
	
	/**
	 * 收款码列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("chequesList")
	@ResponseBody
	public Map<String,Object> chequesList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Payv2BussCompany admin = isOffLine();
		if(admin == null){
			return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN,null,"用户Session不存在或失效,请重新登录");
		}
		boolean isNotNull = ObjectUtil.checkObject(new String[]{"curPage","pageData"}, map);
		if(!isNotNull){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数不允许为空");
		}
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("companyId", admin.getId());
		if(map.containsKey("shopId") && StringUtils.isNotBlank(map.get("shopId").toString())){
			Long shopId = NumberUtils.createLong(map.get("shopId").toString());
			paramMap.put("shopId", shopId);
		}
		if(map.containsKey("codeName") && StringUtils.isNotBlank(map.get("codeName").toString())){
			paramMap.put("codeName", map.get("codeName").toString());
		}
		if(map.containsKey("terminalCode") && StringUtils.isNotBlank(map.get("terminalCode").toString())){
			paramMap.put("terminalCode", map.get("terminalCode").toString());
		}
		if(map.containsKey("pageData") && map.containsKey("curPage") && map.get("pageData") != null && map.get("curPage") != null){
			paramMap.put("pageData", NumberUtils.createInteger(map.get("pageData").toString()));
			paramMap.put("curPage", NumberUtils.createInteger(map.get("curPage").toString()));
		}
		paramMap.put("isDelete", 2);//未删除
		try {
			PageObject<Payv2ShopCode> pageObject = null;
			if(paramMap.containsKey("shopId")){
				Integer count = payv2ShopCodeService.getCount(paramMap);
				PageHelper pageHelper = new PageHelper(count, paramMap);
				HashMap<String, Object> map2 = pageHelper.getMap();
				List<Payv2ShopCode> res = payv2ShopCodeService.queryByLeftJoin(map2);
				pageObject = pageHelper.getPageObject();
				pageObject.setDataList(res);
			}else{
				//如果这个商户没有门店呢 先去看这个商户有没有商铺
				List<Map<String, Object>> shopIdNameList = payv2BussCompanyShopService.getshopIdNameList(paramMap);
				if(shopIdNameList!=null && shopIdNameList.size()>0){
					List<Long> shopIdList = new ArrayList<>();
					for(Map<String,Object> maps : shopIdNameList){
						shopIdList.add(NumberUtils.createLong(maps.get("id").toString()));
					}
					/*PageHelper pageHelper = new PageHelper(shopIdNameList.size(), paramMap);
					HashMap<String, Object> map2 = pageHelper.getMap();
					map2.put("shopIdList", shopIdList);*/
					paramMap.put("shopIdList", shopIdList);
					List<Payv2ShopCode> queryByShopIdList = payv2ShopCodeService.queryByShopIdList(paramMap);
					int size = queryByShopIdList==null ? 0 : queryByShopIdList.size();
					PageHelper pageHelper = new PageHelper(size, paramMap);
					pageObject = pageHelper.getPageObject();
					pageObject.setDataList(queryByShopIdList);
				} else{
					PageHelper pageHelper = new PageHelper(0, paramMap);
					pageObject = pageHelper.getPageObject();
					pageObject.setDataList(null);
				}
			}
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, pageObject);
		} catch (Exception e) {
			logger.error("获取收款码列表异常:", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "服务器正在裸奔,请稍候重试");
		}
		return resultMap;
	}
	
	/**
	 * 收款码详情
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("chequesDetail")
	@ResponseBody
	public Map<String,Object> chequesDetail(@RequestParam Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Payv2BussCompany admin = isOffLine();
		if(admin == null){
			return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN,null,"用户Session不存在或失效,请重新登录");
		}
		boolean isNotNull = ObjectUtil.checkObject(new String[]{"id"}, map);
		if(!isNotNull){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数不允许为空");
		}
		try {
			Long id = NumberUtils.createLong(map.get("id").toString());
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.put("id",id);
			Payv2ShopCode detail = payv2ShopCodeService.detail(paramMap);
			if(detail!=null){
				detail.setTerminalType("门店码");
			}
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, detail);
		} catch (Exception e) {
			logger.error("获取收款码详情异常:", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "服务器正在裸奔,请稍候重试");
		}
		return resultMap;
	}
	
	/**
	 * 新增收款码
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("addCheques")
	@ResponseBody
	public Map<String,Object> addCheques(@RequestParam Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Payv2BussCompany admin = isOffLine();
		if(admin == null){
			return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN,null,"用户Session不存在或失效,请重新登录");
		}
		boolean isNotNull = ObjectUtil.checkObject(new String[]{"shopId","codeName","terminalCode"}, map);
		if(!isNotNull){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数不允许为空");
		}
		Long shopId = NumberUtils.createLong(map.get("shopId").toString());
		String codeName = map.get("codeName").toString();
		String terminalCode = map.get("terminalCode").toString();
		try {
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.put("id",shopId);
			Payv2BussCompanyShop detail = payv2BussCompanyShopService.detail(paramMap);
			if(detail==null){
				return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数数据错误");
			}
			String shopKey = detail.getShopKey();
			Payv2ShopCode shopCode = new Payv2ShopCode();
			shopCode.setCodeName(codeName);
			shopCode.setShopId(shopId);
			shopCode.setTerminalCode(terminalCode);
			if(map.containsKey("tagsMsg") && StringUtils.isNotBlank(map.get("tagsMsg").toString())){
				shopCode.setTagsMsg(map.get("tagsMsg").toString());
			}
			String content = ReadPro.getValue("qr_code")+"?shopKey="+shopKey + "&terminalCode="+terminalCode ;
			String path = request.getSession().getServletContext().getRealPath("/");
			String singleQRcode = singleQRcode(content,path);
			String qrUpdate = qrUpdate(singleQRcode);
			shopCode.setShopTwoCodeUrl(qrUpdate);
			shopCode.setCreateTime(new Date());
			payv2ShopCodeService.add(shopCode);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			logger.error("获取收款码详情异常:", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "服务器正在裸奔,请稍候重试");
		}
		return resultMap;
	}
	
	/**
	 * 修改收款码
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("editCheques")
	@ResponseBody
	public Map<String,Object> editCheques(@RequestParam Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Payv2BussCompany admin = isOffLine();
		if(admin == null){
			return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN,null,"用户Session不存在或失效,请重新登录");
		}
		boolean isNotNull = ObjectUtil.checkObject(new String[]{"id","shopId","codeName","terminalCode"}, map);
		if(!isNotNull){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数不允许为空");
		}
		Long shopId = NumberUtils.createLong(map.get("shopId").toString());
		Long id = NumberUtils.createLong(map.get("id").toString());
		String codeName = map.get("codeName").toString();
		String terminalCode = map.get("terminalCode").toString();
		try {
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.put("id",shopId);
			Payv2BussCompanyShop detail = payv2BussCompanyShopService.detail(paramMap);
			if(detail==null){
				return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数数据错误");
			}
			String shopKey = detail.getShopKey();
			Payv2ShopCode shopCode = new Payv2ShopCode();
			shopCode.setId(id);
			shopCode.setCodeName(codeName);
			shopCode.setShopId(shopId);
			shopCode.setTerminalCode(terminalCode);
			if(map.containsKey("tagsMsg") && StringUtils.isNotBlank(map.get("tagsMsg").toString())){
				shopCode.setTagsMsg(map.get("tagsMsg").toString());
			}
			String content = ReadPro.getValue("qr_code")+"?shopKey="+shopKey + "&terminalCode="+terminalCode ;
			String path = request.getSession().getServletContext().getRealPath("/");
			String singleQRcode = singleQRcode(content,path);
			String qrUpdate = qrUpdate(singleQRcode);
			shopCode.setShopTwoCodeUrl(qrUpdate);
			payv2ShopCodeService.update(shopCode);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			logger.error("获取收款码详情异常:", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "服务器正在裸奔,请稍候重试");
		}
		return resultMap;
	}
	public static String singleQRcode(String content,String path) throws WriterException, IOException{
		String name = System.currentTimeMillis() + "";
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		Hashtable hints = new Hashtable();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		File file1 = new File(path, name + ".jpg");
		
		BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 240, 240, hints);
		
		QRCodeUtilByZXing zx = new QRCodeUtilByZXing();
		zx.writeToFile(bitMatrix, "jpg", file1);
		return file1.getPath();
		/*String filename = System.currentTimeMillis() + "";
		File file2 = new File(path, filename+".jpg");
		//二维码套在背景图中
		ImageUtil.writeBackImg(path+"qrcode_rgb.jpg", file1.getPath(),file2.getPath());*/
	}
	
	public static String qrUpdate(String filePath){
		File file = new File(filePath);
		FtpUploadClient ftp = new FtpUploadClient();
		String ftpDir = System.currentTimeMillis() + File.separator;
		// upGetReady 第一个参数是路径上传路径 第二个参数是文件路径
		Boolean flag = ftp.upGetReady(ftpDir, file.getPath());
		if (flag) {
			 String url = ReadPro.getValue("ftp.visit.path") + ReadPro.getValue("ftp.upload.path") + ftpDir + file.getName();
			if (null != file && file.exists()) {
				file.delete();
			}
			//String url = "http://192.168.1.14" + "/yizhan/aizichan/" + ftpDir + file.getName();
			return url;
		} else {
			return null;
		}
	}
	public static void main(String[] args) throws WriterException, IOException {
		String singleQRcode = singleQRcode("http://192.168.1.118/company.do?shopKey=12345", "E:/workspace-myeclipse/pay_project/");
		System.out.println(singleQRcode);
		//E:\workspace-myeclipse\pay_project\1488850446115.jpg
		//File file1 = new File("E:/workspace-myeclipse/pay_project/1488791899679.jpg");
		String qrUpdate = qrUpdate(singleQRcode);
		System.out.println(qrUpdate);
	}
	/**
	 * 下载收款码
	 * @param map
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws WriterException 
	 * @throws InterruptedException 
	 */
	@RequestMapping("downLoadCheques")
	public void downLoadCheques(@RequestParam Map<String,Object> map,HttpServletRequest request,HttpServletResponse response) throws WriterException, IOException{
		//读取id
		Long id = NumberUtils.createLong(map.get("id").toString());
		map.clear();
		map.put("id", id);
		Payv2ShopCode detail = payv2ShopCodeService.detail(map);
		if(detail != null){
			InputStream in = null;
			OutputStream os = null;
			try {
				byte[] btImg = GetImage.getImageFromNetByUrl(detail.getShopTwoCodeUrl());
				if (null != btImg && btImg.length > 0) {
					String fileName = IdUtils.createId() + ".jpg";
					String realPath = request.getSession().getServletContext()
							.getRealPath("/");
					GetImage.writeImageToDisk(btImg, fileName, realPath);
					String filePath = realPath + fileName;
					in = new FileInputStream(filePath); // 获取文件的流
					os = response.getOutputStream();
					int len = 0;
					byte buf[] = new byte[1024];// 缓存作用
					response.setCharacterEncoding("UTF-8");
					// response.setContentType("application/octet-stream; charset=UTF-8");
					response.setContentType("application/x-download");
					response.setHeader("Content-Disposition",
							"attachment; filename=\"" + fileName + "\";");//
					os = response.getOutputStream();// 输出流
					while ((len = in.read(buf)) > 0) // 切忌这后面不能加 分号 ”;“
					{
						os.write(buf, 0, len);// 向客户端输出，实际是把数据存放在response中，然后web服务器再去response中读取
					}
					in.close();
					os.close();
					File file = new File(filePath);
					if (file.exists()) {
						file.delete();
					}
				} else {
					logger.error("没有从该连接获得内容:" + id);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
