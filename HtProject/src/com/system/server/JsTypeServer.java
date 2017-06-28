package com.system.server;

import java.util.List;

import com.system.dao.JsTypeDao;
import com.system.model.JsTypeModel;

public class JsTypeServer
{
	public List<JsTypeModel> loadJsType()
	{
		return new JsTypeDao().loadJsType();
	}
}
