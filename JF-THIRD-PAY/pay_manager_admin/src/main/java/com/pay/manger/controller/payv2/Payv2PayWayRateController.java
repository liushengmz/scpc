package com.pay.manger.controller.payv2;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.redis.RedisDBUtil;
import com.pay.business.admin.entity.SysUcenterAdmin;
import com.pay.business.admin.service.SysUcenterAdminService;
import com.pay.business.payway.entity.Payv2PayWayRate;
import com.pay.business.payway.entity.Payv2PayWayRateVO;
import com.pay.business.payway.mapper.Payv2PayWayRateMapper;
import com.pay.business.payway.service.Payv2PayWayRateService;
import com.pay.business.sysconfig.entity.SysConfigDictionary;
import com.pay.business.sysconfig.service.SysConfigDictionaryService;
import com.pay.business.util.CSVUtils;
import com.pay.business.util.pinganbank.pay.AddConfig;
import com.pay.manger.controller.admin.BaseManagerController;

/**
 * 
 * @ClassName: Payv2PayWayRateController
 * @Description: 支付通道路由
 * @author mofan
 * @date 2016年12月28日 10:13:11
 */
@Controller
@RequestMapping("/payv2PayWayRate")
public class Payv2PayWayRateController extends
		BaseManagerController<Payv2PayWayRate, Payv2PayWayRateMapper> {
	Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private Payv2PayWayRateService payv2PayWayRateService;
	@Autowired
	private SysConfigDictionaryService sysConfigDictionaryService;
	@Autowired
	private SysUcenterAdminService sysUcenterAdminService;
	
	

	/**
	 * 支付通道路由列表
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/payv2PayWayRateList")
	public ModelAndView payv2PayWayRateList(@RequestParam Map<String, Object> map) {
		ModelAndView mv = new ModelAndView("payv2/paywayrate/payv2paywayrate-list");
		map.put("isDelete", 2);// 未删除
		map.put("sortName", "update_time");
		map.put("sortOrder", "DESC");
		PageObject<Payv2PayWayRate> pageObject = payv2PayWayRateService.Pagequery(map);
		mv.addObject("list", pageObject);

		Map<String, Object> param = new HashMap<String, Object>();
		SysConfigDictionary sysConfigDictionary = new SysConfigDictionary();
		param.put("dictPvalue", -1);
		param.put("dictName", "PAY_TYPE");
		sysConfigDictionary = sysConfigDictionaryService.detail(param);
		if (sysConfigDictionary != null) {
			param = new HashMap<String, Object>();
			param.put("dictPvalue", sysConfigDictionary.getId());
			List<SysConfigDictionary> dicList = sysConfigDictionaryService
					.query(param);
			mv.addObject("dicList", dicList);
		}
		
		mv.addObject("map", map);// payWayId
		return mv;
	}

	/**
	 * @Title: addPayv2PayWayRateTc
	 * @Description:添加支付通道路由弹窗
	 * @param map
	 * @return 设定文件
	 * @return ModelAndView 返回类型
	 * @date 2016年12月6日 上午11:21:22
	 * @throws
	 */
	@RequestMapping("addPayv2PayWayRateTc")
	public ModelAndView addPayv2PayWayRateTc(
			@RequestParam Map<String, Object> map) {
		ModelAndView andView = new ModelAndView(
				"payv2/paywayrate/payv2paywayrate-add_new");
		Map<String, Object> param = new HashMap<String, Object>();
		SysConfigDictionary sysConfigDictionary = new SysConfigDictionary();
		param.put("dictPvalue", -1);
		param.put("dictName", "PAY_TYPE");
		sysConfigDictionary = sysConfigDictionaryService.detail(param);
		if (sysConfigDictionary != null) {
			param = new HashMap<String, Object>();
			param.put("dictPvalue", sysConfigDictionary.getId());
			List<SysConfigDictionary> dicList = sysConfigDictionaryService
					.query(param);
			/*
			 * List<SysConfigDictionary> dicList2 = new
			 * ArrayList<SysConfigDictionary>(); Set<Integer> dictIdSet = new
			 * HashSet<Integer>(); param.remove("dictPvalue");
			 * param.put("wayType", 2); //查询已经使用过的第三方支付 List<Payv2PayWayRate>
			 * payv2PayWayRateList = payv2PayWayRateService.query(param);
			 * for(Payv2PayWayRate payv2PayWayRate : payv2PayWayRateList){
			 * if(payv2PayWayRate.getDicId() != null &&
			 * !dictIdSet.contains(Integer
			 * .valueOf(payv2PayWayRate.getDicId().toString()))){
			 * dictIdSet.add(Integer
			 * .valueOf(payv2PayWayRate.getDicId().toString())); } }
			 * //过滤掉使用过的第三方支付 for(SysConfigDictionary dictionary : dicList){
			 * if(!dictIdSet.contains(dictionary.getId())){
			 * dicList2.add(dictionary); } }
			 */
			andView.addObject("dicList", dicList);
			andView.addObject("map", map);
		}
		return andView;
	}

	/**
	 * @Title: addPayv2PayWay
	 * @Description:添加支付渠道提交
	 * @param map
	 * @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @date 2016年12月7日 下午7:57:11
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("addPayv2PayWayRate")
	public Map<String, Object> addPayv2PayWayRate(
			@RequestParam Map<String, Object> map) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			map.put("createTime", new Date());
			map.put("updateTime", new Date());
			map.put("createUserBy", getAdmin().getId());
			map.put("updateUserBy", getAdmin().getId());
			map.put("status", 2);// 默认不启动

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("dicId", map.get("dicId"));
			paramMap.put("companyName", map.get("companyName"));
			paramMap.put("isDelete", 2);
			List<Payv2PayWayRate> list = payv2PayWayRateService.query(paramMap);
			if (list == null || list.isEmpty()) {
				Payv2PayWayRate rate = payv2PayWayRateService.add(map);
				Payv2PayWayRate uRate = new Payv2PayWayRate();
				String payPlat = "";
				if (map.get("payViewType").equals("1"))
					payPlat = "支付宝支付";
				else if (map.get("payViewType").equals("2"))
					payPlat = "微信支付";
				else if (map.get("payViewType").equals("3"))
					payPlat = "QQ支付";
				else
					payPlat = "其他";
				uRate.setId(rate.getId());
				uRate.setPayWayName(payPlat + rate.getId());
				payv2PayWayRateService.update(uRate);
				returnMap.put("resultCode", 200);
			} else {
				returnMap.put("resultCode", 201);
			}
		} catch (Exception e) {
			logger.error("添加支付渠道提交失败", e);
			e.printStackTrace();
		}
		return returnMap;
	}

	/**
	 * @Title: editPayv2PayWayRateTc
	 * @Description:修改支付通道路由弹窗
	 * @param map
	 * @return 设定文件
	 * @return ModelAndView 返回类型
	 * @date 2016年12月6日 上午11:21:22
	 * @throws
	 */
	@RequestMapping("editPayv2PayWayRateTc")
	public ModelAndView editPayv2PayWayRateTc(
			@RequestParam Map<String, Object> map) {
		ModelAndView andView = new ModelAndView(
				"payv2/paywayrate/payv2paywayrate-edit_new");
		Payv2PayWayRate payv2PayWay = payv2PayWayRateService.detail(map);
		String createUserName = "";
		String updateUserName = "";
		Map<String, Object> adminMap = new HashMap<String, Object>();
		if(payv2PayWay.getCreateUserBy() != null) {
			adminMap.put("id", payv2PayWay.getCreateUserBy());
			SysUcenterAdmin admin = sysUcenterAdminService.detail(adminMap);
			if(admin != null) {
				createUserName = admin.getAdmDisplayName();
			}
		}
		if(payv2PayWay.getUpdateUserBy() != null&&payv2PayWay.getCreateUserBy()!=null) {
			if(payv2PayWay.getUpdateUserBy().longValue() == payv2PayWay.getCreateUserBy().longValue()) {
				updateUserName = createUserName;
			} else {
				adminMap.put("id", payv2PayWay.getUpdateUserBy());
				SysUcenterAdmin admin = sysUcenterAdminService.detail(adminMap);
				if(admin != null) {
					updateUserName = admin.getAdmDisplayName();
				}
			}
		}

		andView.addObject("curPage", map.get("curPage"));
		andView.addObject("createUserName", createUserName);
		andView.addObject("updateUserName", updateUserName);
		andView.addObject("payv2PayWayRate", payv2PayWay);
		SysConfigDictionary sysConfigDictionary = new SysConfigDictionary();
		map.remove("id");
		map.put("dictPvalue", -1);
		map.put("dictName", "PAY_TYPE");
		sysConfigDictionary = sysConfigDictionaryService.detail(map);
		if (sysConfigDictionary != null) {
			map = new HashMap<String, Object>();
			map.put("dictPvalue", sysConfigDictionary.getId());
			List<SysConfigDictionary> dicList = sysConfigDictionaryService
					.query(map);
			/*
			 * List<SysConfigDictionary> dicList2 = new
			 * ArrayList<SysConfigDictionary>(); Set<Integer> dictIdSet = new
			 * HashSet<Integer>(); map.remove("dictPvalue"); map.put("wayType",
			 * 2); //查询已经使用过的第三方支付 List<Payv2PayWayRate> payv2PayWayList =
			 * payv2PayWayRateService.query(map); for(Payv2PayWayRate payway :
			 * payv2PayWayList){ if(payway.getDicId() != null &&
			 * !dictIdSet.contains
			 * (Integer.valueOf(payway.getDicId().toString()))){ //当前的支付方式不过滤
			 * if(payway.getDicId() != payv2PayWay.getDicId()){
			 * dictIdSet.add(Integer.valueOf(payway.getDicId().toString())); } }
			 * } //过滤掉使用过的第三方支付 for(SysConfigDictionary dictionary : dicList){
			 * if(!dictIdSet.contains(dictionary.getId())){
			 * dicList2.add(dictionary); } }
			 */
			andView.addObject("dicList", dicList);
		}
		return andView;
	}

	/**
	 * @Title: clonePayv2PayWayRateTc
	 * @Description:克隆支付通道路由弹窗
	 * @param map
	 * @return 设定文件
	 * @return ModelAndView 返回类型
	 * @date 2016年12月6日 上午11:21:22
	 * @throws
	 */
	@RequestMapping("clonePayv2PayWayRateTc")
	public ModelAndView clonePayv2PayWayRateTc(
			@RequestParam Map<String, Object> map) {
		ModelAndView andView = new ModelAndView(
				"payv2/paywayrate/payv2paywayrate-clone");
		Payv2PayWayRate payv2PayWay = payv2PayWayRateService.detail(map);
		andView.addObject("payv2PayWayRate", payv2PayWay);
		andView.addObject("curPage", map.get("curPage"));
		SysConfigDictionary sysConfigDictionary = new SysConfigDictionary();
		map.remove("id");
		map.put("dictPvalue", -1);
		map.put("dictName", "PAY_TYPE");
		sysConfigDictionary = sysConfigDictionaryService.detail(map);
		if (sysConfigDictionary != null) {
			map = new HashMap<String, Object>();
			map.put("dictPvalue", sysConfigDictionary.getId());
			List<SysConfigDictionary> dicList = sysConfigDictionaryService
					.query(map);
			/*
			 * List<SysConfigDictionary> dicList2 = new
			 * ArrayList<SysConfigDictionary>(); Set<Integer> dictIdSet = new
			 * HashSet<Integer>(); map.remove("dictPvalue"); map.put("wayType",
			 * 2); //查询已经使用过的第三方支付 List<Payv2PayWayRate> payv2PayWayList =
			 * payv2PayWayRateService.query(map); for(Payv2PayWayRate payway :
			 * payv2PayWayList){ if(payway.getDicId() != null &&
			 * !dictIdSet.contains
			 * (Integer.valueOf(payway.getDicId().toString()))){ //当前的支付方式不过滤
			 * if(payway.getDicId() != payv2PayWay.getDicId()){
			 * dictIdSet.add(Integer.valueOf(payway.getDicId().toString())); } }
			 * } //过滤掉使用过的第三方支付 for(SysConfigDictionary dictionary : dicList){
			 * if(!dictIdSet.contains(dictionary.getId())){
			 * dicList2.add(dictionary); } }
			 */
			andView.addObject("dicList", dicList);
		}
		return andView;
	}

	/**
	 * @Title: updatePayv2PayWayRate
	 * @Description: 修改支付渠道 
	 * @param map
	 * @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @date 2016年12月8日 上午9:45:03
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("updatePayv2PayWayRate")
	public Map<String, Object> updatePayv2PayWayRate(
			@RequestParam Map<String, Object> map) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			if (map.containsKey("id") ) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("dicId", map.get("dicId"));
				paramMap.put("companyName", map.get("companyName"));
				//paramMap.put("isDelete", 2);
				List<Payv2PayWayRate> list = payv2PayWayRateService
						.query(paramMap);
				// 判断该上游下面是否存在该公司主体
				boolean isExists = false;
				for (Payv2PayWayRate rate : list) {
					if(rate.getId().longValue() != Long.parseLong(map.get("id").toString())) {
						isExists = true;
					}
				}
				if (!isExists) {
					System.out.println(map);
					map.put("updateTime", new Date());
					map.put("updateUserBy", getAdmin().getId());
					payv2PayWayRateService.updatePayWayRate(map);
					returnMap.put("resultCode", 200);
				} else {
					returnMap.put("resultCode", 201);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMap;
	}
	
	/**
	 * @Title: batchUpdatePayv2PayWayRate
	 * @Description: 批量 开启/关闭/删除
	 * @param map 49,50,51,52  1/2/3
	 * @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @date 2016年12月8日 上午9:45:03
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("batchUpdatePayv2PayWayRate")
	public Map<String, Object> batchUpdatePayv2PayWayRate(
			@RequestParam Map<String, Object> map) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			if (map.containsKey("gids") && map.containsKey("type")) {
				String[] gid = map.get("gids").toString().split(",");
				if(gid.length > 0) {
					String type = map.get("type").toString();
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("ids", map.get("gids").toString());
					paramMap.put("updateTime", new Date());
					paramMap.put("updateUserBy", getAdmin().getId());
					switch (type) {
					case "1":
						// 关闭 ${optionName}=#{optionValue} where id in ${ids}
						paramMap.put("optionName", "status");
						paramMap.put("optionValue", "2");
						for (String string : gid) {
							RedisDBUtil.redisDao.setString("rate_disabled_"
									+ string, "1");
							RedisDBUtil.redisDao.expire(
									"rate_disabled_" + string,
									600);
						}
						break;
					case "2":
						// 删除
						paramMap.put("optionName", "is_delete");
						paramMap.put("optionValue", "1");
						for (String string : gid) {
							RedisDBUtil.redisDao.setString("rate_disabled_"
									+ string, "1");
							RedisDBUtil.redisDao.expire(
									"rate_disabled_" + string,
									600);
						}
						break;
					case "3":
						// 开启
						paramMap.put("optionName", "status");
						paramMap.put("optionValue", "1");
						for (String string : gid) {
							RedisDBUtil.redisDao.delete("rate_disabled_"
									+ string);
						}
						break;
					}
					payv2PayWayRateService.batchUpdate(paramMap);
					returnMap.put("resultCode", 200);
				} else {
					returnMap.put("resultCode", 201);
				}
			} else {
				returnMap.put("resultCode", 201);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMap;
	}
	
	@ResponseBody
	@RequestMapping("/exportExcelRate")
	public Map<String, Object> exportExcelOrder(@RequestParam Map<String, Object> map, HSSFWorkbook workbook, HttpServletResponse response) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<Payv2PayWayRateVO> export = payv2PayWayRateService.getExport(map);
		
		try {
			if (null != export && export.size() > 0) {
				// 导出
				CSVUtils<Object> csv = new CSVUtils<Object>();
				Object[] headers = { "通道名称(下游)", "通道名称(官方)", "支付方式", "上游通道", "主体公司", "录入人", "备注", "开户银行", "账号类型",
										"开户名称", "开户账号", "成本费率‰", "单笔限额", "每日限额", "到期类型", "到账日期", "状态"};
				List<Object> dataset = new ArrayList<Object>();
				for (Payv2PayWayRateVO vo : export) {
					dataset.add(vo);
				}
				OutputStream out = response.getOutputStream();
				String filename = "渠道列表" + new Date().getTime() + ".csv";
				filename = URLEncoder.encode(filename, "UTF-8");// 处理中文文件名
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition", "attachment;filename=" + filename);
				List<Object> headList = Arrays.asList(headers);
				File csvFile = csv.commonCSV(headList, dataset, null, filename);
				InputStream in = new FileInputStream(csvFile);
				int b;  
	            while((b=in.read())!= -1)  
	            {  
	                out.write(b);  
	            }  
	            in.close();
				out.close();
			} else {
				returnMap.put("status", -1);// 失败
			}
		} catch (Exception e) {
			logger.error("导出通道列表错误", e);
			e.printStackTrace();
		}
		return returnMap;
	}
	/**
	 * addWXgzhConfig 
	 * 添加平安微信公众号支付配置
	 * @param map
	 * 			id:支付通道ID
	 * 			ctt_code:合同号：此为平安银行下面的微信公众号配置合同号
	 * 			type:类型
	 * @return    设定文件 
	 * Map<String,Object>    返回类型
	 */
	@ResponseBody
	@RequestMapping("addWXgzhConfig")
	public Map<String, Object> addWXgzhConfig(@RequestParam Map<String, Object> map) {
		Map<String, Object> returnMap=new HashMap<String, Object>();
		Long id = Long.valueOf(map.get("id").toString());
		int type = Integer.valueOf(map.get("type").toString());
		Payv2PayWayRate payv2PayWayRate = payv2PayWayRateService.queryByid(id);
		if (payv2PayWayRate != null) {
			String ctt_code = map.get("ctt_code").toString();
			String sub_appid = null;
			String jsapi_path = null;
			if (type == 1) {
				sub_appid = payv2PayWayRate.getGzhAppId();
			}
			if (type == 2) {
				//授权目录
				jsapi_path = payv2PayWayRate.getGzhStr();
			}
			String OPEN_ID = payv2PayWayRate.getRateKey3();
			String OPEN_KEY = payv2PayWayRate.getRateKey4();
			String PRIVATE_KEY = payv2PayWayRate.getRateKey5();
			String PUBLICKEY = payv2PayWayRate.getRateKey6();
			returnMap=AddConfig.addConfig(ctt_code, sub_appid, jsapi_path, OPEN_ID, OPEN_KEY, PRIVATE_KEY, PUBLICKEY);
		}
		return returnMap;
	}
}
