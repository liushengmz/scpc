package com.shotgun.Tools;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Funcs {

	public static Boolean isNullOrEmpty(String value) {
		return value == null || value.length() == 0;
	}

	/**
	 * 通配符转正则表达式,不区会大小写
	 * 
	 * @param value
	 *            通配表达式
	 * @return
	 */
	public static Pattern genericToRegx(String value) {

		if (value.contains("\\"))
			value = value.replace("\\", "\\\\");

		value = value.replaceAll("([\\.\\[\\]\\|\\^\\$\\<\\>])", "\\$1");

		if (value.contains("?")) {
			Pattern p = Pattern.compile("\\?{1,}");
			Matcher m = p.matcher(value);
			while (m.find()) {
				// System.out.println(m.group());
				value = value.replace(m.group(), ".{0," + m.group().length() + "}");
			}
		}

		if (value.contains("*")) {
			// mStr = Regex.Replace(mStr, @"\*{1,}", ".{0,}");
			Pattern p = Pattern.compile("\\*{1,}");
			Matcher m = p.matcher(value);
			while (m.find()) {
				value = value.replace(m.group(), ".{0,}");
			}
		}
		return Pattern.compile("^" + value + "$", Pattern.CASE_INSENSITIVE);
	}

	/**
	 * IMSI转手机号前七位
	 * 
	 * @param imsi
	 *            手机IMSI
	 * @return 手机号前7位,如果失败返回 null
	 */
	public static String imsiToPhone7(String imsi) {
		String h0;
		String h1;
		String h2;
		String h3;
		final String s56789 = "56789";
		String strDigit;

		if (imsi == null || imsi.length() != 15)
			return null;

		if (imsi.startsWith("46000")) {
			h1 = imsi.substring(5, 6);
			h2 = imsi.substring(6, 7);
			h3 = imsi.substring(7, 8);
			String st = imsi.substring(8, 9);
			h0 = imsi.substring(9, 10);

			if (s56789.contains(st)) {
				return "13" + st + "0" + h1 + h2 + h3;
			} else {
				int tempint = Integer.parseInt(st) + 5;
				return "13" + tempint + h0 + h1 + h2 + h3;
			}
		}

		if (imsi.startsWith("46002")) {
			strDigit = imsi.substring(5, 6);
			h0 = imsi.substring(6, 7);
			h1 = imsi.substring(7, 8);
			h2 = imsi.substring(8, 9);
			h3 = imsi.substring(9, 10);

			if (strDigit.equals("0")) {
				return "134" + h0 + h1 + h2 + h3;
			} else if (strDigit.equals("1")) {
				return "151" + h0 + h1 + h2 + h3;
			} else if (strDigit.equals("2")) {
				return "152" + h0 + h1 + h2 + h3;
			} else if (strDigit.equals("3")) {
				return "150" + h0 + h1 + h2 + h3;
			} else if (strDigit.equals("5")) {
				return "183" + h0 + h1 + h2 + h3;
			} else if (strDigit.equals("6")) {
				return "182" + h0 + h1 + h2 + h3;
			} else if (strDigit.equals("7")) {
				return "187" + h0 + h1 + h2 + h3;
			} else if (strDigit.equals("8")) {
				return "158" + h0 + h1 + h2 + h3;
			} else if (strDigit.equals("9")) {
				return "159" + h0 + h1 + h2 + h3;
			}
		}

		if (imsi.startsWith("46003")) {
			strDigit = imsi.substring(5, 6);
			h0 = imsi.substring(6, 7);
			h1 = imsi.substring(7, 8);
			h2 = imsi.substring(8, 9);
			h3 = imsi.substring(9, 10);

			if (!strDigit.equals("0"))
				return "153" + h0 + h1 + h2 + h3;
			if (h0.equals("9") && (h1 + h2).equals("00")) {
				return "13301" + h3 + imsi.substring(10, 1);
			}
			return "133" + h0 + h1 + h2 + h3;
		}
		if (imsi.startsWith("46011")) {
			h0 = imsi.substring(6, 7);
			h1 = imsi.substring(7, 8);
			h2 = imsi.substring(8, 8);
			h3 = imsi.substring(9, 10);
			return "177" + h0 + h1 + h2 + h3;
		}

		if (imsi.startsWith("46007")) {
			strDigit = imsi.substring(5, 1);
			h0 = imsi.substring(6, 7);
			h1 = imsi.substring(7, 8);
			h2 = imsi.substring(8, 9);
			h3 = imsi.substring(9, 10);

			if (strDigit.equals("5")) {
				return "178" + h0 + h1 + h2 + h3;
			} else if (strDigit.equals("7")) {
				return "157" + h0 + h1 + h2 + h3;
			} else if (strDigit.equals("8")) {
				return "188" + h0 + h1 + h2 + h3;
			} else if (strDigit.equals("9")) {
				return "147" + h0 + h1 + h2 + h3;
			}
		}

		if (imsi.startsWith("46001")) {
			// 中国联通，只有46001这一个IMSI号码段
			h1 = imsi.substring(5, 6);
			h2 = imsi.substring(6, 7);
			h3 = imsi.substring(7, 8);
			h0 = imsi.substring(8, 9);
			strDigit = imsi.substring(9, 10); // for A
			if (strDigit.equals("0") || strDigit.equals("1")) {
				return "130" + h0 + h1 + h2 + h3;
			} else if (strDigit.equals("9")) {
				return "131" + h0 + h1 + h2 + h3;
			} else if (strDigit.equals("2")) {
				return "132" + h0 + h1 + h2 + h3;
			} else if (strDigit.equals("3")) {
				return "156" + h0 + h1 + h2 + h3;
			} else if (strDigit.equals("4")) {
				return "155" + h0 + h1 + h2 + h3;
			} else if (strDigit.equals("6")) {
				return "186" + h0 + h1 + h2 + h3;
			} else if (strDigit.equals("7")) {
				return "145" + h0 + h1 + h2 + h3;
			}
		}

		return null;
	}

	/** 将HashMap对像，进行URLEncode */
	public static String urlEncode(HashMap<String, String> data) {
		if (data == null || data.size() == 0)
			return "";

		StringBuilder sb = new StringBuilder();

		for (Entry<String, String> kv : data.entrySet()) {
			sb.append(String.format("%s=%s&", urlEncode(kv.getKey()), urlEncode(kv.getValue())));
		}

		sb.setLength(sb.length() - 1);
		return sb.toString();
	}

	/** URL Encode使用utf-8编码 */
	public static String urlEncode(String data) {
		if (isNullOrEmpty(data))
			return "";
		try {
			return URLEncoder.encode(data, "utf-8");
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 字符转整型
	 * 
	 * @param strInt
	 *            字符串形式数字
	 * @param defValue
	 *            默认值，转换失败将返回该值
	 * @return
	 */
	public static Integer parseInt(String strInt, Integer defValue) {
		if (isNullOrEmpty(strInt))
			return defValue;
		try {
			if (strInt.contains("."))
				return (int) Float.parseFloat(strInt);
			else
				return Integer.parseInt(strInt);
		} catch (Exception e) {
			return defValue;
		}
	}

	/**
	 * 字符转浮点
	 * 
	 * @param strInt
	 *            字符串形式数字
	 * @param d
	 *            默认值，转换失败将返回该值
	 * @return
	 */
	public static Float parseFloat(String strInt, Float d) {
		if (isNullOrEmpty(strInt))
			return d;
		try {
			return Float.parseFloat(strInt);
		} catch (Exception e) {
			return d;
		}
	}

}
