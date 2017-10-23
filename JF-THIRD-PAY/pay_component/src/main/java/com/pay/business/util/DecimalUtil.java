package com.pay.business.util;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;


/**
 * 验证金额是否正确两位小数
 * @author Administrator
 *
 */
public class DecimalUtil {
	public static boolean isNumber(String str) {  
        Pattern pattern = Pattern.compile("[0-9]*");  
        Matcher match = pattern.matcher(str.trim());  
        return match.matches();  
    }  
  
    public static boolean isBigDecimal(String str) {  
        Matcher match =null;  
        if(isNumber(str)==true){  
            Pattern pattern = Pattern.compile("[0-9]*");  
            match = pattern.matcher(str.trim());  
        }else{  
            if(str.trim().indexOf(".")==-1){  
                Pattern pattern = Pattern.compile("^[+-]?[0-9]*");  
                match = pattern.matcher(str.trim());  
            }else{  
                Pattern pattern = Pattern.compile("^[+-]?[0-9]+(\\.\\d{1,2}){1}");  
                match = pattern.matcher(str.trim());                  
            }  
        }  
        return match.matches();  
    }
    
    /**
	 * 优化金额(将以元为单位,补两个0)
	 * @param amount 
	 * @return
	 */
	public synchronized static String optimisedAmount(String amount){
		int indexOf = StringUtils.indexOf(amount, ".");
		if(indexOf==-1){
			//没有小数点,加上小数点
			amount = amount + ".00";
		}else{
			//看小数点后还有几位 不足补零,超出去掉\
			int s = amount.length()-(indexOf+1);
			if(s==1){
				amount = amount + "0";
			}else if(s>2){
				amount = amount.substring(0, indexOf+3);
			}
		}
		return amount;
	}
	
	/**
	 * 元转分
	 * @param amount
	 * @return
	 * @throws Exception 
	 */
	public static String yuanToCents(String amount) throws Exception{
		if(checkAmout(amount)){
			String optimisedAmount = optimisedAmount(amount);
			
			double centos = NumberUtils.createDouble(optimisedAmount)*100;
			
			BigDecimal b = new BigDecimal(centos);
			int f1 = b.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
			
			return String.valueOf(f1);
		}else{
			return null;
		}
	}
	
	/**
	 * 分转元 保留两位小数
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public static String centsToYuan(String amount) throws Exception{
		if(checkAmout(amount)){
			double yuan = NumberUtils.createDouble(amount)/100;
			return optimisedAmount(String.valueOf(yuan));
		}else{
			return null;
		}
	}
	
	public static final String MONEY_REGEX = "^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"; //"\\-?[0-9]+";
	
	/**
	 * 判断是否为金额
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public static boolean checkAmout(String amount) throws Exception{
		Pattern pattern = Pattern.compile(MONEY_REGEX);
		Matcher matcher = pattern.matcher(amount);
		return matcher.matches();
	}
	
	/**
	 * 移除double小数点后面的值(包括小数点)
	 * @param amount
	 * @return
	 */
	public static String removeZero(Double amount){
		if(amount!=null){
			String tempAmount = amount.toString();
			int indexOf = StringUtils.indexOf(tempAmount, ".");
			if(indexOf>0){
				return StringUtils.substring(tempAmount, 0,indexOf);
			}else{
				return tempAmount;
			}
		}else{
			return null;
		}
	}
	
	/**
	 * 向上取整
	 * 
	 * @param d
	 * @param n
	 *            保留位数
	 * @return
	 */
	public static double getCeil(double d, int n) {
		BigDecimal b = new BigDecimal(String.valueOf(d));
		b = b.divide(BigDecimal.ONE, n, BigDecimal.ROUND_CEILING);
		return b.doubleValue();
	}
	
	public static boolean isZero(String str){
		if(Double.valueOf(str)==0){
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) throws Exception {
		String amount = "38.98";
		String optimisedAmount = optimisedAmount(amount);
		double centos = NumberUtils.createDouble(optimisedAmount)*100;
		System.out.println(centos);
		BigDecimal b = new BigDecimal(centos);
		int f1 = b.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
		System.out.println(f1);
		System.out.println(String.valueOf(f1));
	}
}
