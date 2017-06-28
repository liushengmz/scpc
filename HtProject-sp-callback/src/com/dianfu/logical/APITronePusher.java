package com.dianfu.logical;

import java.util.HashMap;

import com.database.Dao.tbl_trone_orderDao;
import com.database.LightModel.tbl_api_orderItem;
import com.database.LightModel.tbl_mrItem;
import com.database.LightModel.tbl_sp_trone_apiItem;
import com.database.LightModel.tbl_trone_orderItem;
import com.dianfu.Interface.ICPPushModel.EPushField;
import com.shotgun.Tools.Funcs;

public class APITronePusher extends BasePusher {

	private tbl_sp_trone_apiItem _apiMatchAPI;
	private tbl_api_orderItem _apiOrder;

	@Override
	public boolean LoadCPAPI(int troneId) {

		// tbl_sp_trone_apiItem.GetRowByTroneId(dBase, TroneId);
		if (troneId == 0) {
			if (super.getTrone() != null)
				troneId = getTrone().get_id();
			else
				return SetError("Trone为空时，troneId 不能为0");
		}
		String sql = "select * from tbl_sp_trone_api where id in ( select trone_api_id from tbl_sp_trone where id in(select sp_trone_id from tbl_trone where id="
				+ Integer.toString(troneId) + ")) limit 1 ";
		tbl_sp_trone_apiItem m = new tbl_sp_trone_apiItem();
		if (!getDBase().sqlToModel(m, sql))
			return SetError("未关联API");
		_apiMatchAPI = m;
		return super.SetSuccess();
	}

	@Override
	public boolean doPush() {
		if (_apiMatchAPI == null)
			return SetError("请先调用LoadCPAPI");

		if (getPushObject().get_cp_id() > 0 && getPushObject().get_cp_id() != 34)
			return true; // 已经处理的数据

		tbl_api_orderItem apiOrder = new tbl_api_orderItem();
		String sql = "select * from ";
		if (!Funcs.isNullOrEmpty(tbl_api_orderItem.pfxSchame))
			sql += tbl_api_orderItem.pfxSchame + ".";
		sql += apiOrder.TableName();
		sql += " where status in(1011,1013,2023) and trone_id=" + Integer.toString(getTrone().get_id());

		switch (_apiMatchAPI.get_match_field()) {
		case 0: // splinkid
			sql += " and sp_linkid='" + getDBase().sqlEncode(getPushObject().GetValue(EPushField.LinkID)) + "'";
			break;
		case 1: // port + msg 动态指令
			sql += " and msg =" + getDBase().sqlEncode(getPushObject().GetValue(EPushField.Msg)) + "'";
			sql += " and port =" + getDBase().sqlEncode(getPushObject().GetValue(EPushField.port)) + "'";
			break;
		case 2: // 透参
			sql += " and api_exdata='" + getDBase().sqlEncode(getPushObject().GetValue(EPushField.cpParam)) + "'";
			break;
		case 3:// 同步指令与上行指令不一至时，使用“port,msg”拼接用逗号分隔，并在sp透传查找
			sql += String.format(" and api_exdata='%s,%s'",
					getDBase().sqlEncode(getPushObject().GetValue(EPushField.port)),
					getDBase().sqlEncode(getPushObject().GetValue(EPushField.Msg)));
			break;
		}

		if (!getDBase().sqlToModel(apiOrder, sql)) {
			return SetError("未匹配API订单号");
		}

		tbl_trone_orderItem cpTrone = tbl_trone_orderDao.queryById(getDBase(), apiOrder.get_trone_order_id());
		// tbl_trone_orderItem cpTrone = new tbl_trone_orderItem();
		// sql = "select * from tbl_trone_order where disable=0 and id= " +
		// apiOrder.get_trone_order_id();
		//
		// if (!getDBase().sqlToModel(cpTrone, sql)) {
		// return SetError("上量通道(CP业务)已经关闭");
		// }
		if (cpTrone == null || cpTrone.get_disable()) {
			return SetError("上量通道(CP业务)已经关闭");
		}

		SetConfig(cpTrone);// 找到对应的渠道上量(相当于执行 base.LoadCPAPI())
		if (getPushObject() instanceof tbl_mrItem) {
			((tbl_mrItem) getPushObject()).set_api_order_id(apiOrder.get_id());
		}
		_apiOrder = apiOrder;
		return super.doPush();
	}

	@Override
	protected void asyncSendQuery(String pfxUrl, HashMap<String, String> data) {
		data.put("cpparam", _apiOrder.get_ExtrData());
		data.put("paycode", String.format("1%05d", _apiOrder.get_trone_order_id()));
		super.asyncSendQuery(pfxUrl, data);
	}
}
