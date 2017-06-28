package com.system.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.system.dao.BaseDataDao;
import com.system.model.CpModel;
import com.system.model.CpSpTroneModel;
import com.system.model.SpModel;
import com.system.model.SpTroneModel;
import com.system.model.TroneModel;

public class BaseDataAnalyServer
{
	Logger logger = Logger.getLogger(BaseDataAnalyServer.class);
	
	public void startAnalyBaseData()
	{
		//analyBaseSpData();
		//analyBaseCpData();
		analyBaseSpTroneData();
		analyBaseTroneData();
		analyCpSpTroneData();
	}
	
	private void analyBaseSpData()
	{
		BaseDataDao dao = new BaseDataDao();
		
		SpModel oriModel = null;
		SpModel descModel = null;
		
		List<SpModel> addList = new ArrayList<SpModel>();
		List<SpModel> updateList = new ArrayList<SpModel>();
		
		for(int i=3; i<=3; i++)
		{
			Map<Integer, SpModel> oriSource = dao.loadOriSpData(i);
			Map<Integer, SpModel> descSource = dao.loadDescSpData(i);
			
			addList.clear();
			updateList.clear();
			
			for(int spId : oriSource.keySet())
			{
				oriModel = oriSource.get(spId);
				descModel = descSource.get(spId);
				
				if(descModel==null)
				{
					addList.add(oriModel);
				}
				else
				{
					if(!oriModel.equals(descModel))
					{
						updateList.add(oriModel);
					}
				}
			}
			
			if(!addList.isEmpty())
			{
				logger.info("增加SP数据:" + i + "->size:" + addList.size());
				dao.addSpData(i, addList);
			}
			
			if(!updateList.isEmpty())
			{
				logger.info("更新SP数据:" + i + "->size:" + updateList.size());
				dao.updateSpData(i, updateList);
			}
		}
	}
	
	private void analyBaseCpData()
	{
		BaseDataDao dao = new BaseDataDao();
		
		CpModel oriModel = null;
		CpModel descModel = null;
		
		List<CpModel> addList = new ArrayList<CpModel>();
		List<CpModel> updateList = new ArrayList<CpModel>();
		
		for(int i=1; i<=4; i++)
		{
			Map<Integer, CpModel> oriSource = dao.loadOriCpData(i);
			Map<Integer, CpModel> descSource = dao.loadDescCpData(i);
			
			addList.clear();
			updateList.clear();
			
			for(int cpId : oriSource.keySet())
			{
				oriModel = oriSource.get(cpId);
				descModel = descSource.get(cpId);
				
				if(descModel==null)
				{
					addList.add(oriModel);
				}
				else
				{
					if(!oriModel.equals(descModel))
					{
						updateList.add(oriModel);
					}
				}
			}
			
			if(!addList.isEmpty())
			{
				logger.info("增加CP数据:" + i + "->size:" + addList.size());
				dao.addCpData(i, addList);
			}
			
			if(!updateList.isEmpty())
			{
				logger.info("更新CP数据:" + i + "->size:" + updateList.size());
				dao.updateCpData(i, updateList);
			}
		}
	}
	
	private void analyBaseSpTroneData()
	{
		BaseDataDao dao = new BaseDataDao();
		
		SpTroneModel oriModel = null;
		SpTroneModel descModel = null;
		
		List<SpTroneModel> addList = new ArrayList<SpTroneModel>();
		List<SpTroneModel> updateList = new ArrayList<SpTroneModel>();
		
		for(int i=1; i<=4; i++)
		{
			Map<Integer, SpTroneModel> oriSource = dao.loadOriSpTroneData(i);
			Map<Integer, SpTroneModel> descSource = dao.loadDescSpTroneData(i);
			
			addList.clear();
			updateList.clear();
			
			for(int cpId : oriSource.keySet())
			{
				oriModel = oriSource.get(cpId);
				descModel = descSource.get(cpId);
				
				if(descModel==null)
				{
					addList.add(oriModel);
				}
				else
				{
					if(!oriModel.equals(descModel))
					{
						updateList.add(oriModel);
					}
				}
			}
			
			if(!addList.isEmpty())
			{
				logger.info("增加SpTrone数据:" + i + "->size:" + addList.size());
				dao.addSpTroneData(i, addList);
			}
			
			if(!updateList.isEmpty())
			{
				logger.info("更新SpTrone数据:" + i + "->size:" + updateList.size());
				dao.updateSpTroneData(i, updateList);
			}
		}
	}
	
	private void analyBaseTroneData()
	{
		BaseDataDao dao = new BaseDataDao();
		
		TroneModel oriModel = null;
		TroneModel descModel = null;
		
		List<TroneModel> addList = new ArrayList<TroneModel>();
		List<TroneModel> updateList = new ArrayList<TroneModel>();
		
		for(int i=1; i<=4; i++)
		{
			Map<Integer, TroneModel> oriSource = dao.loadOriTroneData(i);
			Map<Integer, TroneModel> descSource = dao.loadDescTroneData(i);
			
			addList.clear();
			updateList.clear();
			
			for(int cpId : oriSource.keySet())
			{
				oriModel = oriSource.get(cpId);
				descModel = descSource.get(cpId);
				
				if(descModel==null)
				{
					addList.add(oriModel);
				}
				else
				{
					if(!oriModel.equals(descModel))
					{
						updateList.add(oriModel);
					}
				}
			}
			
			if(!addList.isEmpty())
			{
				logger.info("增加Trone数据:" + i + "->size:" + addList.size());
				dao.addTroneData(i, addList);
			}
			
			if(!updateList.isEmpty())
			{
				logger.info("更新Trone数据:" + i + "->size:" + updateList.size());
				dao.updateTroneData(i, updateList);
			}
		}
	}
	
	private void analyCpSpTroneData()
	{
		BaseDataDao dao = new BaseDataDao();
		
		CpSpTroneModel oriModel = null;
		CpSpTroneModel descModel = null;
		
		List<CpSpTroneModel> addList = new ArrayList<CpSpTroneModel>();
		List<CpSpTroneModel> updateList = new ArrayList<CpSpTroneModel>();
		
		for(int i=1; i<=4; i++)
		{
			Map<Integer, CpSpTroneModel> oriSource = dao.loadOriCpSpTroneData(i);
			Map<Integer, CpSpTroneModel> descSource = dao.loadDescCpSpTroneData(i);
			
			addList.clear();
			updateList.clear();
			
			for(int cpId : oriSource.keySet())
			{
				oriModel = oriSource.get(cpId);
				descModel = descSource.get(cpId);
				
				if(descModel==null)
				{
					addList.add(oriModel);
				}
				else
				{
					if(!oriModel.equals(descModel))
					{
						updateList.add(oriModel);
					}
				}
			}
			
			if(!addList.isEmpty())
			{
				logger.info("增加CpSpTrone数据:" + i + "->size:" + addList.size());
				dao.addCpSpTroneData(i, addList);
			}
			
			if(!updateList.isEmpty())
			{
				logger.info("更新CpSpTrone数据:" + i + "->size:" + updateList.size());
				dao.updateCpSpTroneData(i, updateList);
			}
		}
	}
	
	
	public static void main(String[] args)
	{
		new BaseDataAnalyServer().startAnalyBaseData();
	}
}
