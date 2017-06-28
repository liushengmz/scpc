package com.system.model;

import java.util.ArrayList;
import java.util.List;



public class SpcpProfitModel {
	public int spId;
	public int spTroneId;
	public String spFullName;
	public int spRowSpan = 1;
	
	public List<SpTroneModel> list = new ArrayList<SpcpProfitModel.SpTroneModel>();

	public class SpTroneModel
	{
		public int spTroneId;
		public String spTroneName;
		public double amount;
		public int spTroneRowSpan = 1;
		
		public List<CpModelData> list = new ArrayList<SpcpProfitModel.SpTroneModel.CpModelData>();
		
		public class CpModelData
		{
			public int cpId;
			public double payAmount;
			public String cpFullName;
			public String cpShortName;
			public int spTroneId;
		}
	}
}
