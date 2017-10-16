package com.system.server;

public class PhoneChargeListServer
{
	public void loadChargeList(int channelChildId,String phoneModel)
	{
		//1、验证channelChildId
		//2、验证父渠道
		//3、验证手机号及省份和运营商
		//4、根据3去tbl_f_trone里找出所有符合条件的trone_id
		//5、根据2和3找出配置的trone_id
		//6、根据1和3找出channelChildId配置的trone_id(有符合条件的)
		//7、根据5和6找出trone_id集合
		
	}
}
