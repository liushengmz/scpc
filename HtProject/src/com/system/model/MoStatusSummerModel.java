package com.system.model;

import java.util.List;

public class MoStatusSummerModel
{
	public int spId;
	public int spTroneId;
	public int troneId;
	public float price;
	public String spName;
	public String spTroneName;
	public String troneName;
	public String troneConfigOrders;
	public String troneNum;
	public int troneIdRows;
	public List<TroneOrderDataModel> troneOrderList;
	public int troneRowSpand;
	
	public class TroneOrderDataModel
	{
		public int troneOrderId;
		public int cpId;
		public String troneOrders;
		public String cpName;
		public int troneOrderIdRows;
		public List<TroneOrderStatusDataModel> troneOrderStatusList;
		public int troneOrderRowSpand;
		
		public class TroneOrderStatusDataModel
		{
			public String status;
			public int statusRows;
		}
	}
	
}
