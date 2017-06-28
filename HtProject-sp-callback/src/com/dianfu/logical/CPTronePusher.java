package com.dianfu.logical;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.database.Dao.tbl_trone_orderDao;
import com.database.LightModel.tbl_trone_orderItem;
import com.dianfu.Interface.ICPPushModel.EPushField;
import com.shotgun.Tools.Funcs;

public class CPTronePusher extends BasePusher {

	private ArrayList<tbl_trone_orderItem> _allCfg;

	@Override
	public boolean LoadCPAPI(int trondId) {
		// var l = LightDataModel.tbl_trone_orderItem.GetQueries(dBase);
		// l.Filter.AndFilters.Add(LightDataModel.tbl_trone_orderItem.Fields.trone_id,
		// TroneId);
		// l.Filter.AndFilters.Add(LightDataModel.tbl_trone_orderItem.Fields.disable,
		// 0);
		// l.PageSize = int.MaxValue;
		if (trondId == 0) {
			if (getTrone() == null)
				return SetError("trondId 不能为空");
			trondId = getTrone().get_id();
		}

		// String sql = "select * from tbl_trone_order where disable=0 and
		// trone_id=" + Integer.toString(trondId);
		// _allCfg = super.getDBase().sqlToModels(new tbl_trone_orderItem(),
		// sql);
		_allCfg = tbl_trone_orderDao.queryByTroneId(getDBase(), trondId);

		if (_allCfg == null || _allCfg.size() == 0) {
			tbl_trone_orderItem m = createDefaultTrone();
			_allCfg = new ArrayList<tbl_trone_orderItem>();
			_allCfg.add(m);
		}

		// base.SetConfig(_allCfg[0]);
		return super.SetSuccess();

	}

	private tbl_trone_orderItem createDefaultTrone() {
		tbl_trone_orderItem ret = new tbl_trone_orderItem();
		ret.set_cp_id(34); // 未知CP的ID
		ret.set_create_date(new Date());
		ret.set_disable(false);
		ret.set_is_dynamic(false);
		ret.set_is_unknow(true);
		ret.set_order_num("*");
		ret.set_order_trone_name("未分配指令");
		ret.set_push_url_id(47); // 未知CP推送URL
		ret.set_trone_id(getTrone().get_id());
		getDBase().saveData(ret);
		tbl_trone_orderDao.refreshCache();
		return ret;
	}

	@Override
	public boolean doPush() {
		boolean isRecord = false;
		tbl_trone_orderItem defCfg = null;
		if (getPushObject().get_cp_id() > 0 && getPushObject().get_cp_id() != 34)
			return true;
		for (tbl_trone_orderItem m : _allCfg) {
			if (m.get_is_unknow()) {
				if (defCfg != null)
					super.WriteLog(-3, "存在多个默认CP！cfgId:" + Integer.toString(m.get_id()));
				defCfg = m;
				continue;
			}
			if (!isMatch(m))
				continue;
			if (isRecord) {
				super.WriteLog(-3, "配置有冲突：cfgId:" + Integer.toString(m.get_id()));
				continue;
			}
			isRecord = true;
			SetConfig(m);
			super.doPush();
		}
		if (isRecord)
			return true;
		if (getPushObject().get_cp_id() == 34)
			return true;
		if (defCfg == null)
			defCfg = createDefaultTrone();
		SetConfig(defCfg);
		return super.doPush();
	}

	private boolean isMatch(tbl_trone_orderItem m) {
		String spMsg = getPushObject().GetValue(EPushField.Msg);
		if (spMsg == null)
			spMsg = "";
		if (m.get_is_dynamic()) {// CP可模糊的指令
			Pattern rx = Funcs.genericToRegx(m.get_order_num());
			Matcher mc = rx.matcher(spMsg);
			return mc.find();
		}
		// CP精确指令
		return spMsg.equalsIgnoreCase(m.get_order_num());
	}

}
