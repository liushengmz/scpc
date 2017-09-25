package com.system.flow.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.system.flow.dao.SpTroneDao;
import com.system.flow.dao.TroneDao;
import com.system.flow.model.SpTroneModel;
import com.system.flow.model.TroneModel;
import com.system.server.ProvinceServer;
import com.system.util.StringUtil;

public class SpTroneServer
{
	Logger logger = Logger.getLogger(SpTroneServer.class);
	
	public SpTroneModel getSpTroneById(int id)
	{
		return new SpTroneDao().getSpTroneById(id);
	}
	
	public boolean addSpTrone(Map<String, String> params)
	{
		SpTroneModel spTroneModel = analySpTroneFromMap(params);
		
		if(spTroneModel==null)
			return false;
		
		int spTroneId = new SpTroneDao().addSpTrone(spTroneModel);
		
		new TroneDao().addTroneList(spTroneId, spTroneModel.getTroneList());
		
		return true;
	}
	
	public boolean updateSpTrone(Map<String, String> params)
	{
		SpTroneModel spTroneModel = analySpTroneFromMap(params);
		
		if(spTroneModel==null)
			return false;
		
		List<TroneModel> oriTroneList = new TroneServer().loadTroneBySpTroneId(spTroneModel.getId());
		
		List<TroneModel> newTroneList = spTroneModel.getTroneList();
		
		if(newTroneList==null || newTroneList.isEmpty())
			return false;
		
		new SpTroneDao().updateSpTrone(spTroneModel);
		
		final List<TroneModel> updateTroneList = new ArrayList<TroneModel>();
		
		for(TroneModel troneModel : oriTroneList)
		{
			for(TroneModel model : newTroneList)
			{
				if(troneModel.getId()==model.getId())
				{
					if(!troneModel.equals(model))
						updateTroneList.add(model);
					
					break;
				}
			}
		}
		
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				new TroneDao().updateTroneList(updateTroneList);
			}
		}).start();
		
		return true;
	}
	
	private SpTroneModel analySpTroneFromMap(Map<String, String> params)
	{
		try
		{
			int spTroneId = StringUtil.getInteger(params.get("sp_trone_id"), 0);
			int spId= StringUtil.getInteger(params.get("sp_id"), 0);
			int spApiId = StringUtil.getInteger(params.get("sp_api_id"),0);
			String spTronerName = params.get("sp_trone_name");
			int basePriceId = StringUtil.getInteger(params.get("base_price_id"), 0);
			int sendSms = StringUtil.getInteger(params.get("send_sms"), 0);
			int status = StringUtil.getInteger(params.get("status"), 0);
			int ratio = StringUtil.getInteger(params.get("ratio"), 0);
			int flowTypeId = StringUtil.getInteger(params.get("flow_type"), 0);
			String remark = StringUtil.getString(params.get("remark"), "");
			String proRatioStatusList = StringUtil.getString(params.get("pro_ratio_check"), "");
			String proNames = "";
			List<TroneModel> list = new ArrayList<TroneModel>();
			List<Integer> idList = new ArrayList<Integer>();
			String[] strs = proRatioStatusList.split(",");
			if(strs!=null)
			{
				for(String str : strs)
				{
					String[] data = str.split("\\|");
					if(data!=null)
					{
						TroneModel model = new TroneModel();
						model.setId(StringUtil.getInteger(data[0], 0));
						model.setProId(StringUtil.getInteger(data[1], 0));
						model.setRatio(StringUtil.getInteger(data[2], 0));
						model.setSpTroneId(spTroneId);
						model.setStatus("true".equalsIgnoreCase(data[3]) ? 1 : 0);
						list.add(model);
						if(model.getStatus()==1)
							idList.add(model.getProId());
					}
				}
			}
			
			if(spId<=0 || spApiId<=0 || basePriceId<=0 || list.isEmpty())
				return null;
			
			List<String> proNameList = new ProvinceServer().loadNamesByIds(idList);
			
			for(String s : proNameList)
				proNames +=  s + ",";
			
			proNames = proNames.substring(0, proNames.length()-1);
			
			SpTroneModel model = new SpTroneModel();
			
			model.setId(spTroneId);
			model.setSpId(spId);
			model.setSpApiId(spApiId);
			model.setSpTroneName(spTronerName);
			model.setRemark(remark);
			model.setSendSms(sendSms);
			model.setPriceId(basePriceId);
			model.setRatio(ratio);
			model.setStatus(status);
			model.setProNames(proNames);
			model.setFlowTypeId(flowTypeId);
			model.setTroneList(list);
			
			return model;
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage());
		}
		
		return null;
	}
	
	public Map<String, Object> loadSpTrone(int pageIndex, String keyWord)
	{
		return new SpTroneDao().loadSpTrone(pageIndex, keyWord);
	}
	
}
