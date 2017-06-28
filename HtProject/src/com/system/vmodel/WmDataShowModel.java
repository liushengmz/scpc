package com.system.vmodel;

import java.util.ArrayList;
import java.util.List;

public class WmDataShowModel
{
	public int spId;
	public String spName;
	public int spRowSpand = 1;
	
	public List<SpTroneModel> spTroneList = new ArrayList<WmDataShowModel.SpTroneModel>();
	
	public class SpTroneModel
	{
		public int spTroneId;
		public String spTroneName;
		public int spTroneRowSpand = 1;
		
		public List<TroneModel> troneList = new ArrayList<WmDataShowModel.SpTroneModel.TroneModel>();
		
		public class TroneModel
		{
			public int troneId;
			public String troneName;
			public float spPrice;
			public int troneRowSpand = 1;
			
			public List<TroneOrderModel> troneOrderList = new ArrayList<WmDataShowModel.SpTroneModel.TroneModel.TroneOrderModel>();
			
			public class TroneOrderModel
			{
				public int troneOrderId;
				public int cpId;
				public String cpName;
				public float cpPrice;
				public int dataRows;
				public int showDataRows;
				public double dataAmount;
				public double showDataAmount;
			}
			
		}
		
	}
}
