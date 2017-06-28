package com.system.util;

public class PhoneUtil
{
	/**
	 * 获取IMSI反编后得到的伪码
	 * @param imsi
	 * @return
	 */
	public static String getFakePhonePreByImsi(String imsi)
	{
		String h0,h1,h2,h3,strDigit;
		
		if (imsi == null || imsi.length() != 15)
			return null;
		
		String imsiHead = imsi.substring(0,5);
		
		if (imsiHead.equals("46000"))
		{
			h1 = imsi.substring(5, 6);
			h2 = imsi.substring(6, 7);
			h3 = imsi.substring(7, 8);
			h0 = imsi.substring(9, 10);
			
			String st = imsi.substring(8, 9);
			
			if("56789".contains(st))
			{
				 return "13" + st + "0" + h1 + h2 + h3; 
			}
			else
			{
				int tempint = Integer.parseInt(st) + 5;
				return "13" + tempint + h0 + h1 + h2 + h3;
			}
		}
		else if (imsiHead.equals("46001")) //中国联通，只有46001这一个IMSI号码段
		{
			h1 = imsi.substring(5, 6);
			h2 = imsi.substring(6, 7);
			h3 = imsi.substring(7, 8);
			h0 = imsi.substring(8, 9);
			strDigit = imsi.substring(9, 10);
			if (strDigit.equals("0") || strDigit.equals("1"))
			{
				return "130" + h0 + h1 + h2 + h3;
			}
			else if (strDigit.equals("9"))
			{
				return "131" + h0 + h1 + h2 + h3;
			}
			else if (strDigit.equals("2"))
			{
				return "132" + h0 + h1 + h2 + h3;
			}
			else if (strDigit.equals("3"))
			{
				return "156" + h0 + h1 + h2 + h3;
			}
			else if (strDigit.equals("4"))
			{
				return "155" + h0 + h1 + h2 + h3;
			}
			else if (strDigit.equals("6"))
			{
				return "186" + h0 + h1 + h2 + h3;
			}
			else if (strDigit.equals("7"))
			{
				return "145" + h0 + h1 + h2 + h3;
			}
		}
		else if (imsiHead.equals("46002") || imsiHead.equals("46003") || imsiHead.equals("46011") || imsiHead.equals("46007"))
		{
			h0 = imsi.substring(6, 7);
			h1 = imsi.substring(7, 8);
			h2 = imsi.substring(8, 9);
			h3 = imsi.substring(9, 10);
			
			strDigit = imsi.substring(5, 6);
			
			String h0123 = h0 + h1 + h2 + h3;
			
			if(imsiHead.equals("46002"))
			{
				if (strDigit.equals("0"))
				{
					return "134" + h0123;
				}
				else if (strDigit.equals("1") || strDigit.equals("2"))
				{
					return "15" + strDigit + h0123;
				}
				else if (strDigit.equals("3"))
				{
					return "150" + h0123;
				}
				else if (strDigit.equals("5"))
				{
					return "183" + h0123;
				}
				else if (strDigit.equals("6"))
				{
					return "182" + h0123;
				}
				else if (strDigit.equals("7"))
				{
					return "187" + h0123;
				}
				else if (strDigit.equals("8") || strDigit.equals("9"))
				{
					return "15" + strDigit + h0123;
				}
			}
			else if(imsiHead.equals("46003"))
			{
				if ("0".equals(strDigit))
					return "153" + h0 + h1 + h2 + h3;
				
				if ("9".equals(h0) && "00".equals(h1 + h2))
				{
					return "13301" + h3 + imsi.substring(10, 1);
				}
				
				return "133" + h0123;
			}
			else if(imsiHead.equals("46011"))
			{
				return "177" + h0123;
			}
			else if(imsiHead.equals("46007"))
			{
				if ("5".equals(strDigit))
				{
					return "178" + h0123;
				}
				else if ("7".equals(strDigit))
				{
					return "157" + h0123;
				}
				else if ("8".equals(strDigit))
				{
					return "188" + h0123;
				}
				else if ("9".equals(strDigit))
				{
					return "147" + h0123;
				}
			}
		}
		
		return null;
	}
	
	public static void main(String[] args)
	{
		System.out.println(getFakePhonePreByImsi("460007743190142"));
	}
}
