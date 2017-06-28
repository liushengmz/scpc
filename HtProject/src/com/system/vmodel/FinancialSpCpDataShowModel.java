package com.system.vmodel;

import java.util.ArrayList;
import java.util.List;

public class FinancialSpCpDataShowModel
{
	public int spId;
	public String spFullName;
	public String spShortName;
	public int spRowSpan = 1;
	
	public List<SpTroneModel> list = new ArrayList<FinancialSpCpDataShowModel.SpTroneModel>();

	public class SpTroneModel
	{
		public int spTroneId;
		public String productName;
		public String productLineName;//产品线
		public String spContactName;
		public String spTroneName;
		public double spJieSuanLv;
		public int spTroneRowSpan = 1;
		
		public List<CpModelData> list = new ArrayList<FinancialSpCpDataShowModel.SpTroneModel.CpModelData>();
		
		public class CpModelData
		{
			public int dataRows;
			public double amount;
			public int showDataRows;
			public double showAmount;
			public int cpId;
			public String cpShortName;
			public String cpFullName;
			public double cpJieSuanLv;
		}
	}
}
