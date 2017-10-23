package com.pay.manger.controller.payv2;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.core.teamwork.base.util.ObjectUtil;
import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.payv2.entity.Payv2CompanyApply;
import com.pay.business.payv2.entity.Payv2CompanyApplyBean;
import com.pay.business.payv2.mapper.Payv2CompanyApplyMapper;
import com.pay.business.payv2.service.Payv2CompanyApplyService;
import com.pay.business.util.ParameterEunm;
import com.pay.manger.ExportExcel.TestExportExcel;
import com.pay.manger.controller.admin.BaseManagerController;

/**
* @ClassName: Payv2CompanyApplyController 
* @Description:商户预留信息
* @author qiuguojie
* @date 2017年3月31日 下午2:36:52
*/
@Controller
@RequestMapping("/payv2CompanyApply/*")
public class Payv2CompanyApplyController extends BaseManagerController<Payv2CompanyApply, Payv2CompanyApplyMapper>{
	private static final Logger logger = Logger.getLogger(Payv2CompanyApplyController.class);
	@Autowired
	private Payv2CompanyApplyService payv2CompanyApplyService;
	
	/**
	* @Title: applyList 
	* @Description: 获取客户信息列表
	* @param @param map
	* @param @return    设定文件 
	* @return ModelAndView    返回类型 
	* @throws
	*/
	@RequestMapping("applyList")
	public ModelAndView applyList(@RequestParam Map<String, Object> map) {
		ModelAndView av = new ModelAndView("payv2/companyApply/payv2_company_apply_list");
		PageObject<Payv2CompanyApply> pageList = payv2CompanyApplyService.Pagequery(map);
		av.addObject("list", pageList);
		av.addObject("map", map);
		return av;
	}

	
	/**
	 * 官网联系我们接口
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("addContactCompanyApply")
	public Map<String,Object> addContactCompanyApply(@RequestParam Map<String,Object> map,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap=new HashMap<String, Object>();
		boolean con = ObjectUtil.checkObjectFile(new String[] { "companyName"
				,"contactsName","contactsPhone","email" }, map);
		try {
			if(con){
				map.put("sourceType", 3);
				map.put("createTime", new Date());
				payv2CompanyApplyService.add(map);
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
			}else{
				resultMap = ReMessage.resultBack(ParameterEunm.ERROR_PARAMS_CODE,null,"缺失参数！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null);
		}
		return resultMap;
	}
	
	/**
	 * 官网申请账号接口
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("addCompanyApply")
	public Map<String,Object> addCompanyApply(@RequestParam Map<String,Object> map,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap=new HashMap<String, Object>();
		boolean con = ObjectUtil.checkObjectFile(new String[] { "companyName"
				,"contactsName","contactsPhone","email" }, map);
		try {
			if(con){
				map.put("sourceType", 2);
				map.put("createTime", new Date());
				payv2CompanyApplyService.add(map);
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
			}else{
				resultMap = ReMessage.resultBack(ParameterEunm.ERROR_PARAMS_CODE,null,"缺失参数！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null);
		}
		return resultMap;
	}
	
	/**
	 * @Title: exportExcelApply
	 * @Description: 导出客户信息Excel
	 * @param @param map
	 * @param @param workbook
	 * @param @param request
	 * @param @param response
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("exportExcelApply")
	public Map<String, Object> exportExcelApply(@RequestParam Map<String, Object> map, HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<Payv2CompanyApply> applyList = payv2CompanyApplyService.query(map);
		if (applyList.size() > 0) {
			try {
				// 导出
				TestExportExcel<Payv2CompanyApplyBean> ex = new TestExportExcel<Payv2CompanyApplyBean>();
				String[] headers = { "ID", "信息类型", "公司名字", "联系人", "联系电话", "联系邮箱", "创建时间" };
				List<Payv2CompanyApplyBean> dataset = new ArrayList<Payv2CompanyApplyBean>();
				for (Payv2CompanyApply payv2CompanyApply : applyList) {
					Payv2CompanyApplyBean bte = new Payv2CompanyApplyBean();
					// ID
					bte.setId(payv2CompanyApply.getId());
					// 信息类型
					if (payv2CompanyApply.getSourceType() == 1) {
						bte.setSourceType("商户app申请账号");
					} else if (payv2CompanyApply.getSourceType() == 2) {
						bte.setSourceType("官网申请账号");
					} else if (payv2CompanyApply.getSourceType() == 3) {
						bte.setSourceType("官网联系我们");
					}
					// 公司名字
					bte.setCompanyName(payv2CompanyApply.getCompanyName());
					// 联系人
					bte.setContactsName(payv2CompanyApply.getContactsName());
					// 联系电话
					bte.setContactsPhone(payv2CompanyApply.getContactsPhone());
					// 联系邮箱
					bte.setEmail(payv2CompanyApply.getEmail());
					// 创建时间
					bte.setCreateTime(payv2CompanyApply.getCreateTime());
					dataset.add(bte);
				}
				OutputStream out = response.getOutputStream();
				String filename = "客户信息" + new Date().getTime() + ".xls";// 设置下载时客户端Excel的名称
				filename = URLEncoder.encode(filename, "UTF-8");// 处理中文文件名
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition", "attachment;filename=" + filename);
				ex.exportExcel(headers, dataset, out);
				out.close();
			} catch (IOException e) {
				logger.error("导出客户信息Excel失败", e);
				e.printStackTrace();
				returnMap.put("status", -1);// 失败
			}
		}
		return returnMap;
	}

	/**
	 * @Title: delApply
	 * @Description: 删除
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("delApply")
	public Map<String, Object> delApply(@RequestParam Map<String, Object> map) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			Payv2CompanyApply payv2CompanyApply = new Payv2CompanyApply();
			payv2CompanyApply.setId(Long.valueOf(map.get("id").toString()));
			payv2CompanyApplyService.delete(payv2CompanyApply);
			returnMap.put("resultCode", 200);
		} catch (Throwable e) {
			logger.error("删除失败", e);
			e.printStackTrace();
		}
		return returnMap;
	}
	/**
	* @Title: updateApplyGoTO 
	* @Description: 修改页面跳转
	* @param @param map
	* @param @return    设定文件 
	* @return ModelAndView    返回类型 
	* @throws
	*/
	@RequestMapping("updateApplyGoTO")
	public ModelAndView updateApplyGoTO(@RequestParam Map<String,Object> map){
		ModelAndView av = new ModelAndView("payv2/companyApply/payv2_company_apply_edit");
		Payv2CompanyApply	payv2CompanyApply=	payv2CompanyApplyService.detail(map);
		av.addObject("obj", payv2CompanyApply);
		av.addObject("map",map);
		return av;
	}
	/**
	* @Title: updateApplySubmit 
	* @Description: 修改数据提交
	* @param @param map
	* @param @return    设定文件 
	* @return Map<String,Object>    返回类型 
	* @throws
	*/
	@ResponseBody
	@RequestMapping("updateApplySubmit")
	public Map<String, Object> updateApplySubmit(@RequestParam Map<String, Object> map) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			payv2CompanyApplyService.update(map);
			returnMap.put("resultCode", 200);
		} catch (Throwable e) {
			logger.error("删除失败", e);
			e.printStackTrace();
		}
		return returnMap;
	}
}
