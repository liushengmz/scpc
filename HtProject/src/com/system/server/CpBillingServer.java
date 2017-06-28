package com.system.server;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.system.dao.CpBillingDao;
import com.system.dao.SpBillingDao;
import com.system.excel.ExcelManager;
import com.system.model.CpBillExportModel;
import com.system.model.CpBillingModel;
import com.system.model.CpBillingSpTroneModel;
import com.system.model.CpBillingTroneOrderDetailModel;
import com.system.model.CpExportDetailModel;
import com.system.model.CpSpTroneRateModel;
import com.system.model.ExportDetailModel;
import com.system.model.SettleAccountModel;
import com.system.model.SpBillExportModel;
import com.system.util.ConfigManager;

public class CpBillingServer
{
	/**
	 * 开始CP对帐,从原始的CP_MR表里面获取帐单数据到帐单表，数据一旦对帐和 付款完成，将对数据进行封存
	 * 	 
	 *  
	 * @param cpId
	 * @param jsType
	 * @param startDate
	 * @param endDate
	 */
	private void startExportCpBilling(int cpId,int jsType,String startDate,String endDate)
	{
		List<CpSpTroneRateModel> spTroneRateList = new CpSpTroneRateServer().loadCpSpTroneRateList(cpId, jsType, startDate, endDate);
		CpBillingDao dao = new CpBillingDao();
		List<CpBillingTroneOrderDetailModel> orderDetailList = dao.loadCpBillingTroneOrderDetailOri(cpId, jsType, startDate, endDate);
		//List<CpBillingSptroneDetailModel> spTroneDetailList = dao.loadCpBillingSpTroneDetailOri(cpId, jsType, startDate, endDate);
		List<CpBillingSpTroneModel> spTroneList = dao.loadCpBillingSpTroneOri(cpId, jsType, startDate, endDate);
		
		float preBilling = 0;
		
		float amount = 0;
		
		//因为特殊结算率的数据还是比较少的，所以以这个开头来更新结算率，会减少很多循环
		for(CpSpTroneRateModel rateModel : spTroneRateList)
		{
			for(CpBillingSpTroneModel model : spTroneList)
			{
				if(model.getSpTroneId() == rateModel.getSpTroneId() 
						&& model.getStartDate().equalsIgnoreCase(rateModel.getStartDate())
						&& model.getEndDate().equalsIgnoreCase(rateModel.getEndDate()))
				{
					model.setRate(rateModel.getRate());
				}
			}
		}
		
		for(CpBillingSpTroneModel model : spTroneList)
		{
			amount += model.getAmount();
			preBilling += model.getAmount()*model.getRate();
		}
		
		int billingId = dao.addCpBilling(cpId, jsType, startDate, endDate, preBilling,amount);
		
		dao.addCpBillingSpTroneData(spTroneList, billingId);
		
		dao.addCpBillingTroneOrderDetailData(orderDetailList, billingId);
	}
	
	public CpBillingModel getCpBillingModel(int id)
	{
		return new CpBillingDao().getCpBillingModel(id);
	}
	
	public Map<String, Object> loadCpBilling(String startDate, String endDate,
			int cpId,int jsType,int status,int pageIndex)
	{
		return new CpBillingDao().loadCpBilling(startDate, endDate, cpId,jsType,status,pageIndex);
	}
	
	public List<SettleAccountModel> exportExcelData(int cpBillingId)
	{
		return new CpBillingDao().exportExcelData(cpBillingId);
	}
	
