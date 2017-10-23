package com.system.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.model.CityModel;
import com.system.model.PhoneLocateModel;
import com.system.model.ProvinceModel;

public class LocateCache
{
	private static List<ProvinceModel> _provinceList = new ArrayList<ProvinceModel>();
	
	private static List<CityModel> _cityList = new ArrayList<CityModel>();
	
	private static Map<String, PhoneLocateModel> _phoneLocateMap = new HashMap<String, PhoneLocateModel>();
	
	protected static void setProvince(List<ProvinceModel> list)
	{
		_provinceList = list;
	}
	
	protected static void setCity(List<CityModel> list)
	{
		_cityList = list;
	}
	
	protected static void setPhoneLocate(Map<String, PhoneLocateModel> map)
	{
		_phoneLocateMap = map;
	}
	
	public static PhoneLocateModel getPhoneLocateModelByPhone(String phonePrefix)
	{
		return _phoneLocateMap.get(phonePrefix);
	}
	
	public static ProvinceModel getProvinceByCityId(int cityId)
	{
		for(CityModel model : _cityList)
		{
			if(model.getId()==cityId)
			{
				return getProvinceByProvinceId(model.getProvinceId());
			}
		}
		
		return null;
	}
	
	public static ProvinceModel getProvinceByProvinceId(int provinceId)
	{
		for(ProvinceModel model : _provinceList)
		{
			if(model.getId() == provinceId)
			{
				return model;
			}
		}
		
		return null;
	}
	
}
