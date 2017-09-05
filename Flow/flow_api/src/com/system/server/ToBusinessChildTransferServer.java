package com.system.server;

import java.util.List;

import org.apache.log4j.Logger;

import com.system.model.ToBusinessChildOrderModel;

/**
 * 纯粹中转类，把数据中转到子定单处理流程
 * @author Andy.Chen
 *
 */
public class ToBusinessChildTransferServer implements Runnable
{
	Logger logger = Logger.getLogger(ToBusinessChildTransferServer.class);
	
	private List<ToBusinessChildOrderModel> _childList = null;
	
	private int _cpId = 0;
	
	public void setCpId(int cpId)
	{
		this._cpId = cpId;
	}
	
	public void setChildList(List<ToBusinessChildOrderModel> childList)
	{
		this._childList = childList;
	}
	
	@Override
	public void run()
	{
		if(_childList==null || _childList.isEmpty())
		{
			logger.info("childList Empty");
			return;
		}
		
		for(ToBusinessChildOrderModel model : _childList)
		{
			ToBusinessChildServerV1.handleToBusinessChild(model, _cpId);
		}
	}
	
	

}
