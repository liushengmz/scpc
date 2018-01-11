package com.pay.business.util.pinganbank.pay;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONObject;

import org.apache.commons.lang.RandomStringUtils;

import com.pay.business.util.pinganbank.util.HttpUtil;
import com.pay.business.util.pinganbank.util.HttpsUtil;
import com.pay.business.util.pinganbank.util.TLinxAESCoder;
import com.pay.business.util.pinganbank.util.TLinxMapUtil;
import com.pay.business.util.pinganbank.util.TLinxRSACoder;
import com.pay.business.util.pinganbank.util.TLinxSHA1;
/**
* @Title: AddConfig.java 
* @Package com.pay.business.util.pinganbank.pay 
* @Description: 商户配置公众号支付相关的配置：授权目录
* @author ZHOULIBO   
* @date 2017年8月19日 上午11:42:16 
* @version V1.0
*/
public class AddConfig {
	/**
	 * 
	 * @param ctt_code 平安的合同号
	 * @param sub_appid 微信APPID
	 * @param jsapi_path 授权目录（我们服务器地址）
	 * @param OPEN_ID 机构的参数
	 * @param OPEN_KEY 机构的参数
	 * @param PRIVATE_KEY 机构的参数
	 * @param PUBLICKEY 机构的参数
	 * @return
	 */
	public static Map<String,Object> addConfig(String ctt_code, String sub_appid,
			String jsapi_path, String OPEN_ID, String OPEN_KEY, 
			String PRIVATE_KEY, String PUBLICKEY) {
		Map<String,Object> returnMap=new HashMap<String, Object>();
		String timestamp = new Date().getTime() / 1000 + ""; // 时间
		try {

			// 固定参数
			TreeMap<String, String> postmap = new TreeMap<String, String>(); // post请求参数的map
			TreeMap<String, String> getmap = new TreeMap<String, String>(); // get请求参数的map

			getmap.put("open_id", OPEN_ID);
			getmap.put("lang", "zh-cn");
			getmap.put("timestamp", timestamp);
			getmap.put("randStr", RandomStringUtils.randomAlphanumeric(32));

			TreeMap<String, String> datamap = new TreeMap<String, String>(); // data参数的map
			datamap.put("ctt_code", ctt_code);
			if(sub_appid!=null){
			datamap.put("sub_appid", sub_appid);
			}
			if(jsapi_path!=null){
				datamap.put("jsapi_path", jsapi_path);
			}
			/**
			 * 1 data字段内容进行AES加密，再二进制转十六进制(bin2hex)
			 */
			String data = handleEncrypt(datamap, OPEN_KEY);
			

			postmap.put("data", data);
			System.out.println("=====data=====" + data);

			
			System.out.println("datamap:" + datamap);
			System.out.println("getmap:" + getmap);
			System.out.println("postmap:" + postmap);
			System.out.println("OPEN_KEY:" + OPEN_KEY);
			System.out.println("PRIVATE_KEY:" + PRIVATE_KEY);
			
			/**
			 * 2 请求参数签名 按A~z排序，串联成字符串，先进行sha1加密(小写)，再进行RSA加密(小写),二进制转十六进制，得到签名
			 */
			String sign = handleSign(getmap, postmap, PRIVATE_KEY);

			postmap.put("sign", sign);
			System.out.println("=====sign=====" + sign);

			/**
			 * 3 请求、响应
			 */
			String uri = "contract/addconfig" + "?open_id=" + getmap.get("open_id") + "&lang=" + getmap.get("lang") + "&timestamp=" + getmap.get("timestamp") + "&randStr=" + getmap.get("randStr");
			String rspStr = handlePost(postmap, uri);

			/**
			 * 4 验签 有data节点时才验签
			 */
			JSONObject respObject = JSONObject.fromObject(rspStr);
			System.out.println("===响应错误码：" + respObject.get("errcode"));
			System.out.println("===响应错误提示：" + respObject.get("msg"));
			returnMap.put("code", respObject.get("errcode"));
			returnMap.put("msg", respObject.get("msg"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMap;
	}
	 /**
     * AES加密，再二进制转十六进制(bin2hex)
     * @param datamap 说明：
     *
     * @return 返回值说明：
     * @throws Exception
     */
    public static String handleEncrypt(TreeMap<String, String> datamap,String open_key) throws Exception {
        JSONObject dataobj = JSONObject.fromObject(datamap);

        return TLinxAESCoder.encrypt(dataobj.toString(),open_key);    // AES加密，并bin2hex
    }
    /**
     * 签名
     * @param getmap
     * @param datamap 说明：
     *
     * @return 返回值说明：
     */
    public static String handleSign(TreeMap<String, String> getmap, TreeMap<String, String> datamap,String privatekey) {
        Map<String, String> veriDataMap = new HashMap<String, String>();

        veriDataMap.putAll(getmap);
        veriDataMap.putAll(datamap);

        // 签名
        return sign(veriDataMap,privatekey);
    }
    /**
     * 签名
     * @param postMap
     * @param privatekey 说明：
     * @return
     */
    public static String sign(Map<String, String> postMap, String privatekey) {
        String sortStr = null;
        String sign    = null;

        try {

            /**
             * 1 A~z排序
             */
            sortStr = sort(postMap);
            System.out.println("=======排序后的明文：" + sortStr);

            /**
             * 2 sha1加密(小写)
             */
            String sha1 = TLinxSHA1.SHA1(sortStr);

            System.out.println("=======sha1：" + sha1);

            /**
             * 3 RSA加密(小写),二进制转十六进制
             */
            sign = TLinxRSACoder.sign(sha1.getBytes("utf-8"), privatekey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sign;
    }
 // 排序
    public static String sort(Map paramMap) throws Exception {
        String sort = "";
        TLinxMapUtil signMap = new TLinxMapUtil();
        if (paramMap != null) {
            String key;
            for (Iterator it = paramMap.keySet().iterator(); it.hasNext();) {
                key = (String) it.next();
                String value = ((paramMap.get(key) != null) && (!(""
                        .equals(paramMap.get(key).toString())))) ? paramMap
                        .get(key).toString() : "";
                signMap.put(key, value);
            }
            signMap.sort();
            for (Iterator it = signMap.keySet().iterator(); it.hasNext();) {
                key = (String) it.next();
                sort = sort + key + "=" + signMap.get(key).toString() + "&";
            }
            if ((sort != null) && (!("".equals(sort)))) {
                sort = sort.substring(0, sort.length() - 1);
            }
        }
        return sort;
    }
    /**
     * 请求接口
     * @param postmap
     * @param uri 说明：
     * @return      响应字符串
     */
    public static String handlePost(TreeMap<String, String> postmap, String uri) {
        String url = "https://api.orangebank.com.cn/org1/" + uri;
//        url = "http://openapi.tlinx.cn/org1/order?open_id=txaalQ4ae3fde16fab071bb1bc452dfb&lang=zh-cn&timestamp=1493791966&randStr=lAMUR5ALaxopwkTZSTrUQ4MSXEid9GdJ";
        if (url.contains("https")) {
            return HttpsUtil.httpMethodPost(url, postmap, "UTF-8");
        } else {
            return HttpUtil.httpMethodPost(url, postmap, "UTF-8");
        }
    }
    /**
     * 验签
     * @param respObject
     * @param publickey 说明：
     * @return
     */
    public static Boolean verifySign(JSONObject respObject, String publickey) {
        boolean verify = false;
        try {
            String respSign = respObject.getString("sign");

            respObject.remove("sign");                          // 删除sign节点

            String rspparm = sortjson(respObject);    // ȥsign json
            String sha1    = TLinxSHA1.SHA1(rspparm);           //

            verify = TLinxRSACoder.verify(sha1.getBytes(), publickey, respSign);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return verify;
    }
    // 排序
    public static String sortjson(JSONObject jsonMap) throws Exception {
        Map<String ,String > paramMap=new HashMap<String, String>();
        // 将json字符串转换成jsonObject
        JSONObject jsonObject = JSONObject.fromObject(jsonMap);
        Iterator ite = jsonObject.keys();
        // 遍历jsonObject数据,添加到Map对象
        while (ite.hasNext()) {
            String key = ite.next().toString();
            String value = jsonObject.get(key).toString();
            paramMap.put(key, value);
        }
        String sort = "";
        TLinxMapUtil signMap = new TLinxMapUtil();
        if (paramMap != null) {
            String key1;
            for (Iterator it = paramMap.keySet().iterator(); it.hasNext();) {
                key1 = (String) it.next();
                String value = ((paramMap.get(key1) != null) && (!(""
                        .equals(paramMap.get(key1).toString())))) ? paramMap
                        .get(key1).toString() : "";
                signMap.put(key1, value);
            }
            signMap.sort();
            for (Iterator it = signMap.keySet().iterator(); it.hasNext();) {
                key1 = (String) it.next();
                sort = sort + key1 + "=" + signMap.get(key1).toString() + "&";
            }
            if ((sort != null) && (!("".equals(sort)))) {
                sort = sort.substring(0, sort.length() - 1);
            }
        }
        return sort;
    }
    public static void main(String[] args) {
    	//https://testpayapi.aijinfu.cn/GateWay/ :测试环境授权地址
    	//https://payapi.aijinfu.cn/GateWay/	:生产环境配置授权地址
    	//https://pay.iquxun.cn/GateWay/	:趣讯生产环境配置授权地址
    	//wxe93071f39bf91508 微信公众号ID
    	
    	/*
    	addConfig("880000000-880000784-1502248683", 
    			"wxe93071f39bf91508",
    			null, 
    			"txa42lIMd7ce80cd811a69d9ab2319c4", 
    			"QI89Zau8dSFaZhi4hMFynTKp10uu0mvQ",
    			"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");
    	*/
//    	addConfig("880000000-880000784-1502248683", null, "https://pay.iquxun.cn/GateWay/", "txa42lIMd7ce80cd811a69d9ab2319c4", "QI89Zau8dSFaZhi4hMFynTKp10uu0mvQ","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");
//    	addConfig("880000000-880000904-15030246663821", null, "https://payapi.aijinfu.cn/GateWay/", "txaRILPXc98f38883cc9a2773897cca7", "05l58KTAx2ZbNnyvx5JSdOi6IAHoGgNG","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");
//    	addConfig("880000000-880000904-15030246663821", "wxc3d694c7766e655a", null, "txaRILPXc98f38883cc9a2773897cca7", "05l58KTAx2ZbNnyvx5JSdOi6IAHoGgNG","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");
    	
    	/**
    	 * 
    	 * @param ctt_code 平安的合同号
    	 * @param sub_appid 微信APPID
    	 * @param jsapi_path 授权目录（我们服务器地址）
    	 * @param OPEN_ID 机构的参数
    	 * @param OPEN_KEY 机构的参数
    	 * @param PRIVATE_KEY 机构的参数
    	 * @param PUBLICKEY 机构的参数
    	 * @return
    	 */
    	//趣讯公众号1
    	/*
    	addConfig("880000000-880000902-15028720155333", 
    			"wx5575b6a287f3ce2b", 
    			null, 
    			"txaERheRc20746b89a7950a3a928bfe2", 
    			"WiDEdwgg1IBIf9xmQL6nrmO0oUqHdH9a", 
    			"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");
    	*/
    	//趣讯公众号2
    	/*
    	addConfig("880000000-880000784-1502249229", 
    			null, 
    			"https://pay.iquxun.cn/GateWay/", 
    			"txa42lIMd7ce80cd811a69d9ab2319c4", 
    			"QI89Zau8dSFaZhi4hMFynTKp10uu0mvQ", 
    			"MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDddN6A+Ww6AgygJb7u9J5UCgPMfqEjvkJ6uWk5i1YOukNqiAqY0o3wQPXNnfTVc8LeM3HV4XqVs2evMTTTLjhspmXQlYx9ShaLV3oXHeUx2Jsk/1s/OkuLF1eYNO2vhTcAIYfbuZKRrIYIih+1PpFM3hniA5KdQNX84E6scW1eGfQgUjqCSeKZtX07tIF867BxaQbchlyC71nlgXajg+DONN0NFNiLu+b0sM2UeY+HJQJQ3c+VkS7DAtW8N1vYcO9JzvDu6mnEI2dzAF2fAj78vinBAZFTHc7Ct4dGpPxJTRf8R3bUfXV8xZBc+3kkf5tUbizMkR4LHOftRSyD9YnRAgMBAAECggEBAJYOVaJA39OihdmSGgEiYZICQzayaw+kILm1npYuUr6h+YJa8gtBSIoOCkAsErT7voP/idfp870yFkSAbBHYVMVSLtUaMFrI8+Ow/3pgeGfBJMb5/GMoZf22cFUjMBbphi4hikQZRzZMF3n71aZi4eOa7yDVWOgTAaxadRSluvyyB+DwKdhOB8zOHiDEHgL0p6U+1OIIT0xccPHmnouXDOn9ywF+C7xnHqBPxpHBpvisD6RXD9KDhKdWYCBYlzeLD76NvgaWeny4dRYHKQE0SWYM43YHZqK8E0ofypBpj7zjS+UkpyJ7NZH5voxaf3uLGrH/ccnuFVmJq6TMnwt04z0CgYEA/14cDkYjB9NkeuRfGNrEqv+GqzVvNbHKaYuQ9/tqz7x+87Ph7CGUlLspS7tPjOZXRGlYgAPqD6KwDzo9buQNOZeOh6Agi0I30PE8AQcpVydXfKvXRLux6ZFMYPPjkDlPAXAsQH/94zfLW3TwI1uMh88o2lPCI/sM+/i21w7JNM8CgYEA3gFC+NaseMpUSiwD3D9IApSOnHe/keyUq+umEWB12tsxq+YwkfMvYVrgb8RCa5TNEj9oXsvgO6nDXENUybyCVjcWoU5BWeW4xw++FWUjTyNMyH67agI+9UUjwPVnjYrLmcge/gwINykbtYClQfoHMzKo2ogqtKILqRGXkHL0P18CgYAuV3C18mpnACiq2IidZQ3tjiNtLGw7DUGTN72eEuUGP8m2Bf3IsStadkB/OsWr5x0NECT8TjmKjtZuXP5LAl2YBvXZjOh6/RBN/YkLErag10XcHP8avQkDPtfifD/eq1e4BhgxuEhllHl15lmxwOpWtvRN8oc3qlZn33Gmw0smJwKBgGN2iTzXYTpU2+LHSYt5xpdxW1t6wxdruUg1MZgDcYn2PpDXdtdM7uNdRcSNV3y/lAki423lRbc1XdOOTwR7MqHR2I+4ccsHAvwcb3tCbslb9WC2dt0N2IsmyNgAmr5ter6RTGFhnqSoBEQTOPcQP/2OKtyNuSRonXTH7vHGrutdAoGAWQvc64nGwt1u2U9ct9iY0VDtP1fT38XO67xNSp+zUpjcfYZMho5uKFdO5Qa5/l5q3AVTY7T+dEzHCaYcmMbe+GBLLKjxu4YzPH+crXY3bE5T/XL3gJ6Hvq7iQa9iGSpDnT3Jk5IpOomGewUQ4pu/YBih09IltkEEQurewtLnMFM=",
    			"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3XTegPlsOgIMoCW+7vSeVAoDzH6hI75CerlpOYtWDrpDaogKmNKN8ED1zZ301XPC3jNx1eF6lbNnrzE00y44bKZl0JWMfUoWi1d6Fx3lMdibJP9bPzpLixdXmDTtr4U3ACGH27mSkayGCIoftT6RTN4Z4gOSnUDV/OBOrHFtXhn0IFI6gknimbV9O7SBfOuwcWkG3IZcgu9Z5YF2o4PgzjTdDRTYi7vm9LDNlHmPhyUCUN3PlZEuwwLVvDdb2HDvSc7w7uppxCNncwBdnwI+/L4pwQGRUx3OwreHRqT8SU0X/Ed21H11fMWQXPt5JH+bVG4szJEeCxzn7UUsg/WJ0QIDAQAB");
    	*/
    	//趣讯公众号3
    	addConfig("880000000-880000902-15036535433373", 
    			"wxe008927c1ae20c7c",
    			null,//"https://pay.iquxun.cn/GateWay/", 
    			"txaERheRc20746b89a7950a3a928bfe2", 
    			"WiDEdwgg1IBIf9xmQL6nrmO0oUqHdH9a", 
    			"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");
    	
    }
    
    public static void postData()
    {
    	System.out.println("start post ...");
    	String url = "https://pay.iquxun.cn/pay/sdkPayment.do?package=com.example.pay.demo&paramStr=appKey%3Df97769e5e6951a1c410f3216d0734d4f%26appType%3D1%26bussOrderNum%3D1503462679068%26notifyUrl%3Dhttps%3A%2F%2Fpay.iquxun.cn%2FjinfuOrder%2FtestCallback.do%26orderName%3D%E5%85%85%E5%80%BC%E8%AF%9D%E8%B4%B9%26payMoney%3D0.02%26payPlatform%3D2%26sign%3Def4a8cd5662f5593a42f8fc278387fa7";
    	Map<String, Object> map = new HashMap<>();
    	String result = com.pay.business.util.httpsUtil.HttpsUtil.doPostString(url, map, "UTF-8");
    	System.out.println("result:" + result);
    	
    }
}