	public boolean exportCpBilling(final int cpId,final int jsType,final String startDate,final String endDate)
	{
		CpBillingDao dao = new CpBillingDao();
		
		if(dao.isCpBillingCross(cpId, jsType, startDate, endDate))
		{
			return false;
		}
		
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				startExportCpBilling(cpId, jsType, startDate, endDate);
			}
		}).start();
		
		return true;
	}
	
	public void updateCpBillingActurePay(int cpBillingId,float money)
	{
		new CpBillingDao().updateCpBillingActurePay(cpBillingId, money);
	}
	
	public void delCpBilling(int cpBillingId)
	{
		new CpBillingDao().delCpBilling(cpBillingId);
	}
	
	/**
	 * 更新指定帐单的状态
	 * @param cpBillingId
	 * @param status
	 */
	public void updateCpBillingStatus(int cpBillingId,int status)
	{
		new CpBillingDao().updateCpBillingStatus(cpBillingId, status);
	}
	
	
	/**
	 * 重新生成指定帐单的数据
	 * @param cpBillingId
	 */
	public void reExportCpBillint(int cpBillingId)
	{
		CpBillingDao dao = new CpBillingDao();
		CpBillingModel model = dao.getCpBillingModel(cpBillingId);
		dao.delCpBilling(cpBillingId);
		startExportCpBilling(model.getCpId(),model.getJsType(),model.getStartDate(),model.getEndDate());
	}
	
	/**
	 * 调整CP对应的业务数据后，重新进行计算信息费、核减费用、应结费用等
	 * @param cpBillingId
	 */
	public void updateCpBilling(int cpBillingId)
	{
		new CpBillingDao().updateCpBilling(cpBillingId);
	}
	//更新CpBillingModel
	public void updateCpBillingModel(int id, int type,int status,String date,float cpkaipiaoBilling){
		new CpBillingDao().updateCpBillingModel(id,type,status,date,cpkaipiaoBilling);
	}
	
	public void updateCpBillingActurePay(int cpBillingId,float money,String date)
	{
		new CpBillingDao().updateCpBillingActurePay(cpBillingId, money,date);
	}
	
	/**
	 * 获取账单数据
	 * @param startDate
	 * @param endDate
	 * @param spId
	 * @param jsTypes
	 * @param status
	 * @return
	 */
	public List<CpBillExportModel> exportExcelData(String startDate,String endDate,int cpId,String jsTypes,String status)
	{
		return new CpBillingDao().exportExcelData(startDate, endDate, cpId, jsTypes, status);
	} 
	/**
	 * 导出账单数据
	 * @param channelType
	 * @param dateType
	 * @param channelName
	 * @param startDate
	 * @param endDate
	 * @param list
	 * @param os
	 */
	public void exportSettleAccount(String startDate,String endDate,List<CpBillExportModel> list,OutputStream os)
	{
		String date = getDateFormat(startDate,endDate);
		String filePath =ConfigManager.getConfigData("EXCEL_DEMO")+"Finalce-CP-Demo.xls";
		Map<Integer,Map<String,Object>> map=exportDataHandle(list);
		new ExcelManager().writeCpBillDataToExcel(date, map, filePath, os);
	}
	
	private String getDateFormat(String startDate,String endDate)
	{
		String date = startDate + ":" + endDate;
		try
		{
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
			return sdf2.format(sdf1.parse(startDate)) + "-" + sdf2.format(sdf1.parse(endDate));
		}
		catch(Exception ex)
		{
			
		}
		return date;
	}
	
	@SuppressWarnings("unchecked")
	public Map<Integer,Map<String,Object>>exportDataHandle(List<CpBillExportModel> list){
		LinkedHashMap<Integer,Map<String,Object>> maps=new LinkedHashMap<Integer, Map<String,Object>>();
		List<CpExportDetailModel> tempList=null;
		Map<String,Object> tempMap=null;
		float preBilling=0;
//		float kaipiaoAmount=0; 
		for(CpBillExportModel billExportModel:list){
			if(maps.containsKey(billExportModel.getBillId())){
				tempMap=maps.get(billExportModel.getBillId());
				tempList=(List<CpExportDetailModel>)tempMap.get("list");
				//票帐金额和开票金额
				preBilling=(Float)tempMap.get("preBilling");
//				kaipiaoAmount=(Float)tempMap.get("kaipiaoAmount");
				CpExportDetailModel detailModel=new CpExportDetailModel();
				detailModel.setProductName(billExportModel.getProductName());
				detailModel.setSpTroneName(billExportModel.getSpTroneName());
				detailModel.setRate(billExportModel.getRate());
				detailModel.setAmount(billExportModel.getAmount());
				detailModel.setActureAmount(billExportModel.getActureAmount());
				detailModel.setReduceAmount(billExportModel.getReduceAmount());
				detailModel.setReduceType(billExportModel.getReduceType());
				detailModel.setSpTroneBillAmount(billExportModel.getSpTroneBillAmount());
				preBilling+=billExportModel.getActureAmount();
//				kaipiaoAmount+=billExportModel.getActureAmount();
				tempList.add(detailModel);
				tempMap.put("list", tempList);
				tempMap.put("preBilling", preBilling);
//				tempMap.put("kaipiaoAmount", kaipiaoAmount);
				maps.put(billExportModel.getBillId(), tempMap);
			}else{
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("billMonth",billExportModel.getBillMonth());
				map.put("jsName",billExportModel.getJsName());
				map.put("startDate", billExportModel.getStartDate());
				map.put("endDate", billExportModel.getEndDate());
				map.put("nickName", billExportModel.getNickName());
				map.put("cpFullNam", billExportModel.getCpFullNam());
				List<CpExportDetailModel> delist=new ArrayList<CpExportDetailModel>();
				CpExportDetailModel detailModel=new CpExportDetailModel();
				detailModel.setProductName(billExportModel.getProductName());
				detailModel.setSpTroneName(billExportModel.getSpTroneName());
				detailModel.setRate(billExportModel.getRate());
				detailModel.setAmount(billExportModel.getAmount());
				detailModel.setActureAmount(billExportModel.getActureAmount());
				detailModel.setReduceAmount(billExportModel.getReduceAmount());
				detailModel.setReduceType(billExportModel.getReduceType());
				detailModel.setSpTroneBillAmount(billExportModel.getSpTroneBillAmount());
				delist.add(detailModel);
				map.put("list", delist);
				map.put("preBilling", billExportModel.getActureAmount());
				map.put("billingDate", billExportModel.getBillingDate());
				map.put("kaipiaoAmount", billExportModel.getKaipiaoAmount());
				map.put("getbillDate", billExportModel.getGetbillDate());
				map.put("applyPayBillDate", billExportModel.getApplyPayBillDate());
				map.put("payTime", billExportModel.getPayTime());
				map.put("actureBilling", billExportModel.getActureBilling());
				map.put("status", billExportModel.getStatus());
				map.put("statusName", billExportModel.getStatusName());
				maps.put(billExportModel.getBillId(), map);
			}
		}
		return maps;
	}
}
