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
	
	private static Map<String, Integer> _phoneLocateMap = new HashMap<String, Integer>();
	
	private static List<PhoneLocateModel> _phoneLocateList = new ArrayList<PhoneLocateModel>();
	
	private static Map<Integer,List<PhoneLocateModel>> _phoneOperatorLocateMap = new HashMap<Integer,List<PhoneLocateModel>>();
	
	protected static void setProvince(List<ProvinceModel> list)
	{
		_provinceList = list;
	}
	
	protected static void setCity(List<CityModel> list)
	{
		_cityList = list;
	}
	
	protected static void setPhoneLocate(Map<String, Integer> map)
	{
		_phoneLocateMap = map;
	}
	
	protected static void setPhoneLocateList(List<PhoneLocateModel> list)
	{
		_phoneLocateList = list;
	}
	
	protected static void setPhoneLocateList(Map<Integer,List<PhoneLocateModel>> map)
	{
		_phoneOperatorLocateMap = map;
	}
	
	public static int getCityIdByPhone(String phonePrefix)
	{
		Integer cityId = _phoneLocateMap.get(phonePrefix);
		
		if(cityId!=null)
			return cityId;
		
		return -1;
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
	
	public static List<PhoneLocateModel> getPhoneLocateList()
	{
		return _phoneLocateList;
	}
	
	public static Map<Integer,List<PhoneLocateModel>> getPhoneMap()
	{
		return _phoneOperatorLocateMap;
	}
}
