package com.pay.business.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.core.teamwork.base.util.encrypt.DESUtil;

public class JinfuUtil {
	
	public static void main(String[] args) {
		try {
			System.out.println(DESUtil.encrypt("1,1"));
			System.out.println(DESUtil.decrypt("sWg2l3GNeog="));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 新手、首次、推荐、更新
	public static String xin = "#ffa028";
	// 注意  警示  涨  升值  增加...平台优惠  列表优惠字体
	public static String zhu = "#ff5152";
	// 服务  官方活动  客服
	public static String fu = "#c680d6";
	// 促  少  送  复
	public static String cu = "#66afff";
	// 减  降  返  审核
	public static String jian = "#ff6e51";
	// 免费  安全  保证  记录  成功  温馨提示
	public static String mian = "#71db00";
	// 工具  下载
	public static String gong = "#42d1fa";
	
	
	public static String formartDoubleOrInt(Double d){
		if(d == null)
			return "0";
		double dd = d;
		int idd = (int)dd;
		if(dd == idd)
			return idd+"";
		else
			return dd+"";
	}
	
	/**
     * 全角空格为12288，半角空格为32
     * 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
     * 
     * 将字符串中的全角字符转为半角
     * @param src 要转换的包含全角的任意字符串
     * @return  转换之后的字符串
     */
    public static String toSemiangle(String src) {
        char[] c = src.toCharArray();
        for (int index = 0; index < c.length; index++) {
            if (c[index] == 12288) {// 全角空格
                c[index] = (char) 32;
            } else if (c[index] > 65280 && c[index] < 65375) {// 其他全角字符
                c[index] = (char) (c[index] - 65248);
            }
        }
        return String.valueOf(c);
    }

	
	
	/**
	 * 根据经纬度获取省市
	 * @param longitude 经度,一般在100左右
	 * @param latitude 纬度,一般在30左右
	 * @return 省市,小写逗号分割
	 */
	public static String getProvinceCity(String longitude, String latitude){
		HttpURLConnection connection = null;
		BufferedReader reader = null;
		try {
			String url = "http://api.map.baidu.com/geocoder/v2/?ak=sO6GQc18t1BZrY1gWbMhgOOa&location="+latitude+","+longitude+"&output=json&pois=1";
			URL getUrl = new URL(url);
			connection= (HttpURLConnection) getUrl.openConnection();  
            connection.setConnectTimeout(5000);  
            connection.setReadTimeout(5000);  
            connection.connect();  
            reader= new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));  
            StringBuilder builder = new StringBuilder();  
            String line;
            while ((line =reader.readLine()) !=null) {  
                builder.append(line);  
            }
//            System.out.println(builder.toString());
            JSONObject obj = JSONObject.parseObject(builder.toString());
            if(obj.getInteger("status") == 0) {
            	JSONObject result = obj.getJSONObject("result").getJSONObject("addressComponent");
            	return result.getString("province")+","+result.getString("city");
            } else {
            	return "";
            }
		} catch (Exception e) {
			System.out.println("通过经纬度获取省市出错");
		}
		return "";
	}
	
	/**
	 * 对集合中的数字进行排序,便于调用方取最大、最小值,index 0最小值
	 * @param list
	 * @return
	 */
	public static List<Integer> getIntegerSort(List<Integer> list) {
		if(list == null)
			return new ArrayList<Integer>();
		for (int i = 0; i < list.size(); i++) {
			Integer integer = list.get(i);
			if(integer == null)
				list.set(i, 0);
		}
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = 1; j < list.size() - i; j++) {
				Integer a;
				if (list.get(j - 1).compareTo(list.get(j)) > 0) { // 比较两个整数的大小
					a = list.get(j - 1);
					list.set((j - 1), list.get(j));
					list.set(j, a);// 交换数据
				}
			}
		}
		return list;
	}

	/**
	 * 对集合中的数字进行排序,便于调用方取最大、最小值,index 0最小值
	 * @param list
	 * @return
	 */
	public static List<Double> getFloatSort(List<Double> list) {
		if(list == null)
			return new ArrayList<Double>();
		for (int i = 0; i < list.size(); i++) {
			Double float1 = list.get(i);
			if(float1 == null)
				list.set(i, 0d);
		}
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = 1; j < list.size() - i; j++) {
				Double a;
				if (list.get(j - 1).compareTo(list.get(j)) > 0) { // 比较两个数的大小
					a = list.get(j - 1);
					list.set((j - 1), list.get(j));
					list.set(j, a);// 交换数据
				}
			}
		}
		return list;
	}
	
	/**
	 * 对集合中的数字进行排序,便于调用方取最大、最小值,index 0最小值
	 * @param list
	 * @return
	 */
	public static List<Double> getDoubleSort(List<Double> list) {
		if(list == null)
			return new ArrayList<Double>();
		for (int i = 0; i < list.size(); i++) {
			Double double1 = list.get(i);
			if(double1 == null)
				list.set(i, 0d);
		}
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = 1; j < list.size() - i; j++) {
				Double a;
				if (list.get(j - 1).compareTo(list.get(j)) > 0) { // 比较两个数的大小
					a = list.get(j - 1);
					list.set((j - 1), list.get(j));
					list.set(j, a);// 交换数据
				}
			}
		}
		return list;
	}
}
