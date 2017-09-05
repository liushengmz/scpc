package com.pay.manger.controller.job;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pay.business.record.service.PayDataRecordService;
/**
* @Title: payOrderByHourJob.java 
* @Package com.pay.manger.controller.job 
* @Description: 每小时去统计订单数据
* @author ZHOULIBO   
* @date 2017年8月1日 下午4:36:09 
* @version V1.0
*/
@Component
@Controller
@RequestMapping("/PayOrderByHourJob")
public class PayOrderByHourJob {
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private PayDataRecordService payDataRecordService;
	
	
	/**
	 * startUp 
	 * 每小时执行订单统计
	 * void    返回类型
	 */
	@RequestMapping("/startUp")
	public void startUp() {
		System.out.println("=========》每小时统计开始《==========");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		// 开始时间
		String timeBegin = getHour(date);
		// 结束时间
		String timeEnd = sdf.format(date);
		 System.out.println("结束时间："+timeEnd);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("timeBegin", timeBegin);
		map.put("timeEnd", timeEnd);
		try {
			payDataRecordService.setStatisticsOrderByHour(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * getHour 
	 * 获取时间的前一小时时间
	 * @param date
	 * @return    设定文件 
	 * String    返回类型
	 */
	public static String  getHour(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);//date 换成已经已知的Date对象
        cal.add(Calendar.HOUR_OF_DAY, -1);// before 8 hour
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("开始时间："+format.format(cal.getTime()));
        String hour=format.format(cal.getTime());
		return hour;
	}
	
	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		// 开始时间
		String timeBegin = getHour(date);
		// 结束时间
		String timeEnd = sdf.format(date);
		System.out.println(timeBegin);
		System.out.println(timeEnd);
	}
	
	
}
