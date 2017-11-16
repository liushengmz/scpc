package com.andy.system.util;

import java.util.Map;
import java.util.Random;

public class WeightUtil
{
	private static Random rand = new Random();
	
	/**
	 * 根据VALUE的权重，按权重分配返回相应的KEY值，只能保证在大数据测试的情况是正确的
	 * @param dataMap
	 * @return
	 */
	public static int loadIdByWeight(Map<Integer, Integer> dataMap)
	{
		if(dataMap==null || dataMap.isEmpty())
			return -1;
		
		int totalWeight = 0;
		
		for(int key : dataMap.keySet())
		{
			totalWeight += dataMap.get(key);
		}
		
		if(totalWeight<=0)
			return -1;
		
		int curTotalWeight = 0;
		int curWeight = 0;
		int randSeek = rand.nextInt(totalWeight);
		
		for(int key : dataMap.keySet())
		{
			curWeight = dataMap.get(key);
			curTotalWeight += curWeight;
			if(curTotalWeight > randSeek)
			{
				return key;
			}
		}
		
		return -1;
	}
}
