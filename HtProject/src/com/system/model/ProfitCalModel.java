package com.system.model;

import java.util.ArrayList;
import java.util.List;

public class ProfitCalModel
{
	public int spId;
	public String spName;
	public int spRowsCount;
	public float spMoney;
	public float spProfit;
	
	public List<SpTroneModel> spTroneList = new ArrayList<ProfitCalModel.SpTroneModel>();
	
	public class SpTroneModel
	{
		public int spTroneId;
		public String spTroneName;
		public int spTroneRowsCount;
		public float spTroneMoney;
		public float spTroneProfit;
		public List<CpSpTroneModel> cpSpTroneList = new ArrayList<ProfitCalModel.SpTroneModel.CpSpTroneModel>();
		
		public class CpSpTroneModel
		{
			public int cpId;
			public String cpName;
			public float cpMoney;
		}
	}
}
