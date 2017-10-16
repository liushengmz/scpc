package com.system.flow.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.system.flow.dao.CpRatioDao;
import com.system.flow.model.CpRatioModel;
import com.system.util.StringUtil;

public class CpRatioServer
{
	private Logger logger = Logger.getLogger(CpRatioServer.class);
	
	public List<CpRatioModel> loadCpRatio(int cpId)
	{
		return new CpRatioDao().loadCpRatio(cpId);
	}
	
	public Map<Integer, List<CpRatioModel>> loadCpRatioList(int cpId)
	{
		return new CpRatioDao().loadCpRatioList(cpId);
	}
	
	public boolean updateCpRation(Map<String, String> params)
	{
		try
		{
			int cpId = StringUtil.getInteger(params.get("cp_id"), 0);
			
			List<CpRatioModel> oriRatio = loadCpRatio(cpId);
			
			final List<CpRatioModel> updateRatio = new ArrayList<CpRatioModel>();
			
			String data = params.get("pro_ratio_check");
			
			for(String oriData : data.split(","))
			{
				String[] rData = oriData.split("\\|");
				
				int sCpId = StringUtil.getInteger(rData[0], 0);
				
				if(sCpId<=0)
					continue;
				
				for(CpRatioModel model : oriRatio)
				{
					if(model.getId()==sCpId)
					{
						CpRatioModel tModel = new CpRatioModel();
						tModel.setId(sCpId);
						tModel.setRatio(StringUtil.getInteger(rData[1], 0));
						tModel.setStatus("true".equalsIgnoreCase(rData[2]) ?  1 : 0);
						
						if(!model.equals(tModel))
						{
							updateRatio.add(tModel);
						}
						
						break;
					}
				}
			}
			
			if(updateRatio.size()>0)
			{
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						new CpRatioDao().updateCpRatioList(updateRatio);
					}
				}).start();
			}
			
			return true;
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage());
		}
		
		return false;
	}
}
