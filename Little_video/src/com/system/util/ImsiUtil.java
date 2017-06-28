
package com.system.util;

public class ImsiUtil
{
	public static String ImsiToPhone(String imsi)
	{
		//System.out.println("IMSI:" + imsi);
		if (imsi == null || imsi.length() < 10)
			return null;

		int pfxId;
		String pfxNum = null, h0, h1, h2, h3;
		if (imsi.startsWith("46000"))
		{
			pfxId = Integer.parseInt(imsi.substring(8, 9));

			h0 = imsi.substring(9, 10);
			h1 = imsi.substring(5, 6);
			h2 = imsi.substring(6, 7);
			h3 = imsi.substring(7, 8);

			switch (pfxId)
			{
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
				return "13" + Integer.toString(pfxId) + "0" + h1 + h2 + h3;
			}
			return "3" + Integer.toString(pfxId + 5) + h0 + h1 + h2 + h3;
		}

		if (imsi.startsWith("46002"))
		{
			pfxId = Integer.parseInt(imsi.substring(5, 6));
			h0 = imsi.substring(6, 7);
			h1 = imsi.substring(7, 8);
			h2 = imsi.substring(8, 9);
			h3 = imsi.substring(9, 10);

			switch (pfxId)
			{
			case 0:
				pfxNum = "134";
				break;
			case 1:
				pfxNum = "151";
				break;
			case 2:
				pfxNum = "152";
				break;
			case 3:
				pfxNum = "150";
				break;
			case 4:
				pfxNum = "184";
				break;
			case 5:
				pfxNum = "183";
				break;
			case 6:
				pfxNum = "182";
				break;
			case 7:
				pfxNum = "187";
				break;
			case 8:
				pfxNum = "158";
				break;
			case 9:
				pfxNum = "159";
				break;
			}
			return pfxNum + h0 + h1 + h2 + h3;
		}

		if (imsi.startsWith("46007"))
		{
			pfxId = Integer.parseInt(imsi.substring(5, 6));
			h0 = imsi.substring(6, 7);
			h1 = imsi.substring(7, 8);
			h2 = imsi.substring(8, 9);
			h3 = imsi.substring(9, 10);
			switch (pfxId)
			{
			case 0:
				pfxNum = "170";
				break;
			case 1:
			case 2:
			case 3:
			case 4:
				break;
			case 5:
				pfxNum = "178";
				break;
			case 6:
				break;
			case 7:
				pfxNum = "157";
				break;
			case 8:
				pfxNum = "188";
				break;
			case 9:
				pfxNum = "147";
				break;
			}

			if (pfxNum != null)
				return pfxNum + h0 + h1 + h2 + h3;

			return null;

		}

		if (imsi.startsWith("46001"))
		{
			// 中国联通，只有46001这一个IMSI号码段
			h1 = imsi.substring(5, 6);
			h2 = imsi.substring(6, 7);
			h3 = imsi.substring(7, 8);
			h0 = imsi.substring(8, 9);
			pfxId = Integer.parseInt(imsi.substring(9, 10));

			switch (pfxId)
			{
			case 0:
			case 1:
				pfxNum = "130";
				break;
			case 2:
				pfxNum = "132";
				break;
			case 3:
				pfxNum = "156";
				break;
			case 4:
				pfxNum = "155";
				break;
			case 5:
				pfxNum = "185";
				break;
			case 6:
				pfxNum = "186";
				break;
			case 7:
				pfxNum = "145";
				break;
			case 8:
				pfxNum = "170";
				break;
			case 9:
				pfxNum = "131";
				break;
			}
			return pfxNum + h0 + h1 + h2 + h3;
		}

		if (imsi.startsWith("46003"))
		{ // 电信IMSI段

		}

		return null;

	}
}
