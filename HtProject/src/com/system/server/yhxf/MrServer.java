package com.system.server.yhxf;

import java.util.Map;

import com.system.dao.yhxf.MrDao;
import com.system.model.params.ReportParamsModel;

public class MrServer
{
	private MrDao dao = new MrDao();
	
	public Map<String, Object> loadShowData(ReportParamsModel params)
	{
		return dao.loadShowData(params.getStartDate(), params.getEndDate(),params.getSpId(), params.getCpId(), params.getOperatorId(),params.getShowType());
	}
}
